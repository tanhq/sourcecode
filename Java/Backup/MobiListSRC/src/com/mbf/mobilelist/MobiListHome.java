package com.mbf.mobilelist;

import javax.ejb.EJBHome;
import java.rmi.RemoteException;
import javax.ejb.CreateException;

public interface MobiListHome extends EJBHome{
	MobiList create() throws RemoteException, CreateException;
}
