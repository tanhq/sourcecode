package neo.score24.object;
import java.io.Serializable;

public class TeamInfo implements Serializable
{
	private String team_id;
	private String team_type;
	private String short_name;
	private String full_name;
	private String display_name;
	private String team_alias;
	private String team_icon;
	private String team_dress;
	private String date_found;
	private String join_fifa;
	private String rank_fifa;
	private String chairman;
	private String coach;
	private String stadium;
	private int capacity;
	private String address;
	private String telephone;
	private String fax;
	private String website;
	private String country_id;
	
	public String getTeamID()
	{
		return this.team_id;
	}
	public String getTeamType()
	{
		return this.team_type;
	}    
	public String getShortName()
	{
		return this.short_name;
	}
	public String getFullName()
	{
		return this.full_name;
	}
	public String getDisplayName()
	{
		return this.display_name;
	}
	public String getTeamAlias()
	{
		return this.team_alias;
	}
	public String getTeamIcon()
	{
		return this.team_icon;
	}
	public String getTeamDress()
	{
		return this.team_dress;
	}
	public String getDateFound()
	{
		return this.date_found;
	}
	public String getJoinFifa()
	{
		return this.join_fifa;
	}
	public String getRankFifa()
	{
		return this.rank_fifa;
	}
	public String getChairman()
	{
		return this.chairman;
	}
	public String getCoach()
	{
		return this.coach;
	}
	public String getStadium()
	{
		return this.stadium;
	}
	public int getCapacity()
	{
		return this.capacity;
	}
	public String getAddress()
	{
		return this.address;
	}
	public String getTelephone()
	{
		return this.telephone;
	}
	public String getFax()
	{
		return this.fax;
	}
	public String getWebsite()
	{
		return this.website;
	}
	public String getCountryID()
	{
		return this.country_id;
	}

	
	public void setTeamID(String team_id)
	{
		this.team_id=team_id;
	}
	public void setTeamType(String team_tpe)
	{
		this.team_type=team_type;
	}    
	public void setShortName(String short_name)
	{
		this.short_name = short_name;
	}
	public void setFullName(String full_name)
	{
		this.full_name=full_name;
	}
	public void setDisplayName(String display_name)
	{
		this.display_name=display_name;
	}
	public void setTeamAlias(String team_alias)
	{
		this.team_alias=team_alias;
	}
	public void setTeamIcon(String team_icon)
	{
		this.team_icon=team_icon;
	}
	public void setTeamDress(String team_dress)
	{
		this.team_dress = team_dress;
	}
	public void setDateFound(String date_found)
	{
		this.date_found=date_found;
	}
	public void setJoinFifa(String join_fifa)
	{
		this.join_fifa = join_fifa;
	}
	public void setRankFifa(String rank_fifa)
	{
		this.rank_fifa=rank_fifa;
	}
	public void setChairman(String chairman)
	{
		this.chairman=chairman;
	}
	public void setCoach(String coach)
	{
		this.coach=coach;
	}
	public void setStadium(String stadium)
	{
		this.stadium=stadium;
	}
	public void setCapacity(int capacity)
	{
		this.capacity=capacity;
	}
	public void setAddress(String address)
	{
		this.address=address;
	}
	public void setTelephone(String telephone)
	{
		this.telephone=telephone;
	}
	public void setFax(String fax)
	{
		this.fax=fax;
	}
	public void setWebsite(String website)
	{
		this.website=website;
	}
	public void setCountryID(String country_id)
	{
		this.country_id=country_id;
	}
	
}