package neo.score24.object;

import java.io.Serializable;

public class EventInfo implements Serializable
{

    private String id;
    private String parent;
    private String status;
    private String type;
    private String name;
    private String time;

    public EventInfo()
    {
    }
    
    public String getId()
    {
        return id;
    }

    public String getParent()
    {
        return parent;
    }

    public String getStatus()
    {
        return status;
    }
    
    public String getType()
    {
        return type;
    }
    public String getName()
    {
    	return name;
    }
    public String getTime()
    {
    	return time;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setParent(String parent)
    {
        this.parent = parent;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public void setType(String type)
    {
        this.type = type;
    } 
    public void setName(String name)
    {
        this.name = name;
    } 
    public void setTime(String time)
    {
    	this.time=time;
    }
    public void display()
    {
    	System.out.println("ID: "+id);
    	System.out.println("Parent: "+parent);
    	System.out.println("Status: "+status);
    	System.out.println("Type: "+status);
    	System.out.println("Name: "+name);
    	System.out.println("Time: "+time);
    }
}