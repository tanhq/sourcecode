package com.utils;


import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.io.*;
import com.utils.Base64;


public class AES2 {
	
	private static final String ALGORITHM = "AES";
	private Base64 base64 = new Base64();
    private SecretKey key = null;
    SecretKeySpec keySpec = null;
    IvParameterSpec ivSpec = null;
    Cipher cipher = null;
	////////////////////////////////////////////////////////////////////////////
    public void generateKey() throws NoSuchAlgorithmException {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
			key = keygen.generateKey();			
			System.out.println("NK="+base64.encode(key.getEncoded()));
			
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
			keySpec = new SecretKeySpec(key.getEncoded(), "AES/CBC/NoPadding");
			ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
		} catch (Exception e){
			e.printStackTrace();
		}
		
    }
	////////////////////////////////////////////////////////////////////////////
    public String getCipherKey() {
    	try {
    		
			//SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
			//AESKeySpec keyspec = (DESedeKeySpec)keyfactory.getKeySpec(key, DESedeKeySpec.class);			
			return base64.encode(key.getEncoded());
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
    }
	////////////////////////////////////////////////////////////////////////////
    public String encrypt(String s){
    	try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] enc = cipher.doFinal(base64.stringToByteArray(s));
			return base64.encode(enc);
		} catch (Exception e){
			System.out.println(e);
			return null;
		}
    }
    
    ////////////////////////////////////////////////////////////////////////////
    public String decrypt(String s) {
    	try {

			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] dec = base64.stringToByteArray(base64.decode(s));
            byte[] result = cipher.doFinal(dec);

            return new String(result);
 		} catch (Exception e){
			System.out.println(e);
			return null;
		}
    }
	////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		try {
		    AES2 des = new AES2();
			
		    // Generate a key
			long  t1 = System.currentTimeMillis();
			System.out.println("Generating key. This may take some time...");
			des.generateKey();
			System.out.println("Generated. It takes "+(System.currentTimeMillis()-t1));
			System.out.println("Key: "+des.getCipherKey());
			//System.out.println("Now key is: "+ des.getCipherKey());
			
			//for (int i=0; i<10; i++){
				String s = "Can you see me?";
				System.out.println("Clear text: "+s);
				
				s = des.encrypt(s);
				System.out.println("Cipher text: "+s);

				s = des.encrypt("Can you see me1");
				System.out.println("Cipher text: "+s);
				
				s = des.decrypt(s);
				System.out.println("After decrypt: "+s);			
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

