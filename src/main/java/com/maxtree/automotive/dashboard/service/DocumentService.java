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
	 * @param uuid
	 * @param vin
	 * @return
	 */
	public List<Document> findPrimary(String uuid, String vin) {
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
		
		String sql = "SELECT * FROM DOCUMENTS_1_"+index+" WHERE UUID=? ORDER BY DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {uuid}, new BeanPropertyRowMapper<Document>(Document.class));
		// decode
		for (Document doc : result) {
			doc.setFileFullPath(EncryptionUtils.decryptString(doc.getFileFullPath()));
		}
		return result;
	}
	
	/**
	 * 获取辅助文件列表
	 * 
	 * @param uuid
	 * @param vin
	 * @return
	 */
	public List<Document> findSecondary(String uuid, String vin) {
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
	public int create(Document document) {
		String vin = document.getVin();
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
	 	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	 	jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String SQL = "";
				if (document.getCategory() == 1) {
					SQL = "INSERT INTO DOCUMENTS_1_"+index+"(UUID,ALIAS,FILEFULLPATH) VALUES(?,?,?)";
				} else {
					SQL = "INSERT INTO DOCUMENTS_2_"+index+"(UUID,ALIAS,FILEFULLPATH) VALUES(?,?,?)";
				}
				PreparedStatement ps = con.prepareStatement(
						SQL, new String[] {"documentuniqueid"});
				ps.setString(1, document.getUuid());
				ps.setString(2, document.getAlias());
				ps.setString(3, EncryptionUtils.encryptString(document.getFileFullPath()));
				return ps;
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
		String vin = document.getVin();
		int number = Integer.parseInt(vin.substring(vin.length() - 6));
		int index = number % 256;
		String SQL = "";
		if (document.getCategory() == 1) {
			SQL = "UPDATE DOCUMENTS_1_"+index+" SET FILEFULLPATH=? WHERE DOCUMENTUNIQUEID=?";
		} else {
			SQL = "UPDATE DOCUMENTS_2_"+index+" SET FILEFULLPATH=? WHERE DOCUMENTUNIQUEID=?";
		}
	 	int opt = jdbcTemplate.update(SQL, new Object[] {EncryptionUtils.encryptString(document.getFileFullPath()), document.getDocumentUniqueId()});
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
}
