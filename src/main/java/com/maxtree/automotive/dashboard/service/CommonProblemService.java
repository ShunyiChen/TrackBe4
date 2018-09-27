package com.maxtree.automotive.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.CommonProblem;

@Component
public class CommonProblemService {

	private static final Logger log = LoggerFactory.getLogger(CommonProblemService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<CommonProblem> findByUserName(String userName) {
		String sql = "SELECT * FROM COMMONPROBLEMS WHERE USERNAME=? ORDER BY ? DESC";
		List<CommonProblem> results = jdbcTemplate.query(sql, new Object[] {userName, "FREQUENCY"}, new BeanPropertyRowMapper<CommonProblem>(CommonProblem.class));
		if (results.size() > 0) {
			return results;
		}
		return new ArrayList<CommonProblem>();
	}
	
	/**
	 * 
	 * @param commonProblem
	 */
	public void insert(CommonProblem commonProblem) {
		String sql = "INSERT INTO COMMONPROBLEMS(USERNAME,PROBLEM,FREQUENCY) VALUES(?,?,?)";
	 	int opt = jdbcTemplate.update(sql, new Object[] { commonProblem.getUserName(),commonProblem.getProblem(),commonProblem.getFrequency()});
	 	log.info("Inserted one record "+opt);
	}
	
	/**
	 * 
	 * @param commonProblem
	 */
	public void update(CommonProblem commonProblem) {
		String sql = "UPDATE COMMONPROBLEMS SET PROBLEM=? WHERE PROBLEMUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] { commonProblem.getProblem(),commonProblem.getProblemUniqueId()});
	 	log.info("Updated one record "+opt);
	}
	
	/**
	 * 
	 * @param problemUniqueId
	 */
	public void delete(int problemUniqueId) {
		String sql = "DELETE FROM COMMONPROBLEMS WHERE PROBLEMUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {problemUniqueId});
	}
}
