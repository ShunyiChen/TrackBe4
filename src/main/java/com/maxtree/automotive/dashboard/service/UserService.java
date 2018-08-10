package com.maxtree.automotive.dashboard.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.RoleMember;
import com.maxtree.automotive.dashboard.domain.RoleRight;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.domain.UserProfile;
import com.maxtree.automotive.dashboard.security.PasswordSecurity;

@Component
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param communityUniqueId
	 * @return
	 * @throws EmptyResultDataAccessException
	 */
	public User findImagingAdmin(int communityUniqueId) throws EmptyResultDataAccessException {
		String SQL = "SELECT D.* FROM PERMISSION AS A RIGHT JOIN ROLERIGHT AS B ON A.PERMISSIONUNIQUEID = B.PERMISSIONUNIQUEID RIGHT JOIN ROLEMEMBER AS C ON C.ROLEUNIQUEID = B.ROLEUNIQUEID RIGHT JOIN USERS AS D ON D.USERUNIQUEID = C.USERUNIQUEID WHERE A.CODE=? AND D.COMMUNITYUNIQUEID=?";
		List<User> users = jdbcTemplate.query( SQL, new Object[] {"A8",communityUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		if (users.size() > 0) {
			User user = users.get(0);
			List<Role> roles = getRoles(user);
			user.setRoles(roles);
			
			UserProfile profile = getUserProfile(user.getUserUniqueId());
			user.setProfile(profile);
			
			return user;
		}
		return new User();
	}
	
	/**
	 * Get user object by user uniqueID
	 * 
 	 * @param username
	 * @return
	 * @throws EmptyResultDataAccessException
	 */
	public User findById(int userUniqueId) throws EmptyResultDataAccessException {
		String sql = "SELECT * FROM USERS WHERE USERUNIQUEID=?";
		List<User> lstUsers = jdbcTemplate.query(sql, new Object[] {userUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		if (lstUsers.size() > 0) {
			User user = lstUsers.get(0);
			List<Role> roles = getRoles(user);
			user.setRoles(roles);
			
			UserProfile profile = getUserProfile(user.getUserUniqueId());
			user.setProfile(profile);
			
			return user;
		}
		return new User();
	}
	
	/**
	 * Get user object by username
	 * 
 	 * @param username
	 * @return
	 * @throws EmptyResultDataAccessException
	 */
	public User getUserByUserName(String username) throws EmptyResultDataAccessException {
		String sql = "SELECT * FROM USERS WHERE USERNAME=?";
		List<User> lstUsers = jdbcTemplate.query(sql, new Object[] {username}, new BeanPropertyRowMapper<User>(User.class));
		if (lstUsers.size() > 0) {
			User user = lstUsers.get(0);
			List<Role> roles = getRoles(user);
			user.setRoles(roles);
			
			UserProfile profile = getUserProfile(user.getUserUniqueId());
			user.setProfile(profile);
			
			return user;
		}
		return new User();
	}
	
	/**
	 * 
	 * @param userUniqueId
	 * @return
	 */
	public UserProfile getUserProfile(int userUniqueId) {
		String sql = "SELECT * FROM USERPROFILES WHERE USERUNIQUEID=?";
		List<UserProfile> lstProfiles = jdbcTemplate.query(sql, new Object[] {userUniqueId}, new BeanPropertyRowMapper<UserProfile>(UserProfile.class));
		if (lstProfiles.size() > 0) {
			return lstProfiles.get(0);
		}
		return new UserProfile();
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	private List<Role> getRoles(User user) {
		List<Role> results = new ArrayList<Role>();
		String sql = "SELECT * FROM ROLEMEMBER WHERE USERUNIQUEID=?";
		List<RoleMember> lstRoleMember = jdbcTemplate.query(sql, new Object[] { user.getUserUniqueId() },
				new BeanPropertyRowMapper<RoleMember>(RoleMember.class));
		for (RoleMember roleMember : lstRoleMember) {
			sql = "SELECT * FROM ROLE WHERE ROLEUNIQUEID=?";
			Role role = jdbcTemplate.queryForObject(sql, new Object[] { roleMember.getRoleUniqueId() },
					new BeanPropertyRowMapper<Role>(Role.class));
			List<Permission> lstPermission = getPermissions(role);
			role.setPermissions(lstPermission);
			results.add(role);
		}
		return results;
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	private List<Permission> getPermissions(Role role) {
		List<Permission> results = new ArrayList<Permission>();
		String sql = "SELECT * FROM ROLERIGHT WHERE ROLEUNIQUEID = ?";
		List<RoleRight> lstRoleRight = jdbcTemplate.query(sql, new Object[] { role.getRoleUniqueId() },
				new BeanPropertyRowMapper<RoleRight>(RoleRight.class));
		for (RoleRight roleRight : lstRoleRight) {
			sql = "SELECT * FROM PERMISSION WHERE PERMISSIONUNIQUEID = ?";
			Permission permission = jdbcTemplate.queryForObject(sql, new Object[] { roleRight.getPermissionUniqueId() },
					new BeanPropertyRowMapper<Permission>(Permission.class));
			results.add(permission);
		}
		return results;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<User> findAll(boolean systemIncluded) {
		String sql = "SELECT * FROM USERS WHERE USERNAME <> 'system' ORDER BY USERUNIQUEID";
		if (systemIncluded) {
			sql = "SELECT * FROM USERS ORDER BY USERUNIQUEID";
		}
		List<User> lstUsers = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper<User>(User.class));
		
		if (lstUsers.size() > 0) {
			for (User user : lstUsers) {
				List<Role> roles = getRoles(user);
				user.setRoles(roles);
				UserProfile profile = getUserProfile(user.getUserUniqueId());
				user.setProfile(profile);
			}
		}
		return lstUsers;
	}
	
	/**
	 * 按权限获取用户列表
	 * 
	 * @param operator
	 * @return
	 */
	public List<User> findAll(User operator) {
		String sql = "";
		List<User> lstUsers = null;
		if (operator.isPermitted(PermissionCodes.O1)) {
			sql = "SELECT A.* FROM USERS AS A LEFT JOIN USERPROFILES B ON A.USERUNIQUEID=B.USERUNIQUEID WHERE A.USERNAME <> ? AND A.COMMUNITYUNIQUEID=? OR B.CREATEDBY=? ORDER BY A.USERUNIQUEID";
			lstUsers = jdbcTemplate.query(sql, new Object[] {"system",operator.getCommunityUniqueId(),operator.getUserName()}, new BeanPropertyRowMapper<User>(User.class));
		} else if (operator.isPermitted(PermissionCodes.O2)) {
			sql = "SELECT A.* FROM USERS AS A LEFT JOIN USERPROFILES B ON A.USERUNIQUEID=B.USERUNIQUEID WHERE A.USERNAME <> ? AND A.COMPANYUNIQUEID=? OR B.CREATEDBY=? ORDER BY A.USERUNIQUEID";
			lstUsers = jdbcTemplate.query(sql, new Object[] {"system",operator.getCompanyUniqueId(),operator.getUserName()}, new BeanPropertyRowMapper<User>(User.class));
		}
		if (lstUsers == null) 
			lstUsers = new ArrayList<User>();
		
		if (lstUsers.size() > 0) {
			for (User user : lstUsers) {
				List<Role> roles = getRoles(user);
				user.setRoles(roles);
				UserProfile profile = getUserProfile(user.getUserUniqueId());
				user.setProfile(profile);
			}
		}
		return lstUsers;
	}
	
	
	/**
	 * 
	 * @param u
	 * @param profile
	 * @return
	 */
	public int create(User u, UserProfile profile) {
		// 创建用户
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_USER_SQL = "INSERT INTO USERS(USERNAME,HASHED,ACTIVATED,COMPANYNAME,COMPANYUNIQUEID,COMMUNITYUNIQUEID,EMAIL,PHONE,FAX) VALUES (?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						INSERT_USER_SQL, new String[] {"useruniqueid"});
				ps.setString(1, u.getUserName());
				ps.setString(2, u.getHashed());
				ps.setInt(3, u.getActivated());
				ps.setString(4, u.getCompanyName());
				ps.setInt(5, u.getCompanyUniqueId());
				ps.setInt(6, u.getCommunityUniqueId());
				ps.setString(7, u.getEmail());
				ps.setString(8, u.getPhone());
				ps.setString(9, u.getFax());
				return ps;
			}
			
		}, keyHolder);
		int userUniqueId  = keyHolder.getKey().intValue(); 
		// 创建用户个人配置
	    keyHolder = new GeneratedKeyHolder();
		String INSERT_USERPROFILES_SQL = "INSERT INTO USERPROFILES(USERUNIQUEID,TITLE,PICTURE,MANAGERUNIQUEID,CREATEDBY,CREATORUNIQUEID,DATECREATED,DATELASTMODIFIED,LASTLOGON,SEX,EMAIL,LOCATION,PHONE,FIRSTNAME,LASTNAME) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						INSERT_USERPROFILES_SQL, new String[] {"userprofileuniqueid"});
				ps.setInt(1, userUniqueId);
				ps.setString(2, profile.getTitle());
				ps.setString(3, profile.getPicture());
				ps.setInt(4, profile.getManagerUniqueId());
				ps.setString(5, profile.getCreatedBy());
				ps.setInt(6, profile.getCreatorUniqueId());
				long millis=System.currentTimeMillis();
				java.sql.Date date=new java.sql.Date(millis);
				ps.setDate(7, date);
				ps.setDate(8, null);
				ps.setDate(9, null);
				ps.setString(10, profile.getSex());
				ps.setString(11, profile.getEmail());
				ps.setString(12, profile.getLocation());
				ps.setString(13, profile.getPhone());
				ps.setString(14, profile.getFirstName());
				ps.setString(15, profile.getLastName());
				return ps;
			}
		}, keyHolder);
		int userprofileUniqueId  = keyHolder.getKey().intValue();
		log.info("Created an user.Affected id="+userUniqueId);
		log.info("Created an userprofile.Affected id="+userprofileUniqueId);
		return userUniqueId;
	}
	
	/**
	 * 
	 * @param u
	 */
	public void delete(User u) {
		if (u.getUserName().equals("root")) {
			return;
		}
		int i = 0;
		// 删除用户和角色关系表
		String sql = "DELETE FROM ROLEMEMBER WHERE USERUNIQUEID=?";
		i = jdbcTemplate.update(sql, u.getUserUniqueId());
		log.info("Delete from ROLEMEMBER.Affected row "+i);
		
		sql = "DELETE FROM USERPROFILES WHERE USERUNIQUEID=?";
		i = jdbcTemplate.update(sql, u.getUserUniqueId());
		log.info("Delete from USERPROFILES.Affected row "+i);
		
		sql = "DELETE FROM USERS WHERE USERUNIQUEID=?";
		i = jdbcTemplate.update(sql, u.getUserUniqueId());
		log.info("Delete from USERS.Affected row "+i);
	}
	
	/**
	 * 
	 * @param u
	 * @param ignorePassword
	 */
	public void update(User u, boolean ignorePassword) {
		if (ignorePassword) {
			String sql = "UPDATE USERS SET USERNAME=?,ACTIVATED=?,COMPANYNAME=?,COMPANYUNIQUEID=?,COMMUNITYUNIQUEID=?,EMAIL=?,PHONE=?,FAX=? WHERE USERUNIQUEID=?";
			jdbcTemplate.update(sql, u.getUserName(), u.getActivated(),
					u.getCompanyName(), u.getCompanyUniqueId(), u.getCommunityUniqueId(), u.getEmail(), u.getPhone(),
					u.getFax(), u.getUserUniqueId());

		} else {
			String sql = "UPDATE USERS SET USERNAME=?,HASHED=?,ACTIVATED=?,COMPANYNAME=?,COMPANYUNIQUEID=?,COMMUNITYUNIQUEID=?,EMAIL=?,PHONE=?,FAX=? WHERE USERUNIQUEID=?";
			jdbcTemplate.update(sql, u.getUserName(), u.getHashed(),
					u.getActivated(), u.getCompanyName(), u.getCompanyUniqueId(), u.getCommunityUniqueId(),
					u.getEmail(), u.getPhone(), u.getFax(), u.getUserUniqueId());
		}
	}
	
	/**
	 * 
	 * @param profile
	 */
	public void updateProfile(UserProfile profile) {
		String sql = "UPDATE USERPROFILES SET TITLE=?,PICTURE=?,MANAGERUNIQUEID=?,CREATEDBY=?,DATECREATED=?,DATELASTMODIFIED=?,LASTLOGON=?,SEX=?,EMAIL=?,LOCATION=?,PHONE=?,FIRSTNAME=?,LASTNAME=? WHERE USERPROFILEUNIQUEID=?";
		jdbcTemplate.update(sql, profile.getTitle(), profile.getPicture(), profile.getManagerUniqueId(),
				profile.getCreatedBy(), profile.getDateCreated(), profile.getDateLastModified(), profile.getLastLogon(),profile.getSex(),
				profile.getEmail(),profile.getLocation(), profile.getPhone(), profile.getFirstName(), profile.getLastName(),profile.getUserProfileUniqueId());
	}
	
	/**
	 * 
	 * @param u
	 */
	public void updatePassword(int userUniqueId, String plaintext) {
		String newHashed = PasswordSecurity.hashPassword(plaintext);
		String sql = "UPDATE USERS SET HASHED=? WHERE USERUNIQUEID=?";
		jdbcTemplate.update(sql, newHashed, userUniqueId);
	}
	
	/**
	 * 
	 * @param userUniqueId
	 * @param roles
	 */
	public void updateRoles(int userUniqueId, List<Role> roles) {
		String sql = "DELETE FROM ROLEMEMBER WHERE USERUNIQUEID = ?";
		jdbcTemplate.update(sql, userUniqueId);
		final String inserQuery = "INSERT INTO ROLEMEMBER(USERUNIQUEID, ROLEUNIQUEID) VALUES(?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Role role = roles.get(i);
				ps.setInt(1, userUniqueId);
				ps.setInt(2, role.getRoleUniqueId());
			}

			@Override
			public int getBatchSize() {
				return roles.size();
			}
		});
		log.info("Batch update has done.");
	}
	
	/**
	 * Check if the created user exists.
	 * 
	 * @param newUserName
	 * @param oldUserName
	 * @return
	 */
	public boolean exist(String newUserName, String oldUserName) {
		String sql = "SELECT * FROM USERS WHERE USERNAME=? AND USERNAME NOT IN (?)";
		List<User> lstUSERs = jdbcTemplate.query(sql, new Object[] {newUserName, oldUserName}, new BeanPropertyRowMapper<User>(User.class));
		return lstUSERs.size() > 0 || newUserName.equalsIgnoreCase("root");
	}
	
	/**
	 * 获取已安排的业务
	 * 
	 * @param userUniqueId
	 * @return
	 */
	public List<Business> findAssignedBusinesses(int userUniqueId) {
		String sql = "SELECT A.* FROM BUSINESS AS A RIGHT JOIN USERBUSINESSES AS B ON A.BUSINESSUNIQUEID=B.BUSINESSUNIQUEID WHERE B.USERUNIQUEID=?";
		List<Business> assignedBusinesses = jdbcTemplate.query(sql, new Object[] {userUniqueId}, new BeanPropertyRowMapper<Business>(Business.class));
		return assignedBusinesses;
	}
	
	/**
	 * 删除用户原来的业务关系
	 * 
	 * @param userUniqueId
	 * @return
	 */
	public int deleteBusinesses(int userUniqueId) {
		String sql = "DELETE FROM USERBUSINESSES WHERE USERUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {userUniqueId});
	 	return opt;
	}
	
	/**
	 * 重新设置用户和业务关系
	 * 
	 * @param userUniqueId
	 * @param lstBusiness
	 */
	public void assignBusinesses(int userUniqueId, List<Business> lstBusiness) {
		String sql = "INSERT INTO USERBUSINESSES(USERUNIQUEID,BUSINESSUNIQUEID) VALUES(?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Business business = lstBusiness.get(i);
				ps.setInt(1, userUniqueId);
				ps.setInt(2, business.getBusinessUniqueId());
			}

			@Override
			public int getBatchSize() {
				return lstBusiness.size();
			}
		});
	}
}
