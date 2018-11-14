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

import com.maxtree.automotive.dashboard.domain.Tenant;

@Component
public class TenantService {

	private static final Logger log = LoggerFactory.getLogger(TenantService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param TenantUniqueId
	 * @return
	 */
	public Tenant findById(int tenantUniqueId) {
		String sql = "SELECT * FROM TENANT WHERE TENANTUNIQUEID=?";
		List<Tenant> results = jdbcTemplate.query(sql, new Object[] {tenantUniqueId}, new BeanPropertyRowMapper<Tenant>(Tenant.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Tenant();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Tenant> findAll() {
		String sql = "SELECT * FROM TENANT ORDER BY ? DESC";
		List<Tenant> results = jdbcTemplate.query(sql, new Object[] {"TENANTUNIQUEID"}, new BeanPropertyRowMapper<Tenant>(Tenant.class));
		if (results.size() > 0) {
			return results;
		}
		return new ArrayList<Tenant>();
	}
	
	/**
	 * 
	 * @param Tenant
	 * @return
	 */
	public int insert(Tenant tenant) {
		String INSERT_SQL = "INSERT INTO TENANT(NAME) VALUES(?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] {"tenantuniqueid"});
				ps.setString(1, tenant.getName());
				return ps;
			}
		}, keyHolder);
		int Tenantuniqueid  = keyHolder.getKey().intValue();
		return Tenantuniqueid;
	}
	
	/**
	 * 
	 * @param cs
	 */
	public void update(Tenant tenant) {
		String sql = "UPDATE TENANT SET NAME=? WHERE TENANTUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] { tenant.getName(),tenant.getTenantUniqueId()});
	 	log.info("Updated one record "+opt);
	}
	
	/**
	 * 
	 * @param TenantUNIQUEID
	 */
	public void delete(int tenantUniqueId) {
		String sql = "DELETE FROM TENANT WHERE TENANTUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {tenantUniqueId});
	}
	
	/**
	 * Go up
	 * @param Tenant
	 */
	public void up(Tenant tenant) {
		String sql = "UPDATE TENANT SET FREQUENCY=FREQUENCY+? WHERE TENANTUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {1, tenant.getTenantUniqueId()});
	}
}
