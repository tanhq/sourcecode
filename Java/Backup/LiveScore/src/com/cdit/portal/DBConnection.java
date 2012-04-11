package com.cdit.portal;


import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.naming.*;
import com.cdit.portal.VMSException;
import com.cdit.portal.VMSConfig;
import com.cdit.portal.object.QueryInfo;
import com.cdit.portal.util.Listener;


public class DBConnection {
  // Connection Pool
  private InitialContext initContext = null;
  private DataSource ds = null;
  private Connection conn = null;
  public PreparedStatement pstmt = null;
  public Statement stmt = null;
  static public ResultSet rs = null;

  public static void main(String args[]){
  		DBConnection client = new DBConnection();  		
  		client.createConnection();
  		String sql = "select * from xmses";
  		try
  		{
  			rs = client.stmt.executeQuery(sql);
  			if(rs.next())
  			{
  				System.out.println(rs.getString(1));
  			}
  			rs.close();
  		}
  		catch(Exception e)
  		{
  			e.printStackTrace();
  		}
  }

  public void DBConnection (){
  }

  public Connection getConnection () {
    try{
		String driverName = "oracle.jdbc.driver.OracleDriver";
		String connectionURL = "jdbc:oracle:thin:@(DESCRIPTION= (LOAD_BALANCE=on)(FAILOVER=ON)(ADDRESS=(PROTOCOL=TCP)(HOST= 10.252.20.74)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST= 10.252.20.79)(PORT=1521))  (CONNECT_DATA=(SERVICE_NAME=mbfdb.neo.com.vn)))";
		String username = "vms";
		String password = "inf13579";
		
		Class.forName(driverName).newInstance();
		conn = DriverManager.getConnection(connectionURL,username,password);    	
		
      	return conn;
    } catch (Exception e) {
      	System.out.println("Error [DBConnection]: " + e.toString());
    }
    Connection connection1 = null;
    return connection1;
  }

  public boolean setConnection () {
  	return createConnection();
  }

  public boolean createConnection () {
    try{
	  		String driverName = "oracle.jdbc.driver.OracleDriver";
			String connectionURL = "jdbc:oracle:thin:@(DESCRIPTION= (LOAD_BALANCE=on)(FAILOVER=ON)(ADDRESS=(PROTOCOL=TCP)(HOST= 10.252.20.74)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST= 10.252.20.79)(PORT=1521))  (CONNECT_DATA=(SERVICE_NAME=mbfdb.neo.com.vn)))";
			String username = "vms";
			String password = "inf13579";
		
			
		  	Class.forName(driverName).newInstance();
		  	conn = DriverManager.getConnection(connectionURL,username,password);
		  	stmt = conn.createStatement();
	      	return true;
    } catch (Exception e) 
    {
      System.out.println(e.getMessage());
      System.out.println("Error [DBConnection-createConnection]: " + e.toString());
      //Listener.sendError(5,"Mobifone Portal - Khong the ket noi toi co so du lieu!\r\nHay kiem tra database server.");
      return false;
    }
  }

  public boolean setPrepareStatement(String i_Sql){
    try {
      this.pstmt = this.conn.prepareStatement(i_Sql);
      return true;
    }catch(SQLException e){
      return false;
    }
  }

  /*
  Execution Update
  */
  public QueryInfo executeUpdate(String i_Sql){
    QueryInfo qi = new QueryInfo();
    if (this.conn == null) {
      System.out.println("Error [DBConnection]: connection is null");
      qi.setError(VMSException.DB_CONNECT_ERROR);
      return qi;
    }
    try{
      pstmt=conn.prepareStatement(i_Sql);
      pstmt.executeUpdate();
      return qi;
    }catch (SQLException e){
      System.out.println("Error [DBConnection]: " + e.toString());
      qi.setError(VMSException.DB_EXECUTESQL_ERROR);
      qi.setErrorMessage(e.toString());
      return qi;
    }
  }

  /*
  Execution Query
  */
  public QueryInfo executeQuery(String i_Sql){
    QueryInfo qi = new QueryInfo();
    if (this.conn == null) {
      System.out.println("Error [DBConnection]: connection is null");
      qi.setError(VMSException.DB_CONNECT_ERROR);
      return qi;
    }
    try{
      pstmt=conn.prepareStatement(i_Sql);
      rs = pstmt.executeQuery();
      qi.setResult(rs);
      return qi;
    }catch (SQLException e){
      System.out.println("Error [DBConnection]: " + e.toString());
      qi.setError(VMSException.DB_EXECUTESQL_ERROR);
      qi.setErrorMessage(e.toString());
      return qi;
    }
  }

  	/*
  	Remove connection
  	*/
  	public void removeConnection(){
		try{
		  if (stmt!=null) stmt.close();
		}catch (Exception e){
		  System.out.println("Error [DBConnection][Remove Statement]: " + e.toString());
		}
		try{
		  if (pstmt!=null) pstmt.close();
		}catch (Exception e){
		  System.out.println("Error [DBConnection][Remove Statement]: " + e.toString());
		}
		try{
		  if (conn!=null) conn.close();
		}catch (Exception e){
		  System.out.println("Error [DBConnection][Remove Statement]: " + e.toString());
		}
  	}
    public void closeConnection(Connection con)
    {
        try
        {
            if(con != null)
                con.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error [DBConnection]: ".concat(String.valueOf(String.valueOf(e.toString()))));
        }
    }
  
}
