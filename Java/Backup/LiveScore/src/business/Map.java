package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;


public class Map 
{
	public DBConnection db = null;
	public ResultSet rs = null;
	public ResultSet rs1 = null;
	public String sqlcmd = "";
	public Map()
	{
				
	}
	public void makeConnection()
	{
		try
		{
			if(db==null) db = new DBConnection();
			//db.getConnectionAndVerify();;
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
	//////////////////////////////////////////////////////////////////////////////////////
	public int insertMap(String team_id,String team_name)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			sqlcmd = "select * from score24.team_mapping where team_id='"+team_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{
				sqlcmd="insert into score24.team_mapping values('"+team_id+"','"+team_name+"')";
				response = db.stmt.executeUpdate(sqlcmd);
			}
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return response;
	}
	/////////////////////////////////////////////////////////////////////////////////////
	public int updateMap(String team_id,String team_name)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "update score24.team_mapping set team_name='"+team_name+"' where team_id='"+team_id+"'";
			response = db.stmt.executeUpdate(sqlcmd);
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return response;
	}
	/////////////////////////////////////////////////////////////////////////////////////
	public int deleteMap(String team_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "delete from score24.team_mapping where team_id='"+team_id+"'";
			response = db.stmt.executeUpdate(sqlcmd);
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return response;
	}
	//////////////////////////////////////////////////////////////////////////////////
	public ResultSet getMap(String team_id)
	{		
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "select * from score24.team_mapping where team_id='"+team_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return rs;
	}
	////////////////////////////////////////////////////////////////////////////////
	public int MapSync()
	{
		int response = 0;
		DBConnection db1 = new DBConnection();
		try
		{	
			db.getConnectionAndVerify();		
			db1.getConnectionAndVerify();
			db1.stmt = db1.con.createStatement();
			sqlcmd = "select * from score24.matches where is_active='1000' order by match_name";
			rs1 = db1.stmt.executeQuery(sqlcmd);
			int count = 0;
			while(rs1.next())
			{
				count++;	
				System.out.println(count);			
				String match_name = rs1.getString("match_name");
				System.out.println("Tran dau "+count+": "+match_name);
				String home_id = rs1.getString("home_id");
				String guest_id = rs1.getString("guest_id");
				String home_name = "";
				String guest_name = "";
				try
				{
					home_name = match_name.substring(0,match_name.indexOf("-"));
				}
				catch(Exception e)
				{
					home_name = "";
					e.printStackTrace();
				}
				try
				{
					guest_name = match_name.substring(match_name.indexOf("-")+1,match_name.length());
				}
				catch(Exception e)
				{
					guest_name = "";
					e.printStackTrace();
				}
				if(home_name!=null && home_name.length()>0 && guest_name!=null && guest_name.length()>0)
				{
					home_name = home_name.trim();			
					guest_name = guest_name.trim();
					if(home_id!=null && home_name!=null && home_id.length()>0 && home_name.length()>0)
					{
						this.insertMap(home_id,home_name);	
					}
					if(guest_id!=null && guest_name!=null && guest_id.length()>0 && guest_name.length()>0)
					{
						this.insertMap(guest_id,guest_name);
					}
				}				
			
			}
			rs1.close();
			db1.putConnection();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db1.putConnection();
			db.putConnection();
		}
		
		return response;
	}
	////////////////////////////////////////////////////////////////////////////////
	public int MatchSync()
	{
		int response = 0;
		DBConnection db1 = new DBConnection();
		try
		{
			db.getConnectionAndVerify();			
			db1.getConnectionAndVerify();			
			sqlcmd = "select * from score24.matches order by match_name";
			rs = db.stmt.executeQuery(sqlcmd);
			int count = 0;
			while(rs.next())
			{		
				String match_name = rs.getString("match_name");
				String match_id = rs.getString("match_id");
				String home_id = rs.getString("home_id");
				String guest_id = rs.getString("guest_id");				
				String home_name = "";
				String guest_name = "";
				sqlcmd = "select team_name from score24.team_mapping where team_id='"+home_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					home_name = rs1.getString("team_name");
				rs1.close();
				sqlcmd = "select team_name from score24.team_mapping where team_id='"+guest_id+"'";
				rs1 = db1.stmt.executeQuery(sqlcmd);
				if(rs1.next())
					guest_name = rs1.getString("team_name");
				rs1.close();
				
				if(home_name!=null && home_name.length()>0 && guest_name!=null && guest_name.length()>0)
				{
					count++;
					
					String new_match_name = home_name.trim()+" - "+guest_name.trim();					
					if(!new_match_name.equalsIgnoreCase(match_name))
					{	
						System.out.println("Cap nhat tran dau: "+count);			
						sqlcmd = "update score24.matches set match_name='"+new_match_name.replaceAll("'","''")+"' where match_id='"+match_id+"'";						
						db1.stmt.executeUpdate(sqlcmd);						
					}
				}
				db1.putConnection();	
				db.putConnection();				
			}
			rs.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db1.putConnection();
			db.putConnection();			
		}
		return response;
	}
	////////////////////////////////////////////////////////////////////////////////	

	public static void main(String[] args)
	{
		try
		{
			while (true)
			{
				try
				{
					Map client = new Map();
					client.makeConnection();
					client.MapSync();
					client.removeConnection();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				System.out.println((new Date())+" Mapping service completed, now sleep in 1 day...");				
				Thread.sleep(1000*60*1440);
			}
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
}
