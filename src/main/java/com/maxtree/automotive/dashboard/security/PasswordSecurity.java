package com.maxtree.automotive.dashboard.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordSecurity {

	/**
	 * 
	 * @param plaintext
	 * @return
	 */
	public static String hashPassword(String plaintext) {
		String hashed = BCrypt.hashpw(plaintext, BCrypt.gensalt(4));
		return hashed;
	}
	
	/**
	 * 
	 * @param plaintext
	 * @param hashed
	 * @return
	 */
    public static boolean check(String plaintext, String hashed) {
		// Check that an unencrypted password matches one that has
		// previously been hashed
		if (BCrypt.checkpw(plaintext, hashed)) {
//			System.out.println("It matches");
			return true;
		} else {
//			System.out.println("It does not match");
			return false;
		}
			
	}
    
    public static void main(String[] s) {
    	String hashed = hashPassword("Happy2016");
    	System.out.println(hashed+"  "+hashed.length());
    	
    	boolean successful = check("", hashed);
    	System.out.println(successful);
    	
    	
    }

}
