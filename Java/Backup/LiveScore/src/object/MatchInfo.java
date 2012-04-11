package neo.score24.object;
import java.io.Serializable;

public class MatchInfo implements Serializable
{
	private String match_id;
	private String match_name;
	private String home_id;
	private String guest_id;
	private String is_active;	
	private String referee;
	private String stadium;
	private String parent_id;
	private String eventprimary_id;
	private String result;
	private int match_seq;
	private String match_date;
	private EventInfo eventInfo;
	private CommonInfo commonInfo;
	private	ParticipantInfo participantInfo1 = null;
	private ParticipantInfo participantInfo2 = null;
	////////////////////////////////////////////////////////////////////////////
	public String getMatchID()
	{
		return this.match_id;
	}
	public String getMatchName()
	{
		return this.match_name;
	}
	public String getHomeID()
	{
		return this.home_id;
	}
	public String getGuestID()
	{
		return this.guest_id;
	}
	public String getIsActive()
	{
		return this.is_active;
	}
	
	public String getReferee()
	{
		return this.referee;
	}
	public String getStadium()
	{
		return this.stadium;
	}
	public String getParentID()
	{
		return this.parent_id;
	}
	public String getEventPrimaryID()
	{
		return this.eventprimary_id;
	}
	public String getResult()
	{
		return this.result;
	}
	public int getMatchSequence()
	{
		return this.match_seq;
	}
	public String getMatchDate()
	{
		return this.match_date;
	}
	public CommonInfo getEventPrimary()
	{
		return this.commonInfo;
	}
	public EventInfo getEvent()
	{
		return this.eventInfo;
	}
	public ParticipantInfo getParticipant1()
	{
		return this.participantInfo1;
	}
	public ParticipantInfo getParticipant2()
	{
		return this.participantInfo2;
	}
	
	////////////////////////////////////////////////////////////////////////////
	public void setEventPrimary(CommonInfo commonInfo)
	{
		this.commonInfo = commonInfo;
	}
	public void setEvent(EventInfo eventInfo)
	{
		this.eventInfo=eventInfo;
	}
	public void setParticipant1(ParticipantInfo participantInfo)
	{
		this.participantInfo1=participantInfo;
	}
	public void setParticipant2(ParticipantInfo participantInfo)
	{
		this.participantInfo2=participantInfo;
	}
	public void setMatchDate(String match_date)
	{
		this.match_date=match_date;
	}
	public void setMatchID(String match_id)
	{
		this.match_id = match_id;
	}
	public void setMatchName(String match_name)
	{
		this.match_name = match_name;
	}
	public void setHomeID(String home_id)
	{
		this.home_id=home_id;
	}
	public void setGuestID(String guest_id)
	{
		this.guest_id=guest_id;
	}
	public void setIsActive(String is_active)
	{
		this.is_active=is_active;
	}
	
	public void setReferee(String referee)
	{
		this.referee=referee;
	}
	public void setStadium(String stadium)
	{
		this.stadium=stadium;
	}
	public void setParentID(String parent_id)
	{
		this.parent_id=parent_id;
	}
	public void setEventPrimaryID(String eventprimary_id)
	{
		this.eventprimary_id=eventprimary_id;
	}
	public void setMatchSequence(int match_seq)
	{
		this.match_seq=match_seq;
	}
	public void setResult(String result)
	{
		this.result=result;
	}
	
}