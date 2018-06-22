package com.maxtree.automotive.dashboard.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.PermissionCategory;

@Component
public class PermissionCategoryService {

	private static final Logger log = LoggerFactory.getLogger(PermissionCategoryService.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @return
	 */
	public List<PermissionCategory> findAll() {
		String sql = "SELECT * FROM PERMISSIONCATEGORY ORDER BY CATEGORYUNIQUEID";
		List<PermissionCategory> result = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper<PermissionCategory>(PermissionCategory.class));
		for (PermissionCategory cate : result) {
			sql = "SELECT * FROM PERMISSION WHERE CATEGORYUNIQUEID = ?";
			List<Permission> permissions = jdbcTemplate.query(sql, new Object[] {cate.getCategoryUniqueId()}, new BeanPropertyRowMapper<Permission>(Permission.class));
			cate.setPermissions(permissions);
		}
		return result;
	}

}
