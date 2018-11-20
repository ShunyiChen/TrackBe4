package com.maxtree.automotive.dashboard.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
import org.apache.commons.fileupload.FileUploadException;
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

/**
 * 前台录入用
 * 
 * @author Chen
 *
 */
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setCharacterEncoding("GBK");
		request.setCharacterEncoding("GBK");
		
        PrintWriter out = response.getWriter();
        DiskFileItemFactory fac = new DiskFileItemFactory();
        //设置缓存文件大小
        fac.setSizeThreshold(1024*1024);
        //缓存文件位置，这里取的是默认的位置
        fac.setRepository(fac.getRepository());
        ServletFileUpload upload = new ServletFileUpload(fac);
        //设置最大允许上传的文件大小，这里是5MB
        upload.setFileSizeMax(1024*1024*5);
        List fileList = null;
        try {
            fileList = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            ex.printStackTrace();
            return;
        }
        Iterator iter = fileList.iterator();
        while(iter.hasNext()){
            FileItem fileItem = (FileItem)iter.next();
            if(!fileItem.isFormField()){
                String name = fileItem.getName();
                String fileSize = new Long(fileItem.getSize()).toString();
                if(name == null || name.equals("") || fileSize.equals("0"))
                    continue;
                //截取出纯文件名
                name = name.substring(name.lastIndexOf("\\")+1);
				String[] parameters = name.split("_");
				Integer userUniqueId = Integer.parseInt(parameters[0]);
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
				InputStream inputStream = null;
				String fileFullPath = p.getBatch()+"/"+p.getUuid()+"/"+name;
				String dictionarycode = p.getDictionaryCode();
				inputStream = fileItem.getInputStream();
				byte[] bytes = IOUtils.toByteArray(inputStream);
				inputStream.close();
				
				//保存原图
				ByteArrayInputStream is = new ByteArrayInputStream(bytes);
				inputstreamtofile(is, fileFullPath, site);
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
        }
        
        out.close();
		
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
