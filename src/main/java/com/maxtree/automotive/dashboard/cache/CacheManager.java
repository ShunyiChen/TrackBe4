package com.maxtree.automotive.dashboard.cache;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.ui.UI;

/**
 * 
 * @author Chen
 *
 */
public class CacheManager {

	private static final Logger log = LoggerFactory.getLogger(CacheManager.class);
	
	public final static CacheManager INSTANCE = new CacheManager();
	
	private LoadingCache<Integer, DataObject> cacheData = null;
	
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	/**
	 * 
	 */
	private CacheManager() {
		cacheData = Caffeine.newBuilder()
			    .maximumSize(10_000)
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
			
			cacheData.put(u.getUserUniqueId(), dataObj);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public LoadingCache<Integer, DataObject> getCacheData() {
		return cacheData;
	}
	
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

		Calendar calendar = Calendar.getInstance();
		int hours = calendar.get(Calendar.HOUR_OF_DAY); // 时
		int minutes = calendar.get(Calendar.MINUTE);    // 分
		int seconds = calendar.get(Calendar.SECOND);    // 秒
		System.out.println(String.format("hours: %s, minutes: %s, seconds: %s", hours, minutes, seconds));
		
		try {
			log.info("========= Refresh the cache by name :: "+new String(u.getUserName().getBytes("ISO-8859-1"),"UTF-8")+"::");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return newDataObj;
	}
	
	
//	public static void main(String[] args) throws InterruptedException {
//		
//		System.out.println(User.class.getName());
//		
//		
//		
//		for (int i = 0; i < 100; i++) {
//			Thread t = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					CacheManager c = CacheManager.INSTANCE;
//					
//					DataObject d1 = new DataObject();
//					d1.permissionCodes.add("A1");
//					d1.permissionCodes.add("A2");
//					d1.userUniqueId = 1;
//					
//					c.cacheData.put(1, d1);
//					try {
//						Thread.sleep((long) (Math.random() * 10000));
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					DataObject rs = c.cacheData.get(1);
//					System.out.println(Thread.currentThread().getName()+"  get:"+rs.userUniqueId);
//					
//				}
//			},"线程"+i);
//			t.start();
//		}
		
//		CacheManager c = CacheManager.INSTANCE;
//		
//		DataObject d1 = new DataObject();
//		d1.permissionCodes.add("A1");
//		d1.permissionCodes.add("A2");
//		d1.userUniqueId = 1;
//		Thread.sleep(3000);
//		c.cacheData.put("root", d1);
//		
//		
//		DataObject rs = c.cacheData.get("root");
//		System.out.println(rs.userUniqueId+"  "+rs.permissionCodes.size());
		
		
	
//		DataObject rs2 = c.cacheData.get("root2");
//		
//		c.cacheData.refresh("root");
		
		
		
//		System.out.println("refreshed");
//		System.out.println("refreshed:"+rs.userUniqueId+"  "+rs.permissionCodes.size());
		
//		Thread.sleep(1000 * 2);
		
//		DataObject rs2 = c.cacheData.getIfPresent("root");
//		DataObject rs2 = c.cacheData.get("roo2t");
//		System.out.println("getIfPresent:"+rs2.userUniqueId+"  "+rs2.permissionCodes.size());
		
		
		
		
//		System.out.println("exit");
//	}
}
