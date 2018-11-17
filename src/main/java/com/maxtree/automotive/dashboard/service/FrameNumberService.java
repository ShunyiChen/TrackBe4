package com.maxtree.automotive.dashboard.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.FrameNumber;

@Component
public class FrameNumberService {
	
	private static final Logger log = LoggerFactory.getLogger(FrameNumberService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param frameNumberUniqueId
	 * @return
	 * @throws EmptyResultDataAccessException
	 */
	public FrameNumber findById(int frameNumberUniqueId) throws EmptyResultDataAccessException {
		String sql = "SELECT * FROM FRAMENUMBER WHERE FRAMEUNIQUEID = ?";
		List<FrameNumber> lstFrameNumber = jdbcTemplate.query(sql, new Object[] {frameNumberUniqueId}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		if (lstFrameNumber.size() > 0) {
			return lstFrameNumber.get(0);
		}
		return null;
	}
	
	/**
	 * 获取单元格总数
	 * 
	 * @return
	 */
	public int findCellTotalCount(String storeName) {
		String sql = "select count(*) from FRAMENUMBER where STOREHOUSENAME=? AND FRAMECODE != ?  and CELLCODE=? ";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] {storeName,0,0}, Integer.class);
		return count;
	}
	
	/**
	 * 获取文件夹总数
	 * 
	 * @param storeName
	 * @return
	 */
	public int findFolderTotalCount(String storeName, boolean onlyIncludeNonNullCode) {
		String sql = "SELECT COUNT(*) FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND CODE IS NOT NULL ";
		if(onlyIncludeNonNullCode) {
			sql = "SELECT COUNT(*) FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND CODE IS NOT NULL AND VIN IS NOT NULL";
		}
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] {storeName}, Integer.class);
		return count;
	}
	
	/**
	 * 查询全部库房
	 * 
	 * @return
	 */
	public List<FrameNumber> findAllStorehouse() {
		String sql = "SELECT * FROM FRAMENUMBER WHERE FRAMECODE=? ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {0}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 查询全部密集架
	 * 
	 * @param storehouseName
	 * @return
	 */
	public List<FrameNumber> findAllFrame(String storehouseName) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE != ? AND CELLCODE=? ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storehouseName,0,0}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 查询全部密集架
	 * 
	 * @param storehouseName
	 * @return
	 */
	public List<FrameNumber> findAllFrame(String storehouseName, int limit, int offset) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE != ? AND CELLCODE=? ORDER BY FRAMEUNIQUEID LIMIT ? OFFSET ?";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storehouseName,0,0, limit, offset}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 查询全部单元格
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
	 * 查询VIN值不为空的文件夹
	 * 
	 * @param storehouseName
	 * @param frameCode
	 * @return
	 */
	public List<FrameNumber> findAllFolderWithVIN(String storehouseName, int frameCode) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE=? AND CELLCODE != ? AND VIN IS NOT NULL ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storehouseName, frameCode, 0}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 查询所有文件夹
	 * 
	 * @param storehouseName
	 * @param frameCode
	 * @param cellCode
	 * @return
	 */
	public List<FrameNumber> findAllFolders(String storehouseName, int frameCode, int cellCode) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE=? AND CELLCODE=? AND CODE IS NOT NULL ORDER BY FRAMEUNIQUEID";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storehouseName,frameCode,cellCode}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		return results;
	}
	
	/**
	 * 查找密集架的下一编号
	 * 
	 * @param storehouseName
	 * @return
	 */
	public Integer findNextCodeOfFrame(String storehouseName) {
		String sql = "SELECT COALESCE(MAX(FRAMECODE), '0') + ? FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE !=? AND CELLCODE=?";
		Integer frameCode = jdbcTemplate.queryForObject(sql, new Object[] {1,storehouseName,0,0}, Integer.class);
		return frameCode;
	}
	
	/**
	 * 插入
	 * 
	 * @param frameNumber
	 * @return
	 */
	public int insert(FrameNumber frameNumber) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO FRAMENUMBER(STOREHOUSENAME,FRAMECODE,MAXCOLUMN,MAXROW,CELLCODE,COL,ROW,VIN,CODE,MAXFOLDER) VALUES(?,?,?,?,?,?,?,?,?,?)";
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
				ps.setInt(10, frameNumber.getMaxfolder());
				return ps;
			}
		}, keyHolder);
		int frameUniqueId = keyHolder.getKey().intValue();
		log.info("Affected id:"+frameUniqueId);
		return frameUniqueId;
	}
	
	
	/**
	 * 批量插入
	 * 
	 * @param list
	 */
	public void insert(List<FrameNumber> list) {
		String INSERT_TRANS_SQL = "INSERT INTO FRAMENUMBER(STOREHOUSENAME,FRAMECODE,MAXCOLUMN,MAXROW,CELLCODE,COL,ROW,VIN,CODE) VALUES(?,?,?,?,?,?,?,?,?)";
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
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
	
	/**
	 * 
	 * @param vin
	 * @param code
	 */
	public void updateVIN(String vin, String code) {
		String UPDATE_TRANS_SQL = "UPDATE FRAMENUMBER SET VIN=? WHERE CODE=?";
		int opt = jdbcTemplate.update(UPDATE_TRANS_SQL, new Object[] { vin, code});
		log.info("Affected id:"+opt);
	}
	
	/**
	 * 批量更新VIN
	 * 
	 * @param list
	 */
	public void batchUpdateVIN(List<FrameNumber> list) {
		String INSERT_TRANS_SQL = "UPDATE FRAMENUMBER SET VIN=? WHERE CODE=?";
		int[] opts = jdbcTemplate.batchUpdate(INSERT_TRANS_SQL, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				FrameNumber frameNumber = list.get(i);
				ps.setString(1, frameNumber.getVin());
				ps.setString(2, frameNumber.getCode());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
		for(int i=0; i < opts.length; i++) {
			FrameNumber folder = list.get(i);
			log.info(opts[i]+"-------------"+folder.getCode()+" "+folder.getVin());
		}
	}
	
	/**
	 * 更新库房名称
	 * 
	 * @param storehouse
	 * @param oldName
	 */
	public void updateStorehouse(FrameNumber storehouse, String oldName) {
		String sql = "UPDATE FRAMENUMBER SET STOREHOUSENAME=? WHERE STOREHOUSENAME=?";
		int opt = jdbcTemplate.update(sql, new Object[] { storehouse.getStorehouseName(),oldName});
		log.info("Affected row:"+opt);
	}
	
	/**
	 * 更新密集架信息
	 * 
	 * @param frame
	 */
	public void updateFrame(FrameNumber frame) {
		String sql = "UPDATE FRAMENUMBER SET MAXCOLUMN=?,MAXROW=? WHERE STOREHOUSENAME=? AND FRAMECODE=?";
		int opt = jdbcTemplate.update(sql, new Object[] {frame.getMaxColumn(),frame.getMaxRow(),frame.getStorehouseName(),frame.getFrameCode()});
		log.info("Affected row:"+opt);
	}
	
	/**
	 * 删除库房及其全部的密集架
	 * 
	 * @param storehouseName
	 */
	public void deleteStorehouse(FrameNumber store) {
		String sql = "DELETE FROM FRAMENUMBER WHERE STOREHOUSENAME=?";
		int opt = jdbcTemplate.update(sql, new Object[] {store.getStorehouseName()});
		log.info("Affected row:"+opt);

		sql =  "UPDATE COMPANIES SET STOREHOUSENAME=? WHERE STOREHOUSENAME=?";
		opt = jdbcTemplate.update(sql, new Object[] {null, store.getStorehouseName()});
		log.info("Affected row:"+opt);
	}
	
	/**
	 * 删除密集架及其单元格
	 * 
	 * @param storehouseName
	 * @param frameCode
	 */
	public void deleteFrame(String storehouseName, int frameCode) {
		String sql = "DELETE FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE=?";
		int opt = jdbcTemplate.update(sql, new Object[] {storehouseName, frameCode});
		log.info("Affected row:"+opt);
		
	}
	
	/**
	 * 删除密集架的全部单元格
	 * 
	 * @param storehouseName
	 * @param frameCode
	 */
	public void deleteFrameCells(String storehouseName, int frameCode) {
		String sql = "DELETE FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND FRAMECODE=? AND CELLCODE <>? AND COL<>? AND ROW<>?";
		int opt = jdbcTemplate.update(sql, new Object[] {storehouseName, frameCode,0,0,0});
		log.info("Affected row:"+opt);
		
	}
	
	/**
	 * 获取可用的机构列表
	 * 
	 * @param storehouseName
	 * @return
	 */
	public List<Company> getAvailableCompanies(String storehouseName) {
		String sql = "SELECT * FROM COMPANIES WHERE STOREHOUSENAME IS NULL OR STOREHOUSENAME=? ORDER BY COMPANYUNIQUEID";
		List<Company> results = jdbcTemplate.query(sql, new Object[] {storehouseName}, new BeanPropertyRowMapper<Company>(Company.class));
		return results;
	}
	
	/**
	 * 获取已经分配的机构
	 * 
	 * @param storehouseName
	 * @return
	 */
	public Company findAssignedCompany(String storehouseName) {
		String sql = "SELECT * FROM COMPANIES WHERE STOREHOUSENAME=?";
		List<Company> results = jdbcTemplate.query(sql, new Object[] {storehouseName}, new BeanPropertyRowMapper<Company>(Company.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Company();
	}
	
	/**
	 * 获取一个新的的上架号
	 * 
	 * @param storeHouseName 库房名称
	 * @return
	 */
	public FrameNumber getNewCode(String storeHouseName) {
		String sql = "SELECT * FROM FRAMENUMBER WHERE STOREHOUSENAME=? AND CODE IS NOT NULL AND VIN IS NULL ORDER BY FRAMEUNIQUEID LIMIT ?";
		List<FrameNumber> results = jdbcTemplate.query(sql, new Object[] {storeHouseName,1}, new BeanPropertyRowMapper<FrameNumber>(FrameNumber.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new FrameNumber();
	}
	
}
