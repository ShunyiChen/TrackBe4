package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Feedback;

@Component
public class FeedbackService {

	private static final Logger log = LoggerFactory.getLogger(FeedbackService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param feedbackUniqueId
	 * @return
	 */
	public Feedback findById(int feedbackUniqueId) {
		String sql = "SELECT * FROM FEEDBACK WHERE FEEDBACKUNIQUEID=?";
		List<Feedback> results = jdbcTemplate.query(sql, new Object[] {feedbackUniqueId}, new BeanPropertyRowMapper<Feedback>(Feedback.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Feedback();
	}
	
	/**
	 * 
	 * @param userName
	 * @return
	 */
	public List<Feedback> findByUserName(String userName) {
		String sql = "SELECT * FROM FEEDBACK WHERE USERNAME=? ORDER BY ? DESC";
		List<Feedback> results = jdbcTemplate.query(sql, new Object[] {userName, "FREQUENCY"}, new BeanPropertyRowMapper<Feedback>(Feedback.class));
		if (results.size() > 0) {
			return results;
		}
		return new ArrayList<Feedback>();
	}
	
	/**
	 * 
	 * @param feedback
	 * @return
	 */
	public int insert(Feedback feedback) {
		String INSERT_SQL = "INSERT INTO FEEDBACK(USERNAME,SUGGESTION,FREQUENCY) VALUES(?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						INSERT_SQL, new String[] {"feedbackuniqueid"});
				ps.setString(1, feedback.getUserName());
				ps.setString(2, feedback.getSuggestion());
				ps.setInt(3, feedback.getFrequency());
				return ps;
			}
		}, keyHolder);
		int feedbackuniqueid  = keyHolder.getKey().intValue();
		return feedbackuniqueid;
	}
	
	/**
	 * 
	 * @param cs
	 */
	public void update(Feedback feedback) {
		String sql = "UPDATE FEEDBACK SET SUGGESTION=? WHERE FEEDBACKUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] { feedback.getSuggestion(),feedback.getFeedbackUniqueId()});
	 	log.info("Updated one record "+opt);
	}
	
	/**
	 * 
	 * @param FEEDBACKUNIQUEID
	 */
	public void delete(int FEEDBACKUNIQUEID) {
		String sql = "DELETE FROM FEEDBACK WHERE FEEDBACKUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {FEEDBACKUNIQUEID});
	}
	
	/**
	 * Go up
	 * @param feedback
	 */
	public void up(Feedback feedback) {
		String sql = "UPDATE FEEDBACK SET FREQUENCY=FREQUENCY+? WHERE FEEDBACKUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {1,feedback.getFeedbackUniqueId()});
	}
}
