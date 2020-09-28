package edu.jsu.mcis; 

import java.sql.*;
import org.json.simple.*;


public class databasetest {
/**
     * A method designed to test SQL database interfacing via Java; demonstrates
     * the results of an SQL query being parsed and converted into JSON
     * @return A JSON String representing to contents of a specific database
     * table
     */
    public static JSONArray getJSONData() {
        
        Connection conn = null;
        PreparedStatement pstSelect = null, pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String query;
        JSONArray convertedQueryResults = new JSONArray();
        
        
        boolean hasresults;
        int resultCount, columnCount, updateCount = 0;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/db_test");
            String username = "root";
            String password = "Apple151#";
            System.out.println("Connecting to " + server + "...");
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */
          if (conn.isValid(0)) {
                
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query); 
                
                hasresults = pstSelect.execute();

                if (hasresults) {
                        
                    resultset = pstSelect.getResultSet();
                    metadata = resultset.getMetaData();
                    columnCount = metadata.getColumnCount();
                        
                    while (resultset.next()) {
                        
                        JSONObject JO = new JSONObject();
                        
                        // start from index = 2 to skip key field `id` specified in schema
                        for (int i = 2; i <= columnCount; i++) {
                            
                            String key = metadata.getColumnLabel(i);
                            
                            JO.put(key,resultset.getString(key));
                            
                        }
                        
                        convertedQueryResults.add(JO);
                        
                    }
                        
                }
                
            }
                
            conn.close();
            
        }  
           
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }
            
            if (pstUpdate != null) { try { pstUpdate.close(); pstUpdate = null; } catch (Exception e) {} }
            
        }
    return convertedQueryResults;  
    }
    public static void main(String[] args) {
        
        System.out.println(getJSONData().toJSONString());
        
    }
}