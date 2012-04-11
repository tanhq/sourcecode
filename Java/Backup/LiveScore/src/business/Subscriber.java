package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import neo.score24.object.SubscriberInfo;

public class Subscriber 
{
	public DBConnection db = null;
	public ResultSet rs = null;
	public String sqlcmd = "";
	public Subscriber()
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
	public int insertSubscriber(SubscriberInfo subscriber)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "select * from score24.livescore_subscriber where match_id='"+subscriber.getMatchID()+"' and league_id='"+subscriber.getLeagueID()+"' and msisdn='"+subscriber.getMsisdn()+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(!rs.next())
			{
				sqlcmd="insert into score24.livescore_subscriber values(score24.livescore_subscriber_seq.nextval,'"+subscriber.getMatchID()+"',sysdate,'"+subscriber.getUserStatus()+"','"+subscriber.getMsisdn()+"','"+subscriber.getRoundID()+"','"+subscriber.getLeagueID()+"','"+subscriber.getType()+"')";
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
	public int updateSubscriber(SubscriberInfo subscriber)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "update score24.livescore_subscriber set match_id='"+subscriber.getMatchID()+"',register_time=sysdate,user_status='"+subscriber.getUserStatus()+"',msisdn='"+subscriber.getMsisdn()+"',round_id='"+subscriber.getRoundID()+"',league_id='"+subscriber.getMatchID()+"' where user_id='"+subscriber.getUserID()+"'";
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
	public int deleteSubscriber(String msisdn,String match_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "delete from score24.livescore_subscriber where msisdn='"+msisdn+"' and match_id='"+match_id+"'";
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
	public ResultSet getSubscriber(String user_id)
	{		
		try
		{
			db.getConnectionAndVerify();
			sqlcmd = "select * from score24.livescore_subscriber where user_id='"+user_id+"' order by match_id";
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
	
	/////////////////////////////////////////////////////////////////////////////////
	public int setUserStatus(String user_id,String match_id,String user_status)
	{
		int response =0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="update score24.livescore_subscriber set user_status='"+user_status+"' where user_id='"+user_id+"' and match_id='"+match_id+"'";
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
}
