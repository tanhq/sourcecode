package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import neo.score24.object.LeagueInfo;

public class League 
{
	public DBConnection db = null;
	public ResultSet rs = null;
	public String sqlcmd = "";
	public League()
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
	public int insertLeague(LeagueInfo league)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="insert into score24.Leagues values(league_id_seq.nextval,'"+league.getLeagueName()+"','"+league.getLeagueNameVN()+"','"+league.getStatus()+"','"+league.getSeason()+"','"+league.getCountryID()+"',to_date('"+league.getStartDate()+"','dd/MM/yyyy',to_date('"+league.getEndDate()+"','dd/MM/yyyy')))";
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
	public int deleteLeague(String league_id)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="delete from score24.leagues where league_id='"+league_id+"'";
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
	public int updateLeague(LeagueInfo league)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="update score24.leagues set league_name='"+league.getLeagueName()+"',league_name_vn='"+league.getLeagueNameVN()+"',status='"+league.getStatus()+"',season='"+league.getSeason()+"',country_id='"+league.getCountryID()+"',start_date=to_date('"+league.getStartDate()+"','dd/MM/yyyy'),end_date=to_date('"+league.getEndDate()+"','dd/MM/yyyy') where league_id='"+league.getLeagueID()+"'";
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
	public int setStatus(String league_id,String status)
	{
		int response = 0;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="update score24.leagues set status='"+status+"' where league_id='"+league_id+"'";
			db.stmt.executeUpdate(sqlcmd);
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
	public ResultSet getLeague(String league_id)
	{
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="select * from score24.leagues where league_id='"+league_id+"'";
			rs=db.stmt.executeQuery(sqlcmd);
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return rs;
	}	
}
