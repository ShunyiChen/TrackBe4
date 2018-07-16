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
		String sql = "SELECT * FROM STOREHOUSE ORDER BY STOREHOUSEUNIQUEID";
		List<Storehouse> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Storehouse>(Storehouse.class));
		return results;
	}
	
	/**
	 * 
	 * @param storehouse
	 * @return
	 */
	public int insertStorehouse(Storehouse storehouse) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO STOREHOUSE(NAME,CODE,COMPANYUNIQUEID) VALUES(?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// 注意 storehouseuniqueId需全部小写
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "storehouseuniqueid" });
				ps.setString(1, storehouse.getName());
				ps.setString(2, storehouse.getCode());
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
		String sql = "DELETE FROM PORTFOLIO WHERE FILEBOXCODE IN (SELECT CODE FROM FILEBOX WHERE DENSEFRAMECODE IN (SELECT CODE FROM DENSEFRAME WHERE STOREHOUSECODE IN (SELECT CODE FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?)))";
		int opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
		log.info("Affected row:"+opt);
		
		sql = "DELETE FROM FILEBOX WHERE DENSEFRAMECODE IN (SELECT CODE FROM DENSEFRAME WHERE STOREHOUSECODE IN (SELECT CODE FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?))";
		opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
		log.info("Affected row:"+opt);
		
		sql = "DELETE FROM DENSEFRAME WHERE STOREHOUSECODE IN (SELECT CODE FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?)";
		opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
		log.info("Affected row:"+opt);
		
		sql = "DELETE FROM STOREHOUSE WHERE STOREHOUSEUNIQUEID=?";
		opt = jdbcTemplate.update(sql, new Object[] {storehouseUniqueId});
		log.info("Affected row:"+opt);
	}
	
	/**
	 * 获取密集架
	 * 
	 * @param storehouseUniqueId
	 * @return
	 */
	public List<DenseFrame> findAllDenseFrame(String storehouseCode) {
		String sql = "SELECT * FROM DENSEFRAME WHERE STOREHOUSECODE=? ORDER BY DENSEFRAMEUNIQUEID";
		List<DenseFrame> results = jdbcTemplate.query(sql, new Object[] {storehouseCode}, new BeanPropertyRowMapper<DenseFrame>(DenseFrame.class));
		return results;
	}
	
	/**
	 * 
	 * @param denseFrame
	 * @return
	 */
	public int insertDenseFrame(DenseFrame denseFrame) {
		String sql = "SELECT COALESCE(MAX(SERIALNUMBER), '0')+1 FROM DENSEFRAME WHERE STOREHOUSECODE=?";
		int serialNumber = jdbcTemplate.queryForObject(sql, new Object[] {denseFrame.getStorehouseCode()}, Integer.class);
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		String INSERT_TRANS_SQL = "INSERT INTO DENSEFRAME(NAME,MAXCOL,MAXROW,SERIALNUMBER,CODE,STOREHOUSECODE) VALUES(?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				// 注意 storehouseuniqueId需全部小写
				PreparedStatement ps = con.prepareStatement(INSERT_TRANS_SQL, new String[] { "storehouseuniqueid" });
				ps.setString(1, denseFrame.getName());
				ps.setInt(2, denseFrame.getMaxCol());
				ps.setInt(3, denseFrame.getMaxRow());
				ps.setInt(4, serialNumber);
				ps.setString(5, denseFrame.getCode());
				ps.setString(6, denseFrame.getStorehouseCode());
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
//	public void updateDenseFrame(DenseFrame denseFrame) {
//		String sql = "UPDATE DENSEFRAME SET ROWCOUNT=?,COLUMNCOUNT=? WHERE DENSEFRAMEUNIQUEID=?";
//	 	int opt = jdbcTemplate.update(sql, new Object[] { denseFrame.getRowCount(), denseFrame.getColumnCount(), denseFrame.getDenseFrameUniqueId()});
//	 	log.info("Affected result:"+opt);
//	}
	
//	/**
//	 * 
//	 * @param denseFrameUniqueId
//	 * @throws DataException
//	 */
//	public void deleteDenseFrame(int denseFrameUniqueId) throws DataException {
//		List<FileBox> fileBoxes = findAllFileBox(denseFrameUniqueId);
//		for (FileBox box : fileBoxes) {
//			this.deleteFileBox(box.getFileboxUniqueId());
//		}
//		String sql = "DELETE FROM DENSEFRAME WHERE DENSEFRAMEUNIQUEID = ?";
//	 	int opt = jdbcTemplate.update(sql, new Object[] {denseFrameUniqueId});
//	 	log.info("Deleted affect row:"+opt);
//	}
	
	/**
	 * 
	 * @param fileboxuniqueid
	 * @throws DataException
	 */
	public void deleteFileBox(int fileboxuniqueid) throws DataException {
		List<Portfolio> portfolios = this.findAllPortfolio(fileboxuniqueid);
		for (Portfolio p : portfolios) {
			this.deletePortfolio(p.getPortfolioUniqueId());
		}
		String sql = "DELETE FROM FILEBOX WHERE FILEBOXUNIQUEID = ?";
		int opt = jdbcTemplate.update(sql, new Object[] {fileboxuniqueid});
	 	log.info("Deleted affect row:"+opt);
	}
	
	/**
	 * 
	 * @param portfolioUniqueId
	 * @throws DataException
	 */
	public void deletePortfolio(int portfolioUniqueId) throws DataException {
		String sql = "DELETE FROM PORTFOLIO WHERE PORTFOLIOUNIQUEID = ?";
	 	int opt = jdbcTemplate.update(sql, new Object[] {portfolioUniqueId});
	 	log.info("Deleted affect row:"+opt);
	}

	/**
	 * 获取文件盒
	 * 
	 * @param denseFrameUniquedId
	 * @return
	 */
	public List<FileBox> findAllFileBox(int denseFrameUniquedId) {
		String sql = "SELECT * FROM FILEBOX WHERE DENSEFRAMEUNIQUEID=?";
		List<FileBox> results = jdbcTemplate.query(sql, new Object[] {denseFrameUniquedId}, new BeanPropertyRowMapper<FileBox>(FileBox.class));
		return results;
	}
	
	/**
	 * 获取档案袋
	 * 
	 * @param denseFrameUniquedId
	 * @return
	 */
	public List<Portfolio> findAllPortfolio(int fileBoxUniquedId) {
		String sql = "SELECT * FROM PORTFOLIO WHERE FILEBOXUNIQUEID=?";
		List<Portfolio> results = jdbcTemplate.query(sql, new Object[] {fileBoxUniquedId}, new BeanPropertyRowMapper<Portfolio>(Portfolio.class));
		return results;
	}
	
	/**
	 * 插入密集架
	 * 
	 * @param denseFrame
	 * @return
	 */
//	public int insertDenseFrame(DenseFrame denseFrame) {
//		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//		String INSERT_SQL = "INSERT INTO DENSEFRAME(STOREHOUSEUNIQUEID,ROWCOUNT,COLUMNCOUNT,CODE) VALUES(?,?,?,?)";
//		jdbcTemplate.update(new PreparedStatementCreator() {
//
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] { "denseframeuniqueid" });
//				ps.setInt(1, denseFrame.getStorehouseUniqueId());
//				ps.setInt(2, denseFrame.getRowCount());
//				ps.setInt(3, denseFrame.getColumnCount());
//				ps.setString(4, denseFrame.getCode());
//				return ps;
//			}
//		}, keyHolder);
//		int denseFrameUniquedId = keyHolder.getKey().intValue();
//		log.info("Inserted denseFrameUniquedId:"+denseFrameUniquedId);
//		
//		 
//		return denseFrameUniquedId;
//	}
	
	/**
	 * 插入文件盒
	 * 
	 * @param fileBox
	 * @return
	 */
//	public int insertFileBox(FileBox fileBox) {
//		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//		String INSERT_SQL = "INSERT INTO FILEBOX(DENSEFRAMEUNIQUEID,CODE,ROW,COL) VALUES(?,?,?,?)";
//		jdbcTemplate.update(new PreparedStatementCreator() {
//
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				PreparedStatement ps = con.prepareStatement(INSERT_SQL, new String[] { "fileboxuniqueid" });
//				ps.setInt(1, fileBox.getDenseFrameUniquedId());
//				ps.setString(2, fileBox.getCode());
//				ps.setInt(3, fileBox.getRow());
//				ps.setInt(4, fileBox.getCol());
//				return ps;
//			}
//		}, keyHolder);
//		int fileboxUniqueId = keyHolder.getKey().intValue();
//		log.info("Inserted fileboxUniqueId:"+fileboxUniqueId);
//		return fileboxUniqueId;
//	}
	
//	public void insertPortfolio(FileBox fileBox) {
//		List<Portfolio> list = new ArrayList<>();
//		for (int i = 0; i < 100; i++) {
//			Portfolio p = new Portfolio();
//			p.setFileBoxUniqueId(fileBox.getFileboxUniqueId());
//			p.setCode(fileBox.getCode()+"-"+new CodeGenerator().generateCode((i+1)));
//			p.setVin(null);
//			
//			list.add(p);
//		}
//
//		String inserQuery = "INSERT INTO PORTFOLIO(FILEBOXUNIQUEID,CODE,VIN) VALUES(?,?,?)";
//		jdbcTemplate.batchUpdate(inserQuery, new BatchPreparedStatementSetter() {
//			@Override
//			public void setValues(PreparedStatement ps, int i) throws SQLException {
//				Portfolio p = list.get(i);
//				ps.setInt(1, p.getFileBoxUniqueId());
//				ps.setString(2, p.getCode());
//				ps.setString(3, p.getVin());
//			}
//
//			@Override
//			public int getBatchSize() {
//				return list.size();
//			}
//		});
//	}
	
	/**
	 * 查找一个可用的档案袋
	 * 
	 * @return
	 */
	public Portfolio findAvailablePortfolio() {
		String sql = "SELECT * FROM PORTFOLIO WHERE VIN IS NULL ORDER BY PORTFOLIOUNIQUEID LIMIT ?";
		List<Portfolio> results = jdbcTemplate.query(sql, new Object[] {1}, new BeanPropertyRowMapper<Portfolio>(Portfolio.class));
		if (results.size() > 0) {
			return results.get(0);
		}
		return new Portfolio();
	}
	
	/**
	 * 更新档案袋
	 * 
	 * @return
	 */
	public void updatePortfolio(Portfolio portfolio) {
		String sql = "UPDATE PORTFOLIO SET VIN=? WHERE PORTFOLIOUNIQUEID=?";
		int opt = jdbcTemplate.update(sql, new Object[] {portfolio.getVin(), portfolio.getPortfolioUniqueId()});
		log.info("Updated,affected "+opt+" row.");
	}

	/**
	 * 
	 * @return
	 */
	public String getMaxCodeOfStorehouse() {
		String sql = "SELECT COALESCE(MAX(CODE), '0') FROM STOREHOUSE";
		String maxCode = jdbcTemplate.queryForObject(sql, String.class);
		return maxCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMaxCodeOfDenseFrame(int storehouseUniqueId) {
		String sql = "SELECT COALESCE(MAX(CODE), '0') FROM DENSEFRAME WHERE STOREHOUSEUNIQUEID=?";
		String maxCode = jdbcTemplate.queryForObject(sql, new Object[] {storehouseUniqueId}, String.class);
		return maxCode;
	}
	
}
