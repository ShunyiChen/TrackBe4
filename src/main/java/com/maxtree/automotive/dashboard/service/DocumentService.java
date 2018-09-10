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
		String sql = "SELECT A.*,B.ITEMNAME AS ALIAS FROM DOCUMENTS_1_"+index+" AS A LEFT JOIN DATADICTIONARY AS B ON A.DICTIONARYCODE=B.CODE AND B.ITEMTYPE=? WHERE A.UUID=? ORDER BY A.DICTIONARYCODE,A.DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {3,uuid}, new BeanPropertyRowMapper<Document>(Document.class));
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
		int index = getTableIndex(vin);
		String sql = "SELECT * FROM DOCUMENTS_2_"+index+" WHERE UUID=? ORDER BY DOCUMENTUNIQUEID";
		List<Document> result = jdbcTemplate.query(sql, new Object[] {uuid}, new BeanPropertyRowMapper<Document>(Document.class));
		// decode
		int i = 1;
		for (Document doc : result) {
			doc.setFileFullPath(EncryptionUtils.decryptString(doc.getFileFullPath()));
			doc.setAlias("其他材料_"+i);
			i++;
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
		int index = getTableIndex(vin);
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
					ps.setBinaryStream(4, new ByteArrayInputStream(document.getThumbnail()), document.getThumbnail().length);
					return ps;
				}
				else {
					SQL = "INSERT INTO DOCUMENTS_2_"+index+"(UUID,FILEFULLPATH,THUMBNAIL) VALUES(?,?,?)";
					PreparedStatement ps = con.prepareStatement(
							SQL, new String[] {"documentuniqueid"});
					ps.setString(1, document.getUuid());
					ps.setString(2, EncryptionUtils.encryptString(document.getFileFullPath()));
					ps.setBinaryStream(3, new ByteArrayInputStream(document.getThumbnail()), document.getThumbnail().length);
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
		int index = getTableIndex(vin);
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
	 * @param location
	 * @param vin
	 */
	public void deleteById(int documentUniqueId, int location, String vin) {
		int index = getTableIndex(vin);
		String sql = "DELETE FROM DOCUMENTS_"+location+"_"+index+" WHERE DOCUMENTUNIQUEID=?";
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
		
	    sql = "DELETE FROM DOCUMENTS_"+2+"_"+index+" WHERE UUID=?";
		affected = jdbcTemplate.update(sql, new Object[] {uuid});
		log.info("Affected row "+affected);
	}
	
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
	
}
