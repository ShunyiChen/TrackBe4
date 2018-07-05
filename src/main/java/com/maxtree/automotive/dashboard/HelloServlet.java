package com.maxtree.automotive.dashboard;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.SiteCapacity;
import com.maxtree.automotive.dashboard.exception.FileException;
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
	
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("===============beginning");
		Site site = null;// ui.siteService.findById(3);
		String sql = "SELECT * FROM SITE WHERE SITEUNIQUEID=?";
		List<Site> results = jdbcTemplate.query(sql, new Object[] {3}, new BeanPropertyRowMapper<Site>(Site.class));
		if (results.size() > 0) {
			
		   site = results.get(0);
		}
		
		System.out.println("site==============="+site.getSiteName());
		String targetPath = (String) request.getAttribute("fileFullPath");
		System.out.println("targetPath==============="+targetPath);
		
		if (targetPath == null) {
			targetPath = "aa.jpg";
		}
		
		TB4FileSystem vfs2 = new TB4FileSystem();
		
		try {
			InputStream in = request.getInputStream();
			IOUtils.copy(in,  vfs2.receiveUpload(site, targetPath));
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (FileException e) {
			e.printStackTrace();
		}
		
		System.out.println("===============end");
    	
        try {
            response.getOutputStream().write("hello".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
