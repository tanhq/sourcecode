package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import neo.score24.object.RoundInfo;

public class Round 
{
	public DBConnection db = null;
	public ResultSet rs = null;
	public String sqlcmd = "";
	public Round()
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
	////////////////////////////////////////////////////////////////////////////
	public int insertRound(RoundInfo round)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			sqlcmd = "insert into score24.rounds values(round_id_seq.nextval,'"+round.getRoundName()+"',to_date('"+round.getTime()+"','dd/MM/yyyy'),'"+round.getRoundNameVN()+"','"+round.getStatus()+"','"+round.getLeagueID()+"')";
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
	////////////////////////////////////////////////////////////////////////////
	public int deleteRound(String round_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			sqlcmd="delete from score24.rounds where round_id='"+round_id+"'";
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
	////////////////////////////////////////////////////////////////////////////
	public int updateRound(RoundInfo round)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			sqlcmd = "update score24.rounds set round_name='"+round.getRoundName()+"',time=to_date('"+round.getTime()+"','dd/MM/yyyy'),round_name_vn='"+round.getRoundNameVN()+"',status='"+round.getStatus()+"',league_id='"+round.getLeagueID()+"' where round_id='"+round.getRoundID()+"'";
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
	////////////////////////////////////////////////////////////////////////////
	public int setStatus(String round_id,String status)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			sqlcmd="update score24.rounds set status='"+status+"' where round_id='"+round_id+"'";
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
	////////////////////////////////////////////////////////////////////////////
	public RoundInfo getRound(String round_id)
	{
		RoundInfo round = null;
		try
		{
			db.getConnectionAndVerify();
			db.stmt = db.con.createStatement();
			round = new RoundInfo();
			sqlcmd = "select * from score24.rounds where round_id='"+round_id+"'";
			rs = db.stmt.executeQuery(sqlcmd);
			if(rs.next())
			{
				round.setLeagueID(rs.getString("league_id"));
				round.setRoundID(rs.getString("round_id"));
				round.setRoundName(rs.getString("round_name"));
				round.setRoundNameVN(rs.getString("round_name_vn"));
				round.setStatus(rs.getString("status"));
				round.setTime(rs.getString("time"));
			}
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return round;
	}
}
