package com.mbf.mobilelist.client;

public class Pool implements java.io.Serializable{	
	private static int maxCon = 100;
	private static int minCon = 1;//chua duoc su dung
	private static int poolsize;
	public static int total=0;

	private static boolean firstTime = true;
	public static RTCClient[] cons = null;
	public static byte[] consState = null;
	public static long[] useTime = null;
	public static String[] commands = null;
	
	public RTCClient con = null;
	private int currentId = -1;
	private CheckConnection cdb = null;
	
	public Pool(){
		if (cons==null) cons = new RTCClient[maxCon];
		if (consState==null) consState = new byte[maxCon];  
		if (useTime==null) useTime = new long[maxCon];
		if (commands==null) commands = new String[maxCon];
		if (firstTime) poolsize = maxCon;
		firstTime = false;
	}

	public RTCClient getConnection(){
		RTCClient rs = null;
		try {
			int inuse=0;
			int max=poolsize;
			for (int i=0; i<poolsize; i++){
				if (consState[i]==1) inuse++;
			}
			if (inuse>10)
				System.out.println("Total="+max+" created:"+total+" inuse:"+inuse);
			if (cdb==null) {
				cdb = new CheckConnection();
			}
			cdb.start();
			
			boolean free= false;
			
			int closing = 0;
			String closingCons = "";
			for (int i=0; i<poolsize; i++){//Tim kiem cac connection chua su dung
				if (consState[i]==3){//Trang thai closing
					closing++;	
					closingCons += ","+i;			
				} else if (consState[i]!=1){
					free = true;
					consState[i] = 1;//in use
					useTime[i] = System.currentTimeMillis();//Thoi gian bat dau su dung connection
					if (cons[i]!=null){
						rs = cons[i];
						rs.currentId = i;
						this.con = cons[i];
						this.currentId = i;
					} else {
						try {
							total++;
							System.out.println("Create connection " + i+ " - "+ total +" to RTC Server..");
							cons[i] = new RTCClient();
							cons[i].connect();
							rs = cons[i];
							rs.currentId = i;
							this.con = cons[i];
							this.currentId = i;
						} catch (Exception e) {
							System.out.println("Can not connect to RTC Server: "+e);
							return null;
						}
					}
					System.out.println("Busy: Connection "+ currentId);
					return rs;
				}
			}			
			if (!free){
				System.out.println("Maximum connection to RTC Server!");
			}
			if (closing>10){
				System.out.println("Co nhieu connection khong dong duoc: "+closing+" connections: "+closingCons);
			}
			return null;
		} catch (Exception e) {
			System.out.println("Can not connect to RTC Server: ");
			e.printStackTrace();
			return null;
		}
	}
	//===================================================================================
	public boolean removeConnection(RTCClient cr){
		try {
			if (cr!=null){
				int crid = cr.currentId;
				if (cr.currentId>=0 && cr.currentId<consState.length){
					consState[cr.currentId] = 2;
					System.out.println("Free: Connection "+ cr.currentId+" change state to 2");
				}
			}
			return true;
		} catch (Exception e) {
			System.out.println("Can not remove connect to RTC Server: ");
			e.printStackTrace();
			return false;
		}
	}
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args){
		try {
			Pool rtc = new Pool();
			RTCClient rc = rtc.getConnection();
			if (rc!=null){
				System.out.println("RESULT:"+rc.debit("84906044701",220));
				Thread.sleep(60000);
				System.out.println("RESULT:"+rc.debit("84906044701",220));
				rtc.removeConnection(rc);
			} else {
				System.out.print("He thong qua tai!");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
