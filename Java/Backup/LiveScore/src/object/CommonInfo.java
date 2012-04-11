package neo.score24.object;

import java.io.Serializable;

public class CommonInfo implements Serializable
{

    private String id;
    private String name;

    public CommonInfo()
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

    public void setId(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    public void display()
    {
    	System.out.println("ID: "+id);
    	System.out.println("Name: "+name);
    }
    
}