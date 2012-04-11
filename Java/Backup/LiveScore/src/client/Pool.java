package neo.score.client;

public class Pool
{
  private static int maxCon = 300;//5 phut
  private static int minCon = 1; //chua duoc su dung
  private static int poolsize;
  public static int total = 0;

  private static boolean firstTime = true;
  private static livescoreClient[] cons = null;
  private static byte[] consState = null;

  public livescoreClient con = null;
  private int currentId = -1;

  public Pool()
  {
      if (cons==null) cons = new livescoreClient[maxCon];
      if (consState==null) consState = new byte[maxCon];
      if (firstTime) poolsize = maxCon;
      firstTime = false;
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  public boolean getConnection()
{
        try
        {
                int inuse=0;
                int max=poolsize;
                for (int i=0; i<poolsize; i++){
                        if (consState[i]==1) inuse++;
                }
                if (inuse>3)
                        System.out.println("Total="+max+" created:"+total+" inuse:"+inuse);

                boolean free= false;
                for (int i=0; i<poolsize; i++)
                {
                        if (consState[i]!=1){
                                free = true;
                                consState[i] = 1;//in use
                                if (cons[i]!=null){
                                        this.currentId = i;
                                        if (cons[i].status!=1)
                                                cons[i].init();
                                        this.con = cons[i];

                                } else {
                                        try {
                                                total++;
                                                //System.out.println("Create connection "+ total +" to IN Server..");
                                                cons[i] = new livescoreClient();
                                                cons[i].init();
                                                this.con = cons[i];
                                                this.currentId = i;
                                        } catch (Exception e) {
                                                e.printStackTrace();
                                                System.out.println("Can not connect to CoreHanlder Server: "+e);
                                                return false;
                                        }
                                }
                                return true;
                        }
                }
                if (!free)
                {
                        System.out.println("Het ket noi toi CoreHanlder Server!");
                }
                return false;
        }
        catch (Exception e)
        {
                System.out.println("Can not connect to CoreHanlder Server: ");
                e.printStackTrace();
                return false;
        }
}
////////////////////////////////////////////////////////////////////////////
  public boolean forceCreateConnection(){
          try {
                  if (this.con==null) this.con = new livescoreClient();
                  this.con.init();
                  this.cons[this.currentId] = this.con;
                  return true;
          } catch (Exception e){
                  e.printStackTrace();
                  return false;
          }
  }
  //===================================================================================
  public boolean removeConnection()
  {
          if (currentId>=0 && currentId<consState.length){
                  consState[currentId] = 2;
          }
          return true;
  }
  
  
  
  public static void main(String[] args)
  {
  	try
  	{
  		for(int i=0;i<10;i++)
  		{
  			long t1 = System.currentTimeMillis();
	  		Pool pool = new Pool();
	  		pool.getConnection();
	  		pool.con.init();
	  		
	  		pool.con.getLeagues();
	  		pool.con.removeConnection();
	  		pool.removeConnection();
	  		long t2 = System.currentTimeMillis();
	  		System.out.println("Thoi gian thuc hien: "+(t2-t1)+" mini giay");
	  		Thread.sleep(100);
	  	}	
  	}
  	catch(Exception ex)
  	{
  		ex.printStackTrace();
  	}
  }


}
