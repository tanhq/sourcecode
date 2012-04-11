package neo.score.report;


import neo.score.db.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
public class mobilist_stream
{
	public static DBConnection db = null;
	public static ResultSet rs = null;
	public static String sqlcmd = "";
	public void makeConnection()
	{
		try
		{
			if(db==null) db = new DBConnection();
			//db.getConnectionAndVerify();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void removeConnection()
	{
		try
		{
			if(db!=null)
				db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}	
	public static void main(String[] args)
	{
		try
		{			
		
			java.util.Date date=new java.util.Date();
			String month = String.valueOf(date.getMonth()+1);
			if(Integer.parseInt(month)<10)
				month="0"+month;
			String year = String.valueOf(date.getYear()+1900);
			String tmp = month+"/"+year;
			year = year.substring(2,year.length());		
			SimpleDateFormat sdfm = new SimpleDateFormat("yyMM");
			String sqlTable = "log_mobilist_" + String.valueOf(sdfm.format(new java.util.Date()));			
			DBConnection db = new DBConnection();
			String sqlcmd = "";
			//while (true)
			//{
				try
				{
					ArrayList arrCount = new ArrayList();
					ArrayList arrTime = new ArrayList();
					db.getConnectionAndVerify();
					db.stmt = db.con.createStatement();
					mobilist_stream client = new mobilist_stream();
					client.makeConnection();
					sqlcmd = "";
					sqlcmd = "select count(distinct sender), to_char(time,'dd/mm/yyyy') from "+sqlTable+" ";
					sqlcmd+= "where (receiver = '918' or receiver = '04918') and (to_char(time,'mm/yyyy') = '"+tmp+"') ";
					sqlcmd+= "group by to_char(time,'dd/mm/yyyy') ";
					System.out.println(sqlcmd);
					rs = db.stmt.executeQuery(sqlcmd);
					while(rs.next())
					{
						arrCount.add(rs.getString(1));
						arrTime.add(rs.getString(2));	
					}
					rs.close();	
					for(int i=0;i<arrCount.size();i++)
					{
						int count = Integer.parseInt(arrCount.get(i).toString());
						String ngay = arrTime.get(i).toString();
						boolean fExit = false;
						sqlcmd = "select * from score24.mobilist_stream where ngay='"+ngay+"'";
						rs = db.stmt.executeQuery(sqlcmd);
						if(rs.next())
						{
							fExit = true;
						}
						rs.close();
						if(!fExit)
							sqlcmd = "insert into score24.mobilist_stream values(score24.mobilist_stream_seq.nextval,'"+ngay+"',"+count+",sysdate)";
						else
							sqlcmd = "update score24.mobilist_stream set tongso="+count+" where ngay='"+ngay+"'";
						System.out.println(sqlcmd);
						db.stmt.executeUpdate(sqlcmd);
					}			
					client.removeConnection();
					db.putConnection();				
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
									
					db.putConnection();
				}
				//System.out.println((new java.util.Date())+"MobiList Report Stream Services completed, now sleep in 10 minutes...");				
				//Thread.sleep(1000*60*3600);
			//}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
}