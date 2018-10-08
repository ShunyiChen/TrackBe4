package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import com.maxtree.automotive.dashboard.domain.Imaging;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.exception.DataException;

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
	 * @param exceptUniqueId (ID Which is not included)
	 * @return
	 */
	public List<Transaction> findForList(String vin, int exceptUniqueId) {
		int index = getTableIndex(vin);
		String sql = "SELECT * FROM TRANSACTION_"+index+" WHERE VIN=? AND TRANSACTIONUNIQUEID<>? ORDER BY TRANSACTIONUNIQUEID";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {vin,exceptUniqueId}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
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
		String INSERT_TRANS_SQL = "INSERT INTO TRANSACTION_"+index+"(BARCODE,PLATETYPE,PLATENUMBER,VIN,DATECREATED,STATUS,SITECODE,BUSINESSCODE,COMMUNITYUNIQUEID,COMPANYUNIQUEID,LOCATIONCODE,DATEMODIFIED,BATCH,UUID,CODE,CREATOR,INDEXNUMBER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
//		String sql = "UPDATE TRANSACTION_"+index+" SET BARCODE=?,PLATETYPE=?,PLATENUMBER=?,VIN=?,STATUS=?,DATEMODIFIED=?,BUSINESSCODE=?,UUID=?,CODE=?,INDEXNUMBER=? WHERE TRANSACTIONUNIQUEID=?";
//	 	int affected = jdbcTemplate.update(sql, new Object[] {transaction.getBarcode(), transaction.getPlateType(), transaction.getPlateNumber(), 
//	 			transaction.getVin(), transaction.getStatus(),transaction.getDateModified(), transaction.getBusinessCode(), 
//	 			transaction.getUuid(), transaction.getCode(), transaction.getIndexNumber(), transaction.getTransactionUniqueId()});
		
		String sql = "UPDATE TRANSACTION_"+index+" SET BARCODE=?,PLATETYPE=?,PLATENUMBER=?,VIN=?,STATUS=?,DATEMODIFIED=? WHERE TRANSACTIONUNIQUEID=?";
	 	int affected = jdbcTemplate.update(sql, new Object[] {transaction.getBarcode(), transaction.getPlateType(), transaction.getPlateNumber(), 
	 			transaction.getVin(), transaction.getStatus(),transaction.getDateModified(),transaction.getTransactionUniqueId()});
		
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
//			throw new DataException(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 
	 * @param vin
	 * @return
	 */
	private int getTableIndex(String vin) {
		int sum = 0;
		for (char c : vin.toCharArray()) {
			sum += c;
		}
		return sum % 256;
	}
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param keyword
	 * @param communityName
	 * @return
	 */
	public List<Transaction> findAll(int limit, int offset, String keyword, String communityName) {
		keyword = "%"+keyword+"%";
//		String sql = "SELECT * FROM \""+communityName+"\".CREATE_SEARCH(?,?,?)";
		String sql = "SELECT * FROM CREATE_SEARCH(?,?,?,?)";
//		System.out.println(sql+"  "+limit +"  "+ offset +"  "+ keyword +"  "+communityName);
		List<Transaction> results = jdbcTemplate.query(sql, new Object[] {limit, offset, keyword, communityName}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		return results;
	}
	
	/**
	 * 
	 * @param limit
	 * @param keyword
	 * @param communityName
	 * @return
	 */
	public int findPagingCount(int limit, String keyword, String communityName) {
		keyword = "%"+keyword+"%";
//		String sql = "SELECT * FROM \""+communityName+"\".CREATE_PAGINGCOUNT2(?,?)";
		String sql = "SELECT * FROM  CREATE_PAGINGCOUNT2(?,?,?)";
		int count = jdbcTemplate.queryForObject( sql, new Object[] {limit, keyword, communityName}, Integer.class);
		return count;
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
}
