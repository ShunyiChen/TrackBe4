package com.maxtree.automotive.dashboard.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;

@Component
public class RoleService {

	private static final Logger log = LoggerFactory.getLogger(RoleService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param roleUniqueId
	 * @return
	 * @throws EmptyResultDataAccessException
	 */
	public Role findById(int roleUniqueId) throws EmptyResultDataAccessException {
		String sql = "SELECT * FROM ROLE WHERE ROLEUNIQUEID = ?";
		List<Role> lstRole = jdbcTemplate.query(sql, new Object[] {roleUniqueId}, new BeanPropertyRowMapper<Role>(Role.class));
		if (lstRole.size() > 0) {
			return lstRole.get(0);
		}
		return null;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<Role> findAll(boolean includeSystem) {
		String sql = "SELECT * FROM ROLE WHERE ROLENAME <> ?";
//		if (includeSystem) {
			sql = "SELECT * FROM ROLE";
//		}
		List<Role> lstRole = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Role>(Role.class));
		return lstRole;
	}
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	public int insert(Role role) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO ROLE(ROLENAME, ROLETYPE) VALUES(?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						sql, new String[] {"roleuniqueid"});
				ps.setString(1, role.getRoleName());
				ps.setString(2, "自定义");
				return ps;
			}
			
		}, keyHolder);
		int roleUniqueId  = keyHolder.getKey().intValue(); 
		log.info("Complete insert role and return id::"+roleUniqueId);
		return roleUniqueId;
	}
	
	/**
	 * 
	 * @return
	 */
	public void update(Role role) {
		String sql = "UPDATE ROLE SET ROLENAME=? WHERE ROLEUNIQUEID = ?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {role.getRoleName(), role.getRoleUniqueId()});
	 	log.info("Complete update role:"+opt);
	}
	
	/**
	 * 
	 * @return
	 */
	public int delete(int roleUniqueId, boolean forceDelete) {
		if (!forceDelete) {
			String sql = "DELETE FROM ROLE WHERE ROLEUNIQUEID = ?";
		 	int affectedRowCount = jdbcTemplate.update(sql, new Object[] {roleUniqueId});
		 	return affectedRowCount;
		}
		else {
			String sql = "DELETE FROM ROLEMEMBER WHERE ROLEUNIQUEID = ?";
			jdbcTemplate.update(sql, new Object[] {roleUniqueId});
			sql = "DELETE FROM ROLERIGHT WHERE ROLEUNIQUEID = ?";
			jdbcTemplate.update(sql, new Object[] {roleUniqueId});
			sql = "DELETE FROM ROLE WHERE ROLEUNIQUEID = ?";
		 	int affectedRowCount = jdbcTemplate.update(sql, new Object[] {roleUniqueId});
		 	return affectedRowCount;
		}
	}
	
	/**
	 * 获取已经安排的用户
	 * 
	 * @param roleUniqueId
	 * @return
	 */
	public List<User> assignedUsers(int roleUniqueId) {
		String sql = "SELECT B.* FROM ROLEMEMBER AS A LEFT JOIN USERS AS B ON A.USERUNIQUEID = B.USERUNIQUEID WHERE A.ROLEUNIQUEID=? AND B.USERNAME IS NOT NULL";
		List<User> results = jdbcTemplate.query(sql, new Object[] {roleUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		return results;
	}
	
	/**
	 * 获取已经安排的权限
	 * 
	 * @param roleUniqueId
	 * @return
	 */
	public List<Permission> assignedPermissions(int roleUniqueId) {
		String sql = "SELECT B.* FROM ROLERIGHT AS A LEFT JOIN PERMISSION AS B ON A.PERMISSIONUNIQUEID = B.PERMISSIONUNIQUEID WHERE A.ROLEUNIQUEID = ? AND B.CODE IS NOT NULL";
		List<Permission> results = jdbcTemplate.query(sql, new Object[] {roleUniqueId}, new BeanPropertyRowMapper<Permission>(Permission.class));
		return results;
	}
	
	/**
	 * 更新角色用户
	 */
	public void updateRoleUsers(int roleUniqueId, List<User> users) {
		String sql = "DELETE FROM ROLEMEMBER WHERE ROLEUNIQUEID = ?";
		jdbcTemplate.update(sql, roleUniqueId);
		final String inserQuery = "INSERT INTO ROLEMEMBER(USERUNIQUEID, ROLEUNIQUEID) VALUES(?, ?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				User user = users.get(i);
				ps.setInt(1, user.getUserUniqueId());
				ps.setInt(2, roleUniqueId);
			}

			@Override
			public int getBatchSize() {
				return users.size();
			}
		});
		log.info("updateRoleUsers batch has done.");
	}
	
	/**
	 * 
	 * @param roleUniqueId
	 * @param permissionUniqueIds
	 */
	public void updateRolePermissions(int roleUniqueId, List<Integer> permissionUniqueIds) {
		String sql = "DELETE FROM ROLERIGHT WHERE ROLEUNIQUEID = ?";
		jdbcTemplate.update(sql, roleUniqueId);
		final String inserQuery = "INSERT INTO ROLERIGHT(ROLEUNIQUEID, PERMISSIONUNIQUEID) VALUES(?, ?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Integer permissionUniqueID = permissionUniqueIds.get(i);
				ps.setInt(1, roleUniqueId);
				ps.setInt(2, permissionUniqueID);
			}

			@Override
			public int getBatchSize() {
				return permissionUniqueIds.size();
			}
		});
		log.info("updateRoleUsers batch has done.");
	}
	
	/**
	 * 
	 * @param roleUniqueId
	 * @return
	 */
	public int getUserCount(int roleUniqueId) {
		String sql = "SELECT COUNT(*) FROM ROLEMEMBER WHERE ROLEUNIQUEID = ?";
		int count = jdbcTemplate.queryForObject(
                sql, new Object[] { roleUniqueId }, Integer.class);

		return count;
	}
	
	public int getCapabilityCount(int roleUniqueId) {
		String sql = "SELECT COUNT(*) FROM ROLERIGHT WHERE ROLEUNIQUEID = ?";
		int count = jdbcTemplate.queryForObject(
                sql, new Object[] { roleUniqueId }, Integer.class);

		return count;
	}
}
