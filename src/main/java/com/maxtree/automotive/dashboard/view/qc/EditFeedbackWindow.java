package com.maxtree.automotive.dashboard.view.qc;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class EditFeedbackWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public EditFeedbackWindow() {
		initComponents();
	}
	
	private void initComponents() {
		this.setModal(true);
		this.setResizable(true);
		this.setClosable(true);
		this.setWidth("500px");
		this.setHeight("350px");
		this.setCaption("添加反馈意见");
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setWidth("100%");
		vlayout.setHeightUndefined();
        textArea.setValue("");
        textArea.setRows(9);
        textArea.setWidth("100%");
        textArea.setIcon(VaadinIcons.EDIT);
        
        btnAccepted.addStyleName(ValoTheme.BUTTON_PRIMARY);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
    	buttonLayout.setSpacing(false);
    	buttonLayout.setMargin(false);
    	buttonLayout.setWidthUndefined();
    	buttonLayout.setHeight("40px");
    	buttonLayout.addComponents(btnBack, Box.createHorizontalBox(5), btnAccepted, Box.createHorizontalBox(5), btnCancel);
    	buttonLayout.setComponentAlignment(btnAccepted, Alignment.MIDDLE_LEFT);
    	buttonLayout.setComponentAlignment(btnBack, Alignment.MIDDLE_LEFT);
    	buttonLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
        
        vlayout.addComponents(textArea, buttonLayout);
        vlayout.setComponentAlignment(textArea, Alignment.TOP_CENTER);
        vlayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
        
        this.setContent(vlayout);
		
		btnCancel.addClickListener(e->{
			close();
		});
	}
	
	/**
	 * 
	 * @param accept
	 * @param sendback
	 * @param showingAcceptButton
	 */
	public static void open(Callback2 accept, Callback2 sendback) {
        EditFeedbackWindow w = new EditFeedbackWindow();
        w.btnAccepted.addClickListener(e -> {
        	
        	if (w.textArea.getValue().length() > 160) {
        		Notifications.warning("字数不得超出160。");
        		return;
        	}
        	
        	w.close();
        	accept.onSuccessful(w.textArea.getValue());
		});
        
        w.btnBack.addClickListener(e -> {
        	
        	w.close();
        	sendback.onSuccessful(w.textArea.getValue());
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Button btnCancel = new Button("取消");
	private Button btnAccepted = new Button("合格");
	private Button btnBack  = new Button("不合格");
	private TextArea textArea = new TextArea("反馈意见:");
}
