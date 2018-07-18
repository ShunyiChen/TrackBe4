package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.SiteCapacity;

@Component
public class FrameNumberService {
	
	private static final Logger log = LoggerFactory.getLogger(FrameNumberService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 */
	public List<FrameNumber> findAllStorehouse() {
		String sql = "SELECT * FROM FRAMENUMBER WHERE FRAMECODE=? ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {0}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	public List<FrameNumber> findAllFrame(String storehouseName) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE != ? AND CELLCODE=? ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storehouseName,0,0}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 
	 * @param storehouseName
	 * @param frameCode
	 * @return
	 */
	public List<FrameNumber> findAllCell(String storehouseName, int frameCode) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE=? AND CELLCODE != ? AND CODE IS NULL ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storehouseName, frameCode, 0}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 
	 * @param storehouseName
	 * @param frameCode
	 * @return
	 */
	public List<FrameNumber> findAllBag(String storehouseName, int frameCode, int cellCode) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE=? AND CELLCODE=? AND CODE IS NOT NULL ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storehouseName,frameCode,cellCode}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer findNextCodeOfFrame(String storehouseName) {
		String sql = "SELECT COALESCE(MAX(FRAMECODE), '0') + ? FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE !=? AND CELLCODE=?";
		Integer frameCode = jdbcTemplate.queryForObject(sql, new Object[] {1,storehouseName,0,0}, Integer.class);
		return frameCode;
	}
	
	
	/**
	 * 
	 * @param storehouse
	 * @return
	 */
	public int insert(FrameNumber frameNumber) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO FRAMENUMBER(STOREHOUSENAME,FRAMECODE,MAXCOLUMN,MAXROW,CELLCODE,COL,ROW,VIN,CODE,COMPANYUNIQUEID) VALUES(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// 注意 frameuniqueid需全部小写
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "frameuniqueid" });
				ps.setString(1, frameNumber.getStorehouseName());
				ps.setInt(2, frameNumber.getFrameCode());
				ps.setInt(3, frameNumber.getMaxColumn());
				ps.setInt(4, frameNumber.getMaxRow());
				ps.setInt(5, frameNumber.getCellCode());
				ps.setInt(6, frameNumber.getCol());
				ps.setInt(7, frameNumber.getRow());
				ps.setString(8, frameNumber.getVin());
				ps.setString(9, frameNumber.getCode());
				ps.setInt(10, frameNumber.getCompanyUniqueId());
				return ps;
			}
		}, keyHolder);
		int frameUniqueId = keyHolder.getKey().intValue();
		log.info("Affected id:"+frameUniqueId);
		return frameUniqueId;
	}
	
	
	/**
	 * 
	 * @param list
	 */
	public void insert(List<FrameNumber> list) {
		String INSERT_TRANS_SQL = "INSERT INTO FRAMENUMBER(STOREHOUSENAME,FRAMECODE,MAXCOLUMN,MAXROW,CELLCODE,COL,ROW,VIN,CODE,COMPANYUNIQUEID) VALUES(?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(INSERT_TRANS_SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				FrameNumber frameNumber = list.get(i);
				ps.setString(1, frameNumber.getStorehouseName());
				ps.setInt(2, frameNumber.getFrameCode());
				ps.setInt(3, frameNumber.getMaxColumn());
				ps.setInt(4, frameNumber.getMaxRow());
				ps.setInt(5, frameNumber.getCellCode());
				ps.setInt(6, frameNumber.getCol());
				ps.setInt(7, frameNumber.getRow());
				ps.setString(8, frameNumber.getVin());
				ps.setString(9, frameNumber.getCode());
				ps.setInt(10, frameNumber.getCompanyUniqueId());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
	
	/**
	 * 
	 * @param storehouse
	 * @param oldName
	 */
	public void updateStorehouse(FrameNumber storehouse, String oldName) {
		String sql = "UPDATE FRAMENUMBER SET STOREHOUSENAME=?,COMPANYUNIQUEID=? WHERE STOREHOUSENAME=?";
		int opt = jdbcTemplate.update(sql, new Object[] { storehouse.getStorehouseName(),storehouse.getCompanyUniqueId(),oldName});
		log.info("Affected row:"+opt);
	}
	
	/**
	 * 
	 * @param frame
	 */
	public void updateFrame(FrameNumber frame) {
		String sql = "UPDATE FRAMENUMBER SET MAXCOLUMN=?,MAXROW=? WHERE STOREHOUSENAME=? AND FRAMECODE=?";
		int opt = jdbcTemplate.update(sql, new Object[] {frame.getMaxColumn(),frame.getMaxRow(),frame.getStorehouseName(),frame.getFrameCode()});
		log.info("Affected row:"+opt);
	}
	
	
	
	
	public void deleteStorehouse(String storehouseName) {
		String sql = "DELETE FROM FRAMENUMBER WHERE STOREHOUSENAME=?";
		int opt = jdbcTemplate.update(sql, new Object[] {storehouseName});
		log.info("Affected row:"+opt);
		
	}
	
	public void deleteFrame(String storehouseName, int frameCode) {
		String sql = "DELETE FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE=?";
		int opt = jdbcTemplate.update(sql, new Object[] {storehouseName, frameCode});
		log.info("Affected row:"+opt);
		
	}
}
