package com.maxtree.automotive.dashboard.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.SystemSettings;

@Component
public class SystemSettingsService {

	private static final Logger log = LoggerFactory.getLogger(SystemSettingsService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public SystemSettings findByName(String name) {
		String sql = "SELECT * FROM SYSTEMSETTINGS WHERE ITEMNAME=?";
		List<SystemSettings> results = jdbcTemplate.query(sql, new Object[] {name}, new BeanPropertyRowMapper<SystemSettings>(SystemSettings.class));
		if(results.size() > 0) {
			return results.get(0);
		}
		return new SystemSettings();
	}
	
	/**
	 * 
	 * @param settings
	 */
	public void update(SystemSettings settings) {
		String sql = "UPDATE SYSTEMSETTINGS SET ITEMSETTINGS=? WHERE ITEMNAME=?";
		jdbcTemplate.update(sql, new Object[] {settings.getItemSettings(),settings.getItemName()});
	}
	
}
