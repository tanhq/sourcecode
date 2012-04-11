package neo.score.object;

import java.util.Date;
public class UserInfo 
{
	private String userid;
	private String username;
	private String password;
	private int roll;
	private String fullname;
	private String donvi;
	private String tendonvi;
	private String chucdanh;
	private String email;
	private int is_enable;
	private int is_livescore;
	private Date last_access;
	
	//Cac phuong thuc get
	public String getUserId()
	{
		return userid;
	}
	public String getUsername()
	{
		return this.username;
	}
	public String getPassword()
	{
		return this.password;
	}	
	public int getRoll()
	{
		return this.roll;
	}
	public String getFullname()
	{
		return this.fullname;
	}
	public String getDonvi()
	{
		return this.donvi;
	}
	public String getTendonvi()
	{
		return tendonvi;
	}
	public String getChucdanh()
	{
		return this.chucdanh;
	}
	public String getEmail()
	{
		return this.email;
	}
	public int getIs_enable()
	{
		return this.is_enable;
	}
	public int getIs_livescore()
	{
		return this.is_livescore;
	}
	public Date getLast_access()
	{
		return this.last_access;
	}
	//Cac phuong thuc set
	public void setUserId(String userid)
	{
		this.userid=userid;
	}
	public void setUsername(String username)
	{
		this.username=username;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public void setRoll(int roll)
	{
		this.roll=roll;
	}
	public void setFullname(String fullname)
	{
		this.fullname=fullname;
	}
	public void setDonvi(String donvi)
	{
		this.donvi=donvi;
	}
	public void setTendonvi(String tendonvi)
	{
		this.tendonvi=tendonvi;
	}
	public void setChucdanh(String chucdanh)
	{
		this.chucdanh=chucdanh;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public void setIs_enable(int is_enable)
	{
		this.is_enable=is_enable;
	}
	public void setIs_livescore(int is_livescore)
	{
		this.is_livescore=is_livescore;
	}
	public void setLast_access(Date last_access)
	{
		this.last_access=last_access;
	}
}
