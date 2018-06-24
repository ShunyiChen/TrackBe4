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

import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.MessageRecipient;
import com.maxtree.automotive.dashboard.domain.ReminderFrequency;
import com.maxtree.automotive.dashboard.domain.SendDetails;
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
		String sql = "INSERT INTO MESSAGES(SUBJECT,MESSAGEBODY,CREATORUNIQUEID,DATECREATED,SENTTIMES,REMINDERFREQUENCYID,DELETED) VALUES(?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						sql, new String[] {"messageuniqueid"});
				ps.setString(1, message.getSubject());
				ps.setString(2, message.getMessageBody());
				ps.setInt(3, message.getCreatorUniqueId());
				long millis = System.currentTimeMillis();
				ps.setTimestamp(4, new Timestamp(millis));
				ps.setInt(5, message.getSentTimes());
				ps.setInt(6, message.getReminderFrequencyId());
				ps.setInt(7, message.getDeleted());
//				long millis = System.currentTimeMillis();
//				ps.setTimestamp(4, new Timestamp(millis));
//				ps.setInt(5, message.getParentMessageUniqueId());
//				long millis2 = message.getExpiryDate().getTime();
//				ps.setTimestamp(6, new Timestamp(millis2));
//				ps.setInt(7, message.getReminder());
//				long millis3 = message.getNextRemindDate().getTime();
//				ps.setTimestamp(8, new Timestamp(millis3));
//				ps.setInt(9, message.getReminderFrequencyId());
//				ps.setString(10, message.getRecipientName());
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
	 * @param detailsList
	 */
	public void insertSendDetails(List<SendDetails> detailsList) {
		final String inserQuery = "INSERT INTO SENDDETAILS(RECIPIENTUNIQUEID,MESSAGEUNIQUEID,MARKEDASREAD,VIEWNAME) VALUES(?,?,?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				SendDetails details = detailsList.get(i);
				ps.setInt(1, details.getRecipientUniqueId());
				ps.setInt(2, details.getMessageUniqueId());
				ps.setInt(3, details.getMarkedAsRead());
				ps.setString(4, details.getViewName());
			}

			@Override
			public int getBatchSize() {
				return detailsList.size();
			}
		});
		log.info("InsertSendDetails has done.");
	}
	
	/**
	 * 
	 * @param messageUniqueId
	 * @param recipientUniqueId
	 */
	public void markAsRead(int messageUniqueId, int recipientUniqueId) {
		String sql = "UPDATE MESSAGERECIPIENT SET MARKEDASREAD=? WHERE MESSAGEUNIQUEID=? AND RECIPIENTUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {1, messageUniqueId, recipientUniqueId});
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
	 * @param user
	 * @param viewName
	 * @return
	 */
	public List<Map<String, Object>> findAllMessagesByUser(User user, String viewName) {
		String sql = "SELECT B.*,A.MARKEDASREAD,A.VIEWNAME,C.USERNAME,D.PICTURE FROM MESSAGERECIPIENT AS A LEFT JOIN MESSAGES AS B ON A.MESSAGEUNIQUEID=B.MESSAGEUNIQUEID LEFT JOIN USERS AS C ON C.USERUNIQUEID=B.CREATORUNIQUEID LEFT JOIN USERPROFILES AS D ON D.USERUNIQUEID=C.USERUNIQUEID WHERE A.RECIPIENTUNIQUEID=? AND A.VIEWNAME IN('', ?) ORDER BY B.MESSAGEUNIQUEID DESC";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] {user.getUserUniqueId(), viewName});
		return rows;
	}
	
	/**
	 * 
	 * @param user
	 * @param viewName
	 * @return
	 */
	public int getUnreadCount(User user, String viewName) {
		String sql = "SELECT COUNT(*) FROM MESSAGERECIPIENT WHERE RECIPIENTUNIQUEID=? AND MARKEDASREAD=? AND VIEWNAME=?";
		int count = jdbcTemplate.queryForObject(
                sql, new Object[] {user.getUserUniqueId(),0,viewName}, Integer.class);
		return count;
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
		complexSQL.append(" ELSE (ROUND(CAST((SELECT COUNT(*) FROM SENDDETAILS WHERE MARKEDASREAD= 1 AND MESSAGEUNIQUEID=A.MESSAGEUNIQUEID) AS NUMERIC) /CAST(COUNT(B.MARKEDASREAD) AS NUMERIC ) * 100,2))");
		complexSQL.append(" END AS READRATE ");
		complexSQL.append(" FROM MESSAGES AS A LEFT JOIN SENDDETAILS AS B ON A.MESSAGEUNIQUEID=B.MESSAGEUNIQUEID");
		complexSQL.append(" GROUP BY A.MESSAGEUNIQUEID HAVING A.CREATORUNIQUEID=? AND A.DELETED=0 ORDER BY MESSAGEUNIQUEID DESC");
		List<Message> results = jdbcTemplate.query(complexSQL.toString(), new Object[] {userUniqueId}, new BeanPropertyRowMapper<Message>(Message.class));
		
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
	public void deleteMessage(int messageUniqueId) {
//		String sql = "DELETE FROM MESSAGERECIPIENT WHERE MESSAGEUNIQUEID=?";
//		jdbcTemplate.update(sql, new Object[] {messageUniqueId});
//		
//		sql = "DELETE FROM REMINDERFREQUENCY AS A WHERE A.FREQUENCYUNIQUEID = (SELECT B.REMINDERFREQUENCYID FROM MESSAGES AS B WHERE A.FREQUENCYUNIQUEID=B.REMINDERFREQUENCYID AND B.MESSAGEUNIQUEID=?)";
//		jdbcTemplate.update(sql, new Object[] {messageUniqueId});
		
		String sql = "UPDATE MESSAGES SET DELETED=? WHERE MESSAGEUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {1, messageUniqueId});
	}
	
	/**
	 * 
	 * @param message
	 */
	public void updateMessage(Message message) {
		String sql = "UPDATE MESSAGES SET SUBJECT=?,MESSAGEBODY=?,DATECREATED=?,SENTTIMES=?,REMINDERFREQUENCYID=? WHERE MESSAGEUNIQUEID=?";
		jdbcTemplate.update(sql, new Object[] {
				message.getSubject(),
				message.getMessageBody(),
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
