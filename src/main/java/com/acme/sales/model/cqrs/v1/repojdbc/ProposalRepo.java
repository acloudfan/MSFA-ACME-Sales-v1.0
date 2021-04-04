package com.acme.sales.model.cqrs.v1.repojdbc;

import com.acme.infra.postgresql.JDBCBase;
import com.acme.sales.model.Pax;
import com.acme.sales.model.Proposal;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Pattern : Repository
 * Extends the JDBCBase class for managing the data in PostgreSQL
 *
 * NOTE: Not all functions implemented
 */
public class ProposalRepo extends JDBCBase implements com.acme.sales.model.repo.ProposalRepo  {

    /**
     * Returns a JSON String that has the proposal_id of the newly created proposal
     */
    @Override
    public String add(Proposal proposal) {
        try{

            // Simplify pax name
            ArrayList<String>  pax = new ArrayList<>();
            ArrayList<Integer>  paxAge = new ArrayList<>();
            ArrayList<Pax> passengers = proposal.getPassengers();
            for(Pax passenger : passengers){

                pax.add(passenger.fName+"-"+passenger.mName+"-"+passenger.lName);
                paxAge.add(passenger.age);
            }

            // Returns the JSON with proposal_id
            String proposalIdJSON = addProposal(proposal.getCustomerReference(), proposal.getVacationPackage().getName(),pax, paxAge);

            // System.out.println(proposalIdJSON);

            return proposalIdJSON;

        } catch(SQLException sqle){
            sqle.printStackTrace();
            return  null;
        }
    }

    /**
     * Creates the proposal in PostgreSQL
     */
    protected String addProposal(int customerId, String packageId, ArrayList<String> pax, ArrayList<Integer> paxAge) throws SQLException {

        // SQL Statement for insert
        String sql = createInsertSQL(customerId, packageId, pax, paxAge);

        // Execute the SQL
        String proposalIdJSON = executeSQLUpdate(sql);


        return proposalIdJSON;
    }

    /**
     *
     * Utility method
     * Creates the INSERT SQL statement for proposal
     */
    protected String createInsertSQL(int customerId, String packageId, ArrayList<String> pax, ArrayList<Integer> paxAge ){
        String sql = "INSERT INTO proposals(customer_id, package_id, pax_0, pax_age_0";
        String values = "VALUES("+customerId+", '"+ packageId +"' , '"+pax.get(0)+"',"+paxAge.get(0);

        // Add the pax_? to the SQL INSERT statement
        for(int i=1; i < pax.size(); i++){
            sql += ","+"pax_"+i+", "+"pax_age_"+i;
            values += ", '"+pax.get(i) + "' ," + paxAge.get(i);
        }

        // PostgreSQL allows the RETURNING of values from the newly inserted rows
        return sql+")  "+values+") RETURNING proposal_id AS proposal_id";
    }

    @Override
    public Proposal get(int reference) {

        try {
            // 1. Create the SQL
            String sql = "SELECT * from proposals WHERE proposal_id=" + reference;

            // 2. Execute the SQL
            String proposalJson = executeSQL(sql, true);

            if(proposalJson.equalsIgnoreCase("{}")) return null;

            // 3. Translate the data from JDBC to proposal model object
            JSONObject jsonObject = new JSONObject(proposalJson);
            Proposal proposal = createProposalFromJSON(jsonObject);

            return proposal;
        }catch(SQLException sqle){
            sqle.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Proposal> getCustomerProposals(int customerReference) {

        // 1. Create the SQL
        String sql =  "SELECT * from proposals WHERE customer_id="+customerReference;

        // 2. Execute the SQL
        try {
            String proposalsJSON = executeSQL(sql);

            ArrayList<Proposal>  proposals = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(proposalsJSON);

            // 3. Translate from Database representation to Model
            for(int i=0 ; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Proposal proposal = createProposalFromJSON(jsonObject);
                proposals.add(proposal);
            }

            return proposals;

        } catch(SQLException sqle){
            sqle.printStackTrace();
        }
        return null;
    }

    /**
     * Utility method
     * Converts the JSON to Proposal object
     */
    private Proposal createProposalFromJSON(JSONObject jsonObject){


        int proposalId = jsonObject.getInt("proposal_id");
        int customerId = jsonObject.getInt("customer_id");
        String packageId = jsonObject.getString("package_id");

        Proposal proposal = new Proposal(proposalId,customerId, packageId);
        for(int i=0; i < 5; i++){
            if(jsonObject.isNull("pax_"+i)) break;

            String fullName = jsonObject.getString("pax_"+i);
            String[] name = fullName.split("-");
            int age = jsonObject.getInt("pax_age_"+i);

            proposal.addPax(name[0],name[1],name[2],age);
        }
        return proposal;
    }

    @Override
    public ArrayList<Proposal> getCustomerProposals(int customerReference, int number) {
        return null;
    }

    @Override
    public boolean remove(int reference) {
        return false;
    }
}
