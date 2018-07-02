package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("mytheme")
public class MyPopupUI extends UI {
    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Popup Window");
        
        Button close = new Button("Close Window", event -> {// Java 8
            // Close the popup
            JavaScript.eval("close()");

            // Detach the UI from the session
            getUI().close();
        });
        
        String htmlFilePath = request.getParameter("htmlFilePath");
		htmlFile = new File(htmlFilePath);//("reports/generates/"+transactionId+"/report.html");
		String fileAsString;
		try {
			fileAsString = Files.toString(htmlFile, Charsets.UTF_8);
			
		    // Have some content to print
	        setContent(new Label(fileAsString, ContentMode.HTML));
			
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