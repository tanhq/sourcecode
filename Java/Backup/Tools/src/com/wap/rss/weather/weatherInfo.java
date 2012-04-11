package com.wap.rss.weather;

import java.util.*;
import java.util.Enumeration;
import com.chilkatsoft.*;


public class weatherInfo 
{
	public static HashMap hsCity = new HashMap();
	public static HashMap hsStatus = new HashMap();
	
	static
	{	
		hsCity.put("bac giang","http://weather.yahooapis.com/forecastrss?p=VMXX0002&u=c");
		hsCity.put("bien hoa","http://weather.yahooapis.com/forecastrss?p=VMXX0003&u=c");
		hsCity.put("ca mau","http://weather.yahooapis.com/forecastrss?p=VMXX0031&u=c");
		hsCity.put("can tho","http://weather.yahooapis.com/forecastrss?p=VMXX0004&u=c");
		hsCity.put("cao bang","http://weather.yahooapis.com/forecastrss?p=VMXX0020&u=c");
		hsCity.put("da nang","http://weather.yahooapis.com/forecastrss?p=VMXX0028&u=c");
		hsCity.put("dong hoi","http://weather.yahooapis.com/forecastrss?p=VMXX0027&u=c");
		hsCity.put("ha noi","http://weather.yahooapis.com/forecastrss?p=VMXX0006&u=c");
		hsCity.put("hai phong","http://weather.yahooapis.com/forecastrss?p=VMXX0005&u=c");
		hsCity.put("hcm","http://weather.yahooapis.com/forecastrss?p=VMXX0007&u=c");
		hsCity.put("hoa binh","http://weather.yahooapis.com/forecastrss?p=VMXX0008&u=c");
		hsCity.put("hue","http://weather.yahooapis.com/forecastrss?p=VMXX0009&u=c");
		hsCity.put("lang son","http://weather.yahooapis.com/forecastrss?p=VMXX0023&u=c");
		hsCity.put("lao cai","http://weather.yahooapis.com/forecastrss?p=VMXX0019&u=c");
		hsCity.put("my tho","http://weather.yahooapis.com/forecastrss?p=VMXX0010&u=c");
		hsCity.put("nam dinh","http://weather.yahooapis.com/forecastrss?p=VMXX0011&u=c");
		hsCity.put("nha trang","http://weather.yahooapis.com/forecastrss?p=VMXX0029&u=c");
		hsCity.put("phan thiet","http://weather.yahooapis.com/forecastrss?p=VMXX0012&u=c");
		hsCity.put("thai nguyen","http://weather.yahooapis.com/forecastrss?p=VMXX0015&u=c");
		hsCity.put("tuy hoa","http://weather.yahooapis.com/forecastrss?p=VMXX0016&u=c");
		hsCity.put("viet tri","http://weather.yahooapis.com/forecastrss?p=VMXX0017&u=c");
		hsCity.put("vinh","http://weather.yahooapis.com/forecastrss?p=VMXX0026&u=c");
		hsCity.put("vung tau","http://weather.yahooapis.com/forecastrss?p=VMXX0018&u=c");
		
		
		hsStatus.put("tornado","L&#7889;c xo&#225;y");
		hsStatus.put("tropical storm","B&#227;o nhi&#7879;t &#273;&#7899;i");
		hsStatus.put("hurricane","Gi&#243; xo&#225;y");
		hsStatus.put("severe thunderstorms","B&#227;o c&#243; s&#7845;m s&#233;t, m&#432;a to");
		hsStatus.put("thunderstorms","B&#227;o c&#243; s&#7845;m s&#233;t, m&#432;a to");
		hsStatus.put("mixed rain and snow","");
		hsStatus.put("mixed rain and sleet","");
		hsStatus.put("mixed snow and sleet","");
		hsStatus.put("freezing drizzle","");
		hsStatus.put("drizzle","");
		hsStatus.put("freezing rain","");
		hsStatus.put("showers","");
		
		hsStatus.put("snow flurries","");
		hsStatus.put("light snow showers","");
		hsStatus.put("blowing snow","");
		hsStatus.put("snow","");
		hsStatus.put("hail","");
		hsStatus.put("sleet","");
		hsStatus.put("dust","");
		hsStatus.put("foggy","");
		hsStatus.put("haze","");
		hsStatus.put("smoky","");
		hsStatus.put("blustery","");
		hsStatus.put("windy","");
		hsStatus.put("cold","");
		
		
		hsStatus.put("cloudy","");
		hsStatus.put("mostly cloudy (night)","");
		hsStatus.put("mostly cloudy (day)","");
		hsStatus.put("partly cloudy (night)","");
		hsStatus.put("partly cloudy (day)","");
		hsStatus.put("clear (night)","");
		hsStatus.put("sunny","");
		hsStatus.put("fair (night)","");
		hsStatus.put("fair (day)","");
		hsStatus.put("mixed rain and hail","");
		hsStatus.put("hot","");
		hsStatus.put("isolated thunderstorms","");
		hsStatus.put("scattered thunderstorms","");
		hsStatus.put("scattered thunderstorms","");
		hsStatus.put("scattered showers","");
		hsStatus.put("heavy snow","");
		hsStatus.put("scattered snow showers","");
		hsStatus.put("partly cloudy","");
		hsStatus.put("thundershowers","");
		hsStatus.put("snow showers","");
		hsStatus.put("isolated thundershowers","");
		hsStatus.put("not available","");
		
				
	}
		
	
	static 
	{
	    try 
	    {
	        System.load("E:/www/wap/xhtml/res/chilkat.dll");
	        //System.load("G:/Services/chilkatJava/chilkat.dll");
	    } 
	    catch (UnsatisfiedLinkError e) 
	    {
	      System.err.println("Native code library failed to load.\n" + e);
	    }
	
	}
	
	public Hashtable getWeather(String city)
	{
		Hashtable info = new Hashtable();
		String link = "";
		try
		{
			city = city.toLowerCase().trim();
			if(hsCity.containsKey(city))
			{
				link = hsCity.get(city).toString();
				info = this.getInfo(link);	
			}
			else
			{
				return null;
			}
				
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return info;
	}
	
	public Hashtable getInfo(String link)
	{
		Hashtable info = new Hashtable();
		try
		{
			CkRss rss = new CkRss();
    		boolean success;
    		// Download from the feed URL:
    		success = rss.DownloadRss(link);
    		if (success != true) 
    		{
        		System.out.println(rss.lastErrorText());
        		return null;
    		}

    		//  Get the 1st channel.
    		CkRss rssChannel;

    		rssChannel = rss.GetChannel(0);
    		if (rssChannel == null ) 
    		{
        		System.out.println("No channel found in RSS feed.");
        		return null;
    		}
				
		    //  Display the various pieces of information about the channel:
		    info.put("city",rssChannel.getAttr("yweather:location","city"));  
		     
		    int numItems;
		    numItems = rssChannel.get_NumItems();
		    int i;
		
		    for (i = 0; i <= numItems - 1; i++) 
		    {
		        CkRss rssItem;
		        rssItem = rssChannel.GetItem(i);
		        		        
		        info.put("title",rssItem.getString("title"));
		        info.put("date",rssItem.getString("pubDate"));
		        info.put("text",rssItem.getAttr("yweather:condition","text"));
		        info.put("code",rssItem.getAttr("yweather:condition","code"));
		        info.put("temp",rssItem.getAttr("yweather:condition","temp"));
		        info.put("low",rssItem.getAttr("yweather:forecast","low"));	
		        info.put("high",rssItem.getAttr("yweather:forecast","high"));
		        String img_path = rssItem.getString("description");	
		        img_path = img_path.substring(0,img_path.indexOf("/>")+2);
		        info.put("img_path",img_path);
		        
		        
		    }	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return info;
	}
	
	public static void main(String[] args)
	{
		try
		{
			weatherInfo client = new weatherInfo();
			String city = "thai nguyen";
			Hashtable info = client.getWeather(city);
			if(info!=null && !info.isEmpty())
			{
				System.out.println("city: "+info.get("city"));
				System.out.println("title: "+info.get("title"));
				System.out.println("date: "+info.get("date"));
				System.out.println("text: "+info.get("text"));
				System.out.println("code: "+info.get("code"));
				System.out.println("temp: "+info.get("temp"));
				System.out.println("low: "+info.get("low"));
				System.out.println("high: "+info.get("high"));
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
}
