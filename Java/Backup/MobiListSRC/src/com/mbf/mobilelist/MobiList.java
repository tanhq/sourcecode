package com.mbf.mobilelist;

import java.util.*;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import com.mbf.mobilelist.objects.*;

public interface MobiList extends EJBObject{

	//Phuong thuc kiem tra tinh hop le cua so dien thoai	
	public String getMobileNumber(String mobileNumber) throws  RemoteException;
	
	//Tao nhom moi voi ten nhom va so dien thoai cua nguoi tao nhom
	public int createGroup(String groupName, String mobileGroup) throws RemoteException;
	
	//Tao nhom moi voi ten nhom, so dien thoai cua nguoi tao nhom va so dien thoai trong nhom
	public int createGroup(String groupName, String mobileGroup, String mobileNumber) throws RemoteException;

	//Tao nhom moi voi ten nhom, so dien thoai cua nguoi tao nhom va so dien thoai trong nhom
	public int createGroup(String groupName, String mobileGroup, String mobileNumber, String groupDesc) throws RemoteException;

	//Xoa nhom theo ten nhom va so dien thoai cua nguoi tao nhom
	public int deleteGroup(String groupName, String mobileGroup) throws RemoteException;
	
	public String deleteGroup(String groupName, String mobileGroup, String mobileNumber) throws RemoteException;
	
	//Lay tat ca cac so dien thoai theo ten nhom va so dien thoai cua chu nhom
	public ArrayList getMobileOfGroup(String groupName, String mobileNumber) throws RemoteException;
	
	//Them 1 hay nhieu so dien thoai vao 1 hay nhieu nhom
	public String addMobileToGroup(String mobileNumber, String groupName, String mobileGroup) throws RemoteException;
	
	//Xoa 1 hay nhieu so dien thoai khoi 1 hay nhieu nhom
	public int removeMobileFromGroup(String groupName, String mobileGroup, String mobileNumber) throws RemoteException;
	
	//Kiem tra xem khach hang da dang ky mobileList hay chua
	public boolean customerIsExist(String mobileNumber) throws RemoteException;
	
	//Kiem tra xem nhom co thuoc so thue bao xxx khong
	public boolean groupIsExist(String groupName, String mobileGroup) throws RemoteException;
	
	//Kiem tra xem so dien thoai co thuoc trong cac nhom khong
	public boolean mobileIsExist(String mobileGroup, String mobileNumber) throws RemoteException;
	
	//Phuong thuc lay tat ca cac nhom
	public ArrayList getAllGroup(String mobileGroup) throws RemoteException;
	
	//Phuong thuc lay tat ca cac so dien thoai trong mot nhom
	public ArrayList getAllMobileFromGroup(String mobileGroup, String groupName) throws RemoteException;
	
	//Phuong thuc lay tat ca cac so dien thoai cua tat ca cac nhom
	public ArrayList getMobileOfGroup(String mobileNumber) throws RemoteException;
	
	//Phuong thuc lay loai cua nhom
	public int getTypeOfGroup(String groupName, String mobileNumber) throws RemoteException;
	
	//Phuong thuc xoa nhom theo id
	public int deleteGroupById(String groupId) throws RemoteException;
	
	//Phuong thuc xoa cac so dien thoai trong nhom
	public int deleteMobiles(int groupId, String mobileNumber) throws RemoteException;
	
	//Phuong thuc cap nhat cac so dien thoai trong nhom
	public int updateMobile(int groupId, String oldMobileNumber, String newMobileNumber) throws RemoteException;
	
	//Phuong thuc cap nhat thong tin cua nhom
	public int updateGroup(int groupId, String groupName, String groupDesc) throws RemoteException;
	
	//Phuong thuc lay tat ca cac so dien thoai trong nhom
	public ArrayList getAllMobileFromGroup(int groupId) throws RemoteException;
	
	//Phuong thuc kiem tra ten nhom la hop le
	public boolean groupNameIsValid(String groupName) throws RemoteException;
	
	//Phuong thuc lay thong tin cua mot nhom theo ma nhom
	public objGroup getGroupInfor(int groupId) throws RemoteException;
	
	//Phuong thuc xoa cac so dien thoai trong nhom
	public int deleteMobileFromGroup(String groupName, String mobileGroup, String mobileNumber) throws RemoteException;
	
	//Phuong thuc kiem tra so dien thoai ton tai trong nhom
	public boolean mobileIsExistInGroup(int groupId, String mobileNumber) throws RemoteException;
	
	//Phuong thuc cap nhat nhom
	public int updateGroup(int groupId, String groupName, String groupDesc, String mobileNumbers) throws RemoteException;
	
	//Phuong thuc them cac so dien thoai vao nhom tren web
	public int addMobileToGroup(String groupId, String mobileNumbers) throws RemoteException;
	
	public void sendSMS(String sender,String receiver,String content) throws RemoteException;
	
	public void sendSMS(String sender,String receiver,String content, String host, int port) throws RemoteException;
	
	public void toLogMobiList(String sender, String receiver, String content) throws RemoteException;
	
	public void toCDR(String sender,String receiver,int type) throws RemoteException;
	
	public void makeConnection() throws RemoteException;
	public void removeConnection() throws RemoteException;
}

