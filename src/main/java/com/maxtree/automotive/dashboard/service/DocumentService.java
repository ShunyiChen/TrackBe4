package com.maxtree.automotive.dashboard.service;

import java.io.IOException;
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
	public List<Document> findAllDocument1(String vin, String uuid, String dictionaryCode) {
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
		
		String sql = "SELECT * FROM DOCUMENTS_1_"+index+" WHERE UUID=? AND DICTIONARYCODE=? ORDER BY DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {uuid, dictionaryCode}, new BeanPropertyRowMapper<Document>(Document.class));
		// decode
		for (Document doc : result) {
			doc.setFileFullPath(EncryptionUtils.decryptString(doc.getFileFullPath()));
		}
		return result;
	}
	
	/**
	 * 获取辅助文件列表
	 * 
	 * @param vin
	 * @param uuid
	 * @return
	 */
	public List<Document> findAllDocument2(String vin, String uuid) {
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
		
		String sql = "SELECT * FROM DOCUMENTS_2_"+index+" WHERE UUID=? ORDER BY DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {uuid}, new BeanPropertyRowMapper<Document>(Document.class));
		// decode
		for (Document doc : result) {
			doc.setFileFullPath(EncryptionUtils.decryptString(doc.getFileFullPath()));
		}
		return result;
	}
	
	/**
	 * 创建一个文件
	 * 
	 * @param document
	 * @return
	 */
	public int insert(Document document) {
		String vin = document.vin;
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
	 	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	 	jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String SQL = "";
				if (document.location == 1) {
					SQL = "INSERT INTO DOCUMENTS_1_"+index+"(UUID,DICTIONARYCODE,FILEFULLPATH,THUMBNAIL) VALUES(?,?,?,?)";
					PreparedStatement ps = con.prepareStatement(
							SQL, new String[] {"documentuniqueid"});
					ps.setString(1, document.getUuid());
					ps.setString(2, document.getDictionarycode());
					ps.setString(3, EncryptionUtils.encryptString(document.getFileFullPath()));
					try {
						ps.setBinaryStream(4, document.getThumbnail(), document.getThumbnail().available());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return ps;
				}
				else {
					SQL = "INSERT INTO DOCUMENTS_2_"+index+"(UUID,FILEFULLPATH,THUMBNAIL) VALUES(?,?,?)";
					PreparedStatement ps = con.prepareStatement(
							SQL, new String[] {"documentuniqueid"});
					ps.setString(1, document.getUuid());
					ps.setString(2, EncryptionUtils.encryptString(document.getFileFullPath()));
					try {
						ps.setBinaryStream(3, document.getThumbnail(), document.getThumbnail().available());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return ps;
				}
				
			}
		}, keyHolder);
		
		int documentUniqueId  = keyHolder.getKey().intValue();
		log.info("Affected row id= "+documentUniqueId);
		return documentUniqueId;
	}
	
	/**
	 * 更新文件信息
	 * 
	 * @param document
	 */
	public void update(Document document) {
		String vin = document.vin;
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
		String SQL = "";
		if (document.location == 1) {
			SQL = "UPDATE DOCUMENTS_1_"+index+" SET FILEFULLPATH=?,THUMBNAIL=? WHERE DOCUMENTUNIQUEID=?";
		} else {
			SQL = "UPDATE DOCUMENTS_2_"+index+" SET FILEFULLPATH=?,THUMBNAIL=? WHERE DOCUMENTUNIQUEID=?";
		}
	 	int opt = jdbcTemplate.update(SQL, new Object[] {
	 			EncryptionUtils.encryptString(document.getFileFullPath()),
	 			document.getThumbnail(),
	 			document.getDocumentUniqueId()});
	 	log.info("Affected row="+opt);
	}
	
	/**
	 * 
	 * @param documentUniqueId
	 */
	public void deleteById(int documentUniqueId, String vin) {
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
		String sql = "DELETE FROM DOCUMENTS_2_"+index+" WHERE DOCUMENTUNIQUEID=?";
		int affected = jdbcTemplate.update(sql, new Object[] {documentUniqueId});
		log.info("Affected row "+affected);
	}
	
//	/**
//	 * 
//	 * @param userUniqueId
//	 * @return
//	 */
//	public List<UploadedFileQueue> findAllUploadedFileQueue(int userUniqueId) {
//		String sql = "SELECT * FROM UPLOADEDFILEQUEUE WHERE USERUNIQUEID=? AND REMOVABLE=?";
//		List<UploadedFileQueue> result = jdbcTemplate.query(sql, new Object[] {userUniqueId, 0}, new BeanPropertyRowMapper<UploadedFileQueue>(UploadedFileQueue.class));
//		
//		return result;
//	}
//	
//	/**
//	 * 
//	 */
//	public void insertUploadedFileQueue(UploadedFileQueue ufq) {
//		String sql = "INSERT INTO UPLOADEDFILEQUEUE(USERUNIQUEID,DICTIONARYCODE,DOCUMENTUNIQUEID,REMOVABLE) VALUES(?,?,?,?)";
//		int opt = jdbcTemplate.update(sql, new Object[] {ufq.getUserUniqueId(), ufq.getDictionaryCode(), ufq.getDocumentUniqueId(), ufq.getRemovable()});
//	 	log.info("Affected row="+opt);
//	}
//	
//	public void updateUploadedFileQueue(int queueUniqueId) {
//		String sql = "UPDATE UPLOADEDFILEQUEUE SET REMOVABLE=? WHERE QUEUEUNIQUEID=?";
//		int opt = jdbcTemplate.update(sql, new Object[] {1, queueUniqueId});
//	 	log.info("Affected row="+opt);
//	}
//	
//	/**
//	 * 
//	 * @param userUniqueId
//	 * @param removable
//	 */
//	public void deleteUploadedFileQueue(int userUniqueId, int removable) {
//		String sql = "DELETE FROM UPLOADEDFILEQUEUE WHERE USERUNIQUEID=? AND REMOVABLE=?";
//		int opt = jdbcTemplate.update(sql, new Object[] {userUniqueId, removable});
//	 	log.info("Affected row="+opt);
//	}
}
