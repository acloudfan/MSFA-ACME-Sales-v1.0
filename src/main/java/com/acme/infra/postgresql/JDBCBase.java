package com.acme.infra.postgresql;

import com.acme.utils.JDBCResultSetToJSON;
import org.json.JSONArray;

import java.sql.*;

/**
 * Base class for all PostgreSQL operations.
 * PostgreSQL Repositories will be created by extending this class.
 *
 * In order to keep the consumers independent of the JDBC related API, this implementation
 * always return JSON that can be converted to model representation.
 *
 * Uses the utility class com.acme.utils.JDBCResultSetToJSON to convert JDBC result set
 * into a JSON Object.
 *
 * PRE-REQUISITES: Basic knowledge of JSON & JDBC
 *
 * PS: The performance of this class may be improved - did it this way to simplify usage
 */
abstract public class JDBCBase {

    // MUST Change this for your setup
    // This infor is available on the Elephant SQL 'Detail' page for instance
    // If you are running PostgreSQL localy then you need to check the setp of local instance
    protected final static String JDBC_HOST="ziggy.db.elephantsql.com";
    protected final static String JDBC_PORT="5432";
    protected final static String JDBC_USER="zuzyxits";
    protected final static String JDBC_PASSWORD="1EvNtOtnUM_uj4OMgzwDa0mXT-ag5WUf";

    // Name of the default DB is the same as the user - you need to change it for local DB
    protected final static String JDBC_DB= JDBC_USER;


    protected final static String JDBC_URL="jdbc:postgresql://"+JDBC_HOST+":"+JDBC_PORT+"/"+ JDBC_DB;


    // JDBC Driver
    protected final static String JDBC_PG_DRIVER = "org.postgresql.Driver";

    // PostgreSQL JDBC driver MUST be available
    static {
        try {
            Class.forName(JDBC_PG_DRIVER);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param sql                 SQL Statement to be executed
     * @param firstElementOnly    true = if ONLY the first row is to be returned from a collection
     * @return
     * @throws SQLException
     */
    protected String executeSQL(String sql, boolean firstElementOnly) throws SQLException {
        Connection connection = null;

        // Logic for executing the query
        try {

            // 1. Create the connection to DB
            connection  = DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
            connection.setAutoCommit(true);

            // 2. Create the SQL Statement
            Statement statement = connection.createStatement();

            // 3. Execute the query
            ResultSet rs = statement.executeQuery(sql);

            // 4. Convert the ResultSet to JSON
            JSONArray jsonArray = JDBCResultSetToJSON.convert(rs);

            // 5. Close the statement
            statement.close();

            // 6. Check if only first element is to be returned
            if(firstElementOnly){
                if(jsonArray.length() > 0){
                    return jsonArray.getJSONObject(0).toString(4);
                } else {
                    return "{}";
                }
            }

            // 7. Return pretty JSON
            return jsonArray.toString(4);

        } finally {
            // 8. Ensure connection is closed
            try{  connection.close(); }catch(Exception e){}
        }
    }



    /**
     * This will execute the SQL Update Query
     * Takes the SQL query as an argument - Returns a JSON
     */
    protected String executeSQLUpdate(String sql) throws SQLException {
        Connection connection = null;
        String  returnJson = "{}";

        // Executes the SQL Update Statement
        try {

            // 1. Create the connection to DB
            connection  = DriverManager.getConnection(JDBC_URL,JDBC_USER,JDBC_PASSWORD);
            connection.setAutoCommit(false);

            // 2. Create the Statement
            Statement statement = connection.createStatement();

            // 3. Execute the SQL
            statement.execute(sql);

            // 4. Convert the JDBC ResultSet into JSON
            ResultSet  rs = statement.getResultSet();
            if(rs != null) {
                returnJson = JDBCResultSetToJSON.convert(rs).getJSONObject(0).toString(4);
                ;
            } else {
                returnJson = "{}";
            }

            // 5. Commit
            connection.commit();

            // 6. Close the statement
            statement.close();

        } finally {
            // 7. Ensure connection is closed
            try{  connection.close(); }catch(Exception e){}
        }

        return returnJson;
    }

    /**
     * Execute a query
     */
    protected String executeSQL(String sql) throws SQLException {
        return  executeSQL(sql, false);
    }


}
