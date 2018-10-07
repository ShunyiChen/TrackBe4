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
import com.maxtree.automotive.dashboard.domain.DataDictionary;
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
			List<DataDictionary> items = getDataDictionaries(business.getCode(),3);
			business.setItems(items);
			return business;
		}
		return new Business();
	}
	
	/**
	 * 
	 * @param businessCode
	 * @return
	 */
	public Business findByCode(String businessCode) {
		String sql = "SELECT * FROM BUSINESS WHERE CODE=?";
		List<Business> results = jdbcTemplate.query(sql, new Object[] {businessCode}, new BeanPropertyRowMapper<Business>(Business.class));
		for (Business business : results) {
			List<DataDictionary> items = getDataDictionaries(business.getCode(),3);
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
			List<DataDictionary> items = getDataDictionaries(business.getCode(),3);
			business.setItems(items);
		}
		return results;
	}
	
	/**
	 * 
	 * @param businessCode
	 * @param type
	 * @return
	 */
	public List<DataDictionary> getDataDictionaries(String businessCode, int type) {
		String sql = "SELECT B.* FROM BUSINESSITEMS AS A LEFT JOIN DATADICTIONARY AS B ON A.DICTIONARYCODE = B.CODE WHERE A.BUSINESSCODE=? AND B.ITEMNAME IS NOT NULL AND B.ITEMTYPE=? ORDER BY A.ITEMUNIQUEID";
		if (type == -1) {
			sql = "SELECT B.* FROM BUSINESSITEMS AS A LEFT JOIN DATADICTIONARY AS B ON A.DICTIONARYCODE = B.CODE WHERE A.BUSINESSCODE=? AND B.ITEMNAME IS NOT NULL ORDER BY A.ITEMUNIQUEID";
		}
		List<DataDictionary> results = jdbcTemplate.query(sql, new Object[] {businessCode, type}, new BeanPropertyRowMapper<DataDictionary>(DataDictionary.class));
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
			List<DataDictionary> items = getDataDictionaries(business.getCode(),3);
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
		String sql = "INSERT INTO BUSINESS(NAME,CHECKLEVEL,CODE) VALUES(?,?,?)";
		jdbcTemplate.update(sql, new Object[] {
				business.getName(),
				business.getCheckLevel(),
				business.getCode() // 快捷代码
		});
		return business;
	}

	/**
	 * 
	 * @param business
	 * @return
	 */
	public Business update(Business business) {
		String sql = "UPDATE BUSINESS SET NAME=?,CHECKLEVEL=?,CODE=? WHERE BUSINESSUNIQUEID=?";
		jdbcTemplate.update(sql,
				new Object[] {
						business.getName(),
						business.getCheckLevel(),//审档级别
						business.getCode(), // 业务类型编号
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
			sql = "DELETE FROM BUSINESSITEMS WHERE ITEMUNIQUEID=?";
			jdbcTemplate.update(sql, new Object[] { businessUniqueId });
			sql = "DELETE FROM BUSINESS WHERE BUSINESSUNIQUEID = ?";
			jdbcTemplate.update(sql, new Object[] { businessUniqueId });
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param businessCode
	 * @param items
	 */
	public void updateDataItems(String businessCode,  List<DataDictionary> items) {
		String sql = "DELETE FROM BUSINESSITEMS WHERE BUSINESSCODE=?";
		jdbcTemplate.update(sql, businessCode);
		final String inserQuery = "INSERT INTO BUSINESSITEMS(BUSINESSCODE,DICTIONARYCODE) VALUES(?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				DataDictionary item = items.get(i);
				ps.setString(1, businessCode);
				ps.setString(2, item.getCode());
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
	 * @param businessCode
	 * @return
	 */
	public List<DataDictionary> assignedItems(String businessCode) {
		String sql = "SELECT B.* FROM BUSINESSITEMS AS A LEFT JOIN DATADICTIONARY AS B ON A.DICTIONARYCODE = B.CODE WHERE A.BUSINESSCODE=? AND B.ITEMNAME IS NOT NULL ORDER BY A.ITEMUNIQUEID";
		List<DataDictionary> results = jdbcTemplate.query(sql, new Object[] {businessCode}, new BeanPropertyRowMapper<DataDictionary>(DataDictionary.class));
		return results;	
	}
 
}
