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

import com.maxtree.automotive.dashboard.domain.EmbeddedServer;


@Component
public class EmbeddedServerService {

	private static final Logger log = LoggerFactory.getLogger(EmbeddedServerService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param serverUniqueId
	 * @return
	 */
	public EmbeddedServer findById(int serverUniqueId) {
		String sql = "SELECT * FROM EMBEDDEDSERVER WHERE SERVERUNIQUEID=?";
		List<EmbeddedServer> results = jdbcTemplate.query(sql, new Object[] {serverUniqueId}, new BeanPropertyRowMapper<EmbeddedServer>(EmbeddedServer.class));
		if(results.size() > 0) {
			return results.get(0);
		}
		return new EmbeddedServer();
	}
	
	/**
	 * 
	 * @return
	 */
	public List<EmbeddedServer> findAll() {
		String sql = "SELECT * FROM EMBEDDEDSERVER ORDER BY SERVERUNIQUEID";
		List<EmbeddedServer> results = jdbcTemplate.query(sql, new Object[] {}, new BeanPropertyRowMapper<EmbeddedServer>(EmbeddedServer.class));
		return results;
	}
	
	
	/**
	 * 
	 * @param embeddedServer
	 * @return
	 */
	public int insert(EmbeddedServer embeddedServer) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO EMBEDDEDSERVER(SERVERNAME,SERVERTYPE,HOSTADDR,PORT,DEFAULTREMOTEDIRECTORY,USERNAME,PASSWORD,RUNNINGSTATUS,CODE) VALUES(?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "serveruniqueid" });
				ps.setString(1, embeddedServer.getServerName());
				ps.setString(2, embeddedServer.getServerType());
				ps.setString(3, embeddedServer.getHostAddr());
				ps.setInt(4, embeddedServer.getPort());
				ps.setString(5, embeddedServer.getDefaultRemoteDirectory());
				ps.setString(6, embeddedServer.getUserName());
				ps.setString(7, embeddedServer.getPassword());
				ps.setInt(8, embeddedServer.getRunningStatus());
				ps.setString(9, embeddedServer.getCode());
				return ps;
			}
		}, keyHolder);
		int serveruniqueid = keyHolder.getKey().intValue();
		return serveruniqueid;
	}
	
	/**
	 * 
	 * @param embeddedServer
	 */
	public void update(EmbeddedServer embeddedServer) {
		String sql = "UPDATE EMBEDDEDSERVER SET SERVERNAME=?,SERVERTYPE=?,HOSTADDR=?,PORT=?,DEFAULTREMOTEDIRECTORY=?,USERNAME=?,PASSWORD=?,RUNNINGSTATUS=?,CODE=? WHERE SERVERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] { embeddedServer.getServerName(), embeddedServer.getServerType(),
				embeddedServer.getHostAddr(), embeddedServer.getPort(), embeddedServer.getDefaultRemoteDirectory(),
				embeddedServer.getUserName(), embeddedServer.getPassword(), embeddedServer.getRunningStatus(),
				embeddedServer.getCode(), embeddedServer.getServerUniqueId() });
	}
	
	/**
	 * 
	 * @param serverUniqueId
	 */
	public void delete(int serverUniqueId) {
		String sql = "DELETE FROM EMBEDDEDSERVER WHERE SERVERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {serverUniqueId});
	}
}
