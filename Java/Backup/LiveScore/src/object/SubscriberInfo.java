package neo.score24.object;

import java.io.Serializable;

public class SubscriberInfo implements Serializable
{

    private String user_id;
    private String match_id;
    private String user_status;
    private String register_time;
    private String msisdn;
   	private String round_id;
   	private String league_id;
   	private String type;

    public SubscriberInfo()
    {
    }
    
    public String getUserID()
    {
        return user_id;
    }

    public String getMatchID()
    {
        return match_id;
    }   
	public String getUserStatus()
	{
		return user_status;
	}
	public String getRegisterTime()
	{
		return register_time;
	}
	public String getMsisdn()
	{
		return msisdn;
	}
	public String getRoundID()
	{
		return round_id;
	}
	public String getLeagueID()
	{
		return league_id;
	}
	public String getType()
	{
		return type;
	}
	
   
    public void setUserID(String user_id)
    {
    	this.user_id=user_id;
    }
    public void setMatchID(String match_id)
    {
    	this.match_id=match_id;
    }
    public void setUserStatus(String user_status)
    {
    	this.user_status=user_status;
    }
    public void setRegisterTime(String register_time)
    {
    	this.register_time=register_time;
    }
    public void setMsisdn(String msisdn)
    {
    	this.msisdn=msisdn;
    }
    public void setRoundID(String round_id)
    {
    	this.round_id=round_id;
    }
    public void setLeagueID(String league_id)
    {
    	this.league_id=league_id;
    }
    public void setType(String type)
    {
    	this.type=type;
    }
    
}