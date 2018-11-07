package com.maxtree.automotive.dashboard.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Queue;
import com.maxtree.automotive.dashboard.exception.DataException;

@Component
public class QueueService {

private static final Logger log = LoggerFactory.getLogger(QueueService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	/**
	 * 获取锁定的队列
	 * 
	 * @param serial //1:质检队列 2:审档队列
	 * @param userUniqueId
	 * @return
	 */
	public Queue getLockedQueue(int serial, int userUniqueId) {
		String sql = "SELECT * FROM QUEUE_"+serial+" WHERE LOCKEDBYUSER=?";
		List<Queue> results = jdbcTemplate.query(sql, new Object[] {userUniqueId}, new BeanPropertyRowMapper<Queue>(Queue.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Queue();
	}
	
	/**
	 * 
	 * @param serial
	 * @param companyUniqueId
	 * @param communityUniqueId
	 * @return
	 */
	public Queue poll(int serial, int companyUniqueId ,int communityUniqueId) {
		String sql = "SELECT * FROM QUEUE_"+serial+" WHERE LOCKEDBYUSER=? AND COMPANYUNIQUEID=? AND COMMUNITYUNIQUEID=? ORDER BY QUEUEUNIQUEID ASC LIMIT ?";
		List<Queue> results = jdbcTemplate.query(sql, new Object[] {0,companyUniqueId,communityUniqueId,1}, new BeanPropertyRowMapper<Queue>(Queue.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Queue();
	}
	
	/**
	 * 
	 * @param QueueUniqueId
	 * @param serial  1-队列（质检取） 2队列（审档取）
	 * @return
	 */
	public Queue findById(int transactionUniqueId, int lockedByUser, int serial) {
		String sql = "SELECT * FROM QUEUE_"+serial+" WHERE TRANSACTIONUNIQUEID = ? AND LOCKEDBYUSER = ?";
		List<Queue> results = jdbcTemplate.query(sql, new Object[] {transactionUniqueId, lockedByUser}, new BeanPropertyRowMapper<Queue>(Queue.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Queue();
	}
	
	/**
	 * 
	 * @param serial -队列（质检取） 2队列（审档取）
	 * @return
	 */
	public List<Queue> findAll(int serial) {
		String sql = "SELECT * FROM QUEUE_"+serial;
		List<Queue> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Queue>(Queue.class));
		return results;
	}
	
	/**
	 * 1-队列（质检取） 2队列（审档取）,3-确认审档队列
	 * 
	 * @param serial
	 * @param communityUniqueId
	 * @param companyUniqueId
	 * @return
	 */
	public List<Queue> findAvaliable(int serial, int communityUniqueId, int companyUniqueId) {
		String sql = "SELECT * FROM QUEUE_"+serial+" WHERE LOCKEDBYUSER=? AND COMMUNITYUNIQUEID=? AND COMPANYUNIQUEID=?";
		List<Queue> results = jdbcTemplate.query(sql, new Object[] {0,communityUniqueId,companyUniqueId}, new BeanPropertyRowMapper<Queue>(Queue.class));
		return results;
	}
	
	/**
	 * 
	 * @param Queue
	 * @param serial -队列（质检取） 2队列（审档取）
	 */
	public void lock(Queue queue, int serial) {
		String sql = "UPDATE QUEUE_"+serial+" SET LOCKEDBYUSER=? WHERE QUEUEUNIQUEID = ? ";
	 	int opt = jdbcTemplate.update(sql, new Object[] {queue.getLockedByUser(), queue.getQueueUniqueId()});
	 	log.info("updated row "+opt);
	}
	
	/**
	 * 
	 * @param queueUniqueId
	 * @param serial
	 * @throws DataException
	 */
	public void delete(int queueUniqueId, int serial) throws DataException {
		try {
			String sql = "DELETE FROM QUEUE_"+serial+" WHERE QUEUEUNIQUEID = ?";
		 	int opt = jdbcTemplate.update(sql, new Object[] {queueUniqueId});
		 	log.info("delete affected row "+opt);
		} catch (Exception e) {
			throw new DataException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param queue
	 * @param serial
	 */
	public void create(Queue queue, int serial) {
		String sql = "INSERT INTO QUEUE_"+serial+"(UUID,VIN,LOCKEDBYUSER,COMPANYUNIQUEID,COMMUNITYUNIQUEID) VALUES(?,?,?,?,?)";
	 	int opt = jdbcTemplate.update(sql, new Object[] {
	 			queue.getUuid(),
	 			queue.getVin(),
	 			queue.getLockedByUser(),
	 			queue.getCompanyUniqueId(),
	 			queue.getCommunityUniqueId()});
	 	log.info("Affected row "+opt);
	}
}
