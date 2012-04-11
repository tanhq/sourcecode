package com.neo.wap;


import java.sql.*;
import java.io.*;
import javax.sql.*;
import javax.naming.*;
import java.util.*;

public class DBWAP3GConnection implements java.io.Serializable {

        // Connection Pool
        InitialContext initContext = null;
        DataSource ds = null;

        private static int maxCon = 80;
        private static int minCon = 10;
        private static int poolsize;
        public static int total=0;

        private static boolean firstTime = true;	

                
        private static String server = "10.252.20.66:1521";
    	private static String username = "my_wap";
    	private static String password = "pass4my_wap";
    	private static String serviceName = "orclwap";

        public static Connection[] cons = null;
        public static byte[] consState = null;
        public static long[] useTime = null;	
        public static Statement stmts[] = null;
        public static String[] sqlcommands = null;

        public static long lastRetry = 0;

        public Connection con = null;
        public Statement stmt = null;
        public PreparedStatement pstmt = null;	
        public int currentId = -1;
        public String currCommand="";

        private CheckDBWAP3GConnection cdb = null;

        public DBWAP3GConnection (){
                if (cons==null) cons = new Connection[maxCon];
                if (consState==null) consState = new byte[maxCon];  
                if (useTime==null) useTime = new long[maxCon];		
                if (stmts==null) stmts = new Statement[maxCon];
                if (sqlcommands==null) sqlcommands = new String[maxCon];
                if (firstTime) poolsize = maxCon;			
                firstTime = false;

        }

        public static void main(String[] args)
        {
                try
                {
                        DBWAP3GConnection ms=new DBWAP3GConnection();			
                        ms.getConnectionAndVerify();	
                        String sqlcmd = "select * from score24.matches where is_active='1000' and match_seq='0' and match_date-sysdate<=1/24/6 order by match_date";
                        java.sql.ResultSet rs = ms.stmt.executeQuery(sqlcmd);
                        while(rs.next())
                        {
                        	//System.out.println("OK");
                        }
                        ms.putConnection();
                } catch(Exception e) {
                        //e.printStackTrace();
                }
        }
        public void SetSqlCommand(String s){
          try {
            this.sqlcommands[this.currentId] = s;
          } catch (Exception e){
            //e.printStackTrace();
          }
        }
        //Tao connection moi
        private boolean createConnection(int id) throws Exception{
                try {
                        String connectionURL = "jdbc:oracle:thin:@"+server+":"+serviceName;						
                        //System.out.println("Create connection "+ (total++) +" to ORACLE..");
                        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                        cons[id] = DriverManager.getConnection(connectionURL,username,password);
                        this.con = cons[id];
                        this.stmt = this.con.createStatement();		
                        this.currentId = id;
                        return true;
                } catch (Exception e){
                        //e.printStackTrace();
                        return false;
                }
        }
        ////////////////////////////////////////////////////////////////////////////
        private boolean makeConnectionAndVerify() throws Exception{
                if (this.makeConnection()){
                        try {                          
                          stmt=con.createStatement();
                          stmt.executeQuery("select 1 from dual");
                          return true;
                        } catch (Exception e){
                          e.printStackTrace();
                          //System.out.println("Oracle Connection may be died, create new...");
                          return createConnection(currentId);
                        }			
                } else {
                        return false;
                }
        }
        ////////////////////////////////////////////////////////////////////////////
        private boolean makeConnection(){
                try {
                        int inuse=0;
                        int max=poolsize;
                        for (int i=0; i<poolsize; i++){
                                if (consState[i]==1) {
                                        inuse++;
                                }
                        }
                        //if (inuse>50)
                                //System.out.println("DBConn: total="+max+" created:"+total+" inuse:"+inuse);
                        if (cdb==null) cdb = new CheckDBWAP3GConnection();
                        cdb.start();

                        boolean free= false;
                        int closing = 0;
                        String closingCons = "";
                        
                        for (int i=0; i<poolsize; i++)
                        {//Tim kiem cac connection chua su dung
                          if (consState[i]==3)
                          {//Trang thai closing
                                  closing++;	
                                  closingCons += ","+i;			
                          } 
                          else if (consState[i]!=1)
                          {//Truong hop chua su dung co state khac 1
                                        //System.out.println("Found connection: "+i);
                                        free = true;
                                        consState[i] = 1;//in use
                                        sqlcommands[i] = this.currCommand;
                                        useTime[i] = System.currentTimeMillis();//Thoi gian bat dau su dung connection
                                        if (cons[i]!=null){
                                                this.con = cons[i];						
                                                this.stmt = stmts[i];
                                                this.currentId = i;
                                        } else {
                                                //System.out.println("Connection "+i+" is null, try to connect!");
                                                try {
                                                        //System.out.println("Create connection "+ i + "/" + (total++) +" to ORACLE..");
                                                        createConnection(i);
                                                        this.con = cons[i];	
                                                        this.stmt = stmts[i];					
                                                        this.currentId = i;
                                                        //System.out.println("Connection "+i+" created!");
                                                } catch (Exception e) {
                                                        //java.sql.SQLException: ORA-00020: maximum number of processes (300) exceeded
                                                        lastRetry = System.currentTimeMillis();
                                                        //System.out.println("Can not connect to ORACLE: "+e);
                                                        return false;
                                                }
                                        }
                                        currCommand="";//reset current command khi co phien lam viec moi
                                        return true;
                                }				
                        }			
                        if (!free){
                                System.out.println("ORACLE het ket noi toi co so du lieu!");
                        }
                        if (closing>10){
                                System.out.println("Co nhieu connection khong dong duoc: "+closing+" connections: "+closingCons);
                        }
                        return false;
                } catch (Exception e) {
                        System.out.println("Can not connect to ORACLE: "+e);
                        return false;
                }
        }
        ////////////////////////////////////////////////////////////////////////////
        public Connection getConnection () {		
                try{
                        if (makeConnection())
                                return con;
                        else
                                return null;
                } catch (Exception e) {
                        return null;
                }
        }
        ////////////////////////////////////////////////////////////////////////////
        public Connection getConnectionAndVerify () {
                try{
                        if (makeConnectionAndVerify())
                                return con;
                        else
                                return null;
                } catch (Exception e) {
                        return null;
                }
        }

        public boolean putConnection(){
                if (currentId>=0 && currentId<consState.length){
                        //System.out.println("Giai phong connection "+currentId+" toi Consolidate");
                        consState[currentId] = 2;
                }
                try {
                        if (stmt!=null) stmt.close();
                } catch(Exception e){
                        //e.printStackTrace();
                }		
                return true;
        }
        public boolean setPrepareStatement(String i_Sql)
        {
                try 
                {
                  this.pstmt = this.con.prepareStatement(i_Sql);
                  return true;
                }
                catch(SQLException e)
                {
                  return false;
                }
        }

        public void removeStatement(CallableStatement cstmt){
                try{
                  if (cstmt!=null) cstmt.close();
                }catch (SQLException e){
                  //e.printStackTrace();
                }
        }
        private void removeConnection(){
                try{
                  if (this.con!=null) con.close();
                  con = null;
                  if (cons[currentId]!=null) cons[currentId].close();
                  cons[currentId] = null;
                }catch (SQLException e){
                  //e.printStackTrace();
                }
        }
}

