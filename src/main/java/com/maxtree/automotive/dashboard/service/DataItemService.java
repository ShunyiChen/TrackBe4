package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.exception.DataException;

@Component
public class DataItemService {

	private static final Logger log = LoggerFactory.getLogger(DataItemService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param dataItemUniqueId
	 * @return
	 */
	public DataDictionary findById(int dictionaryuniqueid) {
		String sql = "SELECT * FROM DATADICTIONARY WHERE DICTIONARYUNIQUEID=?";
		List<DataDictionary> results = jdbcTemplate.query(sql, new Object[] {dictionaryuniqueid}, new BeanPropertyRowMapper<DataDictionary>(DataDictionary.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new DataDictionary();
	}
	
	/**
	 * 
	 * 获取字典
	 * 
	 * @param itemType 1：号牌种类，2：地区代号，3：业务材料
	 * @return
	 */
	public List<DataDictionary> findAllByType(int itemType) {
		String sql = "SELECT * FROM DATADICTIONARY WHERE ITEMTYPE=? ORDER BY DICTIONARYUNIQUEID";
		List<DataDictionary> results = jdbcTemplate.query(sql, new Object[] {itemType}, new BeanPropertyRowMapper<DataDictionary>(DataDictionary.class));
		return results;
	}
	
	/**
	 * 
	 * @param itemType
	 * @return
	 */
	public List<String> findNamesByType(int itemType) {
		String sql = "SELECT ITEMNAME FROM DATADICTIONARY WHERE ITEMTYPE=? ORDER BY DICTIONARYUNIQUEID";
		List<String> results = jdbcTemplate.queryForList(sql, new Object[] {itemType}, String.class);
		return results;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public String findCodeByName(String name) {
		String sql = "SELECT CODE FROM DATADICTIONARY WHERE ITEMNAME=? ORDER BY DICTIONARYUNIQUEID";
		List<String> results = jdbcTemplate.queryForList(sql, new Object[] {name}, String.class);
		if(results.size() > 0) {
			return results.get(0);
		}
		return "";
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public String findNameByCode(String code) {
		String sql = "SELECT ITEMNAME FROM DATADICTIONARY WHERE CODE=? ORDER BY DICTIONARYUNIQUEID";
		List<String> results = jdbcTemplate.queryForList(sql, new Object[] {code}, String.class);
		if(results.size() > 0) {
			return results.get(0);
		}
		return "";
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public DataDictionary findDDByName(String name) {
		String sql = "SELECT * FROM DATADICTIONARY WHERE ITEMTYPE=? AND ITEMNAME=?";
		List<DataDictionary> results = jdbcTemplate.query(sql, new Object[] {3,name}, new BeanPropertyRowMapper<DataDictionary>(DataDictionary.class));
		if(results.size() > 0) {
			return results.get(0);
		}
		return new DataDictionary();
	}
	
	/**
	 * 
	 * @param dict
	 * @return
	 */
	public int insert(DataDictionary dict) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO DATADICTIONARY(ITEMTYPE,ITEMNAME,CODE) VALUES(?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						sql, new String[] {"dictionaryuniqueid"});
				ps.setInt(1, dict.getItemType());
				ps.setString(2, dict.getItemName());
				ps.setString(3, dict.getCode());
				return ps;
			}
			
		}, keyHolder);
		int dduniqueid  = keyHolder.getKey().intValue(); 
		return dduniqueid;
	}
	
	/**
	 * 
	 * @param dict
	 */
	public void update(DataDictionary dict) {
		String sql = "UPDATE DATADICTIONARY SET ITEMTYPE=?,ITEMNAME=?,CODE=? WHERE DICTIONARYUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {dict.getItemType(),dict.getItemName(),dict.getCode(),dict.getDictionaryUniqueId()});
	 	log.info("Updated row "+opt);
	}
	
	/**
	 * 
	 * @param dict
	 * @throws DataException
	 */
	public void delete(DataDictionary dict) throws DataException {
		String sql = "";
		sql = "DELETE FROM BUSINESSITEMS WHERE DICTIONARYCODE=?";
		jdbcTemplate.update(sql, new Object[] {dict.getCode()});
		
		sql = "DELETE FROM DATADICTIONARY WHERE DICTIONARYUNIQUEID = ?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {dict.getDictionaryUniqueId()});
	 	log.info("Deleted row "+opt);
	}
}
