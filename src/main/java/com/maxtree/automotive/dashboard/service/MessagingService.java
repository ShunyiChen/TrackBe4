package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.MessageRecipient;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.ReminderFrequency;
import com.maxtree.automotive.dashboard.domain.User;

@Component
public class MessagingService {

	private static final Logger log = LoggerFactory.getLogger(MessagingService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param message
	 * @return
	 */
	public int insertMessage(Message message) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO MESSAGES(SUBJECT,CONTENT,MATEDATA,CREATORUNIQUEID,DATECREATED,SENTTIMES,REMINDERFREQUENCYID,DELETED,POPUPAUTO) VALUES(?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						sql, new String[] {"messageuniqueid"});
				ps.setString(1, message.getSubject());
				ps.setString(2, message.getContent());
				ps.setString(3, message.getMatedata());
				ps.setInt(4, message.getCreatorUniqueId());
				long millis = message.getDateCreated().getTime();
				ps.setTimestamp(5, new Timestamp(millis));
				ps.setInt(6, message.getSentTimes());
				ps.setInt(7, message.getReminderFrequencyId());
				ps.setInt(8, message.getDeleted());
				ps.setBoolean(9, message.getPopupAuto());
				return ps;
			}
			
		}, keyHolder);
		int messageuniqueid  = keyHolder.getKey().intValue(); 
		return messageuniqueid;
	}
	
	/**
	 * 
	 * @param recipients
	 */
	public void insertMessageRecipients(List<MessageRecipient> recipients) {
		final String inserQuery = "INSERT INTO MESSAGERECIPIENT(RECIPIENTUNIQUEID,RECIPIENTNAME,RECIPIENTTYPE,MESSAGEUNIQUEID) VALUES(?,?,?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				MessageRecipient recipient = recipients.get(i);
				ps.setInt(1, recipient.getRecipientUniqueId());
				ps.setString(2, recipient.getRecipientName());
				ps.setInt(3, recipient.getRecipientType());
				ps.setInt(4, recipient.getMessageUniqueId());
			}

			@Override
			public int getBatchSize() {
				return recipients.size();
			}
		});
		log.info("InsertMessageRecipients has done.");
	}
	
	/**
	 * 
	 * @param frequencry
	 * @return
	 */
	public int insertReminderFrequency(ReminderFrequency frequencry) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO REMINDERFREQUENCY(NAME,FREQUENCY,ENABLED,STARTINGTIME,ENDINGTIME) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						sql, new String[] {"frequencyuniqueid"});
				ps.setString(1, frequencry.getName());
				ps.setInt(2, frequencry.getFrequency());
				ps.setInt(3, frequencry.getEnabled());
				long start = frequencry.getStartingTime().getTime();
				ps.setTimestamp(4, new Timestamp(start));
				long end = frequencry.getEndingTime().getTime();
				ps.setTimestamp(5, new Timestamp(end));
				return ps;
			}
			
		}, keyHolder);
		int frequencyuniqueid  = keyHolder.getKey().intValue(); 
		return frequencyuniqueid;
	}
	
	/**
	 * 
	 * @param notificationsList
	 */
	public void insertNotifications(List<Notification> notificationsList) {
		final String inserQuery = "INSERT INTO NOTIFICATIONS(MESSAGEUNIQUEID,WARNING,USERUNIQUEID,VIEWNAME,SENDTIME,MARKEDASREAD) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Notification n = notificationsList.get(i);
				ps.setInt(1, n.getMessageUniqueId());
				ps.setBoolean(2, n.isWarning());
				ps.setInt(3, n.getUserUniqueId());
				ps.setString(4, n.getViewName());
				long millis = n.getSendTime().getTime();
				ps.setTimestamp(5, new Timestamp(millis));
				ps.setBoolean(6, n.isMarkedAsRead());
			}

			@Override
			public int getBatchSize() {
				return notificationsList.size();
			}
		});
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param userUniqueId
	 */
	public void markAsRead(int messageUniqueId, int userUniqueId) {
		String sql = "UPDATE NOTIFICATIONS SET MARKEDASREAD=? WHERE MESSAGEUNIQUEID=? AND USERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {true, messageUniqueId, userUniqueId});
	}
	
	/**
	 * 
	 * @param userUniqueId
	 */
	public void allMarkAsRead(int userUniqueId) {
		String sql = "UPDATE NOTIFICATIONS SET MARKEDASREAD=? WHERE USERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {true, userUniqueId});
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param recipientUniqueId
	 */
	public void deleteMessageRecipient(int messageUniqueId, int recipientUniqueId) {
		String sql = "DELETE FROM MESSAGERECIPIENT WHERE MESSAGEUNIQUEID=? AND RECIPIENTUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {messageUniqueId, recipientUniqueId});
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param userUniqueId
	 */
	public void deleteNotifications(int messageUniqueId, int userUniqueId) {
		String sql = "DELETE FROM NOTIFICATIONS WHERE MESSAGEUNIQUEID=? AND USERUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {messageUniqueId,userUniqueId});
	}
	
	/**
	 * 
	 * @param user
	 * @param viewName
	 * @return
	 */
	public List<Map<String, Object>> findAllMessagesByUser(User user, String viewName) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT B.*,A.MARKEDASREAD,A.VIEWNAME,C.USERNAME,D.PICTURE");
		sql.append(" FROM NOTIFICATIONS AS A ");
		sql.append("  LEFT JOIN MESSAGES AS B ON A.MESSAGEUNIQUEID=B.MESSAGEUNIQUEID ");
		sql.append("  LEFT JOIN USERS AS C ON C.USERUNIQUEID=B.CREATORUNIQUEID ");
		sql.append("  LEFT JOIN USERPROFILES AS D ON D.USERUNIQUEID=C.USERUNIQUEID WHERE A.USERUNIQUEID=? AND A.VIEWNAME IN('',?)ORDER BY B.MESSAGEUNIQUEID DESC ");
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString(), new Object[] {user.getUserUniqueId(), viewName});
		return rows;
	}
	
	/**
	 * 
	 * @param frequencyUniqueId
	 * @return
	 */
	public ReminderFrequency findByFrequencyId(int frequencyUniqueId) {
		String sql = "SELECT * FROM REMINDERFREQUENCY WHERE FREQUENCYUNIQUEID=?";
		List<ReminderFrequency> results = jdbcTemplate.query(sql, new Object[] {frequencyUniqueId},new BeanPropertyRowMapper<ReminderFrequency>(ReminderFrequency.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new ReminderFrequency();
	} 
	
	/**
	 * 
	 * @param userUniqueId
	 * @return
	 */
	public List<Message> findAll(int userUniqueId) {
		StringBuilder complexSQL = new StringBuilder();
		complexSQL.append("SELECT A.*,");
		complexSQL.append("CASE COUNT(B.MARKEDASREAD) ");
		complexSQL.append("WHEN 0 THEN (0)");
		complexSQL.append(" ELSE (ROUND(CAST((SELECT COUNT(*) FROM NOTIFICATIONS WHERE MARKEDASREAD=? AND MESSAGEUNIQUEID=A.MESSAGEUNIQUEID) AS NUMERIC) /CAST(COUNT(B.MARKEDASREAD) AS NUMERIC ) * 100,2))");
		complexSQL.append(" END AS READRATE ");
		complexSQL.append(" FROM MESSAGES AS A LEFT JOIN NOTIFICATIONS AS B ON A.MESSAGEUNIQUEID=B.MESSAGEUNIQUEID");
		complexSQL.append(" GROUP BY A.MESSAGEUNIQUEID HAVING A.CREATORUNIQUEID=? AND A.DELETED=0 ORDER BY MESSAGEUNIQUEID DESC");
		List<Message> results = jdbcTemplate.query(complexSQL.toString(), new Object[] {true,userUniqueId}, new BeanPropertyRowMapper<Message>(Message.class));
		
		return results;
	}
	
	/**
	 * 
	 * @param userUniqueId
	 * @param onlyUnread
	 * @param viewName
	 * @return
	 */
	public List<Notification> findAllNotifications(int userUniqueId, boolean onlyUnread,String viewName) {
		String condition1 = "";
		if(onlyUnread) {
			condition1 = " A.MARKEDASREAD=FALSE AND ";
		}
		String condition2 = "";
		if(!StringUtils.isEmpty(viewName)) {
			condition2 = " A.VIEWNAME='"+viewName+"' AND ";
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.*,B.SUBJECT AS SUBJECT,C.USERNAME AS USERNAME,D.PICTURE");
		sql.append(" FROM NOTIFICATIONS AS A ");
		sql.append("  LEFT JOIN MESSAGES AS B ON A.MESSAGEUNIQUEID=B.MESSAGEUNIQUEID ");
		sql.append("  LEFT JOIN USERS AS C ON C.USERUNIQUEID=B.CREATORUNIQUEID ");
		sql.append("  LEFT JOIN USERPROFILES AS D ON D.USERUNIQUEID=C.USERUNIQUEID WHERE "+condition1+condition2+" A.USERUNIQUEID=? ORDER BY B.MESSAGEUNIQUEID DESC ");
		List<Notification> results = jdbcTemplate.query(sql.toString(), new Object[] {userUniqueId}, new BeanPropertyRowMapper<Notification>(Notification.class));
		return results;
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @return
	 */
	public Message findById(int messageUniqueId) {
		String sql = "SELECT * FROM MESSAGES WHERE MESSAGEUNIQUEID=?";
		List<Message> results = jdbcTemplate.query(sql, new Object[] {messageUniqueId},new BeanPropertyRowMapper<Message>(Message.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Message();
	} 
	
	/**
	 * 
	 * @param messageUniqueId
	 */
	public void markAsDeleted(int messageUniqueId) {
		String sql = "UPDATE MESSAGES SET DELETED=? WHERE MESSAGEUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {1, messageUniqueId});
		
		Message msg = findById(messageUniqueId);
		sql = "DELETE FROM REMINDERFREQUENCY WHERE FREQUENCYUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {msg.getReminderFrequencyId()});
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 */
	public void permanentlyDeleteMessage(int messageUniqueId) {
		String sql = "DELETE FROM MESSAGES WHERE MESSAGEUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {messageUniqueId});
	}
	
	/**
	 * 
	 * @param message
	 */
	public void updateMessage(Message message) {
		String sql = "UPDATE MESSAGES SET SUBJECT=?,CONTENT=?,DATECREATED=?,SENTTIMES=?,REMINDERFREQUENCYID=? WHERE MESSAGEUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {
				message.getSubject(),
				message.getContent(),
				message.getDateCreated(),
				message.getSentTimes(),
				message.getReminderFrequencyId(),
				message.getMessageUniqueId()});
	}
	
	/**
	 * 
	 * @param frequencry
	 * @return
	 */
	public void updateReminderFrequency(ReminderFrequency frequencry) {
		String sql = "UPDATE REMINDERFREQUENCY SET NAME=?,FREQUENCY=?,ENABLED=?,STARTINGTIME=?,ENDINGTIME=?,NEXTREMINDERTIME=? WHERE FREQUENCYUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {
				frequencry.getName(),
				frequencry.getFrequency(),
				frequencry.getEnabled(), 
				frequencry.getStartingTime(),
				frequencry.getEndingTime(),
				frequencry.getFrequencyUniqueId()});
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @return
	 */
	public List<MessageRecipient> findRecipientsByMessageId(int messageUniqueId) {
		String sql = "SELECT * FROM MESSAGERECIPIENT WHERE MESSAGEUNIQUEID=?";
		List<MessageRecipient> results = jdbcTemplate.query(sql, new Object[] {messageUniqueId},new BeanPropertyRowMapper<MessageRecipient>(MessageRecipient.class));
		return results;
	}
}
