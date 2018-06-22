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

import com.maxtree.automotive.dashboard.domain.DataItem;
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
	public DataItem findById(int dataItemUniqueId) {
		String sql = "SELECT * FROM DATAITEMS WHERE DATAITEMUNIQUEID = ?";
		List<DataItem> results = jdbcTemplate.query(sql, new Object[] {dataItemUniqueId}, new BeanPropertyRowMapper<DataItem>(DataItem.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new DataItem();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<DataItem> findAll() {
		String sql = "SELECT * FROM DATAITEMS ORDER BY DATAITEMUNIQUEID";
		List<DataItem> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<DataItem>(DataItem.class));
		return results;
	}
	
	/**
	 * 
	 * @param categoryName
	 * @return
	 */
	public List<DataItem> findAllByCategory(String categoryName) {
		String sql = "SELECT * FROM DATAITEMS WHERE CATEGORYNAME=? ORDER BY DATAITEMUNIQUEID";
		List<DataItem> results = jdbcTemplate.query(sql, new Object[] {categoryName}, new BeanPropertyRowMapper<DataItem>(DataItem.class));
		return results;
	}
	
	/**
	 * 
	 * @param categoryName
	 * @return
	 */
	public List<String> findAllByCategoryName(String categoryName) {
		String sql = "SELECT ITEMNAME FROM DATAITEMS WHERE CATEGORYNAME=? ORDER BY DATAITEMUNIQUEID";
		List<String> results = jdbcTemplate.queryForList(sql, new Object[] {categoryName}, String.class);
		return results;
	}
	
	/**
	 * 
	 * @param dataItem
	 * @return
	 */
	public int create(DataItem dataItem) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO DATAITEMS(CATEGORYNAME,ITEMNAME) VALUES(?,?)";
		
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						sql, new String[] {"dataitemuniqueid"});
				ps.setString(1, dataItem.getCategoryName());
				ps.setString(2, dataItem.getItemName());
				return ps;
			}
			
		}, keyHolder);
		int companyuniqueid  = keyHolder.getKey().intValue(); 
		return companyuniqueid;
	}
	
	/**
	 * 
	 * @param dataItem
	 */
	public void update(DataItem dataItem) {
		String sql = "UPDATE DATAITEMS SET CATEGORYNAME=?,ITEMNAME=? WHERE DATAITEMUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {dataItem.getCategoryName(),dataItem.getItemName(), dataItem.getDataItemUniqueId()});
	 	log.info("Updated row "+opt);
	}
	
	/**
	 * 
	 * @param dataItem
	 * @throws DataException
	 */
	public void delete(DataItem dataItem) throws DataException {
		String sql = "";
		sql = "DELETE FROM BUSINESSITEMS WHERE DATAITEMUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {dataItem.getDataItemUniqueId()});
		sql = "DELETE FROM DATAITEMS WHERE DATAITEMUNIQUEID = ?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {dataItem.getDataItemUniqueId()});
	 	log.info("Deleted row "+opt);
	}
}
