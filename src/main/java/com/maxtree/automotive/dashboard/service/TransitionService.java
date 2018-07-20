package com.maxtree.automotive.dashboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Transition;

@Component
public class TransitionService {

	private static final Logger log = LoggerFactory.getLogger(TransitionService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param transition
	 * @param vin
	 */
	public void insert(Transition transition, String vin) {
		int index = getTableIndex(vin);
		String UPDATE_TRANS_SQL = "INSERT INTO TRANSITION_"+index+"(TRANSACTIONUUID,ACTION,DETAILS,USERNAME,DATEUPDATED) VALUES(?,?,?,?,?)";
		int opt = jdbcTemplate.update(UPDATE_TRANS_SQL, new Object[] {
				transition.getTransactionUUID(), 
				transition.getAction(),
				transition.getDetails(),
				transition.getUserName(),
				transition.getDateUpdated()});
		log.info("Affected id:"+opt);
	}
	
	/**
	 * 
	 * @param vin
	 * @return
	 */
	private int getTableIndex(String vin) {
		int num = Integer.parseInt(vin.substring(vin.length() - 4));
		return num % 256;
	}
}
