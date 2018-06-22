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

import com.maxtree.automotive.dashboard.domain.Document;

@Component
public class DocumentService {

	private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
//	/**
//	 * 
//	 * @param uuid
//	 * @return
//	 */
//	public int getMaxOrdinal(String uuid) {
//		String sql = "SELECT COUNT(DOCUMENTUNIQUEID) FROM DOCUMENTS_2 WHERE UUID=?";
//		int count = jdbcTemplate.queryForObject(
//                sql, new Object[] {uuid}, Integer.class);
//
//		return count;
//	}
	
	/**
	 * 获取主要文件列表
	 * 
	 * @param uuid
	 * @param businessUniqueId
	 * @return
	 */
	public List<Document> findPrimary(String uuid, int businessUniqueId) {
		String sql = "SELECT * FROM DOCUMENTS_1 WHERE UUID=? AND BUSINESSUNIQUEID=? ORDER BY DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {uuid, businessUniqueId}, new BeanPropertyRowMapper<Document>(Document.class));
		return result;
	}
	
	/**
	 * 获取辅助文件列表
	 * 
	 * @param uuid
	 * @param businessUniqueId
	 * @return
	 */
	public List<Document> findSecondary(String uuid, int businessUniqueId) {
		String sql = "SELECT * FROM DOCUMENTS_2 WHERE UUID=? AND BUSINESSUNIQUEID=? ORDER BY DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {uuid, businessUniqueId}, new BeanPropertyRowMapper<Document>(Document.class));
		return result;
	}
	
	/**
	 * 创建一个文件
	 * 
	 * @param document
	 * @return
	 */
	public int create(Document document) {
	 	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String SQL = "";
				if (document.getCategory() == 0) {
					SQL = "INSERT INTO DOCUMENTS_1(UUID,BUSINESSUNIQUEID,ALIAS,FILENAME,FILEFULLPATH,CATEGORY) VALUES(?,?,?,?,?,?)";
				} else {
					SQL = "INSERT INTO DOCUMENTS_2(UUID,BUSINESSUNIQUEID,ALIAS,FILENAME,FILEFULLPATH,CATEGORY) VALUES(?,?,?,?,?,?)";
				}
				PreparedStatement ps = con.prepareStatement(
						SQL, new String[] {"documentuniqueid"});
				ps.setString(1, document.getUuid());
				ps.setInt(2, document.getBusinessUniqueId());
				ps.setString(3, document.getAlias());
				ps.setString(4, document.getFileName());
				ps.setString(5, document.getFileFullPath());
				ps.setInt(6, document.getCategory());
				return ps;
			}
		}, keyHolder);
		int documentUniqueId  = keyHolder.getKey().intValue();
		log.info("Created one document.Affected row id= "+documentUniqueId);
		return documentUniqueId;
	}
	
	/**
	 * 更新文件信息
	 * 
	 * @param document
	 */
	public void update(Document document) {
		String SQL = "";
		if (document.getCategory() == 0) {
			SQL = "UPDATE DOCUMENTS_1 SET FILENAME=?,FILEFULLPATH=?,CATEGORY=? WHERE DOCUMENTUNIQUEID=?";
		} else {
			SQL = "UPDATE DOCUMENTS_2 SET FILENAME=?,FILEFULLPATH=?,CATEGORY=? WHERE DOCUMENTUNIQUEID=?";
		}
	 	int opt = jdbcTemplate.update(SQL, new Object[] {document.getFileName(), document.getFileFullPath(), document.getCategory(), document.getDocumentUniqueId()});
	 	log.info("Document updated.Affected row="+opt);
	}
	
//	/**
//	 * 
//	 * @param transactionUniqueId
//	 * @param businessUniqueId
//	 */
//	public void deleteExclude(int transactionUniqueId, int businessUniqueId) {
//		String SQL = "DELETE FROM DOCUMENTS_1 WHERE TRANSACTIONUNIQUEID=? AND BUSINESSUNIQUEID <> ?";
//		int affected = jdbcTemplate.update(SQL, new Object[] {transactionUniqueId, businessUniqueId});
//		log.info("Affected row "+affected);
//		SQL = "DELETE FROM DOCUMENTS_2 WHERE TRANSACTIONUNIQUEID=? AND BUSINESSUNIQUEID <> ?";
//		affected = jdbcTemplate.update(SQL, new Object[] {transactionUniqueId, businessUniqueId});
//		log.info("Affected row "+affected);
//	}
	
	/**
	 * 
	 * @param documentUniqueId
	 */
	public void deleteById(int documentUniqueId) {
		String sql = "DELETE FROM DOCUMENTS_2 WHERE DOCUMENTUNIQUEID=?";
		int affected = jdbcTemplate.update(sql, new Object[] {documentUniqueId});
		log.info("Affected row "+affected);
	}
}
