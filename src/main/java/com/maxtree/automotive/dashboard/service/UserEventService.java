package com.maxtree.automotive.dashboard.service;

import org.postgresql.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.maxtree.automotive.dashboard.domain.UserEvent;

@Component
public class UserEventService {

	private static final Logger log = LoggerFactory.getLogger(UserEventService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param event
	 * @param userName
	 */
	public void insert(UserEvent event, String userName) {
		int index = getTableIndex(userName);
		String UPDATE_TRANS_SQL = "INSERT INTO USEREVENT_"+index+"(TRANSITIONUNIQUEID,USERNAME,DATEUPDATED) VALUES(?,?,?)";
		int opt = jdbcTemplate.update(UPDATE_TRANS_SQL, new Object[] {
				event.getTransitionUniqueId(),
				event.getUserName(),
				event.getDateUpdated()});
		log.info("Affected id:"+opt);
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	private int getTableIndex(String userName) {
		String encode = Base64.encodeBytes(userName.getBytes());
    	int sum = 0;
    	for(char c : encode.toCharArray()) {
    		sum+=c;
    	}
    	return sum % 256;
	}
}
