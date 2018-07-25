package com.maxtree.automotive.dashboard.view.quality;

import java.util.Arrays;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.Suggestion;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.vaadin.icons.VaadinIcons;
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
public class Router extends Window {

	/**
	 * Constructor
	 */
	public Router() {
		initComponents();
	}
	
	private void initComponents() {
		this.setModal(true);
		this.setResizable(true);
		this.setClosable(true);
		this.setWidth("672px");
		this.setHeight("400px");
		this.setCaption("质检建议");
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setWidth("100%");
		vlayout.setHeightUndefined();
		Suggestion suggest = Yaml.readSuggestion();
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setWidth("100%");
		combobox.setItems(Arrays.asList(suggest.getSuggestions()));
		combobox.setTextInputAllowed(true);
		combobox.setEmptySelectionAllowed(false);
		combobox.setWidth("100%");
		combobox.setSelectedItem(suggest.getSuggestions()[0]);
		Button btnAdd = new Button("添加");
		hLayout.addComponents(combobox,btnAdd);
		hLayout.setComponentAlignment(combobox, Alignment.MIDDLE_LEFT);
		hLayout.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
		
		content.setValue("");
        content.setRows(9);
        content.setWidth("100%");
        content.setIcon(VaadinIcons.EDIT);
        btnOk.addStyleName(ValoTheme.BUTTON_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
    	buttonLayout.setSpacing(false);
    	buttonLayout.setMargin(false);
    	buttonLayout.setWidthUndefined();
    	buttonLayout.setHeight("40px");
    	buttonLayout.addComponents(btnBad, Box.createHorizontalBox(5), btnOk, Box.createHorizontalBox(5), btnCancel);
    	buttonLayout.setComponentAlignment(btnOk, Alignment.MIDDLE_LEFT);
    	buttonLayout.setComponentAlignment(btnBad, Alignment.MIDDLE_LEFT);
    	buttonLayout.setComponentAlignment(btnCancel, Alignment.MIDDLE_LEFT);
        
        vlayout.addComponents(hLayout,content,buttonLayout);
        vlayout.setComponentAlignment(hLayout, Alignment.TOP_CENTER);
        vlayout.setComponentAlignment(content, Alignment.TOP_CENTER);
        vlayout.setComponentAlignment(buttonLayout, Alignment.TOP_RIGHT);
        
        this.setContent(vlayout);
		
		btnCancel.addClickListener(e->{
			close();
		});
		btnAdd.addClickListener(e->{
			String item = combobox.getValue();
			StringBuilder stb = new StringBuilder(content.getValue());
			stb.append(item);
			stb.append("\n");
			content.setValue(stb.toString());
		});
	}
	
	/**
	 * 
	 * @param accept
	 * @param reject
	 */
	public static void open(Callback2 accept, Callback2 reject) {
        Router w = new Router();
        w.btnOk.addClickListener(e -> {
        	
        	if (w.content.getValue().length() > 160) {
        		Notifications.warning("字数不得超出160。");
        		return;
        	}
        	
        	w.close();
        	accept.onSuccessful(w.content.getValue());
		});
        
        w.btnBad.addClickListener(e -> {
        	
        	w.close();
        	reject.onSuccessful(w.content.getValue());
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ComboBox<String> combobox = new ComboBox<String>();
	private TextArea content = new TextArea("审批建议:");
	private Button btnCancel = new Button("取消");
	private Button btnOk = new Button("合格");
	private Button btnBad = new Button("不合格");
}
