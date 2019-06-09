
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import utilities.Configuration;

public class DataConnection {
    private static Connection con=null;

    public DataConnection() {
    }
    
    public static Connection getConnection() throws Exception{
        if(con==null){
            Class.forName(Configuration.Driver_Name);
            con=DriverManager.getConnection(Configuration.CONNECTION_URL, Configuration.DB_USER, Configuration.DB_PASS);
        }
        return con;
    }
    
    public static void closeConnection() throws Exception{
        if(con!=null){
        con.close();
        }
    }
    
    public static PreparedStatement getStatement(String query) throws Exception{
        return getConnection().prepareStatement(query);
    }
}
