package com.maxtree.automotive.dashboard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CompanyCategoryService {

	private static final Logger log = LoggerFactory.getLogger(CompanyCategoryService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 */
	public List<String> findAll() {
		List<String> lst = new ArrayList<>();
		String sql = "SELECT NAME FROM COMPANYCATEGORY ORDER BY ? DESC";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] {"NAME"});
		for (Map row : rows) {
			lst.add((String)row.get("NAME"));
		}
		return lst;
	}
	
}
