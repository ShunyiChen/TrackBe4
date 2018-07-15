package com.maxtree.automotive.dashboard.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
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
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.service.DocumentService;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;

import net.coobird.thumbnailator.Thumbnails;

@WebServlet("/hello")
public class UploadFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	DocumentService documentService;
	//userUniqueId作为Key
	public static Map<Integer, List<UploadOutDTO>> OUT_DTOs = new HashMap<Integer, List<UploadOutDTO>>();
	//userUniqueId作为Key
	public static Map<Integer, UploadInDTO> IN_DTOs = new HashMap<Integer, UploadInDTO>();
	
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
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
						Integer userUniqueId = Integer.parseInt(parameters[0]);
						fileName = parameters[1];
						
//						Integer userUniqueId = 2;
//						fileName = new File(item.getName()).getName();
						
						UploadInDTO p = IN_DTOs.get(userUniqueId);
						if (!checkEmpty(p)) {
							return;
						}
						
						Site site = null;
						String sql = "SELECT * FROM SITE WHERE SITEUNIQUEID=?";
						List<Site> results = jdbcTemplate.query(sql, new Object[] {p.getSiteID()}, new BeanPropertyRowMapper<Site>(Site.class));
						if (results.size() > 0) {
							site = results.get(0);
						}
						
						//准备document参数
						String fileFullPath = p.getBatch()+"/"+p.getUuid()+"/"+fileName;
						String dictionarycode = p.getDictionaryCode();
						inputStream = item.getInputStream();
						byte[] bytes = IOUtils.toByteArray(inputStream);
						inputStream.close();
						
						//保存原图
						ByteArrayInputStream is = new ByteArrayInputStream(bytes);
						inputstreamtofile(is, fileFullPath, site);
//						out.print(fileName);
						//生成缩略图
						is = new ByteArrayInputStream(bytes);
						ByteArrayOutputStream smallOutputStream = new ByteArrayOutputStream();
						Thumbnails.of(is).size(100, 100)/*.scale(0.033f)*/.toOutputStream(smallOutputStream);//.toFile("devices/"+userUniqueId+"/thumbnails/"+documentUniqueId+".jpg");  
						is.close();
						
						//创建Document
						Document document = new Document();
						document.vin = p.getVin();
						document.location = dictionarycode.equals("$$$$")?2:1;//1:主要材料 2:次要材料 
						document.setUuid(p.getUuid());
						document.setDictionarycode(dictionarycode);
						document.setFileFullPath(fileFullPath);
						document.setThumbnail(smallOutputStream.toByteArray());
						int documentUniqueId = documentService.insert(document);
						
						UploadOutDTO ufq = new UploadOutDTO();
						ufq.location = dictionarycode.equals("$$$$")?2:1;//1:主要材料 2:次要材料  
						ufq.thumbnail = new ByteArrayInputStream(smallOutputStream.toByteArray());
						ufq.setDictionaryCode(dictionarycode);
						ufq.setDocumentUniqueId(documentUniqueId);
						ufq.setUserUniqueId(userUniqueId);
						ufq.setFileFullPath(fileFullPath);
						ufq.setRemovable(0);
						smallOutputStream.close();
						
						if(OUT_DTOs.get(userUniqueId) == null) {
							OUT_DTOs.put(userUniqueId, new ArrayList<UploadOutDTO>());
						}
						List<UploadOutDTO> list = OUT_DTOs.get(userUniqueId);
						list.add(ufq);
						
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

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}
	
	/**
	 * 
	 * @param p
	 * @return
	 */
	private boolean checkEmpty(UploadInDTO p) {
		if (StringUtils.isEmpty(p.getVin())
				|| StringUtils.isEmpty(p.getUuid())
				|| StringUtils.isEmpty(p.getSiteID()+"")
				|| StringUtils.isEmpty(p.getDictionaryCode())
				|| StringUtils.isEmpty(p.getBatch())) {
			return false;
		}
		else {
			
			return true;
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
	 * @param fileFullPath
	 * @param site
	 */
	public static void inputstreamtofile(InputStream ins, String fileFullPath, Site site) {
		try {
			TB4FileSystem vfs2 = new TB4FileSystem();
			OutputStream os = vfs2.receiveUpload(site, fileFullPath);
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
