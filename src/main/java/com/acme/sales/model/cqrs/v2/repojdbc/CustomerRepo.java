package com.acme.sales.model.cqrs.v2.repojdbc;

import java.sql.SQLException;

public class CustomerRepo extends com.acme.sales.model.cqrs.v1.repojdbc.CustomerRepo {

    public String getCustomerJSON(int customerId) throws SQLException {
        // 1. Create the SQL
        String sql = "SELECT * from customers WHERE customer_id="+customerId;

        // 2. Execute the SQL
        String customerJSON =  executeSQL(sql, true);

        return customerJSON;
    }
}
