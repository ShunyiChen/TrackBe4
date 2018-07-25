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
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Transaction;

@Component
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 
	 * @param transactionUniqueId
	 * @param vin
	 * @return
	 */
	public Transaction findById(int transactionUniqueId, String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT * FROM TRANSACTION_"+index+" WHERE TRANSACTIONUNIQUEID=?";
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
		String sql = "SELECT * FROM TRANSACTION_"+index+" WHERE UUID=?";
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
	 * @return
	 */
	public List<Transaction> findForList(String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT * FROM TRANSACTION_"+index+" WHERE VIN=? ORDER BY TRANSACTIONUNIQUEID";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {vin}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
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
		String INSERT_TRANS_SQL = "INSERT INTO TRANSACTION_"+index+"(BARCODE,PLATETYPE,PLATENUMBER,VIN,DATECREATED,STATUS,SITECODE,BUSINESSCODE,COMMUNITYUNIQUEID,COMPANYUNIQUEID,LOCATIONCODE,DATEMODIFIED,BATCH,UUID,CREATOR,INDEXNUMBER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				ps.setString(7, transaction.getSiteCode());
				ps.setString(8, transaction.getBusinessCode());
				ps.setInt(9, transaction.getCommunityUniqueId());
				ps.setInt(10, transaction.getCompanyUniqueId());
				ps.setString(11, transaction.getLocationCode());
				java.sql.Timestamp modifyDate = new java.sql.Timestamp(transaction.getDateModified().getTime());
				ps.setTimestamp(12, modifyDate);
				ps.setInt(13, transaction.getBatch());
				ps.setString(14, transaction.getUuid());
				ps.setString(15, transaction.getCreator());
				ps.setInt(16, transaction.getIndexNumber());
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
		String sql = "UPDATE TRANSACTION_"+index+" SET BARCODE=?,PLATETYPE=?,PLATENUMBER=?,VIN=?,STATUS=?,DATEMODIFIED=?,BUSINESSCODE=?,UUID=?,CODE=?,INDEXNUMBER=? WHERE TRANSACTIONUNIQUEID=?";
	 	int affected = jdbcTemplate.update(sql, new Object[] {transaction.getBarcode(), transaction.getPlateType(), transaction.getPlateNumber(), 
	 			transaction.getVin(), transaction.getStatus(),transaction.getDateModified(), transaction.getBusinessCode(), 
	 			transaction.getUuid(), transaction.getCode(), transaction.getIndexNumber(), transaction.getTransactionUniqueId()});
	 	log.info("Updated.Affected row = "+affected);
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
	 * 获取新车注册业务的上架号
	 * 
	 * @param vin
	 * @return
	 */
	public String findFirstCode(String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT CODE FROM TRANSACTION_"+index+" WHERE VIN=? AND INDEXNUMBER=?";
		String code = jdbcTemplate.queryForObject( sql, new Object[] {vin, 1}, String.class);
		return code;
	}
	
	/**
	 * 
	 * @param vin
	 * @return
	 */
	private int getTableIndex(String vin) {
		int num = Integer.parseInt(vin.substring(vin.length() - 4));
		return num % 256;
	}
}
