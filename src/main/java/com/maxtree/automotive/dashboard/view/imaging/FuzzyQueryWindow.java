package com.maxtree.automotive.dashboard.view.imaging;

import java.util.List;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.data.Address;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Imaging;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.ShortcutListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class FuzzyQueryWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 * @param grid
	 */
	public FuzzyQueryWindow(TodoListGrid grid) {
		this.grid = grid;
		this.setWidth("420px");
		this.setHeight("100px");
		this.setModal(false);
		this.setResizable(false);
		this.setCaption("车牌号查询");
		mainLayout = new VerticalLayout(); 
		mainLayout.setSizeFull();
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setWidthUndefined();
		toolbar.setHeight("30px");
		btnSearch.setStyleName(ValoTheme.BUTTON_ICON_ONLY);
		btnSearch.setIcon(VaadinIcons.SEARCH);
		Address addr = Yaml.readAddress();
		plateField.setWidth("240px");
		plateField.setPlaceholder("请输入车牌号后5位或后6位");
		plateField.setHeight("28px");
//		plateField.setValue(addr.getLicenseplate());
		plateField.focus();
		ShortcutListener keyListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ENTER, null) {
			/**/
			private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				doSearch();
			}
		};
		plateField.addShortcutListener(keyListener);
		
		Label fieldName = new Label(VaadinIcons.CAR.getHtml()+"车牌号: ");
		Label location = new Label(addr.getLicenseplate());
		fieldName.setContentMode(ContentMode.HTML);
		toolbar.addComponents(fieldName,location,plateField,btnSearch);
		toolbar.setComponentAlignment(fieldName, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(location, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(plateField, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(btnSearch, Alignment.MIDDLE_LEFT);
		mainLayout.addComponents(toolbar);
		this.setContent(mainLayout);
		btnSearch.addClickListener(e -> {
			close();
			doSearch();
		});
	}
	
	/**
	 * 
	 */
	private void doSearch() {
		grid.keyword = plateField.getValue();
		grid.controls.recount();
		
		List<Imaging> rs = ui.imagingService.findAll(20, 0, grid.keyword);
		grid.setPerPageData(rs);
	}
	
	/**
	 * 
	 * @param callback
	 */
	public static void open(TodoListGrid grid) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        FuzzyQueryWindow w = new FuzzyQueryWindow(grid);
        UI.getCurrent().addWindow(w);
        w.center();
    }
	

	private TodoListGrid grid;
	private TextField plateField = new TextField();
	private VerticalLayout mainLayout =null;
	private Button btnSearch = new Button();
	private static DashboardUI ui = (DashboardUI) UI.getCurrent();
}
