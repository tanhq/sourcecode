package neo.score24.object;

import java.io.Serializable;

public class MessageInfo implements Serializable
{

    private String id;
    private String result;
    private String timezone;
    private String timestamp;

    public MessageInfo()
    {
    }
    
    public String getId()
    {
        return id;
    }

    public String getResult()
    {
        return result;
    }

    public String getTimeZone()
    {
        return timezone;
    }
    
    public String getTimeStamp()
    {
        return timestamp;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public void setTimeZone(String timezone)
    {
        this.timezone = timezone;
    }
    
    public void setTimeStamp(String timestamp)
    {
        this.timestamp = timestamp;
    } 
}