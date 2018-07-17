package com.maxtree.automotive.dashboard.view.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.CodeGenerator;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Hr;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.DenseFrame;
import com.maxtree.automotive.dashboard.domain.Storehouse;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Notification.Type;

public class ManageDenseFrameWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param storehouse
	 */
	public ManageDenseFrameWindow(Storehouse storehouse) {
		this.storehouse = storehouse;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("库房"+storehouse.getName());
		this.setSizeFull();
		this.setClosable(true);
		this.setResizable(true);
		this.setModal(true);
		
		Panel mainPane = new Panel();
		mainPane.setSizeFull();
		VerticalLayout main = new VerticalLayout();
		main.setMargin(false);
		main.setSpacing(false);
		main.setWidth("100%");
		main.setHeightUndefined();
		
		clayout = new CssLayout() {
            @Override
            protected String getCss(final Component c) {
                return "";//"font-size: " + (12 + getComponentIndex(c)) + "px";
            }
        };
        clayout.addStyleName("csslayout-overflow");
		
		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(false);
		toolbar.setMargin(false);
		toolbar.setWidthUndefined();
		toolbar.setHeight("40px");
		btnAdd.setDescription("添加新密集架");
		btnEdit.setDescription("编辑密集架");
		btnRemove.setDescription("删除密集架");
		btnCopy.setDescription("复制密集架");
		
		toolbar.addComponents(btnAdd, Box.createHorizontalBox(5), btnEdit, Box.createHorizontalBox(5), btnRemove, Box.createHorizontalBox(5), btnCopy, Box.createHorizontalBox(5), selectAll);
		toolbar.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(btnEdit, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(btnRemove, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(btnCopy, Alignment.MIDDLE_LEFT);
		toolbar.setComponentAlignment(selectAll, Alignment.MIDDLE_LEFT);
		
		btnAdd.addClickListener(e -> {
			Callback2 callback = new Callback2() {

				@Override
				public void onSuccessful(Object... objects) {
					DenseFrame denseFrame = (DenseFrame) objects[0];
					DenseFrameComponent component = new DenseFrameComponent(denseFrame);
					add(component);
				}
			};
			EditDenseFrameWindow.open(storehouse, callback);
        });
		
		btnEdit.addClickListener(e -> {
			Iterator<Component> iter = clayout.iterator();
			while(iter.hasNext()) {
				Component comp = iter.next();
				if (comp instanceof DenseFrameComponent) {
					DenseFrameComponent dcomp = (DenseFrameComponent) comp;
					if (dcomp.isSelected()) {
						
						Callback2 callback = new Callback2() {

							@Override
							public void onSuccessful(Object... objects) {
								clayout.removeComponent(dcomp);
								
								DenseFrame denseFrame = (DenseFrame) objects[0];
								DenseFrameComponent component = new DenseFrameComponent(denseFrame);
								add(component);
							}
						};
						
						EditDenseFrameWindow.edit(dcomp.getDenseFrame(), callback);
						break;
					}
				}
			}
			
        });
        
		btnRemove.addClickListener(e -> {
			Callback okEvent = new Callback() {
				@Override
				public void onSuccessful() {
					List<Component> removeableList = new ArrayList<Component>();
					Iterator<Component> iter = clayout.iterator();
					while(iter.hasNext()) {
						Component comp = iter.next();
						if (comp instanceof DenseFrameComponent) {
							DenseFrameComponent dcomp = (DenseFrameComponent) comp;
							if (dcomp.isSelected()) {
								ui.storehouseService.deleteDenseFrame(dcomp.getDenseFrame().getStorehouseSN());
								removeableList.add(comp);
							}
						}
					}
					// remove component on the UI
					removeComponent(removeableList);
				}
			};
			
			MessageBox.showMessage("删除提示", "注意：删除密集架将会删除其所有的单元格和档案袋。<br>请确定是否彻底删除密集架？", MessageBox.INFO, okEvent, "删除");
			
        });
		
		btnCopy.addClickListener(e -> {
        	
        	List<DenseFrameComponent> selected = new ArrayList<DenseFrameComponent>();
        	Iterator<Component> iter = clayout.iterator();
			while(iter.hasNext()) {
				Component comp = iter.next();
				if (comp instanceof DenseFrameComponent) {
					DenseFrameComponent dcomp = (DenseFrameComponent) comp;
					if (dcomp.isSelected()) {
						selected.add(dcomp);
					}
				}
			}
			
			if (selected.size() == 0) {
				Notifications.warning("复制前至少选择一个密集架作为模板。");
			} else {
				
				Callback event = new Callback() {

					@Override
					public void onSuccessful() {
						for (DenseFrameComponent dcomp : selected) {
							DenseFrame df = dcomp.getDenseFrame();
							
							DenseFrame frame = new DenseFrame();
							frame.setName(df.getName()+"-copy");
							frame.setMaxCol(df.getMaxCol());
							frame.setMaxRow(df.getMaxRow());
							frame.setSerialNumber(ui.storehouseService.findNextSerialnumberOfDenseFrame(df.getStorehouseSN()));
							frame.setStorehouseSN(df.getStorehouseSN());
							int denseFrameUniqueId = ui.storehouseService.insertDenseFrame(frame);
							frame.setDenseFrameUniqueId(denseFrameUniqueId);
							
							DenseFrameComponent component = new DenseFrameComponent(frame);
							
							add(component);
						}
						
					}
				};
				MessageBox.showMessage("提示", "请确认是否增加"+selected.size()+"个新的拷贝", MessageBox.WARNING, event, "确定");
			}
        });
	
        selectAll.addValueChangeListener(e -> {
        	Iterator<Component> iter = clayout.iterator();
			while(iter.hasNext()) {
				Component comp = iter.next();
				if (comp instanceof DenseFrameComponent) {
					DenseFrameComponent dcomp = (DenseFrameComponent) comp;
					dcomp.sertSelected(e.getValue());
				}
			}
        });
        
        main.addComponents(toolbar, new Hr(), clayout);
        
        mainPane.setContent(main);
        
        this.setContent(mainPane);
        
	}
	
	private void load(int storehouseSN) {
		List<DenseFrame> lst = ui.storehouseService.findAllDenseFrame(storehouseSN);
		for (DenseFrame frame : lst) {
			DenseFrameComponent component = new DenseFrameComponent(frame);
			add(component);
		}
	}
	
	private void removeComponent(List<Component> removeableList) {
		if (removeableList.size() > 0) {
			
			Component comp = removeableList.get(0);
			
			clayout.removeComponent(comp);
			
			removeableList.remove(0);
			
			removeComponent(removeableList);
		}
	}
	
	/**
	 * 
	 * @param component
	 */
	private void add(DenseFrameComponent component) {
		clayout.addComponents(component);
	}
	
	/**
	 * 
	 * @param storehouse
	 */
	public static void open(Storehouse storehouse) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ManageDenseFrameWindow w = new ManageDenseFrameWindow(storehouse);
        w.load(storehouse.getSerialNumber());
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private Storehouse storehouse;
	private Button btnAdd = new Button("添加密集架");
	private Button btnEdit = new Button("编辑");
	private Button btnRemove = new Button("删除");
	private Button btnCopy = new Button("复制");
	private CheckBox selectAll = new CheckBox("全选/全不选");
	private CssLayout clayout;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
}
