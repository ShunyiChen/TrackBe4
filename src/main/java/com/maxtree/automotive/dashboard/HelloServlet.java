package com.maxtree.automotive.dashboard;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.SiteCapacity;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.service.DocumentService;
import com.maxtree.automotive.dashboard.service.SiteService;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.ui.UI;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	DocumentService documentService;
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		DiskFileItemFactory factory = new DiskFileItemFactory();// 将请求消息实体中的每一个项目封装成单独的DiskFileItem (FileItem接口的实现) 对象
		ServletFileUpload upload = new ServletFileUpload(factory);// ServletFileUpload负责处理上传的文件数据,并将表单中每个输入项封装成
																	// 一个FileItem对象中.
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {

			String params = "";// 传入参数
			String params1 = "";// 传入参数2
			boolean upflag = false;
			String fileName = "";// 文件名

			// 通过迭代器
			java.util.List items = upload.parseRequest(request);// 迭代器是一种设计模式，它是一个对象，它可以遍历并选择序列中的对象
			Iterator itr = items.iterator();// 使用方法iterator()要求容器返回一个Iterator
			InputStream inputStream = null;
			while (itr.hasNext()) {// 则是判断当前元素是否存在，并指向下一个元素
				FileItem item = (FileItem) itr.next();
				if (!item.isFormField()) { // 判断该表单项是否是普通类型
					if (item.getName() != null && !item.getName().equals("")) {
						
						String paramString = item.getName();
						String[] parameters = paramString.split("_");
						int siteID = Integer.parseInt(parameters[0]);
						String uuid = parameters[1];
						fileName = parameters[2];
						String batch = "";
						
						System.out.println("==============="+siteID+","+uuid+","+fileName);
						Site site = null;// ui.siteService.findById(3);
						String sql = "SELECT * FROM SITE WHERE SITEUNIQUEID=?";
						List<Site> results = jdbcTemplate.query(sql, new Object[] {siteID}, new BeanPropertyRowMapper<Site>(Site.class));
						if (results.size() > 0) {
							site = results.get(0);
						}
						
						//准备document参数
						String fileFullPath = batch+"/"+uuid+"/"+fileName;
						String vin = "";
						String businessCode = "";
						String dictionarycode = "";
						
						//创建Document
						Document document = new Document();
						document.setUuid(uuid);
						document.setBusinessCode(businessCode);
						document.setDictionarycode(dictionarycode);
						document.setFileFullPath(fileFullPath);
						documentService.insert(document);
						
						
//						fileName = item.getName();
//						int index = fileName.lastIndexOf("\\");
//						fileName = fileName.substring(index + 1, fileName.length());
						inputStream = item.getInputStream();
						inputstreamtofile(inputStream, fileName, site);
//						out.print(fileName);
					}
				} else {
					if ("paramValues".equals(item.getFieldName())) {
						params = URLDecoder.decode(item.getString(), "utf-8");// UrlDecode是对字符串进行URL解码的编码处理函数。
						params1 = URLDecoder.decode(item.getString(), "utf-8");
						System.out.println("params="+params);
						System.out.println("params1="+params1);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}

	}

	/**
	 * 
	 * @param b
	 * @param from
	 * @param end
	 * @return
	 */
	private static byte[] subBytes(byte[] b, int from, int end) {
		byte[] result = new byte[end - from];
		System.arraycopy(b, from, result, 0, end - from);
		return result;
	}

	/**
	 * 
	 * @param ins
	 * @param fileName
	 * @param site
	 */
	public static void inputstreamtofile(InputStream ins, String fileName, Site site) {
		try {
			TB4FileSystem vfs2 = new TB4FileSystem();
			OutputStream os = vfs2.receiveUpload(site, fileName);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
