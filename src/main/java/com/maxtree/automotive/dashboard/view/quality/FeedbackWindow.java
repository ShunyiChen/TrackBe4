package com.maxtree.automotive.dashboard.view.quality;

import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Feedback;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
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
public class FeedbackWindow extends Window {

	/**
	 * Constructor
	 */
	public FeedbackWindow() {
		initComponents();
	}
	
	private void initComponents() {
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		loggedInUser = ui.userService.getUserByUserName(username);
		this.setModal(true);
		this.setResizable(false);
		this.setClosable(true);
		this.setWidth("672px");
		this.setHeight("400px");
		this.setCaption("反馈意见");
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setWidth("100%");
		vlayout.setHeightUndefined();
		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setWidth("100%");
		List<Feedback> list = ui.feedbackService.findByUserName(loggedInUser.getUserName());
		combobox.setItems(list);
		combobox.setWidth("100%");
		combobox.setTextInputAllowed(true);
		combobox.setEmptySelectionAllowed(true);
		Button btnManage = new Button("管理意见");
		btnManage.setIcon(VaadinIcons.WRENCH);
		btnManage.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		header.addComponents(combobox,Box.createHorizontalBox(5),btnManage);
		header.setComponentAlignment(combobox, Alignment.TOP_LEFT);
		header.setComponentAlignment(btnManage, Alignment.TOP_LEFT);
		header.setExpandRatio(combobox, 1);
		header.setExpandRatio(btnManage, 0);
		
		content.setValue("");
        content.setRows(9);
        content.setWidth("100%");
        content.setIcon(VaadinIcons.EDIT);
        btnApproved.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnReject.addStyleName(ValoTheme.BUTTON_DANGER);
        HorizontalLayout buttonLayout = new HorizontalLayout();
    	buttonLayout.setSpacing(false);
    	buttonLayout.setMargin(false);
    	buttonLayout.setWidthUndefined();
    	buttonLayout.setHeight("40px");
    	buttonLayout.addComponents(btnReject, Box.createHorizontalBox(5), btnApproved, Box.createHorizontalBox(5), btnCancel);
    	buttonLayout.setComponentAlignment(btnApproved, Alignment.MIDDLE_LEFT);
    	buttonLayout.setComponentAlignment(btnReject, Alignment.MIDDLE_LEFT);
    	buttonLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
        
        vlayout.addComponents(header,content,buttonLayout);
        vlayout.setComponentAlignment(header, Alignment.TOP_CENTER);
        vlayout.setComponentAlignment(content, Alignment.TOP_CENTER);
        vlayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
        
        this.setContent(vlayout);
		
		btnCancel.addClickListener(e->{
			close();
		});
		combobox.addValueChangeListener(e -> {
			if(e.getValue() != null) {
				ui.feedbackService.up(e.getValue());
				StringBuilder sb = new StringBuilder(content.getValue());
				sb.append(e.getValue().getSuggestion());
				sb.append("\n");
				content.setValue(sb.toString());
			}
		});
		btnManage.addClickListener(e->{
			Callback callback = new Callback() {
				@Override
				public void onSuccessful() {
					List<Feedback> list = ui.feedbackService.findByUserName(loggedInUser.getUserName());
					combobox.setItems(list);
				}
			};
			
			FeedbackManagementWindow.open(callback);
		});
	}
	
	/**
	 * 
	 * @param accept
	 * @param reject
	 */
	public static void open(Callback2 accept, Callback2 reject) {
        FeedbackWindow w = new FeedbackWindow();
        w.btnApproved.addClickListener(e -> {
        	if (w.content.getValue().length() > 200) {
        		Notifications.warning("字数不得超出200。");
        		return;
        	}
        	w.close();
        	accept.onSuccessful(w.content.getValue());
		});
        
        w.btnReject.addClickListener(e -> {
        	if (w.content.getValue().length() > 200) {
        		Notifications.warning("字数不得超出200。");
        		return;
        	}
        	w.close();
        	reject.onSuccessful(w.content.getValue());
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }

	
	public User loggedInUser;	//登录用户
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private static final long serialVersionUID = 1L;
	private ComboBox<Feedback> combobox = new ComboBox<Feedback>();
	private int rowCount = 1;
	private TextArea content = new TextArea("审批建议:");
	private Button btnCancel = new Button("取消");
	private Button btnApproved = new Button("合格");
	private Button btnReject = new Button("不合格");
}
