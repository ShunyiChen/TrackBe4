package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;


@Component
public class CompanyService {

	private static final Logger log = LoggerFactory.getLogger(CompanyService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param companyUniqueId
	 * @return
	 */
	public Company findById(int companyUniqueId) {
		String sql = "SELECT * FROM COMPANIES WHERE COMPANYUNIQUEID = ?";
		List<Company> results = jdbcTemplate.query(sql, new Object[] {companyUniqueId}, new BeanPropertyRowMapper<Company>(Company.class));
		if (results.size() > 0) {
			
			sql = "SELECT * FROM USERS WHERE COMPANYUNIQUEID=?";
			List<User> users = jdbcTemplate.query(sql, new Object[] {results.get(0).getCompanyUniqueId()}, new BeanPropertyRowMapper<User>(User.class));
			results.get(0).setEmployees(users);
			
			return results.get(0);
		}
		return new Company();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Company> findAll() {
		String sql = "SELECT * FROM COMPANIES ORDER BY COMPANYUNIQUEID";
		List<Company> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Company>(Company.class));
		
		for (Company company : results) {
			sql = "SELECT * FROM USERS WHERE COMPANYUNIQUEID=? ORDER BY COMPANYUNIQUEID";
			List<User> users = jdbcTemplate.query(sql, new Object[] {company.getCompanyUniqueId()}, new BeanPropertyRowMapper<User>(User.class));
			company.setEmployees(users);
		}
		
		return results;
	}
	
	/**
	 * 
	 * @param company
	 */
	public int create(Company company) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO COMPANIES(COMMUNITYUNIQUEID,COMPANYNAME,PROVINCE,CITY,PREFECTURE,DISTRICT,ADDRESS,CANCREATESTOREHOUSE,STOREHOUSEUNIQUEID,IGNORECHECKER) VALUES(?,?,?,?,?,?,?,?,?,?)";
		
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						sql, new String[] {"companyuniqueid"});
				ps.setInt(1, company.getCommunityUniqueId());
				ps.setString(2, company.getCompanyName());
				ps.setString(3, company.getProvince());
				ps.setString(4, company.getCity());
				ps.setString(5, company.getPrefecture());
				ps.setString(6, company.getDistrict());
				ps.setString(7, company.getAddress());
				ps.setInt(8, company.getCanCreateStorehouse());
				ps.setInt(9, company.getStorehouseUniqueId());
				ps.setInt(10, company.getIgnoreChecker());
				return ps;
			}
			
		}, keyHolder);
		int companyuniqueid  = keyHolder.getKey().intValue(); 
		return companyuniqueid;
	}
	
	/**
	 * 
	 * @param Community
	 */
	public void update(Company company) {
		
		// 更新机构内的用户
		List<User> employees = company.getEmployees();
		for (User u : employees) {
			u.setCommunityUniqueId(company.getCommunityUniqueId());
			u.setCompanyName(company.getCompanyName());
			
			String Sql2 = "UPDATE USERS SET COMMUNITYUNIQUEID=?,COMPANYUNIQUEID=?,COMPANYNAME=? WHERE USERUNIQUEID=?";
			jdbcTemplate.update(Sql2, new Object[] {u.getCommunityUniqueId(), u.getCompanyUniqueId(), u.getCompanyName(), u.getUserUniqueId()});
		}
		
		String sql = "UPDATE COMPANIES SET COMMUNITYUNIQUEID=?,COMPANYNAME=?,PROVINCE=?,CITY=?,PREFECTURE=?,DISTRICT=?,ADDRESS=?,CANCREATESTOREHOUSE=?,STOREHOUSEUNIQUEID=?,IGNORECHECKER=? WHERE COMPANYUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {company.getCommunityUniqueId(), company.getCompanyName(),company.getProvince(),company.getCity(),company.getPrefecture(),company.getDistrict(), company.getAddress(), company.getCanCreateStorehouse(), company.getStorehouseUniqueId(), company.getIgnoreChecker(),company.getCompanyUniqueId()});
	 	log.info("Updated row "+opt);
	}
	
	public void updateStorehouse(Company company) {
		String sql = "UPDATE COMPANIES SET STOREHOUSEUNIQUEID=? WHERE COMPANYUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {company.getStorehouseUniqueId(), company.getCompanyUniqueId()});
	 	log.info("Updated row "+opt);
	}
	
	/**
	 * 
	 * @param company
	 * @throws DataException
	 */
	public void delete(Company company) throws DataException {
		int companyUniqueId = company.getCompanyUniqueId();
		// 更新机构内的用户
		List<User> employees = company.getEmployees();
		for (User u : employees) {
			u.setCommunityUniqueId(0);
			u.setCompanyUniqueId(0);
			u.setCompanyName("");
			
			String Sql2 = "UPDATE USERS SET COMMUNITYUNIQUEID=?,COMPANYUNIQUEID=?,COMPANYNAME=? WHERE USERUNIQUEID=?";
			jdbcTemplate.update(Sql2, new Object[] {u.getCommunityUniqueId(), u.getCompanyUniqueId(), u.getCompanyName(), u.getUserUniqueId()});
		}
		
		String sql = "DELETE FROM COMPANIES WHERE COMPANYUNIQUEID = ?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {companyUniqueId});
	 	log.info("Deleted row "+opt);
	}
	
	/**
	 * Get all users not including 'system' user.
	 * 
	 * @return
	 */
	public List<User> findAllUsers(int companyUniqueId) {
		String sql = "SELECT * FROM USERS WHERE USERNAME NOT IN('system') AND COMPANYUNIQUEID IN(?,?)";
		List<User> unassignedUsers = jdbcTemplate.query(sql, new Object[] {0, companyUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		return unassignedUsers;
	}
	
	/**
	 * Get assigned users by companyUniqueId.
	 * 
	 * @param companyUniqueId
	 * @return
	 */
	public List<User> findAssignedUsers(int companyUniqueId) {
		String sql = "SELECT * FROM USERS WHERE COMPANYUNIQUEID=?";
		List<User> assignedUsers = jdbcTemplate.query(sql, new Object[] {companyUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		return assignedUsers;
	}
	
	/**
	 * 
	 * @param companyUniqueId
	 * @return
	 */
	public List<Business> findAssignedBusinesses(int companyUniqueId) {
		String sql = "SELECT A.* FROM BUSINESS AS A RIGHT JOIN COMPANYBUSINESSES AS B ON A.BUSINESSUNIQUEID=B.BUSINESSUNIQUEID WHERE B.COMPANYUNIQUEID=?";
		List<Business> assignedBusinesses = jdbcTemplate.query(sql, new Object[] {companyUniqueId}, new BeanPropertyRowMapper<Business>(Business.class));
		return assignedBusinesses;
	}
	
	/**
	 * 
	 * @param companyUniqueId
	 * @return
	 */
	public int deleteBusinesses(int companyUniqueId) {
		String sql = "DELETE FROM COMPANYBUSINESSES WHERE COMPANYUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {companyUniqueId});
	 	return opt;
	}
	
	/**
	 * 
	 * @param companyUniqueId
	 * @param lstBusiness
	 */
	public void assignBusinesses(int companyUniqueId, List<Business> lstBusiness) {
		String sql = "INSERT INTO COMPANYBUSINESSES(COMPANYUNIQUEID,BUSINESSUNIQUEID) VALUES(?,?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Business business = lstBusiness.get(i);
				ps.setInt(1, companyUniqueId);
				ps.setInt(2, business.getBusinessUniqueId());
			}

			@Override
			public int getBatchSize() {
				return lstBusiness.size();
			}
		});
		log.info("assignBusinesses has done.");
	}
}
