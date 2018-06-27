package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class PrintUI extends UI {
	
    /**
	 * virtual machine
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
    protected void init(VaadinRequest request) {
		try {
			// 获得html文件并打印该文件
			String htmlFilePath = request.getParameter("htmlFilePath");
			htmlFile = new File(htmlFilePath);//("reports/generates/"+transactionId+"/report.html");
			String fileAsString = Files.toString(htmlFile, Charsets.UTF_8);
			
			 // Have some content to print
	        setContent(new Label(fileAsString, ContentMode.HTML));

	        // Print automatically when the window opens
	        JavaScript.getCurrent().execute(
	            "setTimeout(function() {" +
	            "  print(); self.close();}, 0);");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
//		System.out.println("--------PrintUI--close");
//		new TB4Reports().deleteReportFiles(htmlFile.getParentFile().getPath());
	}
	
	private File htmlFile;
}
