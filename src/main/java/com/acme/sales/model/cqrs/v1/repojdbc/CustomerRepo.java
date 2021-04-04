package com.acme.sales.model.cqrs.v1.repojdbc;

import com.acme.infra.postgresql.JDBCBase;
import com.acme.sales.model.Address;
import com.acme.sales.model.Customer;
import org.json.JSONObject;

import java.sql.SQLException;

/**
 * Pattern : Repository
 * Extends the JDBCBase class for managing the data in PostgreSQL
 *
 * NOTE: Not all functions implemented as the testing is focussed on proposals
 */
public class CustomerRepo extends JDBCBase implements com.acme.sales.model.repo.CustomerRepo {

    @Override
    public Customer get(int reference) {

        try {
            // 1. Create the SQL
            String sql = "SELECT * from customers WHERE customer_id=" + reference;

            // 2. Execute the SQL
            String customerJSON = executeSQL(sql, true);

            // 3. Convert the result from JDBC source to Model
            JSONObject jsonObject = new JSONObject(customerJSON);

            Address  address = null;
            String fname = jsonObject.getString("fname");
            String mname = jsonObject.getString("mname");
            String lname = jsonObject.getString("lname");
            String phoneNumber = jsonObject.getString("phone");
            String email = jsonObject.getString("email_address");

            // 4. Create an instance of the customer model object
            Customer customer = new Customer(fname,mname,lname,address,phoneNumber,email);

            return customer;
        } catch (SQLException sqle){
            sqle.printStackTrace();
            return null;
        }

    }

    @Override
    public Customer get(String email, String phoneNumber) {

        /** Left as an exercise **/

        return null;
    }

    @Override
    public boolean add(Customer customer) {
        return false;
    }


    @Override
    public boolean remove(Customer customer) {
        return false;
    }

    @Override
    public boolean remove(int reference) {
        return false;
    }
}
