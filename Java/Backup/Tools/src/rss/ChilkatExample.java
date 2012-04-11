package rss;



import com.chilkatsoft.*;

public class ChilkatExample {

  static 
  {
    try 
    {
        System.load("G:/Services/chilkatJava/chilkat.dll");
    } 
    catch (UnsatisfiedLinkError e) 
    {
      System.err.println("Native code library failed to load.\n" + e);
    }

  }

  public static void main(String argv[])
  {
    CkRss rss = new CkRss();

    boolean success;

    //  Download from the feed URL:
    success = rss.DownloadRss("http://weather.yahooapis.com/forecastrss?p=VMXX0031&u=c");
    if (success != true) {
        System.out.println(rss.lastErrorText());
        return;
    }

    //  Get the 1st channel.
    CkRss rssChannel;

    rssChannel = rss.GetChannel(0);
    if (rssChannel == null ) {
        System.out.println("No channel found in RSS feed.");
        return;
    }

    //  Display the various pieces of information about the channel:  
    System.out.println("City: "+rssChannel.getAttr("yweather:location","city"));  
    int numItems;
    numItems = rssChannel.get_NumItems();
    int i;

    for (i = 0; i <= numItems - 1; i++) 
    {
        CkRss rssItem;
        rssItem = rssChannel.GetItem(i);

        System.out.println("Title: " + rssItem.getString("title"));        
        System.out.println("PubDate: " + rssItem.getString("pubDate"));
        System.out.println("Condition text: " + rssItem.getAttr("yweather:condition","text"));
        System.out.println("Condition code: " + rssItem.getAttr("yweather:condition","code"));
        System.out.println("Temp: " + rssItem.getAttr("yweather:condition","temp"));
    }


  }
}

