package com.maxtree.automotive.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.CorrectingSuggestions;

@Component
public class CorrectingSuggestionsService {

	private static final Logger log = LoggerFactory.getLogger(CorrectingSuggestionsService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<CorrectingSuggestions> findByUserName(String userName) {
		String sql = "SELECT * FROM CORRECTINGSUGGESTIONS WHERE USERNAME=? ORDER BY ? DESC";
		List<CorrectingSuggestions> results = jdbcTemplate.query(sql, new Object[] {userName, "FREQUENCY"}, new BeanPropertyRowMapper<CorrectingSuggestions>(CorrectingSuggestions.class));
		if (results.size() > 0) {
			return results;
		}
		return new ArrayList<CorrectingSuggestions>();
	}
	
	/**
	 * 
	 * @param cs
	 */
	public void insert(CorrectingSuggestions cs) {
		String sql = "INSERT INTO CORRECTINGSUGGESTIONS(USERNAME,SUGGESTION,FREQUENCY) VALUES(?,?,?)";
	 	int opt = jdbcTemplate.update(sql, new Object[] { cs.getUserName(),cs.getSuggestion(),cs.getFrequency()});
	 	log.info("Inserted one record "+opt);
	}
	
	/**
	 * 
	 * @param cs
	 */
	public void update(CorrectingSuggestions cs) {
		String sql = "UPDATE CORRECTINGSUGGESTIONS SET SUGGESTION=? WHERE SUGGESTIONUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] { cs.getSuggestion(),cs.getSuggestionUniqueId()});
	 	log.info("Updated one record "+opt);
	}
	
	/**
	 * 
	 * @param suggestionUniqueId
	 */
	public void delete(int suggestionUniqueId) {
		String sql = "DELETE FROM CORRECTINGSUGGESTIONS WHERE SUGGESTIONUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {suggestionUniqueId});
	}
}
