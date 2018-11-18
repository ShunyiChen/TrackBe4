package com.maxtree.automotive.dashboard.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;

@Component
public class PermissionService {

	private static final Logger log = LoggerFactory.getLogger(PermissionService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 取权限分类中权限最多的数
	 * 
	 * @return
	 */
	public int getMaxChildrenCount() {
		String sql = "SELECT COUNT(*) AS A FROM PERMISSION GROUP BY CATEGORYUNIQUEID ORDER BY A DESC LIMIT ?";
		int maxCount = jdbcTemplate.queryForObject(sql, new Object[] {1}, Integer.class);
		return maxCount;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Permission> findAll() {
		String sql = "SELECT * FROM PERMISSION ORDER BY PERMISSIONUNIQUEID";
		List<Permission> result = jdbcTemplate.query(sql, new Object[] {},
				new BeanPropertyRowMapper<Permission>(Permission.class));
		return result;
	}

	/**
	 * 
	 * @param categoryUniqueId
	 * @return
	 */
	public List<Permission> findByCategoryUniqueId(int categoryUniqueId) {
		String sql = "SELECT * FROM PERMISSION WHERE CATEGORYUNIQUEID=? ORDER BY PERMISSIONUNIQUEID";
		List<Permission> result = jdbcTemplate.query(sql, new Object[] {categoryUniqueId},
				new BeanPropertyRowMapper<Permission>(Permission.class));
		return result;
	}
	
	/**
	 * 
	 * @param permissionUniqueId
	 * @return
	 */
	public Permission findById(int permissionUniqueId) {
		String sql = "SELECT * FROM PERMISSION WHERE PERMISSIONUNIQUEID = ?";
		List<Permission> result = jdbcTemplate.query(sql, new Object[] { permissionUniqueId },
				new BeanPropertyRowMapper<Permission>(Permission.class));
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	/**
	 * 更新权限上分配的角色
	 * 
	 * @param permissionUniqueId
	 * @param roles
	 */
	public void updatePermissionRoles(int permissionUniqueId, List<Role> roles) {
		String sql = "DELETE FROM ROLERIGHT WHERE PERMISSIONUNIQUEID = ?";
		jdbcTemplate.update(sql, permissionUniqueId);
		final String inserQuery = "INSERT INTO ROLERIGHT(ROLEUNIQUEID, PERMISSIONUNIQUEID) VALUES(?, ?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Role role = roles.get(i);
				ps.setInt(1, role.getRoleUniqueId());
				ps.setInt(2, permissionUniqueId);
			}

			@Override
			public int getBatchSize() {
				return roles.size();
			}
		});
		log.info("Batch update has done.");
	}
	
	/**
	 * 获得已分配的角色
	 * 
	 * @param roleUniqueId
	 * @return
	 */
	public List<Role> assignedRoles(int permissionUniqueId) {
		String sql = "SELECT B.* FROM ROLERIGHT AS A LEFT JOIN ROLE AS B ON A.ROLEUNIQUEID = B.ROLEUNIQUEID WHERE A.PERMISSIONUNIQUEID = ? AND B.ROLENAME IS NOT NULL";
		List<Role> results = jdbcTemplate.query(sql, new Object[] {permissionUniqueId}, new BeanPropertyRowMapper<Role>(Role.class));
		return results;
	}

	/**
	 * 
	 * @return
	 */
	public void update(Permission permission) {
		String sql = "UPDATE PERMISSION SET NAME=?, DESCRIPTION=? WHERE PERMISSIONUNIQUEID = ?";
		int opt = jdbcTemplate.update(sql,
				new Object[] { permission.getName(), permission.getDescription(), permission.getPermissionUniqueId() });
		log.info("update result:" + opt);
	}
	
	/**
	 * 
	 * @param roleUniqueId
	 * @return
	 */
	public int getRoleCount(int permissionUniqueId) {
		String sql = "SELECT COUNT(*) FROM ROLERIGHT WHERE PERMISSIONUNIQUEID = ?";
		int count = jdbcTemplate.queryForObject(
                sql, new Object[] { permissionUniqueId }, Integer.class);
		return count;
	}
	
	/**
	 * 
	 * @param permissionUniqueId
	 * @return
	 */
	public int getUserCount(int permissionUniqueId) {
		String sql = "SELECT COUNT(B.USERUNIQUEID) FROM ROLERIGHT AS A LEFT JOIN ROLEMEMBER AS B ON A.ROLEUNIQUEID = B.ROLEUNIQUEID AND A.PERMISSIONUNIQUEID = ?";
		int count = jdbcTemplate.queryForObject(
                sql, new Object[] { permissionUniqueId }, Integer.class);
		return count;
	}
	
	/**
	 * 获取此权限相关的全部用户ID
	 * 
	 * @param permissionUniqueId
	 * @return
	 */
	public List<User> getPermissionAssignedUserIDs(int permissionUniqueId) {
		String sql = "SELECT C.USERUNIQUEID FROM ROLERIGHT AS A LEFT JOIN ROLEMEMBER AS B ON A.ROLEUNIQUEID = B.ROLEUNIQUEID AND A.PERMISSIONUNIQUEID = ? "
				+ " LEFT JOIN USERS AS C ON B.USERUNIQUEID = C.USERUNIQUEID WHERE C.USERUNIQUEID IS NOT NULL";
		List<User> assignedUsers = jdbcTemplate.query(sql, new Object[] {permissionUniqueId}, new BeanPropertyRowMapper<User>(User.class));
		return assignedUsers;
	}
	
}
