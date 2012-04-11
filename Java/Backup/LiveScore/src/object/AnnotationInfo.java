package neo.score24.object;

import java.io.Serializable;

public class AnnotationInfo implements Serializable
{

    private String id;
    private String order;
    private String time;
    private String type;
    private String freetext; 
    private String resulttype;   

    public AnnotationInfo()
    {
    }
    
    public String getId()
    {
        return id;
    }

    public String getOrder()
    {
        return order;
    }
    public String getTime()
    {
        return time;
    }  
    public String getType()
    {
        return type;
    } 
    public String getFreeText()
    {
        return freetext;
    } 
    public String getResultType()
    {
        return resulttype;
    }  

    public void setId(String id)
    {
        this.id = id;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }
    public void setTime(String time)
    {
        this.time = time;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public void setFreeText(String freetext)
    {
        this.freetext = freetext;
    }
    public void setResultType(String resulttype)
    {
        this.resulttype = resulttype;
    }
}