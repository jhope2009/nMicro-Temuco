package inf.uct.nmicro.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Statement;

public class ImportToDB {

    //Datos por default de la conexion
    static String strUser = "sgonzalez";
    static String strPassword = "q11IpXNDIkuO5l5I";
    //static String strPassword = "sebastian2016";
    static String strDB ="sgonzalez";
    //static String strHost = "164.77.114.247";
    static String strHost = "w3.inf.uct.cl";
    public Connection connection =null;
     
    //Constructor, le llegan los datos con los que se conectara al DBMS
    public void createConnection(String usr,String pw, String bd)
    {
        strUser = usr;
        strPassword = pw;
        strDB =bd;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");      
        } catch ( ClassNotFoundException e )
        {
            System.out.println("ERROR: Error al cargar la clase del Driver");       
        }
    }
     
    //Constructor, le llegan los datos con los que se conectara al DBMS
    // a dif. del otro constructor le llega tbn el host (servidor)
    public void createHostConnection() throws SQLException
    {
    this.connection=getConnection();
    }
     
    public Connection getConnection() throws SQLException 
    {
        //a continuacion vamos a formar la cadena de conexion, pero...
        //OJO aca debes poner el nombre de la instancia de SQL Server que vas a usar
        String url = "jdbc:mysql://"+strHost+"/"+strDB;
        return DriverManager.getConnection(url,strUser,strPassword);
    }
 
    //Cierra objeto Resultset
    public static void closeResultset(ResultSet rs)
    {   
        try
        {
            rs.close();
        } 
        catch(Exception ex)
        {}
    }
     
    //Cierra objeto Statement
    public static void closeStatement(Statement st)
    {
        try
        {
            st.close();
        } 
        catch(Exception ex)
        {}
    }
     
    //Cierra objeto Connection
    public static void closeConnection(Connection con)
    {   
        try
        {
            con.close();
        } 
        catch(Exception ex)
        {}
    }
}
