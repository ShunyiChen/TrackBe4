package com.maxtree.automotive.dashboard.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.BusinessState;

@Component
public class BusinessStateService {

	private static final Logger log = LoggerFactory.getLogger(BusinessStateService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 */
	public List<BusinessState> findAll() {
		String sql = "SELECT * FROM BUSINESSSTATE";
		List<BusinessState> results = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper<BusinessState>(BusinessState.class));
		return results;
	}
	
}
