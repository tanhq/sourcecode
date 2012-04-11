package neo.score.ejb;

import java.util.*;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import neo.score24.object.*;

public interface LiveScore extends EJBObject 
{
	public ArrayList getLeagues() throws RemoteException;
	public int isLeagueExit(String league_code) throws RemoteException;
	public ArrayList getListofTeams(String league_code) throws RemoteException;
	public ArrayList getListofTeams(String league_code,String msisdn,int show) throws RemoteException;	
	public ArrayList getListofRounds(String league_code) throws RemoteException;
	public ArrayList getListofRounds(String league_code,String msisdn,int show) throws RemoteException;
	public ArrayList getListofMatchs(String league_code) throws RemoteException;
	public ArrayList getListofMatchs(String league_code,int show) throws RemoteException;
	public ArrayList getListofMatchs(String league_code,String msisdn,int show) throws RemoteException;
	public ArrayList getListofStartedMatchs(String league_code,int show) throws RemoteException;
	public ArrayList getListMatchsofTeam(String team_id,String league_id,String status) throws RemoteException;	
	public int regMatchs(String league_code,String msisdn,String matchs,String type) throws RemoteException; 
	public int regMatchsWeb(String league_code,String msisdn,String match_id,String type) throws RemoteException;
	public int regRounds(String league_code,String msisdn,String rounds,String type) throws RemoteException;
	public int regTeams(String league_code,String msisdn,String teams,String type) throws RemoteException;
	public int unRegMatchs(String league_code,String msisdn,String match_id,String type) throws RemoteException;
	public int unRegMatchsWeb(String msisdn,String match_id,String type) throws RemoteException;
	public int unRegRounds(String league_code,String msisdn,String rounds,String type) throws RemoteException;
	public int unRegTeams(String league_code,String msisdn,String teams,String type) throws RemoteException;
	public ArrayList getListofMatchsReg(String league_code,String msisdn,String type) throws RemoteException;
	public ArrayList getListofTeamsReg(String league_code,String msisdn,String type) throws RemoteException;
	public ArrayList getListofRoundsReg(String league_code,String msisdn,String type) throws RemoteException;
	
	public void makeConnection() throws RemoteException;
	public void removeConnection() throws RemoteException;
}

