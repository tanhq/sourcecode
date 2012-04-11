package neo.score.ejb;

import javax.ejb.EJBHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

public interface LiveScoreHome extends EJBHome
{
	LiveScore create() throws RemoteException, CreateException;
}
