package neo.score24.object;

import java.io.Serializable;

public class ResultInfo implements Serializable
{

    private String id;
    private String period;
    private String type;
    private String text;
    private String trigger;
    private String time;
    private String updatetime;

    public ResultInfo()
    {
    }
    
    public String getId()
    {
        return id;
    }

    public String getPeriod()
    {
        return period;
    }  
    
    public String getType()
    {
        return type;
    }  
    public String getText()
    {
        return text;
    }
    public String getTrigger()
    {
        return trigger;
    } 
    public String getTime()
    {
        return time;
    } 
    public String getUpdateTime()
    {
        return updatetime;
    } 

    public void setId(String id)
    {
        this.id = id;
    }

    public void setPeriod(String period)
    {
        this.period = period;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public void setText(String text)
    {
        this.text = text;
    }
    public void setTrigger(String trigger)
    {
        this.trigger = trigger;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public void setUpdateTime(String updatetime)
    {
        this.updatetime = updatetime;
    }
}