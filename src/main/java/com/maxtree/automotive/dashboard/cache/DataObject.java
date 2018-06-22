package com.maxtree.automotive.dashboard.cache;

import java.util.ArrayList;
import java.util.List;

public class DataObject {

	public int userUniqueId;
	public List<String> permissionCodes = new ArrayList<String>();
	
	/**
	 * 
	 * @param permissionCode
	 * @return
	 */
	public boolean isPermitted(String code) {
//		
//		for(String d : permissionCodes) {
//			System.out.println("d="+d+",code="+code);
//		}
//		
		return permissionCodes.contains(code);
	}
}
