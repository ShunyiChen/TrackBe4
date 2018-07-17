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

import com.maxtree.automotive.dashboard.domain.DenseFrame;
import com.maxtree.automotive.dashboard.domain.FileBox;
import com.maxtree.automotive.dashboard.domain.Portfolio;
import com.maxtree.automotive.dashboard.domain.Storehouse;
import com.maxtree.automotive.dashboard.exception.DataException;


@Component
public class StorehouseService {

	private static final Logger log = LoggerFactory.getLogger(StorehouseService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @return
	 */
	public List<Storehouse> findAllStorehouses() {
		String sql = "SELECT * FROM STOREHOUSE ORDER BY SERIALNUMBER";
		List<Storehouse> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Storehouse>(Storehouse.class));
		return results;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer findNextSerialnumberOfStorehouse() {
		String sql = "SELECT COALESCE(MAX(SERIALNUMBER), '0') + 1 FROM STOREHOUSE";
		Integer maxSerialNumber = jdbcTemplate.queryForObject(sql, Integer.class);
		return maxSerialNumber;
	}
	
	/**
	 * 
	 * @param storehouse
	 * @return
	 */
	public int insertStorehouse(Storehouse storehouse) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO STOREHOUSE(NAME,SERIALNUMBER,COMPANYUNIQUEID) VALUES(?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// 注意 storehouseuniqueId需全部小写
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "storehouseuniqueid" });
				ps.setString(1, storehouse.getName());
				ps.setInt(2, storehouse.getSerialNumber());
				ps.setInt(3, storehouse.getCompanyUniqueId());
				return ps;
			}
		}, keyHolder);
		int storehouseUniqueId = keyHolder.getKey().intValue();
		log.info("Inserted storehouseUniqueId:"+storehouseUniqueId);
		return storehouseUniqueId;
	}
	
	/**
	 * 
	 * @param storehouse
	 */
	public void updateStorehouse(Storehouse storehouse) {
		String sql = "UPDATE STOREHOUSE SET NAME=?,COMPANYUNIQUEID=? WHERE STOREHOUSEUNIQUEID=?";
		int opt = jdbcTemplate.update(sql, new Object[] { storehouse.getName(),storehouse.getCompanyUniqueId(),storehouse.getStorehouseUniqueId()});
		log.info("Affected row:"+opt);
	}
	
	/**
	 * 
	 * @param storehouseUniqueId
	 */
	public void deleteStorehouse(int storehouseUniqueId) {
//		String sql = "DELETE FROM PORTFOLIO WHERE FILEBOXSN IN (SELECT SERIALNUMBER FROM FILEBOX WHERE DENSEFRAMESN IN (SELECT SERIALNUMBER FROM DENSEFRAME WHERE STOREHOUSESN IN (SELECT SERIALNUMBER FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?)))";
//		int opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
//		log.info("Affected row:"+opt);
//		
//		sql = "DELETE FROM FILEBOX WHERE DENSEFRAMESN IN (SELECT SERIALNUMBER FROM DENSEFRAME WHERE STOREHOUSESN IN (SELECT SERIALNUMBER FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?))";
//		opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
//		log.info("Affected row:"+opt);
//		
//		sql = "DELETE FROM DENSEFRAME WHERE STOREHOUSESN IN (SELECT SERIALNUMBER FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?)";
//		opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
//		log.info("Affected row:"+opt);
//		
		
		
//		String sql = "SELECT * FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?";
//		List<Storehouse> results = jdbcTemplate.query(sql,new Object[] {storehouseUniqueId}, new BeanPropertyRowMapper<Storehouse>(Storehouse.class));
//		
//		
//		
//		
////		String sql = "DELETE FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?";
//		int opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
//		log.info("Affected row:"+opt);
		
		
	}
	
	/**
	 * 获取密集架
	 * 
	 * @param storehouseUniqueId
	 * @return
	 */
	public List<DenseFrame> findAllDenseFrame(int storehouseSN) {
		String sql = "SELECT * FROM DENSEFRAME WHERE STOREHOUSESN=? ORDER BY SERIALNUMBER";
		List<DenseFrame> results = jdbcTemplate.query(sql, new Object[] {storehouseSN}, new BeanPropertyRowMapper<DenseFrame>(DenseFrame.class));
		return results;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer findNextSerialnumberOfDenseFrame(int storehouseSN) {
		String sql = "SELECT COALESCE(MAX(SERIALNUMBER), '0') + 1 FROM DENSEFRAME WHERE STOREHOUSESN=?";
		Integer maxSerialNumber = jdbcTemplate.queryForObject(sql, new Object[] {storehouseSN}, Integer.class);
		return maxSerialNumber;
	}
	
	/**
	 * 
	 * @param denseFrame
	 * @return
	 */
	public int insertDenseFrame(DenseFrame denseFrame) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO DENSEFRAME(NAME,MAXCOL,MAXROW,SERIALNUMBER,STOREHOUSESN) VALUES(?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// 注意 storehouseuniqueId需全部小写
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "denseframeuniqueid" });
				ps.setString(1, denseFrame.getName());
				ps.setInt(2, denseFrame.getMaxCol());
				ps.setInt(3, denseFrame.getMaxRow());
				ps.setInt(4, denseFrame.getSerialNumber());
				ps.setInt(5, denseFrame.getStorehouseSN());
				return ps;
			}
		}, keyHolder);
		int denseFrameUniqueId = keyHolder.getKey().intValue();
		log.info("Inserted denseFrameUniqueId:"+denseFrameUniqueId);
		return denseFrameUniqueId;
	}
	
	/**
	 * 
	 * @param denseFrame
	 */
	public void updateDenseFrame(DenseFrame denseFrame) {
		String sql = "UPDATE DENSEFRAME SET NAME=?,MAXCOL=?,MAXROW=? WHERE DENSEFRAMEUNIQUEID=?";
	 	int opt = jdbcTemplate.update(sql, new Object[] { denseFrame.getName(),denseFrame.getMaxCol(),denseFrame.getMaxRow(),denseFrame.getDenseFrameUniqueId()});
	 	log.info("Affected result:"+opt);
	}
	
	/**
	 * 
	 * @param storehouseSN
	 */
	public void deleteDenseFrame(int storehouseSN) {
		String sql = "DELETE FROM PORTFOLIO WHERE FILEBOXSN IN (SELECT SERIALNUMBER FROM FILEBOX WHERE DENSEFRAMESN IN (SELECT SERIALNUMBER FROM DENSEFRAME WHERE STOREHOUSESN IN (SELECT SERIALNUMBER FROM STOREHOUSE WHERE STOREHOUSESN=?)))";
		int opt = jdbcTemplate.update(sql, new Object[] {storehouseSN});
		log.info("Affected row:"+opt);
		
		sql = "DELETE FROM FILEBOX WHERE DENSEFRAMESN IN (SELECT SERIALNUMBER FROM DENSEFRAME WHERE STOREHOUSESN IN (SELECT SERIALNUMBER FROM STOREHOUSE WHERE STOREHOUSESN=?))";
		opt = jdbcTemplate.update(sql, new Object[] {storehouseSN});
		log.info("Affected row:"+opt);
		
		sql = "DELETE FROM DENSEFRAME WHERE STOREHOUSESN IN (SELECT SERIALNUMBER FROM STOREHOUSE WHERE STOREHOUSESN=?)";
		opt = jdbcTemplate.update(sql, new Object[] {storehouseSN});
		log.info("Affected row:"+opt);
	}
	
	
	public List<FileBox> findAllFileBox(int storehouseSN, int denseFrameSN) {
		String sql = "SELECT * FROM DENSEFRAME WHERE DENSEFRAMESN IN (SELECT SERIALNUMBER FROM STOREHOUSE WHERE STOREHOUSESN=?) ORDER BY SERIALNUMBER";
		List<FileBox> results = jdbcTemplate.query(sql, new Object[] {storehouseSN}, new BeanPropertyRowMapper<DenseFrame>(DenseFrame.class));
		return results;
	}
	
	/**
	 * 插入文件盒
	 * 
	 * @param fileBox
	 * @return
	 */
	public int insertFileBox(FileBox fileBox) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_SQL = "INSERT INTO FILEBOX(COL,ROW,SERIALNUMBER,DENSEFRAMESN) VALUES(?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] { "fileboxuniqueid" });
				ps.setInt(1, fileBox.getCol());
				ps.setInt(2, fileBox.getRow());
				ps.setInt(3, fileBox.getSerialNumber());
				ps.setInt(4, fileBox.getDenseframeSN());
				return ps;
			}
		}, keyHolder);
		int fileboxUniqueId = keyHolder.getKey().intValue();
		log.info("Inserted fileboxUniqueId:"+fileboxUniqueId);
		return fileboxUniqueId;
	}
	
	/**
	 * 
	 * @param list
	 */
	public void insertPortfolio(List<Portfolio> list) {
		String inserQuery = "INSERT INTO PORTFOLIO(VIN,CODE,FILEBOXSN) VALUES(?,?,?)";
		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Portfolio p = list.get(i);
				ps.setString(1, p.getVin());
				ps.setString(2, p.getCode());
				ps.setInt(3, p.getFileBoxSN());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}
	
	/**
	 * 获取档案袋
	 * 
	 * @param denseFrameUniquedId
	 * @return
	 */
	public List<Portfolio> findAllPortfolio(int fileBoxSN) {
		String sql = "SELECT * FROM PORTFOLIO WHERE FILEBOXSN=?";
		List<Portfolio> results = jdbcTemplate.query(sql, new Object[] {fileBoxSN}, new BeanPropertyRowMapper<Portfolio>(Portfolio.class));
		return results;
	}
	
//	/**
//	 * 
//	 * @param fileboxuniqueid
//	 * @throws DataException
//	 */
//	public void deleteFileBox(int fileboxuniqueid) throws DataException {
//		List<Portfolio> portfolios = this.findAllPortfolio(fileboxuniqueid);
//		for (Portfolio p : portfolios) {
//			this.deletePortfolio(p.getPortfolioUniqueId());
//		}
//		String sql = "DELETE FROM FILEBOX WHERE FILEBOXUNIQUEID = ?";
//		int opt = jdbcTemplate.update(sql, new Object[] {fileboxuniqueid});
//	 	log.info("Deleted affect row:"+opt);
//	}
//	
//	/**
//	 * 
//	 * @param portfolioUniqueId
//	 * @throws DataException
//	 */
//	public void deletePortfolio(int portfolioUniqueId) throws DataException {
//		String sql = "DELETE FROM PORTFOLIO WHERE PORTFOLIOUNIQUEID = ?";
//	 	int opt = jdbcTemplate.update(sql, new Object[] {portfolioUniqueId});
//	 	log.info("Deleted affect row:"+opt);
//	}
//
//	/**
//	 * 获取文件盒
//	 * 
//	 * @param denseFrameUniquedId
//	 * @return
//	 */
//	public List<FileBox> findAllFileBox(int denseFrameUniquedId) {
//		String sql = "SELECT * FROM FILEBOX WHERE DENSEFRAMEUNIQUEID=?";
//		List<FileBox> results = jdbcTemplate.query(sql, new Object[] {denseFrameUniquedId}, new BeanPropertyRowMapper<FileBox>(FileBox.class));
//		return results;
//	}
//	
//	/**
//	 * 查找一个可用的档案袋
//	 * 
//	 * @return
//	 */
//	public Portfolio findAvailablePortfolio() {
//		String sql = "SELECT * FROM PORTFOLIO WHERE VIN IS NULL ORDER BY PORTFOLIOUNIQUEID LIMIT ?";
//		List<Portfolio> results = jdbcTemplate.query(sql, new Object[] {1}, new BeanPropertyRowMapper<Portfolio>(Portfolio.class));
//		if (results.size() > 0) {
//			return results.get(0);
//		}
//		return new Portfolio();
//	}
//	
//	/**
//	 * 更新档案袋
//	 * 
//	 * @return
//	 */
//	public void updatePortfolio(Portfolio portfolio) {
//		String sql = "UPDATE PORTFOLIO SET VIN=? WHERE PORTFOLIOUNIQUEID=?";
//		int opt = jdbcTemplate.update(sql, new Object[] {portfolio.getVin(), portfolio.getPortfolioUniqueId()});
//		log.info("Updated,affected "+opt+" row.");
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public String getMaxCodeOfStorehouse() {
//		String sql = "SELECT COALESCE(MAX(CODE), '0') FROM STOREHOUSE";
//		String maxCode = jdbcTemplate.queryForObject(sql, String.class);
//		return maxCode;
//	}
//	
//	
	
}
