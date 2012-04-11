package neo.score24.object;
import java.io.Serializable;

public class RoundInfo implements Serializable
{
	private String round_id;
	private String round_name;
	private String round_name_vn;
	private String time;
	private String status;
	private String league_id;
	private EventInfo eventInfo;
	private CommonInfo commonInfo;
	
	public String getRoundID()
	{
		return this.round_id;
	}    
	public String getRoundName()
	{
		return this.round_name;
	}
	public String getRoundNameVN()
	{
		return this.round_name_vn;
	}
	public String getTime()
	{
		return this.time;
	}
	public String getStatus()
	{
		return this.status;
	}
	public String getLeagueID()
	{
		return this.league_id;
	}
	public EventInfo getEventInfo()
	{
		return this.eventInfo;
	}
	public CommonInfo getCommonInfo()
	{
		return this.commonInfo;
	}
	
	public void setRoundID(String round_id)
	{
		this.round_id=round_id;
	}
	public void setRoundName(String round_name)
	{
		this.round_name = round_name;
	}
	public void setRoundNameVN(String round_name_vn)
	{
		this.round_name_vn=round_name_vn;
	}
	public void setTime(String time)
	{
		this.time=time;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public void setLeagueID(String league_id)
	{
		this.league_id=league_id;
	}
	public void setEventInfo(EventInfo eventInfo)
	{
		this.eventInfo=eventInfo;
	}
	public void setCommonInfo(CommonInfo commonInfo)
	{
		this.commonInfo=commonInfo;
	}
}