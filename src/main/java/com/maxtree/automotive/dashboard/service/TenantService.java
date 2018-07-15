package com.maxtree.automotive.dashboard.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Tenant;

@Component
public class TenantService {

	private static final Logger log = LoggerFactory.getLogger(TenantService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Tenant> findAllTenants() {
		String sql = "SELECT A.*,C.COMMUNITYNAME AS COMMUNITYNAME FROM TENANTS AS A LEFT JOIN COMMUNITYTENANTS AS B ON A.TENANTUNIQUEID=B.TENANTUNIQUEID LEFT JOIN COMMUNITIES AS C ON B.COMMUNITYUNIQUEID=C.COMMUNITYUNIQUEID ORDER BY A.TENANTUNIQUEID";
		List<Tenant> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Tenant>(Tenant.class));
		return results;
	}

	/**
	 * 
	 * @param tenant
	 */
	public void insertTenant(Tenant tenant) {
		String sql = "INSERT INTO TENANTS(TENANTNAME) VALUES(?)";
		int opt = jdbcTemplate.update(sql, new Object[] { tenant.getTenantName() });
		log.info("Created row " + opt);
	}

	/**
	 * 
	 * @param tenant
	 */
	public void updateTenant(Tenant tenant) {
		String sql = "UPDATE TENANTS SET TENANTNAME=? WHERE TENANTUNIQUEID=?";
		int opt = jdbcTemplate.update(sql, new Object[] { tenant.getTenantName(), tenant.getTenantUniqueId() });
		log.info("Updated row " + opt);
	}

	/**
	 * 
	 * @param tenant
	 */
	public void deleteTenant(Tenant tenant) {
		String sql = "DELETE FROM COMMUNITYTENANTS WHERE TENANTUNIQUEID=?";
		jdbcTemplate.update(sql, tenant.getTenantUniqueId());

		sql = "DELETE FROM TENANTS WHERE TENANTUNIQUEID=?";
		int opt = jdbcTemplate.update(sql, new Object[] { tenant.getTenantUniqueId() });
		log.info("Deleted row " + opt);
	}

	/**
	 * 
	 * @param communityUniqueId
	 */
	public void deleteTenantByCommunityUniqueId(int communityUniqueId) {
		String sql = "DELETE FROM COMMUNITYTENANTS WHERE COMMUNITYUNIQUEID=?";
		int opt = jdbcTemplate.update(sql, communityUniqueId);
		log.info("Affected row " + opt);
	}
	
	/**
	 * 
	 * @param tenantUniqueId
	 */
	public void deleteCommunityByTenantUniqueId(int tenantUniqueId) {
		String sql = "DELETE FROM COMMUNITYTENANTS WHERE TENANTUNIQUEID=?";
		int opt = jdbcTemplate.update(sql, tenantUniqueId);
		log.info("Affected row " + opt);
	}

	/**
	 * 
	 * @param tenantUniqueId
	 * @return
	 */
	public Tenant findTenantById(int tenantUniqueId) {
		String sql = "SELECT * FROM TENANTS WHERE TENANTUNIQUEID= ?";
		List<Tenant> results = jdbcTemplate.query(sql, new Object[] { tenantUniqueId },
				new BeanPropertyRowMapper<Tenant>(Tenant.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Tenant();
	}

	/**
	 * 
	 * @param communityUniqueId
	 * @param tenantUniqueId
	 */
	public void insertCommunityTenants(int communityUniqueId, int tenantUniqueId) {
		String sql = "INSERT INTO COMMUNITYTENANTS(COMMUNITYUNIQUEID,TENANTUNIQUEID) VALUES(?,?)";
		int opt = jdbcTemplate.update(sql, new Object[] { communityUniqueId, tenantUniqueId });
		log.info("inserted row " + opt);
	}
	
	/**
	 * 
	 * @param tenantUniqueId
	 * @return
	 */
	public List<Community> findAssignedCommunites(int tenantUniqueId) {
		String sql = "SELECT * FROM COMMUNITYTENANTS WHERE TENANTUNIQUEID=?";
		List<Community> results = jdbcTemplate.query(sql, new Object[] {tenantUniqueId}, new BeanPropertyRowMapper<Community>(Community.class));
		return results;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Community> findUnassignedCommunities(int tenantUniqueId) {
		String sql = "SELECT * FROM COMMUNITIES WHERE COMMUNITYUNIQUEID NOT IN (SELECT COMMUNITYUNIQUEID FROM COMMUNITYTENANTS WHERE TENANTUNIQUEID<>?)";
		List<Community> results = jdbcTemplate.query(sql, new Object[] {tenantUniqueId}, new BeanPropertyRowMapper<Community>(Community.class));
		return results;
	}
}
