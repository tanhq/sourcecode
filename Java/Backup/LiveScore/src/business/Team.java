package neo.score.business;

import neo.score.db.DBConnection;
import java.sql.*;
import java.util.Date;
import neo.score24.object.TeamInfo;

public class Team 
{
	public DBConnection db = null;
	public ResultSet rs = null;
	public String sqlcmd = "";
	public Team()
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
	//Lay thong tin ve doi bong
	public TeamInfo getTeam(String team_id)
	{
		TeamInfo team = null;
		try
		{
			db.getConnectionAndVerify();
			sqlcmd="select team_id,team_type,short_name,full_name,display_name,team_alias,team_icon,to_char(date_found,'yyyy') as nam_thanh_lap,join_fifa,rank_fifa,chairman,coach,stadium,capacity,address,telephone,fax,website,team_dress,country_id from score24.team_info where team_id='"+team_id+"'";
			rs=db.stmt.executeQuery(sqlcmd);
			if(rs.next())
			{
				team = new TeamInfo();
				if(rs.getString("Address")==null || rs.getString("Address").length()==0)
					team.setAddress("unknown");
				else
					team.setAddress(rs.getString("Address"));
				
				if(rs.getString("capacity")==null || rs.getString("capacity").length()==0)	
					team.setCapacity(0);
				else
					team.setCapacity(rs.getInt("capacity"));	
					
				if(rs.getString("chairman")==null || rs.getString("chairman").length()==0)
					team.setChairman("unknown");
				else 
					team.setChairman(rs.getString("chairman"));
					
				if(rs.getString("coach")==null || rs.getString("coach").length()==0)
					team.setCoach("unknown");
				else
					team.setCoach(rs.getString("coach"));
					
				team.setCountryID(rs.getString("country_id"));
				if(rs.getString("nam_thanh_lap")==null || rs.getString("nam_thanh_lap").length()==0)
					team.setDateFound("unknown");
				else
					team.setDateFound(rs.getString("nam_thanh_lap"));
					
				team.setDisplayName(rs.getString("display_name"));
				
				if(rs.getString("fax")==null || rs.getString("fax").length()==0)
					team.setFax("unknown");
				else 
					team.setFax(rs.getString("fax"));
					
				team.setFullName(rs.getString("full_name"));
				
				if(rs.getString("join_fifa")==null || rs.getString("join_fifa").length()==0)
					team.setJoinFifa("unknown");
				else
					team.setJoinFifa(rs.getString("join_fifa"));	
					
				if(rs.getString("rank_fifa")==null || rs.getString("rank_fifa").length()==0)
					team.setRankFifa("unknown");
				else
					team.setRankFifa(rs.getString("rank_fifa"));
				team.setShortName(rs.getString("short_name"));
				team.setStadium(rs.getString("stadium"));
				team.setTeamAlias(rs.getString("team_alias"));
				team.setTeamDress(rs.getString("team_dress"));
				team.setTeamIcon(rs.getString("team_icon"));
				team.setTeamID(team_id);
				team.setTeamType(rs.getString("team_type"));
				if(rs.getString("telephone")==null || rs.getString("telephone").length()==0)
					team.setTelephone("unknown");
				else
					team.setTelephone(rs.getString("telephone"));
				if(rs.getString("website")==null || rs.getString("website").length()==0)
					team.setWebsite("unknown");
				else
					team.setWebsite(rs.getString("website"));
				
			}
			rs.close();
			db.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			db.putConnection();
		}
		return team;
	}
	////////////////////////////////////////////////////////////////////////////
	//Lay thong tin cac tran dau da dien ra cua mot doi bong thuoc mot giai dau
	////////////////////////////////////////////////////////////////////////////
	//Lay thong tin cac tran dau chua dien ra cua mot doi bong thuoc mot giai dau
	public static void main(String[] args)
	{
		try
		{
			Team client = new Team();
			client.makeConnection();
			TeamInfo team = client.getTeam("757");
			System.out.println(team.getDisplayName());
			client.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}	
	}
	
}
