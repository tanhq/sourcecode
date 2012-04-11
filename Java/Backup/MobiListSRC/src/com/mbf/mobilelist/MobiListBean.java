package com.mbf.mobilelist;

import java.sql.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import com.mbf.mobilelist.db.*;
import com.mbf.mobilelist.objects.*;
import infogate.core.sms.HandlerBean;

public class MobiListBean implements SessionBean{
	public DBConnection dbConnection =  new DBConnection();
	public static int counter = 0;
	public ResultSet rs = null;
	public void ejbCreate(){
	}
	
	public void ejbRemove(){
	}
	
	public void ejbActivate(){
	}
	
	public void ejbPassivate(){
	}
	public void makeConnection()
	{
		long t1 = System.currentTimeMillis();
		try
		{
			if (dbConnection==null)
				dbConnection = new DBConnection();			
			//dbConnection.getConnectionAndVerify();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		long t2 = System.currentTimeMillis();
	}
	public void removeConnection()
	{
		try
		{
			if(dbConnection!=null)
				dbConnection.putConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	public void setSessionContext(SessionContext context){
	}
	
	/*
	 *	Phuong thuc kiem tra tinh hop le cua so dien thoai
	 *		return 0		: so dien thoai khong hop le
	 *		return khac 0	: so dien thoai hop le
	 */
	public String getMobileNumber(String mobileNumber){
			String result = "0";
          String validRange = "0123456789";
          for(int i=0;i<mobileNumber.length();i++){
                  char at = mobileNumber.charAt(i);
                  if(validRange.indexOf(at)==-1) return "0";
          }		
          //Truong hop dau so bat dau la so 0 (09, 0121, 0122, 0126,128)
          if((mobileNumber.startsWith("090") && mobileNumber.length()==10) || (mobileNumber.startsWith("093") && mobileNumber.length()==10) || (mobileNumber.startsWith("0121") && mobileNumber.length()==11) || (mobileNumber.startsWith("0122") && mobileNumber.length()==11) || (mobileNumber.startsWith("0126") && mobileNumber.length()==11) || (mobileNumber.startsWith("0128") && mobileNumber.length()==11)){
                  result = "84"+mobileNumber.substring(1,mobileNumber.length());
          }

          //Truong hop dau so bat dau la 9 hoac 121 hoac 122 hoac 126 hoac 128
          if((mobileNumber.startsWith("90") && mobileNumber.length()==9) || (mobileNumber.startsWith("93") && mobileNumber.length()==9) || (mobileNumber.startsWith("121") && mobileNumber.length()==10) || (mobileNumber.startsWith("122") && mobileNumber.length()==10) || (mobileNumber.startsWith("126") && mobileNumber.length()==10) || (mobileNumber.startsWith("128") && mobileNumber.length()==10)){
                  result = "84"+mobileNumber;
          }

          //Truong hop dau so bat dau la 849 hoac 84122 hoac 84126 hoac 84128
          if((mobileNumber.startsWith("8490") && mobileNumber.length()==11) || (mobileNumber.startsWith("8493") && mobileNumber.length()==11) || (mobileNumber.startsWith("84121") && mobileNumber.length()==12) || (mobileNumber.startsWith("84122") && mobileNumber.length()==12) || (mobileNumber.startsWith("84126") && mobileNumber.length()==12) || (mobileNumber.startsWith("84128") && mobileNumber.length()==12)){
                  result = mobileNumber;			
          }

          return result;
	}
	
	
	/*
	 *	Tao nhom moi voi ten nhom va so dien thoai cua nguoi tao nhom
	 *		return -1 : So dien thoai cua nguoi tao nhom khong hop le
	 *		return  0 : Co loi xay ra
	 *		return >0 : Tao nhom thanh cong
	 */
	public int createGroup(String groupName, String mobileGroup){
		
		if(groupName  ==null) return -3;
		if(mobileGroup==null) return -1;
		
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return -1;
		
		
		int result = 0;
		String sqlCmd = "";
		//DBConnection dbConnection = new DBConnection();		
		
		try
		{	
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();	
			//Khong the ton tai hai nhom co cung ten
			String[] groups = groupName.split(",");
			for(int i=0;i<groups.length;i++)
			{
				boolean groupIsExist = false;
				sqlCmd = "select * from score24.mbf_group where LOWER(group_name) = '"+groups[i].toLowerCase()+"' and mobile_number = '"+mobileGroup+"'";
				ResultSet rs = dbConnection.stmt.executeQuery(sqlCmd);
				if(rs.next()){
					groupIsExist = true;
				}rs.close();
							
				if(groupIsExist) {
					dbConnection.putConnection();
					return -2;
				}
				
				sqlCmd = "insert into score24.mbf_group(group_id, mobile_number, group_name) values(seq_group_id.NEXTVAL,'"+mobileGroup+"','"+groups[i]+"')";					
				result = dbConnection.stmt.executeUpdate(sqlCmd);
				
			}	
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from createGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return 0;
		}
		return result;			
	}
		
	/*	Tao nhom moi voi ten nhom, so dien thoai cua nguoi tao nhom va so dien thoai trong nhom
	 *		return -1: So dien thoai khong hop le
	 *		return -2: Nhom da ton tai
	 *		return  0: Co loi xay ra
	 *		return >0: Tao nhom thanh cong	 
	 */
	public int createGroup(String groupName, String mobileGroup, String mobileNumber){
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return -1;
		
		//Kiem tra xem trong so dien thoai nhap vao co so nao la ngoai mang khong
		String[] mobiles = mobileNumber.split(",");
		for(int i=0;i<mobiles.length;i++)
		{
			String tmpMobile = getMobileNumber(mobiles[i]);
			if(tmpMobile.equals("0"))
			{
				return -3;
			}
		}

		int result = 0;
		String sqlCmd = "";
		
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();						
			//Them nhom moi (Luu y, khong the ton tai hai nhom co cung ten)			
			String[] groups = groupName.split(",");
			for(int i=0;i<groups.length;i++)
			{
				boolean groupIsExist = false;
				sqlCmd = "select * from score24.mbf_group where LOWER(group_name) = '"+groupName.toLowerCase()+"' and mobile_number = '"+mobileGroup+"'";
				ResultSet rs = dbConnection.stmt.executeQuery(sqlCmd);
				if(rs.next()){
					groupIsExist = true;
				}rs.close();
							
				if(groupIsExist) {
					dbConnection.putConnection();
					return -2;
				}
				
				sqlCmd = "insert into score24.mbf_group(group_id, mobile_number, group_name) values(seq_group_id.NEXTVAL,'"+mobileGroup+"','"+groups[i]+"')";
				result = dbConnection.stmt.executeUpdate(sqlCmd);
			
				//Them so dien thoai vao nhom vua tao
				//String[] mobiles = mobileNumber.split(",");
				for(int j=0;j<mobiles.length;j++)
				{			
					String tmpMobile = getMobileNumber(mobiles[j]);
					if(!tmpMobile.equals("0")){				
						sqlCmd = "insert into score24.mbf_mobile(mobile_number, group_id) values('"+tmpMobile+"',seq_group_id.CURRVAL)";
						result = dbConnection.stmt.executeUpdate(sqlCmd);
					}
				}
			}
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from createGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return 0;
		}
		return result;			
	}
	
	/*	Tao nhom moi voi ten nhom, so dien thoai, mieu ta nhom cua nguoi tao nhom va so dien thoai trong nhom
	 *		return -1: So dien thoai khong hop le
	 *		return -2: Nhom da ton tai
	 *		return  0: Co loi xay ra
	 *		return >0: Tao nhom thanh cong	 
	 */
	public int createGroup(String groupName, String mobileGroup, String mobileNumber, String groupDesc){
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return -1;
		
		//mobileNumber = getMobileNumber(mobileNumber);
		//if(mobileNumber.equals("0")) return -1;

		int result = 0;
		String sqlCmd = "";
			
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();					
			//Them nhom moi (Luu y, khong the ton tai hai nhom co cung ten)
			
			String[] groups = groupName.split(",");
			for(int i=0;i<groups.length;i++){
				boolean groupIsExist = false;
				sqlCmd = "select * from score24.mbf_group where LOWER(group_name) = '"+groupName.toLowerCase()+"' and mobile_number = '"+mobileGroup+"'";
				ResultSet rs = dbConnection.stmt.executeQuery(sqlCmd);
				if(rs.next()){
					groupIsExist = true;
				}rs.close();
							
				if(groupIsExist) {
					dbConnection.putConnection();
					return -2;
				}
				
				sqlCmd = "insert into score24.mbf_group(group_id, mobile_number, group_name, group_desc) values(seq_group_id.NEXTVAL,'"+mobileGroup+"','"+groups[i]+"',?)";
				dbConnection.setPrepareStatement(sqlCmd);
				dbConnection.pstmt.setString(1,groupDesc);
				
				result = dbConnection.pstmt.executeUpdate();
			
				//Them so dien thoai vao nhom vua tao
				String[] mobiles = mobileNumber.split(",");
				for(int j=0;j<mobiles.length;j++){			
					String tmpMobile = getMobileNumber(mobiles[j]);
					if(!tmpMobile.equals("0")){				
						sqlCmd = "insert into score24.mbf_mobile(mobile_number, group_id) values('"+tmpMobile+"',seq_group_id.CURRVAL)";
						result = dbConnection.stmt.executeUpdate(sqlCmd);
					}
				}
			}
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from createGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return 0;
		}
		return result;			
	}
	

	/*	Xoa nhom theo ten nhom va so dien thoai cua chu nhom
	 *		return -1: So dien thoai khong hop le
	 *		return  0: Co loi xay ra
	 *		return >0: Xoa nhom thanh cong	 
	 */
	public int deleteGroup(String groupName, String mobileGroup){	
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return -1;		
		
		int result = 0;
		String sqlCmd = "";
			
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();			
			//Xoa cac so dien thoai trong nhom
			sqlCmd = "delete from score24.mbf_mobile where group_id in (select group_id from mbf_group where LOWER(group_name) = '"+groupName.toLowerCase()+"' and mobile_number = '"+mobileGroup+"')";
			result = dbConnection.stmt.executeUpdate(sqlCmd);			
			
			//Xoa nhom
			sqlCmd = "delete from score24.mbf_group where LOWER(group_name) = '"+groupName.toLowerCase()+"' and mobile_number ='"+mobileGroup+"'";
			result = dbConnection.stmt.executeUpdate(sqlCmd);		
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Error from ejb.deleteGroup function"+ex.toString());
			dbConnection.putConnection();
			return 0;
		}
		return result;			

	}
	

	/*	Xoa nhom theo ten nhom va so dien thoai cua chu nhom
	 *		return -1: So dien thoai khong hop le
	 *		return  0: Co loi xay ra
	 *		return >0: Xoa nhom thanh cong	 
	 */	 
	public String deleteGroup(String groupName, String mobileGroup, String mobileNumber){	
		//mobileGroup = getMobileNumber(mobileGroup);
		//if(mobileGroup.equals("0")) return -1;
		
		System.out.println("deleteGroup processing!");
		
		int rowEffect   = 0;
		int countGroup  = 0;
		int countMobile = 0;
		String result   = "";
		String sqlCmd   = "";
		String inMobiles= "";		
		try{
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();	
			if((groupName == null)  || (groupName.equals("null"))) 		groupName = "";
			if((mobileNumber==null) || (mobileNumber.equals("null")))	mobileNumber = "";
			String group_id = "";			
			if((groupName.length()>0 ) && (mobileNumber.length()>0))
			{	//DEL mobiles groups
				String tmp 		 = "";
				tmp = groupName;
				groupName = mobileNumber;
				mobileNumber = tmp;
				String[] groups  = groupName.split(",");
				String[] mobiles = mobileNumber.split(",");				
				boolean first 	 = true;
				tmp 		 = "";
				
				for(int i=0;i<mobiles.length;i++){
					tmp = getMobileNumber(mobiles[i]);
					if(!(tmp.equals("0"))){
						if(first) 	inMobiles += "'"+tmp+"'";	
						else 		inMobiles += ",'"+tmp+"'";	
						first = false;							
					}
				}
				if(inMobiles.length()==0) inMobiles = "''";
				
				//Xoa danh sach so dien thoai co trong nhom i
				for(int i=0;i<groups.length;i++)
				{
					sqlCmd = "select group_id from score24.mbf_group where LOWER(group_name)='"+groups[i].toLowerCase()+"' and mobile_number='"+mobileGroup+"'";
					ResultSet rs = dbConnection.stmt.executeQuery(sqlCmd);
					if(rs.next())
						group_id = rs.getString(1);
					rs.close();
					sqlCmd 	   = "delete from score24.mbf_mobile where group_id='"+group_id+"' and mobile_number in("+inMobiles+")";					
					rowEffect += dbConnection.stmt.executeUpdate(sqlCmd);
					
					//Cap nhat loai nhom
					sqlCmd = "select count(*) from score24.mbf_mobile where group_id='"+group_id+"'";
					int num_of_mobile = 0;
					rs = dbConnection.stmt.executeQuery(sqlCmd);
					if(rs.next())
						num_of_mobile = rs.getInt(1);
					rs.close();						
					int type_group = 1;
					if(num_of_mobile<=5)
					{
						type_group = 1;
					}
					else
					{
						type_group = 2;
					}
					sqlCmd = "update score24.mbf_group set group_type="+type_group+" where group_id='"+group_id+"'";
					dbConnection.stmt.executeUpdate(sqlCmd);		
				}
				if(rowEffect>0)	result = "Quy khach da xoa so dien thoai thanh cong! Xin cam on Quy khach da su dung dich vu MobiList cua MobiFone.";
				else result = "Co loi trong qua trinh xoa so dien thoai khoi nhom. Xin Quy khach vui long thu lai.";
			}
			else
			{		//DEL mobile_or_group
				
				//Xoa cac so dien thoai				
				
				boolean first = true;
				String[] mobiles = mobileNumber.split(",");
				String tmp = "";
				for(int i=0;i<mobiles.length;i++){
					tmp = getMobileNumber(mobiles[i]);
					if(!(tmp.equals("0"))){
						if(first) 	inMobiles += "'"+tmp+"'";	
						else 		inMobiles += ",'"+tmp+"'";	
						first = false;							
					}
				}
				if(inMobiles.length()==0) inMobiles="''";
				
				ArrayList arrAll = getAllGroup(mobileGroup);
				if(arrAll!=null)
				for(int i=0;i<arrAll.size();i++){
					objGroup obj = (objGroup)arrAll.get(i);
					//do insert
					sqlCmd       = "delete from score24.mbf_mobile where mobile_number in ("+inMobiles+") and group_id = "+obj.getGroupId();
					dbConnection.stmt = dbConnection.con.createStatement();
					countMobile += dbConnection.stmt.executeUpdate(sqlCmd);

				}//end outter for loop				
				if(countMobile>0) result = "Quy khach da xoa so dien thoai thanh cong! Xin cam on Quy khach da su dung dich vu MobiList cua MobiFone.";

				//Xoa cac nhom
				String[] groups = groupName.split(",");
				for(int i=0;i<groups.length;i++)
				{
					if(groupIsExist(groups[i], mobileGroup))
					{
						countGroup ++;
						sqlCmd = "select group_id from score24.mbf_group where LOWER(group_name)='"+groups[i].toLowerCase()+"'";
						ResultSet rs = dbConnection.stmt.executeQuery(sqlCmd);
						String id_group = "";
						if(rs.next())
							id_group = rs.getString(1);
						rs.close();
						if(id_group!=null && id_group.length()>0)
						{
							sqlCmd = "delete from score24.mbf_mobile where group_id='"+id_group+"'";
							dbConnection.stmt.executeUpdate(sqlCmd);
						}	
						sqlCmd    = "delete from score24.mbf_group where LOWER(group_name) = '"+groups[i].toLowerCase()+"' and mobile_number ='"+mobileGroup+"'";					
						rowEffect+= dbConnection.stmt.executeUpdate(sqlCmd);
					}
				}
				if(rowEffect>0) result = "Quy khach da xoa nhom thanh cong. Xin cam on quy khach da su dung dich vu MobiList cua MobiList.";
				else if(rowEffect==0) result = "Nhom Quy khach vua nhap vao khong ton tai! Xin Quy khach vui long thu lai.";				
			}						
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Error from ejb.deleteGroup function"+ex.toString());
			dbConnection.putConnection();
			ex.printStackTrace();
			return "Co loi xay ra trong qua trinh xoa nhom! Xin Quy khach vui long thu lai.";
		}
		return result;			

	}
	
	/*	DEL so1[.so2.so3][nhom1.nhom2.nhom3]
	 *		return -1: So dien thoai khong hop le
	 *		return  0: Co loi xay ra
	 *		return >0: Xoa nhom thanh cong	 
	 */
	public int deleteMobileFromGroup(String groupName, String mobileGroup, String mobileNumber){	
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return -1;
		
		System.out.println("deleteMobileFromGroup processing!");
		
		int result = 0;
		String sqlCmd = "";
		
		String[] groups  = groupName.split(",");
		String[] mobiles = mobileNumber.split(",");
		String tmpMobile = "";
				
		try{
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();									
			for(int i=0;i<groups.length;i++){
				for(int j=0;j<mobiles.length;j++){
					tmpMobile = getMobileNumber(mobiles[i]);
					if(!mobileNumber.equals("0")){
						
						//Xoa cac so dien thoai trong nhom
						sqlCmd = "delete from score24.mbf_mobile where group_id in (select group_id from mbf_group where LOWER(group_name) = '"+groups[i].toLowerCase()+"' and mobile_number = '"+mobiles[j]+"')";
						result = dbConnection.stmt.executeUpdate(sqlCmd);			
						
						//Xoa nhom
						sqlCmd = "delete from score24.mbf_group where LOWER(group_name) = '"+groups[i].toLowerCase()+"' and mobile_number ='"+mobiles[j]+"'";
						result = dbConnection.stmt.executeUpdate(sqlCmd);			
						
					}					
				}
			}			
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Error from ejb.deleteMobileFromGroup function"+ex.toString());
			dbConnection.putConnection();
			return 0;
		}
		return result;			

	}
	
	/*
	 *	Lay tat ca cac so dien thoai theo ten nhom va so dien thoai cua chu nhom
	 *		return null: co loi xay ra
	 */
	public ArrayList getMobileOfGroup(String groupName, String mobileNumber){
		mobileNumber = getMobileNumber(mobileNumber);
		if(mobileNumber.equals("0")) return null;
		ArrayList arr = new ArrayList();
		String sqlCmd = "";		
		ResultSet rs = null;
		sqlCmd = "select mbf_mobile.mobile_number, group_name from score24.mbf_mobile, score24.mbf_group where mbf_mobile.group_id = mbf_group.group_id and mbf_group.mobile_number='"+mobileNumber+"' and LOWER(group_name) = '"+groupName.toLowerCase()+"'";
		try{
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();				
			rs = dbConnection.stmt.executeQuery(sqlCmd);
			while(rs.next()){
				arr.add(rs.getString("mobile_number"));
			}rs.close();
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Error from ejb.getMobileOfGroup function"+ex.toString());
			dbConnection.putConnection();
			return null;
		}
					
		return arr;			
	}
	
	/*
	 *	Lay tat ca cac so dien thoai theo so dien thoai cua chu nhom
	 *		return null: co loi xay ra
	 */
	public ArrayList getMobileOfGroup(String mobileNumber){
		mobileNumber = getMobileNumber(mobileNumber);
		if(mobileNumber.equals("0")) return null;
		ArrayList arr = new ArrayList();
		String sqlCmd = "";		
		ResultSet rs = null;
		
		sqlCmd = "SELECT group_id, mobile_number FROM score24.mbf_mobile ";
		sqlCmd+= "WHERE group_id IN (SELECT group_id FROM score24.mbf_group WHERE mobile_number = '"+mobileNumber+"')";
		try{
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();			
			rs = dbConnection.stmt.executeQuery(sqlCmd);
			while(rs.next()){
				objMobile obj = new objMobile();
				arr.add(rs.getString("mobile_number"));
			}rs.close();			
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Error from ejb.getMobileOfGroup function"+ex.toString());
			dbConnection.putConnection();
			return null;
		}			
		return arr;			
	}
	
	/*
	 *	Lay loai cua nhom
	 *		
	 */
	public int getTypeOfGroup(String groupName, String mobileNumber)
	{		
		mobileNumber = getMobileNumber(mobileNumber);
		if(mobileNumber.equals("0")) return 1;
		int type = 1;
		String sqlCmd = "";		
		ResultSet rs = null;		
		sqlCmd = "select group_type from score24.mbf_group where mobile_number='"+mobileNumber+"' and LOWER(group_name)='"+groupName.toLowerCase()+"'";
		try{
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();										
			rs = dbConnection.stmt.executeQuery(sqlCmd);
			if(rs.next())
				type = rs.getInt(1);
			rs.close();		
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Error from ejb.getTypeOfGroup function"+ex.toString());
			dbConnection.putConnection();
			return 1;
		}
					
		return type;			
	}


	/*	
	 *	Them 1 hay nhieu so dien thoai vao 1 nhom tren web
	 *		return -1: Co so dien thoai khong hop le
	 *		return  0: Co loi xay ra
	 *		return >0: Them so dien thoai vao nhom thanh cong
	 */
	public int addMobileToGroup(String groupId, String mobileNumbers){
		int gid = 0;
		try{
			gid = Integer.parseInt(groupId);			
		}catch(Exception ex){
			gid = -1;
		}
		
		int result     = 0;
		String sqlCmd  = "";		
		String tmp	   = "";
		ResultSet rs   = null;
		boolean exist  = false;
		
		
		try{			
			dbConnection.getConnectionAndVerify();			
			dbConnection.stmt = dbConnection.con.createStatement();	
			String[] mobiles = mobileNumbers.split(",");
			
			for(int i=0;i<mobiles.length;i++)
			{
				tmp = getMobileNumber(mobiles[i]);								
				if(!tmp.equals("0"))
				{
					//check is exist
					exist  = false;
					sqlCmd = "SELECT * FROM score24.mbf_mobile WHERE mobile_number='"+tmp+"' AND group_id ="+groupId;
					rs = dbConnection.stmt.executeQuery(sqlCmd);
					if(rs.next())
					{
						exist = true;
					}
					rs.close();
					if(!exist)
					{
						sqlCmd = "select count(*) from score24.mbf_mobile where group_id='"+groupId+"'";
						dbConnection.stmt = dbConnection.con.createStatement();	
						rs = dbConnection.stmt.executeQuery(sqlCmd);
						int num_of_mobile = 0;
						if(rs.next())
							num_of_mobile = rs.getInt(1);
						rs.close();	
						if(num_of_mobile<10)
						{
							sqlCmd = "INSERT INTO score24.mbf_mobile(mobile_number, group_id) VALUES('"+tmp+"',"+groupId+")";
							dbConnection.stmt = dbConnection.con.createStatement();	
							result = dbConnection.stmt.executeUpdate(sqlCmd);
						}
						else
						{
							result = 4;
							break;
						}	
												
					}
					else
					{
						result = 3;
					}
				}
			}			

			dbConnection.putConnection();
			
		}catch(Exception ex){
			System.out.println("Error from ejb.addMobileToGroup function"+ex.toString());
			dbConnection.putConnection();
			return 0;
		}		
		return result;				
		
	}
	
	
	/*	
	 *	Them 1 hay nhieu so dien thoai vao 1 hay nhieu nhom
	 *		return -1: Co so dien thoai khong hop le
	 *		return -5: Co loi xay ra
	 *		return >0: Them so dien thoai vao nhom thanh cong
	 */
	public String addMobileToGroup(String mobileNumber, String groupName, String mobileGroup){		

		//int result 		= 0;
		int group_id 	= 0;		
		int countGroup  = 0;
		int countMobile = 0;
		int rowEffect   = 0;
		
		String sqlCmd 	= "";
		String result   = "";		//format: case&&result
		ResultSet rs 	= null;

		
		
		try{			
			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();	
			if(groupName != null)
			{
				String[] tmpGroups = groupName.split(",");
				for(int i=0;i<tmpGroups.length;i++)
				{	
					if((groupNameIsValid(tmpGroups[i])) && (groupIsExist(tmpGroups[i], mobileGroup)))
						countGroup ++;
				}
			}
		
			if(mobileNumber != null){
				String[] tmpMobiles = mobileNumber.split(",");
				for(int i=0;i<tmpMobiles.length;i++)
				{
					if(!(getMobileNumber(tmpMobiles[i]).equals("0")))
						countMobile ++;
					else
					{
						dbConnection.putConnection();
						result = "Tin nhan khong hop le! Dich vu MobiList chi cho phep gui SMS toi thue bao mang MobiFone.";
						return result;
					}
				}
			}
			
			if((groupName == null) || (groupName.equalsIgnoreCase("null"))) 		groupName 		= "";
			if((mobileNumber == null) || mobileNumber.equalsIgnoreCase("null"))	    mobileNumber	= "";
						
			if((groupName.length()>0) && (countGroup==0)){			//Ten nhom khong hop le
				result = "Nhom Quy khach vua nhap vao khong ton tai! Xin Quy khach vui long thu lai";
				dbConnection.putConnection();
				return result;
			}
			
			if((mobileNumber.length()>0) && (countMobile==0)){	//So dien thoai khong hop le
				result = "So dien thoai Quy khach vua nhap vao khong hop le. Xin Quy khach vui long thu lai";				
				dbConnection.putConnection();
				return result;
			}
			
			if((mobileNumber.length()==0) && (groupName.length()==0)){
				result = "Quy khach phai nhap so dien thoai va ten nhom muon them! Xin Quy khach vui long thu lai";				
				dbConnection.putConnection();
				return result;				
			}
			
			if(countMobile>0){
				if(countGroup > 0){					
				//Them vao cac nhom co trong danh sach
					String[] tmpGroups = groupName.split(",");
					for(int i=0;i<tmpGroups.length;i++){
						if((groupNameIsValid(tmpGroups[i])) && (groupIsExist(tmpGroups[i], mobileGroup))){
							sqlCmd = "select group_id from score24.mbf_group where mobile_number = '"+mobileGroup+"' and LOWER(group_name) = '"+tmpGroups[i].toLowerCase()+"'";							
							dbConnection.stmt = dbConnection.con.createStatement();	
							rs = dbConnection.stmt.executeQuery(sqlCmd);
							if(rs.next()){
								group_id = rs.getInt("group_id");
							}rs.close();

							String[] mobiles = mobileNumber.split(",");
							for(int j=0;j<mobiles.length;j++){
								//do insert
								String tmpAt = mobiles[j];
								tmpAt = getMobileNumber(tmpAt);
								if((!tmpAt.equals("0")) && (!mobileIsExistInGroup(group_id,tmpAt)))
								{
									sqlCmd = "select count(*) from score24.mbf_mobile where group_id='"+group_id+"'";
									dbConnection.stmt = dbConnection.con.createStatement();	
									rs = dbConnection.stmt.executeQuery(sqlCmd);
									int num_of_mobile = 0;
									if(rs.next())
										num_of_mobile = rs.getInt(1);
									rs.close();
									if(num_of_mobile<10)
									{
										sqlCmd = "select * from score24.mbf_mobile where mobile_number='"+tmpAt+"' and group_id='"+group_id+"'";
										
										rs = dbConnection.stmt.executeQuery(sqlCmd);
										boolean fMobileExit = false;
										if(rs.next())
											fMobileExit = true;
										rs.close();
										if(fMobileExit)
											rowEffect+=1;
										else
										{									
											sqlCmd     = "insert into score24.mbf_mobile (mobile_number, group_id) values('"+tmpAt+"',"+group_id+")";
											rowEffect += dbConnection.stmt.executeUpdate(sqlCmd);
											num_of_mobile++;
										}							
										
										int type_group = 1;									
										if(num_of_mobile<=5)
										{
											type_group = 1;
										}
										else
										{
											type_group = 2;
										}
										sqlCmd = "update score24.mbf_group set group_type="+type_group+" where group_id='"+group_id+"'";
										dbConnection.stmt.executeUpdate(sqlCmd);
									}
									else
									{
										result = "Moi nhom chi duoc phep luu khong qua 10 so dien thoai! Xin quy khach vui long thu lai.";	
										dbConnection.putConnection();
										return result;
									}	
								}
								else if(!tmpAt.equals("0"))
								{
									rowEffect++;
								}
							}
							
						}
						
					}//end for gmpGroup loop
					if(rowEffect>0) result = "Them moi so dien thoai thanh cong! Cam on Quy khach da su dung dich vu MobiList cua MobiFone.";
					else result = "Co loi xay ra trong qua trinh them so dien thoai! Xin quy khach vui long thu lai.";
					
				}else if(groupName.length()==0){	//Them vao tat ca cac nhom
					ArrayList arrAll = getAllGroup(mobileGroup);
					if(arrAll!=null)
					for(int i=0;i<arrAll.size();i++){
						objGroup obj = (objGroup)arrAll.get(i);
						
						String[] mobiles = mobileNumber.split(",");
						for(int j=0;j<mobiles.length;j++)
						{
							//do insert
							String tmpAt = mobiles[j];
							tmpAt = getMobileNumber(tmpAt);
							if((!tmpAt.equals("0")) && (!mobileIsExistInGroup(Integer.parseInt(obj.getGroupId()),tmpAt)))
							{
								sqlCmd     = "insert into score24.mbf_mobile (mobile_number, group_id) values('"+tmpAt+"',"+obj.getGroupId()+")";
								rowEffect += dbConnection.stmt.executeUpdate(sqlCmd);
							}
							else if(!tmpAt.equals("0"))
							{
								rowEffect++;
							}
						}												
					}
					if(rowEffect>0) result = "Them moi so dien thoai thanh cong! Cam on Quy khach da su dung dich vu MobiList cua MobiFone.";
					else result = "Co loi xay ra trong qua trinh them so dien thoai! Xin quy khach vui long thu lai.";					
				}
			}
			dbConnection.putConnection();		
		}catch(Exception ex){
			System.out.println("Error from ejb.addMobileToGroup function"+ex.toString());
			dbConnection.putConnection();
			return "Co loi xay ra trong qua trinh them so dien thoai! Xin quy khach vui long thu lai.";
		}
		//Co so dien thoai khong hop le
		return result;		
	}	 
	/*public int addMobileToGroup(String mobileNumber, String groupName, String mobileGroup){		
		if(mobileGroup!=null){
			mobileGroup = getMobileNumber(mobileGroup);
			if(mobileGroup.equals("0")) return -2;
		}

		int result 		= 0;
		int group_id 	= 0;		
		String sqlCmd 	= "";
		ResultSet rs 	= null;

		DBConnection dbConnection = new DBConnection();
		
		try{			
			dbConnection.createConnection();	
			
			if(groupName == null) return -1;
			String[] groups = groupName.split(",");						
				
			if(mobileNumber!=null){					
				for(int j=0;j<groups.length;j++){						
					//lay group_id theo mobileGroup va groupName
					sqlCmd = "select group_id from score24.mbf_group where mobile_number = '"+mobileGroup+"' and group_name = '"+groups[j]+"'";
					rs = dbConnection.stmt.executeQuery(sqlCmd);
					if(rs.next()){
						group_id = rs.getInt("group_id");
					}rs.close();
						
					String[] mobiles = mobileNumber.split(",");
					for(int i=0;i<mobiles.length;i++){											
						//do insert
						String tmpAt = mobiles[i];
						tmpAt = getMobileNumber(tmpAt);						
						if((!tmpAt.equals("0")) && (!mobileIsExistInGroup(group_id,tmpAt))){
							sqlCmd = "insert into score24.mbf_mobile (mobile_number, group_id) values('"+tmpAt+"',"+group_id+")";
							result += dbConnection.stmt.executeUpdate(sqlCmd);							
						}
					}
				}//end for loop				
			}//end if
			
			dbConnection.removeConnection();
		}catch(Exception ex){
			System.out.println("Error from ejb.addMobileToGroup function"+ex.toString());
			return -5;
		}finally{
			dbConnection.removeConnection();
		}
		//Co so dien thoai khong hop le
		return result;		
	}*/
	
	/*
	 *	Xoa 1 hay nhieu so dien thoai khoi 1 hay nhieu nhom
	 *		return -1: So dien thoai khong hop le
	 *		return  0: Co loi xay ra
	 *		return >0: Xoa dien thoai khoi nhom thanh cong
	 */
	 
	public int removeMobileFromGroup(String groupName, String mobileGroup, String mobileNumber){
		if(mobileGroup!=null){
			mobileGroup = getMobileNumber(mobileGroup);
			if(mobileGroup.equals("0")) return -1;
		}
		String[] groups = groupName.split(",");
		int result = 0;
		String sqlCmd = "";
		
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();			
			if(mobileNumber!=null){
				String[] mobiles = mobileNumber.split(",");				
				for(int j=0;j<groups.length;j++){				
					for(int i=0;i<mobiles.length;i++){
						
						String tmp = mobiles[i];
						tmp = getMobileNumber(tmp);
						if(tmp.equals("0")) {
							dbConnection.putConnection();
							return -1;
						}
						
						sqlCmd = " delete from score24.mbf_mobile ";
						sqlCmd+= " where group_id = (select group_id from score24.mbf_group where LOWER(group_name) = '"+groups[j].toLowerCase()+"' and mobile_number = '"+mobileGroup+"') ";
						sqlCmd+= " and mobile_number = '"+tmp+"'";
						//System.out.println("removeMobileFromGroup: "+sqlCmd);
						result += dbConnection.stmt.executeUpdate(sqlCmd);						
						
					}				
				}							
			}	
			dbConnection.putConnection();
		
		}catch(Exception ex){
			System.out.println("Exception from removeMobileFromGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return 0;
		}
		
		return result;
	}
	
	/*
	 *	Ham kiem tra xem khach hang da dang ky dich vu mobiList hay chua
	 */
	public boolean customerIsExist(String mobileNumber){
		
		boolean isExist = false;
		
		if(mobileNumber == null) return false;
		mobileNumber = getMobileNumber(mobileNumber);
		if(mobileNumber.equals("0")) return false;
		
		ResultSet rs = null;
		String sqlCmd = "";
			
		try{			
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();			
			sqlCmd = "SELECT * FROM score24.mbf_group WHERE mobile_number = '"+mobileNumber+"'";
					
			rs     = dbConnection.stmt.executeQuery(sqlCmd);					
			if(rs.next()){
				isExist = true;
			}rs.close();	
			dbConnection.putConnection();		
		}catch(Exception ex){
			System.out.println("Exception from customerIsExist() function: "+ex.toString());
			dbConnection.putConnection();
			return false;
		}		
		return isExist;
	}
	
	/*
	 *	Kiem tra xem so dien thoai co thuoc trong nhom xyz nao do khong
	 */		
	public boolean mobileIsExistInGroup(int groupId, String mobileNumber){
		
		boolean isExist = false;
		
		if(mobileNumber == null) return false;
		mobileNumber = getMobileNumber(mobileNumber);
		if(mobileNumber.equals("0")) return false;
		
		ResultSet rs = null;
		String sqlCmd = "";
				
		try{			
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();		
			sqlCmd = "SELECT * FROM score24.mbf_mobile WHERE mobile_number = '"+mobileNumber+"' and group_id = "+groupId;
						
			rs     = dbConnection.stmt.executeQuery(sqlCmd);					
			if(rs.next()){
				isExist = true;
			}rs.close();
			
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from mobileIsExistInGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return false;
		}		
		return isExist;		
	}

	
	/*
	 *	Kiem tra xem nhom co thuoc so thue bao xxx khong
	 */	
	public boolean groupIsExist(String groupName, String mobileGroup){
		
		boolean isExist = false;
		
		if(mobileGroup == null) return false;
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return false;
		
		ResultSet rs = null;
		String sqlCmd = "";		
		
		try{
			DBConnection dbConnection = new DBConnection();
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();		
			long t1 = System.currentTimeMillis();	
			sqlCmd = "SELECT * FROM score24.mbf_group WHERE mobile_number = '"+mobileGroup+"' AND LOWER(group_name) = '"+groupName.toLowerCase()+"'";			
			//dbConnection.stmt = dbConnection.con.
			rs = dbConnection.stmt.executeQuery(sqlCmd);					
			if(rs.next()){
				isExist = true;
			}rs.close();
			dbConnection.putConnection();
			long t2 = System.currentTimeMillis();
			
		}catch(Exception ex){
			ex.printStackTrace();
			dbConnection.putConnection();
			return false;
		}
		return isExist;
		
	}
	

	/*
	 *	Kiem tra xem so dien thoai co thuoc trong cac nhom khong
	 */		
	public boolean mobileIsExist(String mobileGroup, String mobileNumber){
		
		boolean isExist = false;
		
		if(mobileNumber == null) return false;
		mobileNumber = getMobileNumber(mobileNumber);
		if(mobileNumber.equals("0")) return false;
		
		if(mobileGroup == null) return false;
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return false;

		ResultSet rs = null;
		String sqlCmd = "";
		
		
		try{			
			dbConnection.getConnectionAndVerify();			
			dbConnection.stmt = dbConnection.con.createStatement();	
			sqlCmd = "SELECT mobile_number FROM score24.mbf_mobile ";
			sqlCmd+= "WHERE group_id in (select group_id from score24.mbf_group where mobile_number = '"+mobileGroup+"')";
			sqlCmd+= "AND mobile_number = '"+mobileNumber+"'";
			dbConnection.getConnectionAndVerify();
			rs     = dbConnection.stmt.executeQuery(sqlCmd);					
			if(rs.next()){
				isExist = true;
			}rs.close();	
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from mobileIsExist() function: "+ex.toString());
			dbConnection.putConnection();
			return false;
		}		
		return isExist;		
	}
	
	/*
	 *	Phuong thuc lay tat ca cac nhom theo SDT cua chu nhom
	 */			
	public ArrayList getAllGroup(String mobileGroup){
		ArrayList arrGroup = new ArrayList();
		
		if(mobileGroup == null) return null;
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return null;

		ResultSet rs  = null;
		String sqlCmd = "";
		
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();	
			sqlCmd = "SELECT group_id, group_name, group_desc FROM score24.mbf_group WHERE mobile_number = '"+mobileGroup+"'";		
			rs     = dbConnection.stmt.executeQuery(sqlCmd);					
			while(rs.next()){
				
				objGroup group = new objGroup();
				
				group.setGroupId(rs.getString("group_id"));
				group.setGroupName(rs.getString("group_name"));
				group.setGroupDesc(rs.getString("group_desc"));
				group.setGroupMobile(mobileGroup);
				
				arrGroup.add(group);
			}rs.close();			
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from getAllGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return null;
		}
		
		return arrGroup;
	}
	
	/*
	 *	Phuong thuc lay thong tin mot nhom
	 */			
	public objGroup getGroupInfor(int groupId){

		ResultSet rs  = null;
		String sqlCmd = "";
		objGroup group = new objGroup();
		try{			
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();	
			sqlCmd = "SELECT mobile_number, group_name, group_desc FROM score24.mbf_group WHERE group_id = "+groupId;		
			
			rs     = dbConnection.stmt.executeQuery(sqlCmd);					
			if(rs.next()){								
				
				group.setGroupId(String.valueOf(groupId));
				group.setGroupName(rs.getString("group_name"));
				group.setGroupDesc(rs.getString("group_desc"));
				group.setGroupMobile(rs.getString("mobile_number"));
				
			}rs.close();
			dbConnection.putConnection();
			
		}catch(Exception ex){
			System.out.println("Exception from getGroupInfor() function: "+ex.toString());
			dbConnection.putConnection();
			return null;
		}	
		return group;
	}
	
	/*
	 *	Lay tat ca SDT trong nhom theo SDT chu nhom va ten nhom
	 */			
	public ArrayList getAllMobileFromGroup(String mobileGroup, String groupName){
		ArrayList arrMobiles = new ArrayList();
		
		if(mobileGroup == null) return null;
		mobileGroup = getMobileNumber(mobileGroup);
		if(mobileGroup.equals("0")) return null;
		
		ResultSet rs 	= null;
		String sqlCmd 	= "";
		
		
		try{			
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();			
			sqlCmd = "SELECT mobile_number FROM score24.mbf_mobile WHERE group_id in ";
			sqlCmd+= "(select group_id from score24.mbf_group where LOWER(group_name) = '"+groupName.toLowerCase()+"' and mobile_number = '"+mobileGroup+"')";
			
			rs     = dbConnection.stmt.executeQuery(sqlCmd);					
			while(rs.next()){
				arrMobiles.add(rs.getString("mobile_number"));
			}rs.close();			
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from getAllMobileFromGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return null;
		}		
		return arrMobiles;
	}
	
	/*
	 *	Lay tat ca SDT trong nhom theo SDT ma nhom
	 */			
	public ArrayList getAllMobileFromGroup(int groupId){
		ArrayList arrMobiles = new ArrayList();		

		ResultSet rs 	= null;
		String sqlCmd 	= "";
		
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();	
			sqlCmd = "SELECT mobile_number FROM score24.mbf_mobile WHERE group_id = "+groupId;			
			rs     = dbConnection.stmt.executeQuery(sqlCmd);					
			while(rs.next()){
				objMobile obj = new objMobile();
				obj.setGroupId(String.valueOf(groupId));
				obj.setMobileNumber(rs.getString("mobile_number"));
				arrMobiles.add(obj);
			}rs.close();			
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from getAllMobileFromGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return null;
		}
		
		return arrMobiles;
	}

	/*
	 *	Phuong thuc xoa mot nhom
	 */			
	//public int deleteGroupById(int groupId){		
	public int deleteGroupById(String groupId){		
		
		if(groupId == null) return -1;
		int result = 0;
		String sqlCmd 	= "";
		
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();	
			String[] groups = groupId.split(",");
			for(int i=0;i<groups.length;i++){
				try{
					int number = Integer.parseInt(groups[i]);
					//delete from mbf_mobile table						
					sqlCmd = "DELETE FROM score24.mbf_mobile WHERE group_id = "+groups[i];						
					result = dbConnection.stmt.executeUpdate(sqlCmd);					
									
					//delete from mbf_group table						
					sqlCmd = "DELETE FROM score24.mbf_group  WHERE group_id = "+groups[i];						
					result = dbConnection.stmt.executeUpdate(sqlCmd);					
										
				}catch(NumberFormatException nf){}
				 catch(Exception ex){}				
			}
			dbConnection.putConnection();			
		}catch(Exception ex){
			System.out.println("Exception from deleteGroupById() function: "+ex.toString());
			dbConnection.putConnection();
			return -1;
		}
		
		return result;
	}
	
	
	/*
	 *	Phuong thuc xoa cac so dien thoai trong mot nhom
	 */			
	public int deleteMobiles(int groupId, String mobileNumber){		
		
		if(mobileNumber == null) return -2;
		
		int result = 0;
		String sqlCmd 	= "";
	
		try{			
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();	
			//delete from mbf_mobile table						
			String[] mobiles = mobileNumber.split(",");
			String tmp = "";
			for(int i=0;i<mobiles.length;i++){				
				tmp = getMobileNumber(mobiles[i]);
				
				if(tmp.equals("0")) {
					dbConnection.putConnection();
					return -2;
				}
				
				sqlCmd = "DELETE FROM score24.mbf_mobile WHERE group_id = "+groupId+" AND mobile_number='"+tmp+"'";
				result = dbConnection.stmt.executeUpdate(sqlCmd);	
				
			}
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from deleteMobiles() function: "+ex.toString());
			dbConnection.putConnection();
			return -1;
		}
		
		return result;
	}
	
	/*
	 *	Phuong thuc sua noi dung SDT
	 */			
	 public int updateMobile(int groupId, String oldMobileNumber, String newMobileNumber){
		if(oldMobileNumber == null) return -2;
		if(newMobileNumber == null) return -2;
		
		int result = 0;
		String sqlCmd 	= "";
		
		try{
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();			
			String   tmp = "";			
			//update
			tmp = getMobileNumber(oldMobileNumber);
			if(!tmp.equals("0")){
				
				sqlCmd = "UPDATE score24.mbf_mobile SET mobile_number='"+newMobileNumber+"' WHERE mobile_number='"+oldMobileNumber+"' AND group_id = "+groupId;				
				result = dbConnection.stmt.executeUpdate(sqlCmd);																	
			}	
			dbConnection.putConnection();		
		}catch(Exception ex){
			System.out.println("Exception from updateMobile() function: "+ex.toString());
			dbConnection.putConnection();
			return -3;
		}
		
		return result;
	 }
	 
	 /*public int updateMobile(int groupId, String oldMobileNumber, String newMobileNumber){
		if(oldMobileNumber == null) return -2;
		if(newMobileNumber == null) return -2;
		
		int result = 0;
		String sqlCmd 	= "";
		DBConnection dbConnection = new DBConnection();
		
		try{			
			dbConnection.createConnection();	
			
			String[] oldMobiles = oldMobileNumber.split(",");
			String[] newMobiles = newMobileNumber.split(",");
			String   tmp = "";
			
			//deleting
			for(int i=0;i<oldMobiles.length;i++){
				tmp = getMobileNumber(oldMobiles[i]);
				if(!tmp.equals("0")){
					sqlCmd = "DELETE score24.mbf_mobile WHERE mobile_number='"+oldMobiles[i]+"' AND group_id = "+groupId;						
					result = dbConnection.stmt.executeUpdate(sqlCmd);													
				}
			}
			
			//inserting
			for(int i=0;i<newMobiles.length;i++){
				tmp = getMobileNumber(newMobiles[i]);
				if(!tmp.equals("0")){
					sqlCmd = "INSERT INTO score24.mbf_mobile(mobile_number, group_id) VALUES('"+newMobiles[i]+"',"+groupId+")";						
					result = dbConnection.stmt.executeUpdate(sqlCmd);																		
				}
			}
			
			
			dbConnection.removeConnection();
		}catch(Exception ex){
			System.out.println("Exception from updateMobile() function: "+ex.toString());
			return -3;
		}finally{
			dbConnection.removeConnection();
		}
		
		return result;
	 }*/
	 
	/*
	 *	Phuong thuc sua noi dung cua mot nhom
	 */			
	 public int updateGroup(int groupId, String groupName, String groupDesc){

		int result = 0;
		String sqlCmd 	= "";
		try{	
			dbConnection.getConnectionAndVerify();
			dbConnection.stmt = dbConnection.con.createStatement();			
			//delete from mbf_mobile table						
			sqlCmd = "UPDATE score24.mbf_group SET group_name = ?, group_desc = ? WHERE group_id = "+groupId;						
			dbConnection.setPrepareStatement(sqlCmd);
			dbConnection.pstmt.setString(1,groupName.toLowerCase());
			dbConnection.pstmt.setString(2,groupDesc);
			result = dbConnection.pstmt.executeUpdate();			
			dbConnection.putConnection();
		}catch(Exception ex){
			System.out.println("Exception from updateGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return -3;
		}
		return result;
	 	
	 }
	 
	/*
	 *	Phuong thuc sua noi dung cua mot nhom va cac so dien thoai thuoc nhom do
	 */			
	 public int updateGroup(int groupId, String groupName, String groupDesc, String mobileNumbers){

		int result = 0;
		String sqlCmd 	= "";
		try{
			dbConnection.getConnectionAndVerify();	
			dbConnection.stmt = dbConnection.con.createStatement();			
			//update mbf_group table						
			sqlCmd = "UPDATE score24.mbf_group SET group_name = ?, group_desc = ? WHERE group_id = "+groupId;						
			dbConnection.setPrepareStatement(sqlCmd);
			dbConnection.pstmt.setString(1,groupName.toLowerCase());
			dbConnection.pstmt.setString(2,groupDesc);
			result = dbConnection.pstmt.executeUpdate();			
						
			//delete all mobiles from mbf_mobile table have groupId
			sqlCmd = "delete from score24.mbf_mobile where group_id = "+groupId;
			System.out.println(sqlCmd);
			dbConnection.stmt.executeUpdate(sqlCmd);
			
			//update mbf_group table						
			if((mobileNumbers!=null) && (!mobileNumbers.equals(""))){
				String[] mobiles = mobileNumbers.split(",");
				String tmpMobile = "";
				for(int i=0;i<mobiles.length;i++){
					tmpMobile = getMobileNumber(mobiles[i]);
					if(tmpMobile.equals("0")) {
						dbConnection.putConnection();
						return -2;
					}
					
					sqlCmd = "insert into score24.mbf_mobile(mobile_number, group_id) ";	
					sqlCmd+= "values('"+tmpMobile+"',"+groupId+")";
					dbConnection.stmt.executeUpdate(sqlCmd);
				}
				
			}
			dbConnection.putConnection();
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Exception from updateGroup() function: "+ex.toString());
			dbConnection.putConnection();
			return -3;
		}
		return result;
	 	
	 }

	/*
	 *	Phuong thuc kiem tra tinh hop le cua ten nhom
	 */				 
	 public boolean groupNameIsValid(String groupName){
	 	String validRange = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM_";
	 	for(int i=0;i<groupName.length();i++){
	 		char at = groupName.charAt(i);	 		
	 		if(validRange.indexOf(at)==-1) return false;
	 	}
	 	return true;
	 }

	/*
	 *	Write log to MobiList
	 *	sender: 8026
	 */
	public void toLogMobiList(String sender, String receiver, String content)
	{		
		SimpleDateFormat sdfm = new SimpleDateFormat("yyMM");
		String table = "LOG_MOBILIST_" + String.valueOf(sdfm.format(new java.util.Date()));                
        String sqlcmd = "";
        try
        {
        	counter++;
        	dbConnection.getConnectionAndVerify();
        	dbConnection.pstmt = dbConnection.con.prepareStatement("insert into score24."+table+" values(?,?,sysdate,?)");
        	dbConnection.pstmt.setString(1,sender);
        	dbConnection.pstmt.setString(2,receiver);
        	dbConnection.pstmt.setString(3,content);
        	sqlcmd = "insert into score24.mobilist_temp values('"+sender+"','"+receiver+"',sysdate)";
        	dbConnection.stmt.executeUpdate(sqlcmd);
        	dbConnection.pstmt.executeUpdate();
        	if(counter>=1000)
        	{
        		counter=0;
        		sqlcmd = "delete from score24.mobilist_temp where sysdate-log_time>=1";
        		dbConnection.stmt.executeUpdate(sqlcmd);
        	}
        	dbConnection.putConnection();
        }catch(Exception ex){
        	ex.printStackTrace();
        	dbConnection.putConnection();
        }        		
	}
	
	
	public void sendSMS(String sender,String receiver,String content){
		try{
			counter++;
	      infogate.gateway.sms.client.SMSGatewayConnection con = new infogate.gateway.sms.client.SMSGatewayConnection("10.252.20.66",10003);
	      infogate.gateway.sms.utils.SMSMessage msg = new infogate.gateway.sms.utils.SMSMessage(sender,receiver,content);
	      con.sendMessage(msg);
    	}catch(Exception e){
      		e.printStackTrace();
    	}
	}

	public void sendSMS(String sender,String receiver,String content, String host, int port){
		try{
			counter++;
	      infogate.gateway.sms.client.SMSGatewayConnection con = new infogate.gateway.sms.client.SMSGatewayConnection(host,port);
	      infogate.gateway.sms.utils.SMSMessage msg = new infogate.gateway.sms.utils.SMSMessage(sender,receiver,content);
	      con.sendMessage(msg);
    	}catch(Exception e){
      		e.printStackTrace();
    	}
	}
	
	//Ham ghi file CDR
    public void toCDR(String sender,String receiver,int type)
    {
      try
      {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date receive,send;
        receive=new java.util.Date();
        send=new java.util.Date();
        com.utils.CDRWriter writer = new
        com.utils.CDRWriter("G:/resource/cdr/mobilist_tmp/","G:/resource/cdr/mobilist/","txt",900000,9999);
        String fileName = writer.write(sender,receiver,sdf.format(receive),type);
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
    }
    //Dem so luong tin nhan gui tu mot so trong mot gay
    public int getNumberSMS(String sender)
    {
    	int result = 0;
    	String sqlcmd = "select count(*) from mobilist_temp where sender='"+sender+"' and sysdate-log_time<1";
    	try
    	{
    		dbConnection.getConnectionAndVerify();
    		dbConnection.stmt = dbConnection.con.createStatement();
    		ResultSet rs = dbConnection.stmt.executeQuery(sqlcmd);
    		if(rs.next())
    			result = rs.getInt(1);
    		rs.close();
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	} 
    	return result;
    }
	
	public static void main(String[] args)
	{
		try
		{
			long t1 = System.currentTimeMillis();
			MobiListBean bean = new MobiListBean();			
			bean.makeConnection();
			int kq = bean.getNumberSMS("841227644744");
			System.out.println(kq);
			bean.removeConnection();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}		
	}
}
