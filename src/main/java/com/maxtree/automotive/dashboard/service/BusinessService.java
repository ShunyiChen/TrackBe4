package com.maxtree.automotive.dashboard.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataItem;
import com.maxtree.automotive.dashboard.exception.DataException;

@Component
public class BusinessService {

	private static final Logger log = LoggerFactory.getLogger(BusinessService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param businessUniqueId
	 * @return
	 */
	public Business findById(int businessUniqueId) {
		String sql = "SELECT * FROM BUSINESS WHERE BUSINESSUNIQUEID=?";
		List<Business> results = jdbcTemplate.query(sql, new Object[] {businessUniqueId}, new BeanPropertyRowMapper<Business>(Business.class));
		for (Business business : results) {
			sql = "SELECT B.* FROM BUSINESSITEMS AS A LEFT JOIN DATAITEMS AS B ON A.DATAITEMUNIQUEID = B.DATAITEMUNIQUEID WHERE A.BUSINESSUNIQUEID = ? AND B.ITEMNAME IS NOT NULL";
			List<DataItem> items = jdbcTemplate.query(sql, new Object[] {business.getBusinessUniqueId()}, new BeanPropertyRowMapper<DataItem>(DataItem.class));
			business.setItems(items);
			return business;
		}
		return new Business();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Business> findAll() {
		String sql = "SELECT * FROM BUSINESS ORDER BY BUSINESSUNIQUEID";
		List<Business> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Business>(Business.class));
		for (Business business : results) {
			sql = "SELECT B.* FROM BUSINESSITEMS AS A LEFT JOIN DATAITEMS AS B ON A.DATAITEMUNIQUEID = B.DATAITEMUNIQUEID WHERE A.BUSINESSUNIQUEID = ? AND B.ITEMNAME IS NOT NULL";
			List<DataItem> items = jdbcTemplate.query(sql, new Object[] {business.getBusinessUniqueId()}, new BeanPropertyRowMapper<DataItem>(DataItem.class));
			business.setItems(items);
		}
		return results;
	}
	
	/**
	 * 
	 * @param companyUniqueId
	 * @return
	 */
	public List<Business> findAllByCompanyUniqueId(int companyUniqueId) {
		String sql = "SELECT B.* FROM COMPANYBUSINESSES AS A LEFT JOIN BUSINESS AS B ON A.BUSINESSUNIQUEID=B.BUSINESSUNIQUEID WHERE A.COMPANYUNIQUEID=? ORDER BY B.BUSINESSUNIQUEID";
		List<Business> results = jdbcTemplate.query(sql,  new Object[] {companyUniqueId}, new BeanPropertyRowMapper<Business>(Business.class));
		for (Business business : results) {
			sql = "SELECT B.* FROM BUSINESSITEMS AS A LEFT JOIN DATAITEMS AS B ON A.DATAITEMUNIQUEID = B.DATAITEMUNIQUEID WHERE A.BUSINESSUNIQUEID = ? AND B.ITEMNAME IS NOT NULL";
			List<DataItem> items = jdbcTemplate.query(sql, new Object[] {business.getBusinessUniqueId()}, new BeanPropertyRowMapper<DataItem>(DataItem.class));
			business.setItems(items);
		}
		return results;
	}

	/**
	 * 
	 * @param business
	 * @return
	 */
	public Business insert(Business business) {
		String sql = "INSERT INTO BUSINESS(NAME,FILECHECK,HASFIRSTINDEX,LOCALCHECK) VALUES(?,?,?,?)";
		jdbcTemplate.update(sql, new Object[] {
				business.getName(),
				business.getFileCheck(),
				business.getHasFirstIndex(),
				business.getLocalCheck()});
		return business;
	}

	/**
	 * 
	 * @param business
	 * @return
	 */
	public Business update(Business business) {
		String sql = "UPDATE BUSINESS SET NAME=?,FILECHECK=?,HASFIRSTINDEX=?,LOCALCHECK=? WHERE BUSINESSUNIQUEID=?";
		jdbcTemplate.update(sql,
				new Object[] {
						business.getName(),
						business.getFileCheck(),
						business.getHasFirstIndex(),
						business.getLocalCheck(),
						business.getBusinessUniqueId()
						});
		return business;
	}

	/**
	 * 
	 * @param businessUniqueId
	 */
	public void delete(int businessUniqueId) throws DataException {
		String sql = "";
		try {
			sql = "DELETE FROM COMPANYBUSINESSES WHERE BUSINESSUNIQUEID=?";
			jdbcTemplate.update(sql, new Object[] { businessUniqueId });
			sql = "DELETE FROM BUSINESSITEMS WHERE BUSINESSUNIQUEID=?";
			jdbcTemplate.update(sql, new Object[] { businessUniqueId });
			sql = "DELETE FROM BUSINESS WHERE BUSINESSUNIQUEID = ?";
			jdbcTemplate.update(sql, new Object[] { businessUniqueId });
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		}
	}
	
	public void updateDataItems(int businessUniqueId, List<DataItem> items) {
		String sql = "DELETE FROM BUSINESSITEMS WHERE BUSINESSUNIQUEID = ?";
		jdbcTemplate.update(sql, businessUniqueId);
		final String inserQuery = "INSERT INTO BUSINESSITEMS(BUSINESSUNIQUEID, DATAITEMUNIQUEID) VALUES(?, ?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DataItem item = items.get(i);
				ps.setInt(1, businessUniqueId);
				ps.setInt(2, item.getDataItemUniqueId());
			}

			@Override
			public int getBatchSize() {
				return items.size();
			}
		});
		log.info("updateMaterials has done.");
	}
	
	/**
	 * 
	 * @param businessUniqueId
	 * @return
	 */
	public List<DataItem> assignedItems(int businessUniqueId) {
		String sql = "SELECT B.* FROM BUSINESSITEMS AS A LEFT JOIN DATAITEMS AS B ON A.DATAITEMUNIQUEID = B.DATAITEMUNIQUEID WHERE A.BUSINESSUNIQUEID = ? AND B.ITEMNAME IS NOT NULL";
		List<DataItem> results = jdbcTemplate.query(sql, new Object[] {businessUniqueId}, new BeanPropertyRowMapper<DataItem>(DataItem.class));
		return results;	
	}
 
}
