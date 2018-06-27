package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.ClickLabel;
import com.maxtree.automotive.dashboard.component.PermissionBox;
import com.maxtree.automotive.dashboard.domain.Permission;
import com.maxtree.automotive.dashboard.domain.PermissionCategory;
import com.maxtree.automotive.dashboard.domain.Role;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AssigningPermissionsToRoleWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param role
	 */
	public AssigningPermissionsToRoleWindow(Role role) {
		this.setWidth("900px");
		this.setHeight("510px");
		this.setModal(true);
		this.setResizable(false);
		this.setCaption("为角色分配权限 - "+role.getRoleName());
		this.addStyleName("set-permissions-to-role-window-"+this.hashCode());
		
		VerticalLayout mainLayout = new VerticalLayout(); 
		mainLayout.setSpacing(false);
		mainLayout.setMargin(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeightUndefined();
 
		Styles styles = Page.getCurrent().getStyles();
    	String css = ".set-permissions-to-role-window-"+this.hashCode()+" { padding:5px !important; }";
    	String css2 = ".set-permissions-to-role-window-"+this.hashCode()+" .v-image {font-size: 13px !important; }";
    	String css3 = ".set-permissions-to-role-window-"+this.hashCode()+" .v-label {font-size: 13px !important; font-weight: bold;}";
    	styles.add(css);
    	styles.add(css2);
    	styles.add(css3);
    	// 初始化获取已分配的权限
        List<Permission> assignedPermissions = ui.roleService.assignedPermissions(role.getRoleUniqueId());
        for (Permission p : assignedPermissions) {
        	assignedPermissionUniqueIDs.add(p.getPermissionUniqueId());
        }
        
    	HorizontalLayout header = selectAllLink();
//		Hr hr = new Hr();
		gridLayout = new GridLayout();
		gridLayout.setSpacing(false);
		gridLayout.setMargin(false);
        gridLayout.setWidth("100%");
        /*
         Did you mean 100% height and undefined width? (to get horizontal scrollbar, but no vertical one)
Of course - if you want to get both scrollbars and the content size can not be set in pixels, one should use setSizeUndefined().
         */
        gridLayout.setHeightUndefined();
        
        generateMatrixGrid();
		
		HorizontalLayout buttonPane = new HorizontalLayout();
		buttonPane.setSizeFull();
		buttonPane.setSpacing(false);
		buttonPane.setMargin(false);
		Button btnCancel = new Button("取消");
		btnOK = new Button("确定");
		btnApply = new Button("应用");
		
		btnCancel.addStyleName("grid-button-without-border");
		btnOK.addStyleName("grid-button-without-border");
		btnApply.addStyleName("grid-button-without-border");
		
		HorizontalLayout subButtonPane = new HorizontalLayout();
		subButtonPane.setSpacing(false);
		subButtonPane.setMargin(false);
		subButtonPane.setWidth("206px");
		subButtonPane.setHeight("100%");
		subButtonPane.addComponents(btnCancel, btnOK, btnApply);
		subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnOK, Alignment.BOTTOM_CENTER);
		subButtonPane.setComponentAlignment(btnApply, Alignment.BOTTOM_CENTER);
		buttonPane.addComponent(subButtonPane);
		buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
		
		Panel scrollPane = new Panel();
		scrollPane.setWidth("100%");
		scrollPane.setHeight("365px");
		scrollPane.setContent(gridLayout);
		
		mainLayout.addComponents(header, Box.createVerticalBox(1), scrollPane, buttonPane);
		mainLayout.setComponentAlignment(header,Alignment.MIDDLE_CENTER);
		mainLayout.setComponentAlignment(scrollPane, Alignment.TOP_CENTER);
		mainLayout.setComponentAlignment(buttonPane, Alignment.MIDDLE_CENTER);
		this.setContent(mainLayout);
		
		btnCancel.addClickListener(e -> {
			close();
		});
	}

	/**
	 * 
	 * @return
	 */
	private Permission[][] getPermissionArray() {
		
		Permission[][] array = new Permission[13][7];
		List<Permission> values = ui.permissionService.findAll();
		String key = null;
		int i = 0;
		int j = 0;
		for (Permission p : values) {
			String group = p.getCode().substring(0, 1);
			if (key == null) {
				key = group;
			}
			if (!key.equals(group)) {
				i++;
				j=0;
			}
			array[i][j] = p;
			
			j++;
			key = group;
		}
		return array;
	}
	
	/**
	 * 
	 * @param rows
	 * @param columns
	 */
	private void generateMatrixGrid() {
		List<PermissionCategory> categories = ui.permissionCategoryService.findAll();
        
        Permission[][] codes = getPermissionArray();
        
        final int rows = codes.length;
        final int columns = codes[0].length + 1;
        gridLayout.removeAllComponents();
        gridLayout.setRows(rows);
        gridLayout.setColumns(columns);
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
            	
            	if (col == 0) {
            		Label category = new Label(categories.get(row).getName()+":");
            		category.setHeight("50px");
                    gridLayout.addComponents(category);
                    gridLayout.setComponentAlignment(category, Alignment.TOP_LEFT);
            		
            	} else {
            		Permission code = codes[row][col-1];
            		if (code != null) {
            			PermissionBox box = new PermissionBox(code);
                	    gridLayout.addComponents(box);
                	    gridLayout.setComponentAlignment(box, Alignment.TOP_LEFT);
                	    
                	    if (assignedPermissionUniqueIDs.contains(code.getPermissionUniqueId())) {
                	    	box.setSelected(true);
                	    }
                	    
            		} else {
            			Label blankLabel = new Label();
            			gridLayout.addComponents(blankLabel);
                	    gridLayout.setComponentAlignment(blankLabel, Alignment.TOP_LEFT);
            		}
            	}
            }
        }
    }
	
//	private HorizontalLayout createOptions(PermissionCategory category) {
//		HorizontalLayout hlayout = new HorizontalLayout();
//		hlayout.setWidthUndefined();
//		hlayout.setSpacing(false);
//		hlayout.setMargin(false);
//		
//		int size = category.getPermissions().size();
//		PermissionBox[] boxes = new PermissionBox[size];
//		for (int i= 0; i < size; i++) {
//			Permission permission = category.getPermissions().get(i);
//			boxes[i] = new PermissionBox(permission);
//			boxes[i].setWidth("135px");
//			// 初始化选中
//			if (assignedPermissionUniqueIDs.contains(permission.getPermissionUniqueId())) {
//				boxes[i].setSelected(true);
//			}
//			
//			hlayout.addComponents(boxes[i]);
//			hlayout.setComponentAlignment(boxes[i], Alignment.TOP_LEFT);
//		}
//		allboxes.put(category.getName(), boxes);
//		return hlayout;
//	}

	/**
	 * 
	 * @return
	 */
	private HorizontalLayout selectAllLink() {
		HorizontalLayout main = new HorizontalLayout();
		main.setWidth("100%");
		main.setHeightUndefined();
		main.setSpacing(false);
		main.setMargin(false);
		HorizontalLayout sub = new HorizontalLayout();
		sub.setWidthUndefined();
		sub.setHeightUndefined();
		sub.setSpacing(false);
		sub.setMargin(false);
		
		ClickLabel checkAll = new ClickLabel("<span style='cursor:pointer;font-size:13px;font-weight: bold;color: #2499DD;'>全选</span>");
		checkAll.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				Iterator<Component> iter = gridLayout.iterator();
				while(iter.hasNext()) {
					Component comp = iter.next();
					if (comp instanceof PermissionBox) {
						PermissionBox box = (PermissionBox) comp;
						box.setSelected(true);
					}
				}
			}
		});
		ClickLabel checkNone = new ClickLabel("<span style='cursor:pointer;font-size:13px;font-weight: bold;color: #2499DD;'>全不选</span>");
		checkNone.addLayoutClickListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				Iterator<Component> iter = gridLayout.iterator();
				while(iter.hasNext()) {
					Component comp = iter.next();
					if (comp instanceof PermissionBox) {
						PermissionBox box = (PermissionBox) comp;
						box.setSelected(false);
					}
				}
			}
		});
		Label separater = new Label("<span style='font-size:13px;font-weight: bold;color: #2499DD;'>|</span>", ContentMode.HTML);
		sub.addComponents(checkAll, separater, checkNone, Box.createHorizontalBox(21));
		sub.setComponentAlignment(checkAll, Alignment.MIDDLE_CENTER);
		sub.setComponentAlignment(separater, Alignment.MIDDLE_CENTER);
		sub.setComponentAlignment(checkNone, Alignment.MIDDLE_CENTER);
		
		main.addComponent(sub);
		main.setComponentAlignment(sub, Alignment.MIDDLE_RIGHT);
		return main;
	}
 
	private void apply(Role role) {
		
		List<Integer> permissionUniqueIDs = new ArrayList<>();
		
		Iterator<Component> iter = gridLayout.iterator();
		while(iter.hasNext()) {
			Component comp = iter.next();
			if (comp instanceof PermissionBox) {
				PermissionBox box = (PermissionBox) comp;
				if (box.isSelected()) {
					permissionUniqueIDs.add(box.getPermission().getPermissionUniqueId());
				}
			}
		}
		ui.roleService.updateRolePermissions(role.getRoleUniqueId(), permissionUniqueIDs);
		
		List<User> assignedUsers = ui.roleService.assignedUsers(role.getRoleUniqueId());
		for (User user : assignedUsers) {
			// update the cache
		    CacheManager.getInstance().getPermissionCache().refresh(user.getUserUniqueId());
		}
	}
	
	public static void open(Callback callback, Role role) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        AssigningPermissionsToRoleWindow w = new AssigningPermissionsToRoleWindow(role);
        w.btnApply.addClickListener(e -> {
        	w.apply(role);
			callback.onSuccessful();
		});
        w.btnOK.addClickListener(e -> {
        	w.apply(role);
			w.close();
			callback.onSuccessful();
		});
        UI.getCurrent().addWindow(w);
        w.center();
    }

	private GridLayout gridLayout;
	private Button btnApply;
	private Button btnOK;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private List<Integer> assignedPermissionUniqueIDs = new ArrayList<>();
}
