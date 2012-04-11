package neo.score.report;

import neo.score.db.*;
import java.util.*;
import java.sql.*;
public class reportServices
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
	//Ket xuat bao cao theo tran dau
	public void reportMatch(String round_id,String league_id)
	{
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			sqlcmd="select a.match_id, a.match_name, a.home_id, a.guest_id, a.is_active, to_char(a.match_date,'dd/MM/yyyy HH24:mi:ss') as thoigian, a.referee, a.stadium, a.parent_id,a.eventprimary_id,a.result,b.round_name_vn,c.league_name_vn,d.status_name_vn from score24.matches a,score24.rounds b,score24.leagues c,score24.livescore_status d where a.eventprimary_id='"+league_id+"' and a.parent_id='"+round_id+"' and a.parent_id=b.round_id and b.round_id='"+round_id+"' and a.eventprimary_id=c.league_id and c.league_id='"+league_id+"' and a.is_active=d.status_id and d.status_type='match' order by a.match_date";
			rs = db.stmt.executeQuery(sqlcmd);
			while(rs.next())
			{
				
				String match_id = rs.getString("match_id");
				String match_name = rs.getString("match_name");
				String home_id = rs.getString("home_id");
				String guest_id = rs.getString("guest_id");
				String is_active = rs.getString("status_name_vn");
				String match_date = rs.getString("thoigian");				
				String round_name = rs.getString("round_name_vn");
				String result = rs.getString("result");	
			
				ResultSet rs1 = null;			
				DBConnection db1 = new DBConnection();
				db1.getConnectionAndVerify();
				int count = 0;
				sqlcmd = "select count(*) from score24.livescore_subscriber where match_id='"+match_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					count = rs1.getInt(1);
				rs1.close();					
				sqlcmd = "select count(*) from score24.livescore_subscriber_log where match_id='"+match_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					count += rs1.getInt(1);
				rs1.close();
					
				//Insert vao bang REPORT_MATCH
				boolean fInsert = true;
				sqlcmd = "select * from REPORT.report_match where match_id='"+match_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					fInsert = false;
				rs1.close();
				if(fInsert)
				{
					System.out.println("Report match: "+match_id);
					sqlcmd="insert into report.report_match values(report.report_match_seq.nextval,'"+match_name+"','"+match_date+"','"+round_name+"','"+result+"',"+count+",'"+match_id+"','"+round_id+"','"+league_id+"')"; 
					db1.stmt.executeUpdate(sqlcmd);
				}
				else
				{
					System.out.println("Report match: "+match_id);
					sqlcmd="update report.report_match set reg_num="+count+" where match_id='"+match_id+"' and round_id='"+round_id+"' and league_id='"+league_id+"'"; 
					db1.stmt.executeUpdate(sqlcmd);
				}			
				db1.putConnection();
			}
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();	
			db.putConnection();
		}
	}
	//Ket xuat bao cao theo vong dau
	public void reportRound(String league_id)
	{
		try
		{
			db.getConnection();
			db.stmt = db.con.createStatement();
			sqlcmd="select a.round_id,a.round_name,a.time,a.round_name_vn,a.league_id,b.league_name,b.league_name_vn,c.status_name_vn from score24.rounds a,score24.leagues b,score24.livescore_status c where a.status=c.status_id and c.status_type='round' and a.league_id=b.league_id and a.league_id='"+league_id+"' order by a.round_id";
			rs = db.stmt.executeQuery(sqlcmd);
			while(rs.next())
			{
				String round_id = rs.getString("round_id");
				String round_name = rs.getString("round_name");
				String round_name_vn = rs.getString("round_name_vn");
				String league_name = rs.getString("league_name");
				String league_name_vn = rs.getString("league_name_vn");			
				String time = rs.getString("time");
				String status = rs.getString("status_name_vn");	
				
				ResultSet rs1 = null;			
				DBConnection db1 = new DBConnection();
				db1.getConnectionAndVerify();
				int count = 0;
				
				sqlcmd = "select count(*) from score24.livescore_subscriber where round_id='"+round_id+"' and league_id='"+league_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					count += rs1.getInt(1);
				rs1.close();					
				sqlcmd = "select count(*) from score24.livescore_subscriber_log where round_id='"+round_id+"' and league_id='"+league_id+"'";				
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					count += rs1.getInt(1);
				
				//Insert vao bang REPORT_ROUND
				boolean fInsert = true;
				sqlcmd = "select * from REPORT.report_round where round_id='"+round_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					fInsert = false;
				rs1.close();
				if(fInsert)
				{
					System.out.println("Report round: "+round_id);
					sqlcmd="insert into report.report_round values(report.report_round_seq.nextval,'"+round_name+"','"+round_name_vn+"','"+league_name+"',"+count+",'"+round_id+"','"+league_id+"')"; 
					db1.stmt.executeUpdate(sqlcmd);
				}
				else
				{
					System.out.println("Report round: "+round_id);
					sqlcmd="update report.report_round set reg_num="+count+" where round_id='"+round_id+"' and league_id='"+league_id+"'"; 
					db1.stmt.executeUpdate(sqlcmd);
				}
				db1.putConnection();						
			}
			rs.close();
			db.putConnection();			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
	}
	//Ket xuat bao cao theo giai dau
	public void reportLeague()
	{
		try
		{
			db.getConnection();
			db.stmt = db.con.createStatement();
			sqlcmd="select a.league_id,a.league_name,a.league_name_vn,a.status,a.season,to_char(a.statrt_date,'dd/MM/yyyy') as ngay_bat_dau,to_char(end_date,'dd/MM/yyyy') as ngay_ket_thuc,b.country_name_vn,c.status_name_vn from score24.leagues a,score24.country b,score24.livescore_status c where a.country_id=b.country_id and a.status=c.status_id and c.status_type='league' order by a.league_name";
			rs = db.stmt.executeQuery(sqlcmd);
			while(rs.next())
			{
				String league_id = rs.getString("league_id");
				String league_name = rs.getString("league_name");
				String league_name_vn = rs.getString("league_name_vn");
				String country = rs.getString("country_name_vn");
				String status = rs.getString("status_name_vn");
				String season = rs.getString("season");	
				
				ResultSet rs1 = null;			
				DBConnection db1 = new DBConnection();
				db1.getConnectionAndVerify();
				int count = 0;
				
				sqlcmd = "select count(*) from score24.livescore_subscriber where league_id='"+league_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					count += rs1.getInt(1);
				rs1.close();					
				sqlcmd = "select count(*) from score24.livescore_subscriber_log where league_id='"+league_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					count += rs1.getInt(1);
				rs1.close();
				//Insert vao bang REPORT_LEAGUE
				boolean fInsert = true;
				sqlcmd = "select * from REPORT.report_league where league_id='"+league_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					fInsert = false;
				rs1.close();
				if(fInsert)
				{
					System.out.println("Report league: "+league_id);
					sqlcmd="insert into report.report_league values(report.report_league_seq.nextval,'"+league_name+"','"+league_name_vn+"','"+country+"','"+season+"',"+count+",'"+league_id+"')"; 
					db1.stmt.executeUpdate(sqlcmd);
				}
				else
				{
					System.out.println("Report league: "+league_id);
					sqlcmd="update report.report_league set reg_num="+count+" where league_id='"+league_id+"'"; 
					db1.stmt.executeUpdate(sqlcmd);
				}		
				db1.putConnection();
			}
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
	}
	public static void main(String[] args)
	{
		ArrayList arrLeagueID = new ArrayList();		
		ArrayList arrID = new ArrayList();
		String league_id = "";
		try
		{
	
			//while (true)
			//{
				
				try
				{		
							
					reportServices client = new reportServices();
					client.makeConnection();	
					DBConnection db = new DBConnection();
					db.getConnection();				
					db.stmt = db.con.createStatement();
					sqlcmd = "select league_id from score24.leagues";
					rs = db.stmt.executeQuery(sqlcmd);
					while(rs.next())
					{
						arrLeagueID.add(rs.getString("league_id"));	
					}
					rs.close();
					client.reportLeague();
					for(int i=0;i<arrLeagueID.size();i++)
					{
						db.getConnection();
						db.stmt=db.con.createStatement();
						league_id = arrLeagueID.get(i).toString().trim();
						client.reportRound(league_id);
						sqlcmd = "select round_id from rounds where league_id='"+league_id+"'";
						rs = db.stmt.executeQuery(sqlcmd);
						while(rs.next())
						{
							arrID.add(rs.getString("round_id"));	
						}
						rs.close();
						for(int j=0;j<arrID.size();j++)
						{
							client.reportMatch(arrID.get(j).toString(),league_id);
						}
						db.putConnection();	
					}		
					db.putConnection();			
				}
				catch(Exception ex)
				{					
					ex.printStackTrace();
				}
				//System.out.println((new java.util.Date())+"LiveScore Report Services completed, now sleep in 1 day...");				
				//Thread.sleep(1000*60*3600);			
			//}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}	
	}
}