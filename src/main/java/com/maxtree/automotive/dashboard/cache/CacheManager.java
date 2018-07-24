package com.maxtree.automotive.dashboard.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.maxtree.automotive.dashboard.DashboardUI;
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

	private static final Logger log = LoggerFactory.getLogger(CacheManager.class);
	private static Object monitor = new Object();
	private LoadingCache<Integer, DataObject> permissionCache = null;
	private Cache<Integer, List<SendDetails>> sendDetailsCache = null;
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
		permissionCache = Caffeine.newBuilder()
			    .maximumSize(200)
			    .expireAfterWrite(5, TimeUnit.MINUTES)
			    .refreshAfterWrite(1, TimeUnit.MINUTES)
			    .build(key -> createDataObject(key));
		
		// Loading cache for the first time
		List<User> users = ui.userService.findAll(true);
		for (User u : users) {
			DataObject dataObj = new DataObject();
			dataObj.userUniqueId = u.getUserUniqueId();
			List<Role> roles = u.getRoles();
			for (Role r : roles) {
				List<Permission> permissions = r.getPermissions();
				for (Permission p : permissions) {
					dataObj.permissionCodes.add(p.getCode());
				}
			}
			permissionCache.put(u.getUserUniqueId(), dataObj);
		}
		
		// Send details
		sendDetailsCache = Caffeine.newBuilder()
//				.expireAfterWrite(1, TimeUnit.MINUTES)
			    .maximumSize(200)
			    .build();
		// Loading cache for the first time
		users = ui.userService.findAll(true);
		for (User u : users) {
			List<SendDetails> listSendDetails = ui.messagingService.findUnreadSendDetails(u.getUserUniqueId());
			sendDetailsCache.put(u.getUserUniqueId(), listSendDetails);
		}
	}
	
	/**
	 * 
	 */
	public void refreshSendDetailsCache() {
		log.info("refreshing send details.");
		sendDetailsCache.cleanUp();
		List<User> users = ui.userService.findAll(true);
		for (User u : users) {
			List<SendDetails> listSendDetails = ui.messagingService.findUnreadSendDetails(u.getUserUniqueId());
			sendDetailsCache.put(u.getUserUniqueId(), listSendDetails);
		}
		
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
	public Cache<Integer, List<SendDetails>> getSendDetailsCache() {
		return sendDetailsCache;
	}
	
	/**
	 * 
	 * @param userUniqueId
	 * @return
	 */
	private DataObject createDataObject(Integer userUniqueId) {
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
}
