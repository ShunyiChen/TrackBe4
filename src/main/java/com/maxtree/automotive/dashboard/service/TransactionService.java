package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.FinalCheck;
import com.maxtree.automotive.dashboard.domain.Transaction;

@Component
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Connection getConnection() {
		try {
			return jdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 按流水号查
	 * 
	 * @param barcode
	 * @param vin
	 * @return
	 */
	public Transaction findByBarcode(String barcode, String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.NAME AS BUSINESSNAME FROM TRANSACTION_"+index+" AS A LEFT JOIN BUSINESS AS B ON A.BUSINESSCODE=B.CODE WHERE A.BARCODE=?";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {barcode}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		Transaction trans = null;
		if (result.size() > 0) {
			trans = result.get(0);
		}
		return trans;
	}
	
	/**
	 * 
	 * @param plateType
	 * @param plateNumber
	 * @param vin
	 * @return
	 */
	public Transaction findByStatus(String plateType,String plateNumber,String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.NAME AS BUSINESSNAME FROM TRANSACTION_"+index+" AS A LEFT JOIN BUSINESS AS B ON A.BUSINESSCODE=B.CODE WHERE A.PLATETYPE=? AND A.PLATENUMBER=? AND A.VIN=? AND A.STATUS=?";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {plateType,plateNumber,vin,"待补充"}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		Transaction trans = null;
		if (result.size() > 0) {
			trans = result.get(0);
		}
		return trans;
	}
	
	/**
	 * 
	 * @param transactionUniqueId
	 * @param vin
	 * @return
	 */
	public Transaction findById(int transactionUniqueId, String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.NAME AS BUSINESSNAME FROM TRANSACTION_"+index+" AS A LEFT JOIN BUSINESS AS B ON A.BUSINESSCODE=B.CODE WHERE A.TRANSACTIONUNIQUEID=?";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {transactionUniqueId}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		Transaction trans = null;
		if (result.size() > 0) {
			trans = result.get(0);
		}
		return trans;
	}
	
	/**
	 * 
	 * @param uuid
	 * @param vin
	 * @return
	 */
	public Transaction findByUUID(String uuid, String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.NAME AS BUSINESSNAME FROM TRANSACTION_"+index+" AS A LEFT JOIN BUSINESS AS B ON A.BUSINESSCODE=B.CODE WHERE A.UUID=?";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {uuid}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		Transaction trans = null;
		if (result.size() > 0) {
			trans = result.get(0);
		}
		return trans;
	}
	
	/**
	 * 
	 * @param vin
	 * @param barcode
	 * @param exceptUniqueId (ID Which is not included)
	 * @return
	 */
	public List<Transaction> findForList(String vin, String barcode, int exceptUniqueId) {
		String ext = "";
		Object[] param = new Object[] {vin,exceptUniqueId};
		if(barcode != null) {
			ext = " AND A.BARCODE=? ";
			param = new Object[] {vin,exceptUniqueId,barcode};
		}
		
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.NAME AS BUSINESSNAME FROM TRANSACTION_"+index+" AS A LEFT JOIN BUSINESS AS B ON A.BUSINESSCODE=B.CODE WHERE A.VIN=? AND A.TRANSACTIONUNIQUEID<>? "+ext+" ORDER BY A.TRANSACTIONUNIQUEID";
		List<Transaction> result = jdbcTemplate.query(sql,param,new BeanPropertyRowMapper<Transaction>(Transaction.class));
		return result;
	}
	
	/**
	 * 
	 * @param vin
	 * @param businessCode
	 * @return
	 */
	public List<Transaction> findForList(String vin, String businessCode) {
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.NAME AS BUSINESSNAME FROM TRANSACTION_"+index+" AS A LEFT JOIN BUSINESS AS B ON A.BUSINESSCODE=B.CODE WHERE A.VIN=? AND A.BUSINESSCODE=? ORDER BY A.TRANSACTIONUNIQUEID";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {vin,businessCode}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		return result;
	}
	
	/**
	 * 
	 * @param transaction
	 * @return
	 */
	public int insert(Transaction transaction) {
		int index = getTableIndex(transaction.getVin());
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO TRANSACTION_"+index+"(BARCODE,PLATETYPE,PLATENUMBER,VIN,DATECREATED,STATUS,SITEUNIQUEID,BUSINESSCODE,COMMUNITYUNIQUEID,COMPANYUNIQUEID,LOCATIONCODE,DATEMODIFIED,BATCH,UUID,CODE,CREATOR,INDEXNUMBER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "transactionuniqueid" });
				ps.setString(1, transaction.getBarcode());
				ps.setString(2, transaction.getPlateType());
				ps.setString(3, transaction.getPlateNumber());
				ps.setString(4, transaction.getVin());
				long millis = System.currentTimeMillis();
				java.sql.Timestamp date = new java.sql.Timestamp(millis);
				ps.setTimestamp(5, date);
				ps.setString(6, transaction.getStatus());
				ps.setInt(7, transaction.getSiteUniqueId());
				ps.setString(8, transaction.getBusinessCode());
				ps.setInt(9, transaction.getCommunityUniqueId());
				ps.setInt(10, transaction.getCompanyUniqueId());
				ps.setString(11, transaction.getLocationCode());
				java.sql.Timestamp modifyDate = new java.sql.Timestamp(transaction.getDateModified().getTime());
				ps.setTimestamp(12, modifyDate);
				ps.setInt(13, transaction.getBatch());
				ps.setString(14, transaction.getUuid());
				ps.setString(15, transaction.getCode());
				ps.setString(16, transaction.getCreator());
				ps.setInt(17, transaction.getIndexNumber());
				return ps;
			}
		}, keyHolder);
		int transactionUniqueId = keyHolder.getKey().intValue();
		log.info("Affected row = "+transactionUniqueId);
		return transactionUniqueId;
	}
	
	/**
	 * 更改transaction
	 * 
	 * @param transaction
	 */
	public void update(Transaction transaction) {
		int index = getTableIndex(transaction.getVin());
		String sql = "UPDATE TRANSACTION_"+index+" SET BARCODE=?,PLATETYPE=?,PLATENUMBER=?,VIN=?,STATUS=?,DATEMODIFIED=? WHERE TRANSACTIONUNIQUEID=?";
	 	int affected = jdbcTemplate.update(sql, new Object[] {transaction.getBarcode(), transaction.getPlateType(), transaction.getPlateNumber(), 
	 			transaction.getVin(), transaction.getStatus(),transaction.getDateModified(),transaction.getTransactionUniqueId()});
	 	log.info("Updated.Affected row = "+affected);
	 	System.out.println("开始打印sql");
	 	System.out.println("UPDATE TRANSACTION_"+index+" SET BARCODE="+transaction.getBarcode()+",PLATETYPE="+transaction.getPlateType()+",PLATENUMBER="+transaction.getPlateNumber()+",VIN="+transaction.getVin()+",STATUS="+transaction.getStatus()+",DATEMODIFIED="+transaction.getDateModified()+" WHERE TRANSACTIONUNIQUEID="+transaction.getTransactionUniqueId());
	}
	
	/**
	 * 
	 * @param vin
	 * @return
	 */
	public int findIndexNumber(String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT COUNT(TRANSACTIONUNIQUEID) FROM TRANSACTION_"+index+" WHERE VIN=?";
		int count = jdbcTemplate.queryForObject( sql, new Object[] {vin}, Integer.class);
		return count;
	}
	
	/**
	 * 获取transaction上架号
	 * 
	 * @param vin
	 * @return
	 */
	public String findTransactionCode(String vin) {
		try {
			int index = getTableIndex(vin);
			String sql = "SELECT CODE FROM TRANSACTION_"+index+" WHERE VIN=? AND CODE IS NOT NULL LIMIT ?";
			String code = jdbcTemplate.queryForObject( sql, new Object[] {vin,1}, String.class);
			return code;
		} catch (IncorrectResultSizeDataAccessException e){
			return null;
		}
	}
	
	/**
	 * 获取VIN车辆识别代号
	 * 
	 * @param vin
	 * @return
	 */
	public int getTableIndex(String vin) {
		int sum = 0;
		for (char c : vin.toCharArray()) {
			sum += c;
		}
		return sum % 256;
	}
	
	/**
	 * 按关键字查找 20181121
	 * 
	 * @param limit
	 * @param offset
	 * @param keyword
	 * @param locationCode
	 * @return
	 */
	public List<Transaction> search_by_keyword(int limit, int offset, String keyword,String locationCode) {
		String sql = "SELECT * FROM SEARCH_BY_KEYWORD(?,?,?,?,?)";
		List<Transaction> lst = jdbcTemplate.query(sql, new Object[] {limit, offset,keyword,locationCode}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		return lst;
	}
	
	/**
	 * 查询页数 20181121
	 * 
	 * @param limit
	 * @param keyword
	 * @param tenantName
	 * @param locationCode
	 * @return
	 */
	public int search_by_keyword_pagingcount(int limit, String keyword, String tenantName,String locationCode) {
		String sql = "SELECT * FROM SEARCH_BY_KEYWORD_PAGINGCOUNT(?,?,?,?)";
		int count = jdbcTemplate.queryForObject( sql, new Object[] {limit, keyword,tenantName,locationCode}, Integer.class);
		
		System.out.println("count="+count);
		return count;
	}
	
	/**
	 * 
	 * @param limit
	 * @param plateType
	 * @param plateNumber
	 * @param vin
	 * @param communityName
	 * @return
	 */
	public int search_by_platetype_and_platenumber_vin_pagingcount(int limit, String plateType, String plateNumber, String vin, String communityName) {
		String sql = "SELECT * FROM SEARCH_BY_PLATETYPE_AND_PLATENUMBER_OR_VIN_PAGINGCOUNT(?,?,?,?,?)";
		int count = jdbcTemplate.queryForObject(sql, new Object[] {limit, plateType,plateNumber,vin,communityName}, Integer.class);
		return count;
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param plateType
	 * @param plateNumber
	 * @param vin
	 * @param parentCommunityName
	 * @return
	 */
	public List<Transaction> search_by_platetype_and_platenumber_vin(int limit, int offset,String plateType,String plateNumber,String vin,String parentCommunityName) {
		List<Transaction> all = new ArrayList<Transaction>();
		String sql = "SELECT B.COMMUNITYNAME FROM COMMUNITIES AS A INNER JOIN COMMUNITIES AS B ON A.GROUPID=B.GROUPID WHERE A.COMMUNITYNAME=? AND A.LEVEL<=B.LEVEL ORDER BY B.COMMUNITYUNIQUEID";
		List<String> communityNames = jdbcTemplate.queryForList(sql, new Object[] {parentCommunityName}, String.class);
		for (String name : communityNames){
			sql = "SELECT * FROM SEARCH_BY_PLATETYPE_AND_PLATENUMBER_OR_VIN(?,?,?,?,?,?)";
			List<Transaction> lst = jdbcTemplate.query(sql, new Object[] {limit, offset, plateType,plateNumber,vin,name}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
			all.addAll(lst);
		}
		return all;
	}
	
	/**
	 * 
	 * @param vin
	 * @param businessCode
	 * @return
	 */
	public int getCount(String vin, String businessCode) {
		int index = getTableIndex(vin);
		String sql = "SELECT COUNT(TRANSACTIONUNIQUEID) FROM TRANSACTION_"+index+" WHERE BUSINESSCODE=? AND VIN=?";
		int count = jdbcTemplate.queryForObject( sql, new Object[] {businessCode,vin}, Integer.class);
		return count;
	}
	
	/**
	 * 
	 * @param transactionUniqueId
	 * @param vin
	 */
	public void deleteByUniqueID(int transactionUniqueId, String vin) {
		int index = getTableIndex(vin);
		String sql = "DELETE FROM TRANSACTION_"+index+" WHERE TRANSACTIONUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {transactionUniqueId});
	}
	
	/**
	 * 
	 * @param vin
	 * @param plateNumber
	 */
	public void batchUpdate(String vin, String plateNumber) {
		int index = getTableIndex(vin);
		String sql = "UPDATE TRANSACTION_"+index+" SET PLATENUMBER=?,DATEMODIFIED=? WHERE VIN=?";
	 	int affected = jdbcTemplate.update(sql, new Object[] {plateNumber,new Date(), vin});
	 	log.info("Updated.Affected row = "+affected);
	}
	
	/**
	 * 
	 * @param vin
	 * @param uuid
	 * @param status
	 */
	public void updateStatus(String vin, String uuid, String status) {
		int index = getTableIndex(vin);
		String sql = "UPDATE TRANSACTION_"+index+" SET STATUS=?,DATEMODIFIED=? WHERE UUID=?";
	 	int affected = jdbcTemplate.update(sql, new Object[] {status, new Date(),uuid});
	 	log.info("Updated.Affected row = "+affected);
	}
	
	/**
	 * 
	 * @param barcode
	 * @return
	 */
	public FinalCheck findFinalCheck(String barcode) {
		String sql = "SELECT * FROM FINALCHECK WHERE BARCODE=?";
		List<FinalCheck> result = jdbcTemplate.query(sql, new Object[] {barcode}, new BeanPropertyRowMapper<FinalCheck>(FinalCheck.class));
		FinalCheck finalCheck = null;
		if (result.size() > 0) {
			finalCheck = result.get(0);
		}
		return finalCheck;
	}
	
	/**
	 * 插入待终审记录表
	 * 
	 * @param finalCheck
	 * @return
	 */
	public int insertFinalCheck(FinalCheck finalCheck) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO FINALCHECK(BARCODE,VIN) VALUES(?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "checkuniqueid" });
				ps.setString(1, finalCheck.getBarcode());
				ps.setString(2, finalCheck.getVin());
				return ps;
			}
		}, keyHolder);
		int checkuniqueid = keyHolder.getKey().intValue();
		log.info("Affected row = "+checkuniqueid);
		return checkuniqueid;
	}
	
}
