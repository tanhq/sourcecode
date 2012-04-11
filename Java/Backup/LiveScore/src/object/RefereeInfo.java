package neo.score24.object;

import java.io.Serializable;

public class RefereeInfo implements Serializable
{

    private String id;
    private String firstname;
    private String lastname;

    public RefereeInfo()
    {
    }
    
    public String getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstname;
    }  
    
    public String getLastName()
    {
        return lastname;
    }   

    public void setId(String id)
    {
        this.id = id;
    }

    public void setFirstName(String firstname)
    {
        this.firstname = firstname;
    }
    public void setLastName(String lastname)
    {
        this.lastname = lastname;
    }
}