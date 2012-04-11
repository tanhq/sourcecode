package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;


public class Card
{
	public DBConnection db = null;
	public ResultSet rs = null;
	public String sqlcmd = "";
	public Card()
	{
				
	}
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
	//////////////////////////////////////////////////////////////////////////////////////
	public int insertCard(String event_id,String match_id,String player_id,String player_name,int minutes,String team_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			String team_name = "";
			sqlcmd = "select * from score24.team_mapping where team_id='"+team_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(rs.next())
				team_name = rs.getString("team_name");
			rs.close();				
			sqlcmd = "select * from score24.Cards where match_id='"+match_id+"' and event_id='"+event_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{
				sqlcmd="insert into score24.Cards values('"+event_id+"','"+match_id+"','"+player_id+"','"+player_name+"',"+minutes+",sysdate,'"+team_id+"','"+team_name+"')";
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
	//////////////////////////////////////////////////////////////////////////////////////
	public int insertCard(String event_id,String match_id,String player_id,String player_name,int minutes,String team_id,String team_name)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			String tmp_name = "";
			sqlcmd = "select * from score24.team_mapping where team_id='"+team_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(rs.next())
				tmp_name = rs.getString("team_name");
			rs.close();	
			try
			{
				if(tmp_name!=null && tmp_name.length()>0)
					team_name = tmp_name;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
			sqlcmd = "select * from score24.Cards where match_id='"+match_id+"' and event_id='"+event_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{
				sqlcmd="insert into score24.Cards values('"+event_id+"','"+match_id+"','"+player_id+"','"+player_name.replaceAll("'","''")+"',"+minutes+",sysdate,'"+team_id+"','"+team_name.replaceAll("'","''")+"')";
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
	public int updateGoad(String event_id,String match_id,String player_id,String player_name,int minutes,String team_id,String team_name)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "update score24.Cards set player_id='"+player_id+"',player_name='"+player_name+"',minutes="+minutes+",update_time=sysdate,team_id='"+team_id+"',team_name='"+team_name+"' where event_id='"+event_id+"' and match_id='"+match_id+"'";
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
	public int deleteCard(String event_id,String match_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "delete from score24.Cards where event_id='"+event_id+"' and match_id='"+match_id+"'";
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
	public int filCard(ArrayList arrID,String match_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			int counter = 0;
			sqlcmd = "delete from score24.Cards where (";
			for(int i=0;i<arrID.size();i++)
			{
				counter++;
				if(counter==arrID.size())
					sqlcmd += " event_id != '"+arrID.get(i).toString()+"')";
				else
					sqlcmd += " event_id != '"+arrID.get(i).toString()+"' or";
			}
			sqlcmd += " and match_id='"+match_id+"'";
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
	////////////////////////////////////////////////////////////////////////////////
	public String getListofCards(String match_id)
	{		
		String str = "";
		try
		{
			db.getConnectionAndVerify();
			int count = 0;
			String team_id="";
			sqlcmd = "select home_id,guest_id from score24.matches where match_id='"+match_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);	
			String strId="";		
			if(rs.next())
			{
				strId=rs.getString("home_id")+","+rs.getString("guest_id");	
			}
			rs.close();
			String[] arrId = null;
			if(strId.length()>0)
				arrId = strId.split(",");
			
			if(arrId!=null && arrId.length>0)
			{
				//Neu co ban thang thi moi xet
				boolean fCard = false;
				sqlcmd = "select * from score24.Cards where match_id='"+match_id+"'";
				rs = db.stmt.executeQuery(sqlcmd);
				if(rs.next())
					fCard = true;
				rs.close();	
				if(fCard)
				{
				
					//str = "Danh sach cau thu nhan the: ";
					for(int i=0;i<arrId.length;i++)
					{
						count = 0;
						team_id = arrId[i];
						if(team_id!=null && team_id.length()>0)
						{
							String team_name = "";				
							
							sqlcmd = "select a.event_id,a.match_id,a.player_id,a.player_name,a.minutes,a.update_time,a.team_id,b.team_name from score24.Cards a,score24.team_mapping b where a.match_id='"+match_id+"' and a.team_id='"+team_id+"' and a.team_id=b.team_id order by a.team_id,a.minutes";
							rs = db.stmt.executeQuery(sqlcmd);
							String tmp = "";
							
							while(rs.next())
							{
								team_name = rs.getString("team_name");
								count++;
								tmp += rs.getString("player_name")+" "+rs.getString("minutes")+", ";	
							}
							rs.close();
							if(count>0)
							{
								tmp = tmp.trim();
								tmp = tmp.substring(0,tmp.length()-1);
								str += ", "+tmp;
							}
						}
					}
					str = str.trim();
					if(str.startsWith(","))
						str = str.substring(1,str.length());
					System.out.println("Danh sach cau thu bi the do");
				}	
			}
			db.putConnection();		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return str;
	}

	public static void main(String[] args)
	{
		try
		{
			Card client = new Card();
			client.makeConnection();
			String Cards = client.getListofCards("518163");
			client.removeConnection();
			System.out.println(Cards+",");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
