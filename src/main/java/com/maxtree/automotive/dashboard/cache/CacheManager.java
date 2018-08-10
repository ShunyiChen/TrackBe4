package com.maxtree.automotive.dashboard.cache;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.SendDetails;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.ui.UI;

/**
 * 
 * @author Chen
 *
 */
public class CacheManager {

	public static void main(String args[]) {
		CacheManager cm = CacheManager.getInstance();
		for(int i=0;i<100;i++) {
			DataObject d1 = new DataObject();
			d1.userUniqueId = i * i;
			cm.permissionCache.put(i, d1);
		}
		System.out.println(new Date());
		
		Scanner sc = new Scanner(System.in);
        System.out.println("Printing the file passed in:");
        while(sc.hasNextLine()) {
            DataObject d2 = cm.permissionCache.get(98);
            
//            DataObject newd = new DataObject();
//            newd.userUniqueId = 123;
//            cm.permissionCache.put(98, newd);
//            cm.permissionCache.invalidate(98);
//        	DataObject d2 = cm.permissionCache.get(98);
			System.out.println(d2.userUniqueId+"   "+new Date());
        	
        	 String line = sc.nextLine();
        	 if(line.equals("bye")) {
        		 break;
        	 }
        }
	}
	
	private static final Logger log = LoggerFactory.getLogger(CacheManager.class);
	private static Object monitor = new Object();
	private LoadingCache<Integer, DataObject> permissionCache = null;
	private LoadingCache<Integer, List<SendDetails>> sendDetailsCache = null;
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
	private static CacheManager instance;
	
	public static CacheManager getInstance() {
		if (instance == null) {
			synchronized (monitor) {
				if (instance == null) {
					instance = new CacheManager();
				}
			}
		}
		return instance;
	}
	
	/**
	 * 
	 */
	private CacheManager() {
		
		SystemConfiguration sc = Yaml.readSystemConfiguration();
		
		permissionCache = Caffeine.newBuilder()
			    .maximumSize(sc.getMaximumSize())
			    .expireAfterWrite(sc.getExpireAfterWrite(), TimeUnit.MINUTES)
			    .refreshAfterWrite(sc.getRefreshAfterWrite(), TimeUnit.MINUTES)
			    .build(key -> createDataObject(key));
		
//		// Loading cache for the first time
//		List<User> users = ui.userService.findAll(true);
//		for (User u : users) {
//			DataObject dataObj = new DataObject();
//			dataObj.userUniqueId = u.getUserUniqueId();
//			List<Role> roles = u.getRoles();
//			for (Role r : roles) {
//				List<Permission> permissions = r.getPermissions();
//				for (Permission p : permissions) {
//					dataObj.permissionCodes.add(p.getCode());
//				}
//			}
//			permissionCache.put(u.getUserUniqueId(), dataObj);
//		}
		
		// Send details
		sendDetailsCache = Caffeine.newBuilder()
				.maximumSize(sc.getMaximumSize())
				.expireAfterWrite(sc.getExpireAfterWrite(), TimeUnit.MINUTES)
			    .refreshAfterWrite(sc.getRefreshAfterWrite(), TimeUnit.MINUTES)
			    .build(key -> createSendDetails(key));
		
		
		// Loading cache for the first time
//		users = ui.userService.findAll(true);
//		for (User u : users) {
//			List<SendDetails> listSendDetails = ui.messagingService.findUnreadSendDetails(u.getUserUniqueId());
//			sendDetailsCache.put(u.getUserUniqueId(), listSendDetails);
//		}
	}
	
	/**
	 * 
	 * @return
	 */
	public LoadingCache<Integer, DataObject> getPermissionCache() {
		return permissionCache;
	}
	
	/**
	 * 
	 * @return
	 */
	public LoadingCache<Integer, List<SendDetails>> getSendDetailsCache() {
		return sendDetailsCache;
	}
	
	/**
	 * 
	 * @param userUniqueId
	 * @return
	 */
	private DataObject createDataObject(Integer userUniqueId) {
		System.out.println("CreateDataObject by "+userUniqueId);
		User u = ui.userService.findById(userUniqueId);
		DataObject newDataObj = new DataObject();
		newDataObj.userUniqueId = userUniqueId;
		for (Role r : u.getRoles()) {
			List<Permission> permissions = r.getPermissions();
			for (Permission p : permissions) {
				newDataObj.permissionCodes.add(p.getCode());
			}
		}
		return newDataObj;
	}
	
	private List<SendDetails> createSendDetails(Integer userUniqueId) {
		System.out.println("createSendDetails by "+userUniqueId);
		List<SendDetails> listSendDetails = ui.messagingService.findUnreadSendDetails(userUniqueId);
		return listSendDetails;
	}
}
