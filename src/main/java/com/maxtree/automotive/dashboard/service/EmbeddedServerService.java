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
import com.maxtree.automotive.dashboard.domain.ServerUser;


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
		String INSERT_TRANS_SQL = "INSERT INTO EMBEDDEDSERVER(SERVERNAME,SERVERTYPE,PORT,RUNNINGSTATUS,CODE) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "serveruniqueid" });
				ps.setString(1, embeddedServer.getServerName());
				ps.setString(2, embeddedServer.getServerType());
				ps.setInt(3, embeddedServer.getPort());
				ps.setInt(4, embeddedServer.getRunningStatus());
				ps.setString(5, embeddedServer.getCode());
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
		String sql = "UPDATE EMBEDDEDSERVER SET SERVERNAME=?,SERVERTYPE=?,PORT=?,RUNNINGSTATUS=?,CODE=? WHERE SERVERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {
				embeddedServer.getServerName(),
				embeddedServer.getServerType(),
				embeddedServer.getPort(),
				embeddedServer.getRunningStatus(),
				embeddedServer.getCode(),
				embeddedServer.getServerUniqueId()
		});
	}
	
	/**
	 * 
	 * @param serverUniqueId
	 */
	public void delete(int serverUniqueId) {
		String sql = "DELETE FROM EMBEDDEDSERVER WHERE SERVERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {serverUniqueId});
	}
	
	/**
	 * 
	 * @param serverCode
	 * @return
	 */
	public List<ServerUser> findServerUsers(String serverCode) {
		String sql = "SELECT * FROM SERVERUSER WHERE SERVERCODE=?";
		List<ServerUser> results = jdbcTemplate.query(sql, new Object[] {serverCode}, new BeanPropertyRowMapper<ServerUser>(ServerUser.class));
		return results;
	}
	
	/**
	 * 
	 * @param serverUser
	 * @return
	 */
	public int insertUser(ServerUser serverUser) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO SERVERUSER(USERNAME,USERPASSWORD,ENABLEFLAG,WRITEPERMISSION,MAXLOGINNUMBER,MAXLOGINPERIP,IDLETIME,UPLOADRATE,DOWNLOADRATE,SERVERCODE) VALUES(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "serveruseruniqueid" });
				ps.setString(1, serverUser.getUserName());
				ps.setString(2, serverUser.getUserPassword());
				ps.setBoolean(3, serverUser.getEnableflag());
				ps.setBoolean(4, serverUser.getWritepermission());
				ps.setInt(5, serverUser.getMaxloginnumber());
				ps.setInt(6, serverUser.getMaxloginperip());
				ps.setInt(7, serverUser.getIdletime());
				ps.setInt(8, serverUser.getUploadrate());
				ps.setInt(9, serverUser.getDownloadrate());
				ps.setString(10, serverUser.getServerCode());
				return ps;
			}
		}, keyHolder);
		int serveruseruniqueid = keyHolder.getKey().intValue();
		return serveruseruniqueid;
	}
	
	/**
	 * 
	 * @param user
	 */
	public void updateUser(ServerUser user) {
		String sql = "UPDATE SERVERUSER SET USERNAME=?,USERPASSWORD=?,ENABLEFLAG=?,WRITEPERMISSION=?,MAXLOGINNUMBER=?,MAXLOGINPERIP=?,IDLETIME=?,UPLOADRATE=?,DOWNLOADRATE=? WHERE SERVERUSERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {
				user.getUserName(),
				user.getUserPassword(),
				user.getEnableflag(),
				user.getWritepermission(),
				user.getMaxloginnumber(),
				user.getMaxloginperip(),
				user.getIdletime(),
				user.getUploadrate(),
				user.getDownloadrate(),
				user.getServerUserUniqueId()
		});
	}
	
	/**
	 * 
	 * @param serveruseruniqueid
	 */
	public void deleteUser(int serveruseruniqueid) {
		String sql = "DELETE FROM SERVERUSER WHERE SERVERUSERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {serveruseruniqueid});
	}
}
