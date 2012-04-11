package neo.score24.object;

import java.io.Serializable;

public class LeagueInfo implements Serializable
{
	private String league_id;
	private String league_name;
	private String league_code;
	private String league_name_vn;
	private String status;
	private String season;
	private String country_id;
	private String start_date;
	private String end_date;
	private EventInfo eventInfo;
	
	public String getLeagueID()
	{
		return this.league_id;
	}
	public String getLeagueName()
	{
		return this.league_name;
	}
	public String getLeagueNameVN()
	{
		return this.league_name_vn;
	}
	public String getLeagueCode()
	{
		return this.league_code;
	}
	public String getStatus()
	{
		return this.status;
	}
	public String getSeason()
	{
		return this.season;
	}
	public String getCountryID()
	{
		return this.country_id;
	}
	public String getStartDate()
	{
		return this.start_date;
	}
	public String getEndDate()
	{
		return this.end_date;
	}
	public EventInfo getEventInfo()
	{
		return this.eventInfo;
	}
	
	public void setLeagueID(String league_id)
	{
		this.league_id=league_id;
	}
	public void setLeagueNameVN(String league_name_vn)
	{
		this.league_name_vn=league_name_vn;
	}
	public void setLeagueCode(String league_code)
	{
		this.league_code = league_code;
	}
	public void setLeagueName(String league_name)
	{
		this.league_name=league_name;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public void setSeason(String season)
	{
		this.season=season;
	}
	public void setCountryID(String country_id)
	{
		this.country_id=country_id;
	}
	public void setStartDate(String start_date)
	{
		this.start_date=start_date;
	}	
	public void setEndDate(String end_date)
	{
		this.end_date=end_date;
	}
	public void setEventInfo(EventInfo eventInfo)
	{
		this.eventInfo=eventInfo;
	}
}
