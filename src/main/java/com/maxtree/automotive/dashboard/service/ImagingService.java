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

import com.maxtree.automotive.dashboard.domain.Imaging;


@Component
public class ImagingService {

	private static final Logger log = LoggerFactory.getLogger(ImagingService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<Imaging> findAll(int limit, int offset) {
		String sql = "SELECT * FROM IMAGING ORDER BY DATECREATED  LIMIT ? OFFSET ? ";
		List<Imaging> results = jdbcTemplate.query(sql, new Object[] {limit, offset}, new BeanPropertyRowMapper<Imaging>(Imaging.class));
		return results;
	}
	
	/**
	 * 
	 * @param limit
	 * @return
	 */
	public int findPagingCount(int limit) {
		String sql = "SELECT * FROM CREATE_PAGINGCOUNT(?)";
		int count = jdbcTemplate.queryForObject( sql, new Object[] {limit}, Integer.class);
		return count;
	}
	
	/**
	 * 
	 * @param imagingUniqueId
	 * @param status
	 */
	public void updateImaging(int imagingUniqueId, String status) {
		String sql = "UPDATE IMAGING SET STATUS=? WHERE IMAGINGUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {status, imagingUniqueId});
	 	log.info("Updated row "+opt);
	}
	
	/**
	 * 
	 * @param imaging
	 * @return
	 */
	public int insert(Imaging imaging) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_SQL = "INSERT INTO IMAGING(PLATETYPE,PLATENUMBER,VIN,DATECREATED,DATEMODIFIED,STATUS,CREATOR) VALUES(?,?,?,?,?,?,?)";
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						INSERT_SQL, new String[] {"imaginguniqueid"});
				ps.setString(1, imaging.getPlateType());
				ps.setString(2, imaging.getPlateNumber());
				ps.setString(3, imaging.getVin());
				java.sql.Timestamp dateCreated = new java.sql.Timestamp(imaging.getDateCreated().getTime());
				ps.setTimestamp(4, dateCreated);
				java.sql.Timestamp dateModified = new java.sql.Timestamp(imaging.getDateModified().getTime());
				ps.setTimestamp(5, dateModified);
				ps.setString(6, imaging.getStatus());
				ps.setString(7, imaging.getCreator());
				return ps;
			}
			
		}, keyHolder);
		int imaginguniqueid  = keyHolder.getKey().intValue(); 
		return imaginguniqueid;
	}
	
}
