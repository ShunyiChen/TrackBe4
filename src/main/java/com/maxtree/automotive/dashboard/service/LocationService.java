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

import com.maxtree.automotive.dashboard.domain.Location;

@Component
public class LocationService {

	private static final Logger log = LoggerFactory.getLogger(LocationService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param LocationUniqueId
	 * @return
	 */
	public Location findById(int locationUniqueId) {
		String sql = "SELECT * FROM LOCATIONS WHERE LOCATIONUNIQUEID=?";
		List<Location> results = jdbcTemplate.query(sql, new Object[] {locationUniqueId}, new BeanPropertyRowMapper<Location>(Location.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Location();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Location> findAll() {
		String sql = "SELECT * FROM LOCATIONS ORDER BY ? DESC";
		List<Location> results = jdbcTemplate.query(sql, new Object[] {"LOCATIONUNIQUEID"}, new BeanPropertyRowMapper<Location>(Location.class));
		if (results.size() > 0) {
			return results;
		}
		return new ArrayList<Location>();
	}
	
	/**
	 * 
	 * @param category
	 * @return
	 */
	public List<Location> findByCategory(String category) {
		String sql = "SELECT * FROM LOCATIONS WHERE CATEGORY=? ORDER BY ? DESC";
		List<Location> results = jdbcTemplate.query(sql, new Object[] {category,"LOCATIONUNIQUEID"}, new BeanPropertyRowMapper<Location>(Location.class));
		if (results.size() > 0) {
			return results;
		}
		return new ArrayList<Location>();
	}
	
	/**
	 * 
	 * @param Location
	 * @return
	 */
	public int insert(Location location) {
		String INSERT_SQL = "INSERT INTO LOCATIONS(NAME) VALUES(?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] {"locationuniqueid"});
				ps.setString(1, location.getName());
				return ps;
			}
		}, keyHolder);
		int Locationuniqueid  = keyHolder.getKey().intValue();
		return Locationuniqueid;
	}
	
	/**
	 * 
	 * @param location
	 */
	public void update(Location location) {
		String sql = "UPDATE LOCATIONS SET CATEGORY=?,NAME=?,CODE=? WHERE LOCATIONUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {location.getCategory(),location.getName(),location.getCode(),location.getLocationUniqueId()});
	 	log.info("Updated one record "+opt);
	}
	
	/**
	 * 
	 * @param locationUniqueId
	 */
	public void delete(int locationUniqueId) {
		String sql = "DELETE FROM LOCATIONS WHERE LOCATIONUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {locationUniqueId});
	}

}
