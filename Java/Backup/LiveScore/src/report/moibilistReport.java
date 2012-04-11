package neo.score.report;

import neo.score.db.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;
public class moibilistReport
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
		
		ArrayList arrLeagueID = new ArrayList();		
		ArrayList arrID = new ArrayList();
		String league_id = "";
		java.util.Date date=new java.util.Date();
		String month = String.valueOf(date.getMonth()+1);
		String year = String.valueOf(date.getYear()+1900);
		String tmp = month+"/"+year;
		year = year.substring(2,year.length());		
		SimpleDateFormat sdfm = new SimpleDateFormat("yyMM");
		String sqlTable = "log_mobilist_" + String.valueOf(sdfm.format(new java.util.Date()));  
		//String sqlTable = "log_mobilist_0901";
				
		System.out.println("Ket xuat bao cao thang "+sqlTable);
		long sum_request  = 0;
		long sum_response = 0;
		DBConnection db = new DBConnection();
		//while (true)
		//{
			try
			{
				db.getConnectionAndVerify();
				db.stmt = db.con.createStatement();
				moibilistReport client = new moibilistReport();
				client.makeConnection();
				//Cat nhat bang MOBILIST_CATE_REPORT
				int to = 0;
				int taonhom = 0;
				int xoanhom = 0;
				int xemnhom = 0;
				int themsdt = 0;
				int xoasdt = 0;
				sqlcmd = "SELECT count(*) FROM "+sqlTable+" where content like 'To%' or content like 'TO%' or content like 'to%' or content like 'tO%'";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
					to = rs.getInt(1);
				rs.close();
				sqlcmd = "SELECT count(*) FROM "+sqlTable+" where content like 'Cre%' or content like 'CRE%' or content like 'cre%'";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
					taonhom = rs.getInt(1);
				rs.close();
				sqlcmd = "SELECT count(*) FROM "+sqlTable+" where content like 'Del%' or content like 'DEL%' or content like 'del%'";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
					xoanhom = xoasdt = rs.getInt(1);
				rs.close();
				
				sqlcmd = "SELECT count(*) FROM "+sqlTable+" where content like 'View%' or content like 'VIEW%' or content like 'view%'";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
					xemnhom =  rs.getInt(1);
				rs.close();
				
				sqlcmd = "SELECT count(*) FROM "+sqlTable+" where content like 'Add%' or content like 'ADD%' or content like 'add%'";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
					themsdt = rs.getInt(1);
				rs.close();
				sqlcmd = "select * from score24.mobilist_cate_report where date_report like '%"+tmp+"'";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
					sqlcmd = "update score24.mobilist_cate_report set send="+to+",crea_group="+taonhom+",dele_group="+xoanhom+",view_group="+xemnhom+",add_mobile="+themsdt+",dele_mobile="+xoanhom+" where date_report like '%"+tmp+"'";
				else
					sqlcmd = "insert into score24.mobilist_cate_report values(mobilist_cate_report_seq.nextval,"+to+","+taonhom+","+xoanhom+","+xemnhom+","+themsdt+","+xoanhom+",'"+tmp+"',sysdate)";
				rs.close();
				db.stmt.executeUpdate(sqlcmd);
				//Cap nhat bang MOBILIST_REPORT
				sqlcmd = " select to_char(time,'dd/mm/yyyy') as times, count(*) as sum_response from score24."+sqlTable;		
				sqlcmd+= " where receiver = '918' or receiver = '04918'";
				sqlcmd+= " group by to_char(time,'dd/mm/yyyy') ";
				sqlcmd+= " order by to_char(time,'dd/mm/yyyy') desc";										
				Hashtable ht_response = new Hashtable();			
				rs = db.stmt.executeQuery(sqlcmd);		
				while(rs.next())
				{
					ht_response.put(rs.getString("times"), rs.getString("sum_response"));
				}
				rs.close();	
				
				sqlcmd = " select to_char(time,'dd/mm/yyyy') as times, count(*) as sum_response from "+sqlTable;
				sqlcmd+= " where sender = '918' or sender = '04918'";
				sqlcmd+= " group by to_char(time,'dd/mm/yyyy') ";
				sqlcmd+= " order by to_char(time,'dd/mm/yyyy') desc";					
				rs = db.stmt.executeQuery(sqlcmd);	
				ArrayList arrTime = new ArrayList();
				ArrayList arrCount = new ArrayList();
				while(rs.next())
				{
					arrTime.add(rs.getString("times"));
					arrCount.add(rs.getString("sum_response"));	
				}					
				rs.close();
				int count = 1;
				DBConnection db1 = new DBConnection();
				db1.getConnectionAndVerify();				
				db1.stmt = db1.con.createStatement();
				for(int i = 0;i<arrTime.size();i++)
				{
					String date_report = arrTime.get(i).toString();
					long requests = 0;
					if(ht_response.containsKey(date_report))
						requests  = Long.parseLong((String)ht_response.get(date_report));
					long responses = 0;	
					responses = Long.parseLong(arrCount.get(i).toString());
					sum_response += responses;
					sum_request  += requests;
					sqlcmd = "select * from score24.mobilist_report where date_report='"+date_report+"'";
					java.sql.ResultSet rs1 = db1.stmt.executeQuery(sqlcmd);
					if(rs1.next())
						sqlcmd = "update score24.mobilist_report set request="+requests+",response="+responses+",total="+(responses+requests)+",update_time=sysdate where date_report='"+date_report+"'";
					else
						sqlcmd = "insert into score24.mobilist_report values(mobilist_report_seq.nextval,'"+date_report+"',"+requests+","+responses+","+(requests+responses)+",sysdate)";
					rs1.close();	
					
					db1.stmt.executeUpdate(sqlcmd);						
					count++;
					System.out.println(count);
				}
				db1.putConnection();				
				client.removeConnection();
				db.putConnection();				
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
								
				db.putConnection();
			}
			//System.out.println((new java.util.Date())+"MobiList Report Services completed, now sleep in 10 minutes...");				
			//Thread.sleep(1000*60*3600);
		//}
		
	}
	catch(Exception e)
	{
		e.printStackTrace();
		
	}
	}
}