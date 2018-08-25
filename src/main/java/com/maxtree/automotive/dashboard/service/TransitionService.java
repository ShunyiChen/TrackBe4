package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import com.maxtree.automotive.dashboard.domain.Transition;

@Component
public class TransitionService {

	private static final Logger log = LoggerFactory.getLogger(TransitionService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param uuid
	 * @param vin
	 * @return
	 */
	public Transition findByUUID(String uuid, String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT * FROM TRANSITION_"+index+" WHERE TRANSACTIONUUID=? ORDER BY TRANSITIONUNIQUEID DESC LIMIT ?";
		List<Transition> result = jdbcTemplate.query(sql, new Object[] {uuid,1}, new BeanPropertyRowMapper<Transition>(Transition.class));
		Transition tran = null;
		if (result.size() > 0) {
			tran = result.get(0);
		}
		return tran;
	}
	
	/**
	 * 
	 * @param transition
	 * @param vin
	 * @return
	 */
	public int insert(Transition transition, String vin) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int index = getTableIndex(vin);
		String UPDATE_TRANS_SQL = "INSERT INTO TRANSITION_"+index+"(TRANSACTIONUUID,ACTION,DETAILS,COMMENTS,USERNAME,DATEUPDATED) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						UPDATE_TRANS_SQL, new String[] {"transitionuniqueid"});
				ps.setString(1, transition.getTransactionUUID());
				ps.setString(2, transition.getAction());
				ps.setString(3, transition.getDetails());
				ps.setString(4, transition.getComments());
				ps.setString(5, transition.getUserName());
				long millis=transition.getDateUpdated().getTime();
				java.sql.Date date = new java.sql.Date(millis);
				ps.setDate(6,date);
				return ps;
			}
			
		}, keyHolder);
		int transitionuniqueid = keyHolder.getKey().intValue(); 
		return transitionuniqueid;
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
