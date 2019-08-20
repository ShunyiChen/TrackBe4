package com.maxtree.automotive.dashboard.view.quality;

import com.maxtree.automotive.dashboard.service.AuthService;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.Feedback;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class EditFeedbackWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	public EditFeedbackWindow() {
		this.setWidth("413px");
		this.setHeightUndefined();
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("添加新意见");
		this.addStyleName("edit-window");
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(true);
		mainLayout.setMargin(false);
		
		FormLayout form = new FormLayout();
		form.setSpacing(false);
		form.setMargin(false);
		form.setSizeFull();
		
		suggestionField = new TextField("批改意见:");
		suggestionField.setIcon(VaadinIcons.SUBSCRIPT);
		suggestionField.setReadOnly(false);
		form.addComponents(suggestionField);
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnAdd = new Button("添加");
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("128px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnAdd);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
		subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		mainLayout.addComponents(form, buttonPane);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
		setComponentSize(320, 27);
		
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		bindFields();
		
		// Validating Field Values
		validatingFieldValues();
		
		// Bind an actual concrete Person instance.
		// After this, whenever the user changes the value
		// of nameField, p.setName is automatically called.
		binder.setBean(feedback);
	}
	
	/**
	 * 
	 * @param w
	 * @param h
	 */
	private void setComponentSize(int w, int h) {
		suggestionField.setWidth(w+"px");
		suggestionField.setHeight(h+"px");
	}
	
	/**
	 * 
	 */
	private void bindFields() {
		// Bind nameField to the Person.name property
		// by specifying its getter and setter
		binder.bind(suggestionField, Feedback::getSuggestion, Feedback::setSuggestion);
	}
	
	/**
	 * 
	 */
	private void validatingFieldValues () {
		// Validating Field Values
		binder.forField(suggestionField).withValidator(new StringLengthValidator(
	        "意见长度范围应在1~200个字符",
	        1, 200)) .bind(Feedback::getSuggestion, Feedback::setSuggestion);
		 
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean checkEmptyValues() {
		if (StringUtils.isEmpty(feedback.getSuggestion())) {
			Notification notification = new Notification("提示：", "意见不能为空。", Type.WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			notification.show(Page.getCurrent());
			
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(Callback2 callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditFeedbackWindow w = new EditFeedbackWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
				String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
				User loggedInUser = ui.userService.getUserByUserName(username);
        		w.feedback.setUserName(loggedInUser.getUserName());
    			int feedbackuniqueid = ui.feedbackService.insert(w.feedback);
    			w.close();
    			callback.onSuccessful(feedbackuniqueid);
        	}
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	/**
	 * 
	 * @param feedback
	 * @param callback
	 */
	public static void edit(Feedback feedback, Callback callback) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditFeedbackWindow w = new EditFeedbackWindow();
        Feedback f = ui.feedbackService.findById(feedback.getFeedbackUniqueId());
        w.feedback.setFeedbackUniqueId(f.getFeedbackUniqueId());
        w.suggestionField.setValue(f.getSuggestion());
        w.btnAdd.setCaption("保存");
        w.setCaption("编辑意见");
        w.btnAdd.addClickListener(e -> {
        	if (w.checkEmptyValues()) {
    			ui.feedbackService.update(w.feedback);
    			w.close();
    			callback.onSuccessful();
        	}
		});
        
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private TextField suggestionField;
	private Button btnAdd;
	private Binder<Feedback> binder = new Binder<>();
	private Feedback feedback = new Feedback();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}

