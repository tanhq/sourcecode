package com.wap.rss.weather;

import com.sun.cnpi.rss.parser.*;
import com.sun.cnpi.rss.elements.*;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.HashMap;

public class weather 
{
	public static HashMap hsCity = new HashMap();
	public weather()
	{
		hsCity.put("ca mau","http://newsrss.bbc.co.uk/weather/forecast/1375/Next3DaysRSS.xml");
		hsCity.put("can tho","http://newsrss.bbc.co.uk/weather/forecast/1228/Next3DaysRSS.xml");
		hsCity.put("da nang","http://newsrss.bbc.co.uk/weather/forecast/1278/Next3DaysRSS.xml");
		hsCity.put("ha noi","http://newsrss.bbc.co.uk/weather/forecast/1355/Next3DaysRSS.xml");
		hsCity.put("thanh hoa","http://newsrss.bbc.co.uk/weather/forecast/1774/Next3DaysRSS.xml");
		hsCity.put("hcm","http://newsrss.bbc.co.uk/weather/forecast/157/Next3DaysRSS.xml");
		hsCity.put("hue","http://newsrss.bbc.co.uk/weather/forecast/1375/Next3DaysRSS.xml");
		hsCity.put("nam dinh","http://newsrss.bbc.co.uk/weather/forecast/1581/Next3DaysRSS.xml");
		hsCity.put("nha trang","http://newsrss.bbc.co.uk/weather/forecast/1598/Next3DaysRSS.xml");
		hsCity.put("vinh","http://newsrss.bbc.co.uk/weather/forecast/1817/Next3DaysRSS.xml");
		hsCity.put("vung tau","http://newsrss.bbc.co.uk/weather/forecast/1820/Next3DaysRSS.xml");
	}
	public Hashtable[] getDays(String city)
	{
		Hashtable[] hs = null;
		String img_path = "";
		String link = "";
		try
		{			
			if(hsCity.get(city)!=null)
			{
				link = hsCity.get(city).toString();
				RssParser parser = RssParserFactory.createDefault();
	    		Rss rss = parser.parse(new URL(link));
	    		Channel cn = rss.getChannel();
	    		if(cn!=null)
	    		{
	    			Image img = cn.getImage();
	    			img_path = img.getUrl().toString();
	    			
	    		}
	    		Collection items = rss.getChannel().getItems();		     	     	
		        if(items != null && !items.isEmpty())
		        {
		        	hs = new Hashtable[items.size()];	
		        	int count = 0;
		            for(Iterator i = items.iterator();i.hasNext();)
		            {
		            	hs[count] = new Hashtable();
		                Item item = (Item)i.next();	                
		                //System.out.println("Title: " + item.getTitle());
		                //System.out.println("Link: " + item.getLink());	                
		                //System.out.println("Description: " + item.getDescription());
		                hs[count].put("title",item.getTitle());
		                hs[count].put("description",item.getDescription());
		                hs[count].put("img_path",img_path);
		                hs[count].put("pubDate",item.getPubDate());
		                count++;
		            }
		
		        }  		
				}
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return hs;
	}
	
	public Hashtable getGeneral(String city)
	{
		Hashtable hs = null;
		String link = "";
		try
		{
			
			if(hsCity.get(city)!=null)
			{
				link = hsCity.get(city).toString();
				RssParser parser = RssParserFactory.createDefault();
	    		Rss rss = parser.parse(new URL(link));
	    		Channel cn = rss.getChannel();
	    		if(cn!=null)
	    		{
	    			hs = new Hashtable();
	    			hs.put("description",cn.getDescription());
	    			hs.put("buildate",cn.getLastBuildDate());
	    			Image img = cn.getImage();
	    			hs.put("imgpath",img.getUrl().toString());
	    			hs.put("title",img.getTitle());
	    			
	    			Collection items = rss.getChannel().getItems();
	    			if(items != null && !items.isEmpty())
	    			{
	    				Iterator i = items.iterator();
	    				Item item = (Item)i.next();
	    				hs.put("item_title",item.getTitle());
	    			}
	    		}
	    			
			}			
    		
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return hs;
	}
	public static void main(String[] args)
	{
		try
		{
			weather client = new weather();
			/*Hashtable hs = client.getGeneral("ha noi");
			if(hs!=null && !hs.isEmpty())
			{
				System.out.println(hs.get("title"));
				System.out.println(hs.get("description"));
				System.out.println(hs.get("buildate"));
				System.out.println(hs.get("imgpath"));
				System.out.println(hs.get("item_title"));
			}*/
			Hashtable[] hs = client.getDays("hcm");
			if(hs!=null && hs.length>0)
			{
				for(int i=0;i<hs.length;i++)
				{
					System.out.println("title: "+hs[i].get("title"));
					System.out.println("description: "+hs[i].get("description"));
					System.out.println("img_path: "+hs[i].get("img_path"));
					System.out.println("pubDate: "+hs[i].get("pubDate"));
					System.out.println("===============================");
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
