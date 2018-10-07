package com.maxtree.automotive.dashboard.domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.maxtree.automotive.dashboard.exception.FileException;
import com.vaadin.server.StreamResource;

public class Document {

	public Integer getDocumentUniqueId() {
		return documentUniqueId;
	}

	public void setDocumentUniqueId(Integer documentUniqueId) {
		this.documentUniqueId = documentUniqueId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDictionarycode() {
		return dictionarycode;
	}

	public void setDictionarycode(String dictionarycode) {
		this.dictionarycode = dictionarycode;
	}

	public byte[] getThumbnail() {
		if(thumbnail == null || thumbnail.length == 0) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			FileInputStream input = null;
			try {
				input = new FileInputStream("../VAADIN/themes/dashboard/img/frontui/none_128px_31202_easyicon.net.png");
				byte[] buffer = new byte[4096];
			    int n = 0;
			    while (-1 != (n = input.read(buffer))) {
			        output.write(buffer, 0, n);
			    }
			    thumbnail = output.toByteArray();
			    
			} catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					input.close();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return thumbnail;
	}

	public void setThumbnail(byte[] thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFileFullPath() {
		return fileFullPath;
	}

	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return alias;
	}

	private Integer documentUniqueId = 0; // 文档ID
	private String uuid; // 文件UUID
	private String dictionarycode; // 资料CODE
	private String fileFullPath; // 文件实际存放全路径
	private byte[] thumbnail;//缩略图
	public String vin;			//车辆识别代码，用于获取表索引（非数据表字段）
	public int location = 1;    //存储位置1:主文档表2:次文档表（非数据表字段）
	public String alias;//材料别名（非数据表字段）
}
