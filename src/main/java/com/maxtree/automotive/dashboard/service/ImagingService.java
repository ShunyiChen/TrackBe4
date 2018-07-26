package com.maxtree.automotive.dashboard.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
		String sql = "SELECT * FROM IMAGING LIMIT ? OFFSET ? ORDER BY DATECREATED";
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
		String sql = "UPDATE COMPANIES SET STATUS=? WHERE IMAGINGUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {status, imagingUniqueId});
	 	log.info("Updated row "+opt);
	}
	
	
}
