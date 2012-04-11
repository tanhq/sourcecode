package neo.score24.object;

import java.io.Serializable;

public class ParticipantInfo implements Serializable
{

    private String id;
    private String name;
    private String number;
    private String order;
    private String position;    

    public ParticipantInfo()
    {
    }
    
    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    public String getNumber()
    {
        return number;
    }  
    public String getOrder()
    {
        return order;
    } 
    public String getPosition()
    {
        return position;
    }  

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setNumber(String number)
    {
        this.number = number;
    }
    public void setOrder(String order)
    {
        this.order = order;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }
}