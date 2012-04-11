package neo.score.client;

import com.logica.smpp.TCPIPConnection;
import org.mysmpp.gateway.utils.SMSMessage;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.io.*;
import com.logica.smpp.util.ByteBuffer;
import com.logica.smpp.Data;

public class RTCClient {

    private String host="10.252.20.66";
    private int port=10003;
    private TCPIPConnection con;
    
    private byte msgid=0;
    
    public RTCClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
	////////////////////////////////////////////////////////////////////////////
    private boolean connect() {
        try {
            Socket socket = new Socket(host, port);            
            con = new TCPIPConnection(socket);
            boolean flag = true;
            return flag;
        } catch(Exception e) {
            System.out.println("Exception in connecting to SMSGW (function connect): ".concat(String.valueOf(String.valueOf(e))));
			System.out.println("Host: "+host);
			System.out.println("Port: "+port);
        }
        return false;
    }
	////////////////////////////////////////////////////////////////////////////
    public void sendMessage(SMSMessage msg) {
        if(!connect())
            return;
        try {
            con.send(msg.getByteBuffer());
            con.close();
        } catch(Exception e) {
            System.out.println("Exception in sending message to SMSGW (function sendMessage): ".concat(String.valueOf(String.valueOf(e))));
			System.out.println("Host: "+host);
			System.out.println("Port: "+port);
        }
    }
	////////////////////////////////////////////////////////////////////////////
    public void sendMessage(String sender, String receiver, String content) {
        SMSMessage msg = new SMSMessage(sender, receiver, content);
        sendMessage(msg);
    }
	////////////////////////////////////////////////////////////////////////////
    public void sendMessage(String sender, String receiver, String content, byte emsClass, byte dataCoding, int clientId, String subAddress) {
        SMSMessage msg = new SMSMessage(sender, receiver, content, emsClass, dataCoding);
		msg.setSubAddress(subAddress);
        sendMessage(msg);
    }
    ////////////////////////////////////////////////////////////////////////////
    public void sendMessageRTC(String sender, String receiver, String content,String subAddress) {
        SMSMessage msg = new SMSMessage(sender, receiver, content);
		msg.setSubAddress(subAddress);
        sendMessage(msg);
    }
    ////////////////////////////////////////////////////////////////////////////
    public void sendUnicodeMessage(String sender,String receiver,String content, String subAddress) throws Exception{
        //"M\u01B0\u1EDDi n\u0103m qua, m\u1ED9t b\u00E1c s\u0129 \u1EDF Tr\u00F9ng Kh\u00E1nh, Trung Qu\u1ED1c, \u0111\u00E3 h\u1EB9n h\u00F2 v\u1EDBi h";
    	if (content.length()>70) content = content.substring(0,70);
		ByteBuffer buffer=new ByteBuffer();
		buffer.appendString(content,Data.ENC_UTF16);
		buffer.removeBytes(2);	        
        SMSMessage msg = new SMSMessage(sender, receiver, buffer.removeString(buffer.length(),Data.ENC_ISO8859_1),(byte)0x03,(byte)0x08);
        msg.setSubAddress(subAddress);
        sendMessage(msg);
    }
    public void sendLongMessage(String sender,String receiver,String content, String subAddress) throws Exception{
    	if (content.length()<=160){
    		sendMessage(sender, receiver, content);
    		return;
    	}
    	int id=0;
    	int total = content.length()/152;
    	if (content.length()%152>0) total++;
   		//if (msgid==255)msgid=0; msgid++;
   		//msgid = (byte)(new java.util.Date()).getSeconds();
   		msgid = (byte)160;
    	while (content.length()>0){
    		String message = "";
    		if (content.length()>152){
    			message = content.substring(0,152); 
    			content = content.substring(152);
    		} else {
    			message=content;
    			content="";
    		}
    		id++;
    		byte[] bt = {(byte)5,(byte) 0,(byte)3,msgid,(byte)total,(byte)id};
    		String head = new String(bt);
			ByteBuffer buffer=new ByteBuffer();
			buffer.appendString(message,Data.ENC_ISO8859_1);
	        SMSMessage msg = new SMSMessage(sender, receiver, head+buffer.removeString(buffer.length(),Data.ENC_ISO8859_1),(byte)0x40,(byte)0);
	        msg.setSubAddress(subAddress);
	        System.out.println("Sending long message "+id+" total:"+total+" messageid:"+msgid+" content="+message);
	        sendMessage(msg);
	        Thread.sleep(10);
    	}
    }
    public void sendLongUnicodeMessage(String sender,String receiver,String content, String subAddress) throws Exception{
    	if (content.length()<=70){
    		sendUnicodeMessage(sender, receiver, content,subAddress);
    		return;
    	}
    	int id=0;
    	int total = content.length()/67;
    	if (content.length()%67>0) total++;
   		msgid = (byte)162;
    	while (content.length()>0){
    		String message = "";
    		if (content.length()>67){
    			message = content.substring(0,67); 
    			content = content.substring(67);
    		} else {
    			message=content;
    			content="";
    		}
    		id++;
    		byte[] bt = {(byte)5,(byte) 0,(byte)3,msgid,(byte)total,(byte)id};
    		String head = new String(bt);
			ByteBuffer buffer=new ByteBuffer();
			buffer.appendString(message,Data.ENC_UTF16);
			buffer.removeBytes(2);
	        SMSMessage msg = new SMSMessage(sender, receiver, head+buffer.removeString(buffer.length(),Data.ENC_ISO8859_1),(byte)0x40,(byte)0x08);	        
	        msg.setSubAddress(subAddress);
	        System.out.println("Sending long unicode message "+id+" total:"+total+" messageid:"+msgid+" content="+message);
	        sendMessage(msg);
	        Thread.sleep(10);
    	}
    }
	////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) {
    	try {
	        RTCClient con = new RTCClient("10.252.20.66", 10003);
	        	        
	        String sender="919";
	        String receiver="841228333000";
	        //String receiver="84909099598";
	        
	        //String message = "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 1 ";
	        //message += "ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI A ";
	        //con.sendLongMessage(sender,receiver,message,"");
			
			//String message = "Nh\u00E0 b\u0103ng";	        
	        //String message = "Nh\u00E0 b\u0103ng b\u1ECB c\u01B0\u1EDBp 200.000$. Ch\u1EE7 nh\u00E0 b\u0103ng n\u00F3i v\u1EDBi PV: Anh ghi l\u00E0 nh\u00E0 b\u0103ng b\u1ECB m\u1EA5t n\u1EEDa tri\u1EC7u \u0111\u00F4la xem th\u1EB1ng \u0111\u00F3 thanh minh v\u1EDBi v\u1EE3 th\u1EBF n\u00E0o.";
	        //con.sendLongUnicodeMessage(sender,receiver,message,"84903209789");
	        con.sendMessageRTC(sender,receiver,"aaaaaaa","84906044701");

	        /*
	        byte[] bt1 = {(byte)5,(byte) 0,(byte)3,(byte)166,(byte)2,(byte)1};
	        byte[] bt2 = {(byte)5,(byte) 0,(byte)3,(byte)166,(byte)2,(byte)2};

	        String message1 = "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 1 ";
	        String message2 = "ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI ABCDEFGHI A ";

			ByteBuffer buffer=new ByteBuffer();
	        String head = new String(bt1);
			buffer.appendString(message1,Data.ENC_ISO8859_1);
			//buffer.removeBytes(2);
	        //SMSMessage msg = new SMSMessage("9226", "84909099598", head+message1,(byte)0x40,(byte)0xF5);
	        SMSMessage msg = new SMSMessage(sender, receiver, head+buffer.removeString(buffer.length(),Data.ENC_ISO8859_1),(byte)0x40,(byte)0);
	        con.sendMessage(msg);
	        Thread.sleep(100);
	        buffer=new ByteBuffer();
	        head = new String(bt2);
			buffer.appendString(message2,Data.ENC_ISO8859_1);
			//buffer.removeBytes(2);
	        //msg = new SMSMessage("9226", "84909099598", head+message2,(byte)0x40,(byte)0xF5);
	        msg = new SMSMessage(sender, receiver, head+buffer.removeString(buffer.length(),Data.ENC_ISO8859_1),(byte)0x40,(byte)0);
	        con.sendMessage(msg);
	        */
        } catch(Exception e) { 
        	e.printStackTrace();
        }
    }
	////////////////////////////////////////////////////////////////////////////
}
