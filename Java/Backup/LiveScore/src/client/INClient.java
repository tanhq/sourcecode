package neo.score.client;

import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import java.util.Properties;
import java.util.*;
import neo.score.db.*;
import neo.score.object.*;
import neo.score.ejb.*;
import java.text.SimpleDateFormat;
import infogate.core.sms.HandlerBean;
import java.io.*;
import vms.ingate.*;

public class INClient
{
	public PrepaidInfo remote = null;
	public void init()
	{
		try 
		{
			Properties jndiProps = new Properties();			
			jndiProps.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
			jndiProps.setProperty("java.naming.provider.url", "vmsgateway:1299");
			jndiProps.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			InitialContext jndiContext = new InitialContext(jndiProps);
			String s ="neo/vms/ingateway";
			Object ref = jndiContext.lookup(s);
			PrepaidInfoHome home = (PrepaidInfoHome)PortableRemoteObject.narrow(ref, PrepaidInfoHome.class);
			remote = home.create();
		}
		catch(Exception ex)
		{
			
		}
	}
	////////////////////////////////////////////////////////////////////////////
	public int getBalance(String msisdn)
	{
		String balance = "0";
		
		try
		{			
			ResponseInfo ri = remote.getInInfo(msisdn);
			if (ri.getResponseCode()!=0)
				balance="-1";
			if (ri.getResponseCode()==0)
			{
				Properties pr = (Properties)ri.getContent();	
				String[] values = (String[])pr.get("CREDIT");       		
				balance = values[0];
			}	
			
		}
		catch(Exception ex)
		{			
			ex.printStackTrace();
		}
		return Integer.parseInt(balance);
	}
	////////////////////////////////////////////////////////////////////////////
	public boolean isCDR(String msisdn)
	{
		boolean fCDR = false;
		int balance = 0;
		try
		{
			balance = getBalance(msisdn);
			if(balance>=1000)
				fCDR=true;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fCDR;
	}
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args)
	{
		try
		{
			INClient client = new INClient();
			client.init();
			int balance = client.getBalance("1228333000");
			System.out.println("The balance is: "+balance);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}	
}
