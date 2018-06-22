package com.maxtree.automotive.dashboard.view.search;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.component.Box;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 
 * @author chens
 *
 */
public class CriterionHLayout extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public CriterionHLayout(VerticalLayout parentVLayout, CriterionModel model) {
		this(parentVLayout, model, false);
	}
	
	/**
	 * 
	 * @param parentVLayout
	 * @param model
	 * @param isFirstLayout
	 */
	public CriterionHLayout(VerticalLayout parentVLayout, CriterionModel model, boolean isFirstLayout) {
		this.isFirstLayout = isFirstLayout;
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidthUndefined();//.setWidth("90%");
		Label label = new Label("字段...");
		label.setWidth("56px");
		
		List<String> namesData = new ArrayList<>();
		namesData.add("业务流水号");
		namesData.add("条形码");
		namesData.add("号牌种类");
		namesData.add("号码号牌");
		namesData.add("车辆识别代码");
		namesData.add("业务状态");
		namesData.add("创建日期");
		namesData.add("最后修改日期");
		namesData.add("办结日期");
        propertyName = new ComboBox<>(null, namesData);
		propertyName.setEmptySelectionAllowed(true);
		propertyName.setTextInputAllowed(false);
		propertyName.setWidth("140px");
		if (model != null)
		propertyName.setValue(model.getKey());
		propertyName.addValueChangeListener(e -> {
			
			if (e.getValue() != null && e.getValue().endsWith("日期")) {
				List<String> matchData = new ArrayList<>();
				matchData.add("包含");
				matchData.add("不包含");
				matchData.add("等于");
				matchData.add("不等于");
				matchData.add("大于");
				matchData.add("小于");
				match.setItems(matchData);
			} else {
				List<String> matchData = new ArrayList<>();
				matchData.add("包含");
				matchData.add("不包含");
				matchData.add("等于");
				matchData.add("不等于");
				match.setItems(matchData);
			}
		});
		
		List<String> matchData = new ArrayList<>();
		matchData.add("包含");
		matchData.add("不包含");
		matchData.add("等于");
		matchData.add("不等于");
		matchData.add("大于");
		matchData.add("小于");
	    match = new ComboBox<>(null, matchData);
		match.setEmptySelectionAllowed(true);
		match.setTextInputAllowed(false);
		match.setWidth("98px");
		if (model != null)
		match.setValue(model.getMatching());
		
		value = new TextField();
		value.setWidth("184px");
		if (model != null)
		value.setValue(model.getValue());
		
		List<String> connectorData = new ArrayList<>();
		connectorData.add("并且");
		connectorData.add("或者");
	    connector = new ComboBox<>(null, connectorData);
		connector.setEmptySelectionAllowed(true);
		connector.setTextInputAllowed(false);
		connector.setWidth("80px");
		if (model != null)
		connector.setValue(model.getConnector());
		
		Button add = new Button();
		add.setIcon(VaadinIcons.PLUS);
		add.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		Button minus = new Button();
		minus.setIcon(VaadinIcons.MINUS);
		minus.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		
		add.addClickListener(e -> {
			HorizontalLayout component = (HorizontalLayout) add.getParent();
			int index = parentVLayout.getComponentIndex(component);
			HorizontalLayout newComponent = new CriterionHLayout(parentVLayout, null);
			parentVLayout.addComponent(newComponent, index + 1);
			parentVLayout.setComponentAlignment(newComponent, Alignment.TOP_LEFT);
		});
		minus.addClickListener(e -> {
			if (isFirstLayout) {
				propertyName.setValue("");
				match.setValue("");
				value.setValue("");
				connector.setValue("");
				return;
			} else {
				HorizontalLayout component = (HorizontalLayout) add.getParent();
				parentVLayout.removeComponent(component);
			}
		});
		add.addStyleName("image-cursor");
		minus.addStyleName("image-cursor");
		this.addComponents(label, Box.createHorizontalBox(3), propertyName, Box.createHorizontalBox(3), match, Box.createHorizontalBox(3), value, Box.createHorizontalBox(3), connector, Box.createHorizontalBox(5), add, Box.createHorizontalBox(5), minus);
		this.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(propertyName, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(match, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(value, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(connector, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(add, Alignment.MIDDLE_LEFT);
		this.setComponentAlignment(minus, Alignment.MIDDLE_LEFT);
	}
	
	public void set(CriterionModel model) {
		model.setKey(propertyName.getValue());
		model.setMatching(match.getValue());
		model.setValue(value.getValue());
		model.setConnector(connector.getValue());
	}
	
	private boolean isFirstLayout = false;
	private ComboBox<String> propertyName;
	private ComboBox<String> match;
	private TextField value;
	private ComboBox<String> connector;
}
