package com.maxtree.automotive.dashboard.service;

import java.io.ByteArrayInputStream;
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

import com.maxtree.automotive.dashboard.EncryptionUtils;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.DocumentHistory;

@Component
public class DocumentService {
	private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 获取主要文件列表
	 * 
	 * @param vin
	 * @param uuid
	 * @param dictionaryCode
	 * @return
	 */
	public List<Document> findAllDocument1(String vin, String uuid) {
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.ITEMNAME AS ALIAS FROM DOCUMENTS_1_"+index+" AS A LEFT JOIN DATADICTIONARY AS B ON A.DICTIONARYCODE=B.CODE AND B.ITEMTYPE=? WHERE A.UUID=? ORDER BY A.DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {3,uuid}, new BeanPropertyRowMapper<Document>(Document.class));
		// decode
		for (Document doc : result) {
			doc.setFileFullPath(EncryptionUtils.decryptString(doc.getFileFullPath()));
		}
		return result;
	}
	
	/**
	 * 
	 * @param code
	 * @param uuid
	 * @param vin
	 * @return
	 */
	public Document find(String code,String uuid,String vin) {
		int index = getTableIndex(vin);
		String sql = "SELECT A.*,B.ITEMNAME AS ALIAS FROM DOCUMENTS_1_"+index+" AS A LEFT JOIN DATADICTIONARY AS B ON A.DICTIONARYCODE=B.CODE AND B.ITEMTYPE=? WHERE A.DICTIONARYCODE=? AND A.UUID=?";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {3,code,uuid}, new BeanPropertyRowMapper<Document>(Document.class));
		for (Document doc : result) {
			doc.setFileFullPath(EncryptionUtils.decryptString(doc.getFileFullPath()));
		}
		if(result.size() > 0) {
			return result.get(0);
		}
		return new Document();
	}
	
	/**
	 * 
	 * @param documentUniqueId
	 * @param tableId
	 * @return
	 */
	public Document findById(int documentUniqueId, int tableId) {
		String sql = "SELECT A.*,B.ITEMNAME AS ALIAS FROM DOCUMENTS_1_"+tableId+" AS A LEFT JOIN DATADICTIONARY AS B ON A.DICTIONARYCODE=B.CODE AND B.ITEMTYPE=? WHERE A.DOCUMENTUNIQUEID=?";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {3,documentUniqueId}, new BeanPropertyRowMapper<Document>(Document.class));
		if(result.size() > 0) {
			return result.get(0);
		}
		return new Document();
	}
	
	/**
	 * 创建一个文件
	 * 
	 * @param document
	 * @return
	 */
	public int insert(Document document, String vin) {
		int index = getTableIndex(vin);
	 	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	 	jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String SQL = "INSERT INTO DOCUMENTS_1_"+index+"(UUID,DICTIONARYCODE,FILEFULLPATH,THUMBNAIL) VALUES(?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(
						SQL, new String[] {"documentuniqueid"});
				ps.setString(1, document.getUuid());
				ps.setString(2, document.getDictionarycode());
				ps.setString(3, EncryptionUtils.encryptString(document.getFileFullPath()));
				ps.setBinaryStream(4, new ByteArrayInputStream(document.getThumbnail()), document.getThumbnail().length);
				return ps;
			}
		}, keyHolder);
		
		int documentUniqueId  = keyHolder.getKey().intValue();
		return documentUniqueId;
	}
	
	/**
	 * 更新文件信息
	 * 
	 * @param document
	 */
	public void update(Document document, String vin) {
		int index = getTableIndex(vin);
		update(document, index);
	}
	
	/**
	 * 
	 * @param document
	 * @param tableId
	 */
	public void update(Document document, int tableId) {
		String SQL = "UPDATE DOCUMENTS_1_"+tableId+" SET FILEFULLPATH=?,THUMBNAIL=? WHERE DOCUMENTUNIQUEID=?";
	 	int opt = jdbcTemplate.update(SQL, new Object[] {
	 			EncryptionUtils.encryptString(document.getFileFullPath()),
	 			document.getThumbnail(),
	 			document.getDocumentUniqueId()});
	 	log.info("Affected row="+opt);
	}
	
	/**
	 * 
	 * @param documentUniqueId
	 * @param vin
	 */
	public void deleteById(int documentUniqueId, String vin) {
		int index = getTableIndex(vin);
		String sql = "DELETE FROM DOCUMENTS_1_"+index+" WHERE DOCUMENTUNIQUEID=?";
		int affected = jdbcTemplate.update(sql, new Object[] {documentUniqueId});
		log.info("Affected row "+affected);
	}
	
	/**
	 * 
	 * @param uuid
	 * @param vin
	 */
	public void deleteByUUID(String uuid, String vin) {
		int index = getTableIndex(vin);
		String sql = "DELETE FROM DOCUMENTS_"+1+"_"+index+" WHERE UUID=?";
		int affected = jdbcTemplate.update(sql, new Object[] {uuid});
		log.info("Affected row "+affected);
	}
	
	/**
	 * 
	 * @param code
	 * @param vin
	 */
//	public int deleteByCode(String code, String vin) {
//		int index = getTableIndex(vin);
//		String sql = "DELETE FROM DOCUMENTS_"+1+"_"+index+" WHERE DICTIONARYCODE=?";
//		int affected = jdbcTemplate.update(sql, new Object[] {code});
//		log.info("Affected row "+affected);
//		return affected;
//	}
	
	/**
	 * 
	 * @param vin
	 * @return
	 */
	private int getTableIndex(String vin) {
		int sum = 0;
		for (char c : vin.toCharArray()) {
			sum += c;
		}
		return sum % 256;
	}
	
	/**
	 * 
	 * @param docHistory
	 * @return
	 */
	public int insertHistory(DocumentHistory docHistory) {
	 	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	 	jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String SQL = "INSERT INTO DOCUMENTHISTORY(TABLEID,DOCUMENTUNIQUEID,UUID,DICTIONARYCODE,FILEFULLPATH,THUMBNAIL,USERNAME,DATECREATED) VALUES(?,?,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement(
						SQL, new String[] {"historyuniqueid"});
				ps.setInt(1, docHistory.getTableId());
				ps.setInt(2, docHistory.getDocumentUniqueId());
				ps.setString(3,docHistory.getUuid());
				ps.setString(4, docHistory.getDictionarycode());
				ps.setString(5, docHistory.getFileFullPath());
				ps.setBinaryStream(6, new ByteArrayInputStream(docHistory.getThumbnail()), docHistory.getThumbnail().length);
				ps.setString(7, docHistory.getUserName());
				long millis=System.currentTimeMillis();
				java.sql.Timestamp date=new java.sql.Timestamp(millis);
				ps.setTimestamp(8, date);
				return ps;
			}
		}, keyHolder);
		
		int historyUniqueId  = keyHolder.getKey().intValue();
		log.info("Affected row id= "+historyUniqueId);
		return historyUniqueId;
	}
	
	/**
	 * 
	 * @param documentUniqueId
	 * @return
	 */
	public List<DocumentHistory> findHistoryById(int documentUniqueId) {
		String sql = "SELECT * FROM DOCUMENTHISTORY WHERE DOCUMENTUNIQUEID=? ORDER BY DATECREATED DESC";
		List<DocumentHistory> result = jdbcTemplate.query(sql, new Object[] {documentUniqueId}, new BeanPropertyRowMapper<DocumentHistory>(DocumentHistory.class));
		return result;
	}
}
