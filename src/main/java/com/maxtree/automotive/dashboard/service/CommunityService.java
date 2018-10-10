package com.maxtree.automotive.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;

@Component
public class CommunityService {

private static final Logger log = LoggerFactory.getLogger(CommunityService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param communityUniqueId
	 * @return
	 */
	public List<User> findAllUsers(int communityUniqueId) {
		String sql = "SELECT * FROM USERS WHERE COMMUNITYUNIQUEID=?";
		List<User> results = jdbcTemplate.query(sql, new Object[] {communityUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		if (results.size() > 0) {
			return results;
		}
		return new ArrayList<User>();
	}
	
	
	/**
	 * 
	 * @param CommunityUniqueId
	 * @return
	 */
	public Community findById(int communityUniqueId) {
		String sql = "SELECT * FROM COMMUNITIES WHERE COMMUNITYUNIQUEID=?";
		List<Community> results = jdbcTemplate.query(sql, new Object[] {communityUniqueId}, new BeanPropertyRowMapper<Community>(Community.class));
		if (results.size() > 0) {
			populateAssignedCompanies(results.get(0));
			return results.get(0);
		}
		return new Community();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Community> findAll() {
		String sql = "SELECT * FROM COMMUNITIES ORDER BY COMMUNITYUNIQUEID";
		List<Community> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Community>(Community.class));
		for (Community c : results) {
			populateAssignedCompanies(c);
		}
		return results;
	}
	
	/**
	 * 
	 * @param community
	 */
	private void populateAssignedCompanies(Community community) {
		String sql = "SELECT * FROM COMPANIES WHERE COMMUNITYUNIQUEID=? ORDER BY COMPANYUNIQUEID";
		List<Company> companies = jdbcTemplate.query(sql, new Object[] {community.getCommunityUniqueId()}, new BeanPropertyRowMapper<Company>(Company.class));
		community.setCompanies(companies);
	}
	
	/**
	 * 
	 * @param community
	 */
	public void update(Community community) {
		String sql = "UPDATE COMMUNITIES SET COMMUNITYNAME=?,COMMUNITYDESCRIPTION=?,GROUPID=?,LEVEL=? WHERE COMMUNITYUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] { community.getCommunityName(),community.getCommunityDescription(),community.getGroupId(),community.getLevel(),community.getCommunityUniqueId()});
	 	log.info("Updated row "+opt);
	}
	
	/**
	 * 
	 * @param communityUniqueId
	 * @throws DataException
	 */
	public void delete(int communityUniqueId) throws DataException {
		// 更新用户
		String sql1 = "SELECT * FROM USERS WHERE COMMUNITYUNIQUEID=?";
		List<User> results = jdbcTemplate.query(sql1, new Object[] {communityUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		for (User u : results) {
			u.setCommunityUniqueId(0);
			String sql2 = "UPDATE USERS SET COMMUNITYUNIQUEID=? WHERE USERUNIQUEID=?";
			jdbcTemplate.update(sql2, new Object[] {u.getCommunityUniqueId(), u.getUserUniqueId()});
		}
		// 更新机构
		String sql2 = "SELECT * FROM COMPANIES WHERE COMMUNITYUNIQUEID=?";
		List<Company> companies = jdbcTemplate.query(sql2, new Object[] {communityUniqueId}, new BeanPropertyRowMapper<Company>(Company.class));
		for (Company c : companies) {
			c.setCommunityUniqueId(0);
			sql2 = "UPDATE COMPANIES SET COMMUNITYUNIQUEID=? WHERE COMPANYUNIQUEID=?";
			jdbcTemplate.update(sql2, new Object[] {c.getCommunityUniqueId(), c.getCompanyUniqueId()});
		}
		
		String sql3 = "DELETE FROM COMMUNITYTENANTS WHERE COMMUNITYUNIQUEID=?";
		jdbcTemplate.update(sql3, new Object[] {communityUniqueId});
		
		String sql4 = "DELETE FROM COMMUNITYSITES WHERE COMMUNITYUNIQUEID=?";
		jdbcTemplate.update(sql4, new Object[] {communityUniqueId});
		
		String sql5 = "DELETE FROM COMMUNITIES WHERE COMMUNITYUNIQUEID=?";
		jdbcTemplate.update(sql5, new Object[] {communityUniqueId});
	}
	
	/**
	 * 
	 * @param community
	 */
	public void create(Community community) {
		String sql = "INSERT INTO COMMUNITIES(COMMUNITYNAME,COMMUNITYDESCRIPTION,GROUPID,LEVEL) VALUES(?,?,?,?)";
	 	int opt = jdbcTemplate.update(sql, new Object[] { community.getCommunityName(), community.getCommunityDescription(), community.getGroupId(), community.getLevel()});
	 	log.info("Created row "+opt);
	}
	
	/**
	 * Get all companies
	 * 
	 * @return
	 */
	public List<Company> findAllCompanies(int communityUniqueId) {
		String sql = "SELECT * FROM COMPANIES WHERE COMMUNITYUNIQUEID IN(?,?)";
		List<Company> results = jdbcTemplate.query(sql, new Object[] {0, communityUniqueId}, new BeanPropertyRowMapper<Company>(Company.class));
		
		for (Company company : results) {
			sql = "SELECT * FROM USERS WHERE COMPANYUNIQUEID=? ORDER BY COMPANYUNIQUEID";
			List<User> users = jdbcTemplate.query(sql, new Object[] {company.getCompanyUniqueId()}, new BeanPropertyRowMapper<User>(User.class));
			company.setEmployees(users);
		}
		return results;
	}
	
	/**
	 * Get assigned companies by communityUniqueId
	 * 
	 * @param communityUniqueId
	 * @return
	 */
	public List<Company> findAssignedCompanies(int communityUniqueId) {
		String sql = "SELECT * FROM COMPANIES WHERE COMMUNITYUNIQUEID=?";
		List<Company> results = jdbcTemplate.query(sql, new Object[] {communityUniqueId}, new BeanPropertyRowMapper<Company>(Company.class));
		
		for (Company c : results) {
			sql = "SELECT * FROM USERS WHERE COMPANYUNIQUEID=?";
			List<User> employees = jdbcTemplate.query(sql, new Object[] {c.getCompanyUniqueId()}, new BeanPropertyRowMapper<User>(User.class));
			c.setEmployees(employees);
		}
		
		return results;
	}
	
	/**
	 * 
	 * @param communityUniqueId
	 * @return
	 */
	public List<Site> findAssignedSites(int communityUniqueId) {
		String sql = "SELECT B.* FROM COMMUNITYSITES AS A LEFT JOIN SITE AS B ON A.SITEUNIQUEID=B.SITEUNIQUEID WHERE COMMUNITYUNIQUEID=? AND B.SITENAME IS NOT NULL";
		List<Site> results = jdbcTemplate.query(sql, new Object[] {communityUniqueId}, new BeanPropertyRowMapper<Site>(Site.class));
		return results;
	}
	
}
