package com.acme.sales.model.cqrs.v3.command.events;

import com.acme.infra.postgresql.JDBCBase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * For managing the data in the proposalevents table
 */
public class ProposalEventRepo extends JDBCBase {

    /**
     * Selects the unprocessed events
     * @return  List of unprocessed events
     */
    public ArrayList<ProposalEvent> getUnprocessed(){

        ArrayList<ProposalEvent>  events = new ArrayList<>();

        try {
            // 1. Create the SQL
            String sql = "SELECT event_id, proposal_id, event_guid, payload, processed, processed_date FROM proposalevents WHERE processed = FALSE";

            // 2. Execute the SQL
            String eventsJSON = executeSQL(sql);

            // 3. Convert the result from JDBC source to Model
            JSONArray jsonArray = new JSONArray(eventsJSON);

            // 4. Parse and create the objects
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                ProposalEvent proposalEvent = new ProposalEvent(jsonObject.getInt("event_id"),
                        jsonObject.getInt("proposal_id"), jsonObject.getString("event_guid"),
                        jsonObject.getString("payload"));

                events.add(proposalEvent);
            }


            return events;
        } catch (SQLException sqle){
            sqle.printStackTrace();
            return events;
        }

    }

    public boolean  markEventProcessed(int eventId){
        String sql = "UPDATE proposalevents SET processed=true, processed_date=now() WHERE event_id="+eventId;

        try {
            executeSQLUpdate(sql);
            return true;
        } catch (SQLException sqle){
            sqle.printStackTrace();
            return false;
        }
    }

    // For Unit testing
    public static void main(String[] args){
        ArrayList<ProposalEvent> events = new ProposalEventRepo().getUnprocessed();

        System.out.println(events.size());

        new ProposalEventRepo().markEventProcessed(1);
    }
}
