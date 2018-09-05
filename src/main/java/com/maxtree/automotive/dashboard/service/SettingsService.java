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
public class SettingsService {

	private static final Logger log = LoggerFactory.getLogger(SettingsService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public SystemSettings findByKey(String key) {
		String sql = "SELECT * FROM SYSTEM_SETTINGS WHERE K=?";
		List<SystemSettings> results = jdbcTemplate.query(sql, new Object[] {key}, new BeanPropertyRowMapper<SystemSettings>(SystemSettings.class));
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
		String sql = "UPDATE SYSTEM_SETTINGS SET V=? WHERE K=?";
		jdbcTemplate.update(sql, new Object[] {settings.getV(),settings.getK()});
	}
	
}
