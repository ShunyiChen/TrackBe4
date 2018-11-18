package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

import com.maxtree.automotive.dashboard.domain.Log;

/**
 * 
 * @author Chen
 *
 */
@Component
public class LoggingService {

	private static final Logger log = LoggerFactory.getLogger(LoggingService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param limit
	 * @param offset
	 * @param sortByColumn
	 * @param desc
	 * @return
	 */
	public List<Log> findByPaging(int limit, int offset, String sortByColumn, String desc) {
		String sql = "SELECT * FROM (SELECT * FROM SYSTEMLOG ORDER BY "+sortByColumn+" "+desc+") AS A LIMIT "+limit+" OFFSET "+offset+" ";
//		System.out.println("SELECT * FROM SYSTEMLOG ORDER BY "+sortByColumn+" DESC LIMIT "+limit+" OFFSET "+offset);
//		List<Log> results = jdbcTemplate.query(sql, new Object[] {sortByColumn,limit,offset}, new BeanPropertyRowMapper<Log>(Log.class));
//		for(Log l : results) {
//			System.out.println(l.toString());
//		}
		List<Log> lst = new ArrayList<Log>();
		Connection conn;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
//			PreparedStatement preparedStatement = conn.prepareStatement(sql);
//			preparedStatement.setString(1, sortByColumn);
//			preparedStatement.setInt(2, limit);
//			preparedStatement.setInt(3, offset);
		    Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int logUniqueId = rs.getInt("LOGUNIQUEID");
				String logType = rs.getString("LOGTYPE");
				String userName = rs.getString("USERNAME");
				String ipAddr = rs.getString("IPADDR");
				String module = rs.getString("MODULE");
				String message = rs.getString("MESSAGE");
				String exception = rs.getString("EXCEPTION");
				Date dateCreated = rs.getTimestamp("DATECREATED");
				Log log = new Log();
				log.setLogUniqueId(logUniqueId);
				log.setLogType(logType);
				log.setUserName(userName);
				log.setIpAddr(ipAddr);
				log.setModule(module);
				log.setMessage(message);
				log.setException(exception);
				log.setDateCreated(dateCreated);
				lst.add(log);
//				System.out.println(log.toString());
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lst;
	}
	
	/**
	 * 
	 * @param log
	 */
	public void insert(Log log) {
		String sql = "INSERT INTO SYSTEMLOG(LOGTYPE,USERNAME,IPADDR,MODULE,MESSAGE,EXCEPTION,DATECREATED) VALUES(?,?,?,?,?,?,?)";
	 	int opt = jdbcTemplate.update(sql, new Object[] {log.getLogType(),log.getUserName(),log.getIpAddr(),log.getModule(),log.getMessage(),log.getException(),log.getDateCreated()});
	}
	
	/**
	 * 
	 * @param rowsPerPage
	 * @return
	 */
	public int[] findPagingInfo(int rowsPerPage) {
		String sql = "SELECT COUNT(LOGUNIQUEID) FROM SYSTEMLOG";
		Integer totalRecords = jdbcTemplate.queryForObject(sql, new Object[] {}, Integer.class);
		int pageCount = 0;
		if(totalRecords <= rowsPerPage) {
			pageCount = 1;
		}
		else {
			pageCount = totalRecords/rowsPerPage+(totalRecords % rowsPerPage > 0?1:0);
		}
 
		return new int[] {totalRecords, pageCount};
	}
	
//	/**
//	 * 
//	 * @param userName
//	 * @return
//	 */
//	public List<Log> findByUserName(String userName) {
//		String sql = "SELECT * FROM LOG WHERE USERNAME=? ORDER BY ? DESC";
//		List<Log> results = jdbcTemplate.query(sql, new Object[] {userName, "FREQUENCY"}, new BeanPropertyRowMapper<Log>(Log.class));
//		if (results.size() > 0) {
//			return results;
//		}
//		return new ArrayList<Log>();
//	}
//	
//	/**
//	 * 
//	 * @param feedback
//	 * @return
//	 */
//	public int insert(Log feedback) {
//		String INSERT_SQL = "INSERT INTO LOG(USERNAME,SUGGESTION,FREQUENCY) VALUES(?,?,?)";
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				PreparedStatement ps = con.prepareStatement(
//						INSERT_SQL, new String[] {"feedbackuniqueid"});
//				ps.setString(1, feedback.getUserName());
//				ps.setString(2, feedback.getSuggestion());
//				ps.setInt(3, feedback.getFrequency());
//				return ps;
//			}
//		}, keyHolder);
//		int feedbackuniqueid  = keyHolder.getKey().intValue();
//		return feedbackuniqueid;
//	}
//	

//	
//	/**
//	 * 
//	 * @param LOGUNIQUEID
//	 */
//	public void delete(int LOGUNIQUEID) {
//		String sql = "DELETE FROM LOG WHERE LOGUNIQUEID=?";
//		jdbcTemplate.update(sql, new Object[] {LOGUNIQUEID});
//	}
//	
//	/**
//	 * Go up
//	 * @param feedback
//	 */
//	public void up(Log feedback) {
//		String sql = "UPDATE LOG SET FREQUENCY=FREQUENCY+? WHERE LOGUNIQUEID=?";
//		jdbcTemplate.update(sql, new Object[] {1,feedback.getLogUniqueId()});
//	}
}
