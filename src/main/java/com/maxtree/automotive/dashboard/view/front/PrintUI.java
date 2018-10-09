package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.BrowserFrame;
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
		// 获得html文件并打印该文件
		String htmlFilePath = request.getParameter("htmlFilePath");
//			htmlFile = new File(htmlFilePath);//("reports/generates/"+transactionId+"/report.html");
//			String fileAsString = Files.toString(htmlFile, Charsets.UTF_8);
		
		 // Have some content to print
//	        setContent(new Label(fileAsString, ContentMode.HTML));
		
		
		
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
 			public InputStream getStream() {
 				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream(htmlFilePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return inputStream;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, "report.png");
 		streamResource.setCacheTime(0);
		
		BrowserFrame bf = new BrowserFrame(null);
		bf.setSource(streamResource);
		bf.setSizeFull();
		
		setContent(bf);

        // Print automatically when the window opens
        JavaScript.getCurrent().execute(
            "setTimeout(function() {" +
            "  print(); self.close();}, 0);");
	}
	
	@Override
	public void close() {
//		System.out.println("--------PrintUI--close");
//		new TB4Reports().deleteReportFiles(htmlFile.getParentFile().getPath());
	}
	
	private File htmlFile;
}
