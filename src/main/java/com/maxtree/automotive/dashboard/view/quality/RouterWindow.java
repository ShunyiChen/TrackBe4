package com.maxtree.automotive.dashboard.view.quality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.CommonProblem;
import com.maxtree.automotive.dashboard.domain.User;
import com.vaadin.event.ShortcutListener;
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
public class RouterWindow extends Window {

	/**
	 * Constructor
	 */
	public RouterWindow() {
		initComponents();
	}
	
	private void initComponents() {
		loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		this.setModal(true);
		this.setResizable(false);
		this.setClosable(true);
		this.setWidth("672px");
		this.setHeight("400px");
		this.setCaption("质检建议");
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setWidth("100%");
		vlayout.setHeightUndefined();
		HorizontalLayout header = new HorizontalLayout();
		header.setMargin(false);
		header.setSpacing(false);
		header.setWidthUndefined();
		header.setHeightUndefined();
		
		List<CommonProblem> list = ui.commonProblemService.findByUserName(loggedInUser.getUserName());
		combobox.setItems(list);
		combobox.setWidth("490px");
		combobox.setTextInputAllowed(true);
		combobox.setEmptySelectionAllowed(true);
		ShortcutListener keyListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				add();
			}
		};
		combobox.addShortcutListener(keyListener);
//		combobox.setNewItemHandler(inputString -> {
//			
//			List<String> list = new ArrayList<String>();
//			for(String str : obj.getComments()) {
//				list.add(str);
//			}
//			list.add(inputString.toString());
////
////		    Planet newPlanet = new Planet(planets.size(), inputString);
////		    planets.add(newPlanet);
////
////		    // Update combobox content
//			combobox.setItems(list);
//			
//			combobox.setSelectedItem(inputString);
//			
////		    return Optional.of(inputString);
//		});
		
		
		Button btnAdd = new Button("添加");
		Button btnClear = new Button("清空");
		header.addComponents(combobox,Box.createHorizontalBox(5),btnAdd,Box.createHorizontalBox(5),btnClear);
		header.setComponentAlignment(combobox, Alignment.TOP_LEFT);
		header.setComponentAlignment(btnAdd, Alignment.TOP_LEFT);
		header.setComponentAlignment(btnClear, Alignment.TOP_LEFT);
		
		content.setValue("");
        content.setRows(9);
        content.setWidth("100%");
        content.setIcon(VaadinIcons.EDIT);
        btnApproved.addStyleName(ValoTheme.BUTTON_PRIMARY);
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
		btnAdd.addClickListener(e->{
			add();
		});
		btnClear.addClickListener(e->{
			clear();
		});
	}
	
	private void add() {
		
		
		
//		CommonProblem item = combobox.getValue();
//		if(!StringUtils.isEmpty(item)) {
//			StringBuilder stb = new StringBuilder(content.getValue());
//			stb.append(rowCount);
//			stb.append(".");
//			if(item.contains("_")) {
//				stb.append(item.substring(item.indexOf("_")+1));
//			}
//			else {
//				stb.append(item);
//			}
//			
//			stb.append("\n");
//			content.setValue(stb.toString());
//			rowCount++;
//			combobox.setSelectedItem("");
//		}
		
	}
	
	private void clear() {
		content.setValue("");
		rowCount = 1;
	}
	
	/**
	 * 
	 * @param accept
	 * @param reject
	 */
	public static void open(Callback2 accept, Callback2 reject) {
        RouterWindow w = new RouterWindow();
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
	private ComboBox<CommonProblem> combobox = new ComboBox<CommonProblem>();
	private int rowCount = 1;
	private TextArea content = new TextArea("审批建议:");
	private Button btnCancel = new Button("取消");
	private Button btnApproved = new Button("合格");
	private Button btnReject = new Button("不合格");
}
