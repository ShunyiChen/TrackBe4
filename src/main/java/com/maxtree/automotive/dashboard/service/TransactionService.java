package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.Status;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.maxtree.automotive.dashboard.view.search.CriterionModel;
import com.maxtree.automotive.dashboard.view.search.SearchModel;

@Component
public class TransactionService {

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

//	public Connection getConnection() throws DataException {
//		try {
//			return jdbcTemplate.getDataSource().getConnection();
//		} catch (SQLException e) {
//			throw new DataException(e.getMessage());
//		}
//	}
	
	/**
	 * 
	 * @param plateType
	 * @param vin
	 * @param plateNumber
	 * @return
	 */
	public Transaction find(String plateType, String vin, String plateNumber) {
		Transaction trans = null;
		String sql = "SELECT * FROM TRANSACTION WHERE PLATETYPE=? AND VIN=? AND PLATENUMBER=? AND STATUS=?";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {plateType,vin,plateNumber,Status.S3.name}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		if (result.size() > 0) {
			trans = result.get(0);
		}
		return trans;
	}
	
	/**
	 * 
	 * @param barCode
	 * @return
	 */
	public List<Transaction> findAllByBarCode(String barCode) {
		String sql = "SELECT * FROM TRANSACTION WHERE BARCODE=? ORDER BY TRANSACTIONUNIQUEID DESC";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {barCode}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		return result;
	}
	
	/**
	 * 
	 * @param transactionUniqueId
	 * @return
	 */
	public Transaction findById(int transactionUniqueId) {
		Transaction trans = null;
		String sql = "SELECT * FROM TRANSACTION WHERE TRANSACTIONUNIQUEID = ?";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {transactionUniqueId}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
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
		String sql = "SELECT * FROM TRANSACTION WHERE VIN = ? ORDER BY TRANSACTIONUNIQUEID";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {vin}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		return result;
	}
	
	/**
	 * 
	 * @param transaction
	 * @return
	 */
	public int create(Transaction transaction) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO TRANSACTION(BARCODE,PLATETYPE,PLATENUMBER,VIN,DATECREATED,STATUS,SITEUNIQUEID,BUSINESSUNIQUEID,COMMUNITYUNIQUEID,COMPANYUNIQUEID,PROVINCE,CITY,PREFECTURE,DISTRICT,DATEMODIFIED,UUID,TYPIST,INDEXNUMBER) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				ps.setInt(8, transaction.getBusinessUniqueId());
				ps.setInt(9, transaction.getCommunityUniqueId());
				ps.setInt(10, transaction.getCompanyUniqueId());
				ps.setString(11, transaction.getProvince());
				ps.setString(12, transaction.getCity());
				ps.setString(13, transaction.getPrefecture());
				ps.setString(14, transaction.getDistrict());
				java.sql.Timestamp modifyDate = new java.sql.Timestamp(transaction.getDateModified().getTime());
				ps.setTimestamp(15, modifyDate);
				ps.setString(16, transaction.getUuid());
				ps.setInt(17, transaction.getTypist());
				ps.setInt(18, transaction.getIndexNumber());
				return ps;
			}
		}, keyHolder);
		int transactionUniqueId = keyHolder.getKey().intValue();
		
		log.info("Created transactionId = "+transactionUniqueId);
		
		return transactionUniqueId;
	}
	
	/**
	 * 更改transaction
	 * 
	 * @param transaction
	 */
	public void update(Transaction transaction) {
		String sql = "UPDATE TRANSACTION SET BARCODE=?,PLATETYPE=?,PLATENUMBER=?,VIN=?,STATUS=?,DATEMODIFIED=?,BUSINESSUNIQUEID=?,UUID=?,CODE=?,INDEXNUMBER=? WHERE TRANSACTIONUNIQUEID=?";
	 	int affected = jdbcTemplate.update(sql, new Object[] {transaction.getBarcode(), transaction.getPlateType(), transaction.getPlateNumber(), 
	 			transaction.getVin(), transaction.getStatus(),transaction.getDateModified(), transaction.getBusinessUniqueId(), 
	 			transaction.getUuid(), transaction.getCode(), transaction.getIndexNumber(), transaction.getTransactionUniqueId()});
	 	log.info("Updated.Affected row = "+affected);
	}
	
	/**
	 * 
	 * @param vin
	 * @return
	 */
	public int findIndexByVIN(String vin) {
		String sql = "SELECT COUNT(*) FROM TRANSACTION WHERE VIN=?";
		int count = jdbcTemplate.queryForObject( sql, new Object[] {vin}, Integer.class);
		return count;
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 * @throws DataException
	 */
	public List<Transaction> advancedSearch(SearchModel model) throws DataException {
		
		List<String> insertValues = new ArrayList<>();
		StringBuilder ALLSQL = new StringBuilder();
		// With all of the words
		if (!model.getWithAllOfTheWords().trim().equals("")) {
			String[] words = model.getWithAllOfTheWords().split(" ");
			StringBuilder SQL = new StringBuilder("SELECT * FROM TRANSACTION");
			SQL.append(" WHERE ");
			for (String word : words) {
				SQL.append("(");
				SQL.append("BARCODE LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("PLATETYPE LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("PLATENUMBER LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("VIN LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("STATUS LIKE ? ");
				insertValues.add("%"+word+"%");
				SQL.append(")");
				
				SQL.append(" AND ");
			}
			
			if (SQL.toString().endsWith(" AND ")) {
				SQL.delete(SQL.length() - 5, SQL.length());
			}
			
			StringBuilder criterionsSQL = generateCriterionsSQL(model, insertValues);
			SQL.append(criterionsSQL);
			
			ALLSQL.append(SQL);
		}
		// With the exact phrase
		if (!model.getWithTheExactPhrase().trim().equals("")) {
			
			ALLSQL.append(" UNION ");
			
			String[] words = model.getWithTheExactPhrase().split(" ");
			StringBuilder SQL = new StringBuilder("SELECT * FROM TRANSACTION");
			SQL.append(" WHERE ");
			for (String word : words) {
				SQL.append("(");
				SQL.append("BARCODE = ? OR ");
				insertValues.add(word);
				SQL.append("PLATETYPE = ? OR ");
				insertValues.add(word);
				SQL.append("PLATENUMBER = ? OR ");
				insertValues.add(word);
				SQL.append("VIN = ? OR ");
				insertValues.add(word);
				SQL.append("STATUS = ?");
				insertValues.add(word);
				SQL.append(")");
				
				SQL.append(" AND ");
			}
			
			if (SQL.toString().endsWith(" AND ")) {
				SQL.delete(SQL.length() - 5, SQL.length());
			}
			
			StringBuilder criterionsSQL = generateCriterionsSQL(model, insertValues);
			SQL.append(criterionsSQL);
			
			ALLSQL.append(SQL);
		}
		// With at least one of the words
		if (!model.getWithAtLeastOneOfTheWords().trim().equals("")) {
			
			ALLSQL.append(" UNION ");
			
			String[] words = model.getWithAtLeastOneOfTheWords().split(" ");
			StringBuilder SQL = new StringBuilder("SELECT * FROM TRANSACTION");
			SQL.append(" WHERE ");
			for (String word : words) {
				SQL.append("(");
				SQL.append("BARCODE LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("PLATETYPE LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("PLATENUMBER LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("VIN LIKE ? OR ");
				insertValues.add("%"+word+"%");
				SQL.append("STATUS LIKE ?");
				insertValues.add("%"+word+"%");
				SQL.append(")");
				
				SQL.append(" OR ");
			}
			
			if (SQL.toString().endsWith(" OR ")) {
				SQL.delete(SQL.length() - 4, SQL.length());
			}
			
			StringBuilder criterionsSQL = generateCriterionsSQL(model, insertValues);
			SQL.append(criterionsSQL);
			
			ALLSQL.append(SQL);
		}
		// Without the words
		if (!model.getWithoutTheWords().trim().equals("")) {
			
			ALLSQL.append(" UNION ");
			
			String[] words = model.getWithoutTheWords().split(" ");
			StringBuilder SQL = new StringBuilder("SELECT * FROM TRANSACTION");
			SQL.append(" WHERE ");
			for (String word : words) {
				SQL.append("(");
				SQL.append("BARCODE NOT LIKE ? AND ");
				insertValues.add("%"+word+"%");
				SQL.append("PLATETYPE NOT LIKE ? AND ");
				insertValues.add("%"+word+"%");
				SQL.append("PLATENUMBER NOT LIKE ? AND ");
				insertValues.add("%"+word+"%");
				SQL.append("VIN NOT LIKE ? AND ");
				insertValues.add("%"+word+"%");
				SQL.append("STATUS NOT LIKE ?");
				insertValues.add("%"+word+"%");
				SQL.append(")");
				
				SQL.append(" AND ");
			}
			if (SQL.toString().endsWith(" AND ")) {
				SQL.delete(SQL.length() - 5, SQL.length());
			}
			
			StringBuilder criterionsSQL = generateCriterionsSQL(model, insertValues);
			SQL.append(criterionsSQL);
			
			ALLSQL.append(SQL);
		}
		if (ALLSQL.toString().startsWith(" UNION")) {
			ALLSQL.delete(0, 6);
		}
		
		if (ALLSQL.toString().trim().equals("")) {
			
			if (model.getWithAllOfTheWords().trim().equals("") 
					&& model.getWithTheExactPhrase().trim().equals("")
					&& model.getWithAtLeastOneOfTheWords().trim().equals("")
					&& model.getWithoutTheWords().trim().equals("")) {
				ALLSQL.append(" SELECT * FROM TRANSACTION WHERE ");
				StringBuilder criterionsSQL = generateCriterionsSQL(model, insertValues);
				if (criterionsSQL.toString().startsWith("AND")) {
					criterionsSQL.delete(0, 3);
				}
				ALLSQL.append(criterionsSQL);
				
				if (ALLSQL.toString().endsWith("WHERE ")) {
					ALLSQL.delete(ALLSQL.length() - 6, ALLSQL.length());
				}
			}
		}
		
		Object[] params = insertValues.toArray(new Object[insertValues.size()]);
		List<Transaction> result = jdbcTemplate.query(ALLSQL.toString(), params, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		
		return result;
	}
	
	
	private StringBuilder generateCriterionsSQL(SearchModel model, List<String> insertValues) {
		StringBuilder SUBSQL = new StringBuilder();
		SUBSQL.append("AND (");
		List<CriterionModel> models = model.getLstCriterions();
		int index = 0;
		for (CriterionModel cm : models) {
			if (cm.getKey()== null || cm.getMatching()==null) {
				continue;
			}
			
			// 除最后一组条件，其余都需要判断连接符是否为null
			if (index != (models.size() - 1)) {
				if(cm.getConnector() == null) {
					continue;
				}
			}
		    index++;
			
			
			SUBSQL.append(mappingColumnName(cm.getKey()));	
			if (cm.getMatching().equals("包含")) {
				SUBSQL.append(" LIKE ?");
				insertValues.add("%"+cm.getValue()+"%");
			} else if (cm.getMatching().equals("不包含")) {
				SUBSQL.append(" NOT LIKE ?");
				insertValues.add("%"+cm.getValue()+"%");
			} else if (cm.getMatching().equals("等于")) {
				SUBSQL.append(" = ? ");
				insertValues.add(cm.getValue());
			} else if (cm.getMatching().equals("不等于")) {
				SUBSQL.append(" <> ? ");
				insertValues.add(cm.getValue());
			} else if (cm.getMatching().equals("大于")) {
				SUBSQL.append(" > ? ");
				insertValues.add(cm.getValue());
			} else if (cm.getMatching().equals("小于")) {
				SUBSQL.append(" < ? ");
				insertValues.add(cm.getValue());
			} 
			
			if (cm.getConnector().equals("并且")) {
				SUBSQL.append(" AND ");
			} else if (cm.getConnector().equals("或者")) {
				SUBSQL.append(" OR ");
			}
		}
		if (SUBSQL.toString().endsWith("AND ")) {
			SUBSQL.delete(SUBSQL.length() - 4, SUBSQL.length());
		} else if (SUBSQL.toString().endsWith("OR ")) {
			SUBSQL.delete(SUBSQL.length() - 3, SUBSQL.length());
		}
		SUBSQL.append(")");
		if (SUBSQL.toString().endsWith("()")) { 
			SUBSQL = new StringBuilder("");
		}
		return SUBSQL;
	}
	
	private String mappingColumnName(String key) {
		if (key.equals("条形码")) {
			return "BARCODE";
		} else if (key.equals("号牌种类")) {
			return "PLATETYPE";
		} else if (key.equals("号码号牌")) {
			return "PLATENUMBER";
		} else if (key.equals("车辆识别代码")) {
			return "VIN";
		} else if (key.equals("业务状态")) {
			return "STATUS";
		} else if (key.equals("创建日期")) {
			return "DATECREATED";
		} else if (key.equals("最后修改日期")) {
			return "DATEMODIFIED";
		} else if (key.equals("办结日期")) {
			return "DATEFINISHED";
		}
		return "";
	}
	
	/**
	 * 
	 * @param from
	 * @param perSize
	 * @param communityName
	 * @param plate
	 * @return
	 */
	public List<Transaction> executeBasicSearch(int from, int perSize, String communityName, String plate) {
		String sql = "SELECT * FROM "+communityName+".TRANSACTION WHERE PLATENUMBER=? LIMIT ? OFFSET ?";
		List<Transaction> result = jdbcTemplate.query(sql, new Object[] {plate, perSize, from}, new BeanPropertyRowMapper<Transaction>(Transaction.class));
		return result;
	}
	
}
