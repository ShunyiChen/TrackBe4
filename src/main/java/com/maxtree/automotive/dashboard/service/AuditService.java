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

import com.maxtree.automotive.dashboard.domain.Audit;

@Component
public class AuditService {

	private static final Logger log = LoggerFactory.getLogger(AuditService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param audit
	 */
	public void createAudit(Audit audit) {
		String INSERT_TRANS_SQL = "INSERT INTO AUDITS(TRANSACTIONUNIQUEID,AUDITOR,AUDITRESULTS,AUDITDATE) VALUES(?,?,?,?)";
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] {"audituniqueid"});
				ps.setInt(1, audit.getTransactionUniqueId());
				ps.setInt(2, audit.getAuditor());
				ps.setString(3, audit.getAuditResults());
				long millis = audit.getAuditDate().getTime();////System.currentTimeMillis();
				java.sql.Timestamp date = new java.sql.Timestamp(millis);
				ps.setTimestamp(4, date);
				return ps;
			}
		}, keyHolder);
		int audituniqueid = keyHolder.getKey().intValue();
	}
	
	/**
	 * 
	 * @param transactionUniqueId
	 * @return
	 */
	public Audit findLastAuditByTransID(int transactionUniqueId) {
		String sql = "SELECT B.FIRSTNAME AS AUDITORFIRSTNAME,B.LASTNAME AS AUDITORLASTNAME,A.* FROM AUDITS AS A LEFT JOIN USERPROFILES AS B ON A.AUDITOR=B.USERUNIQUEID WHERE A.TRANSACTIONUNIQUEID=? ORDER BY A.AUDITDATE DESC LIMIT ?";
		List<Audit> result = jdbcTemplate.query(sql, new Object[] {transactionUniqueId, 1}, new BeanPropertyRowMapper<Audit>(Audit.class));
		if (result.size() > 0) {
			return result.get(0);
		}
		return new Audit();
	}
}
