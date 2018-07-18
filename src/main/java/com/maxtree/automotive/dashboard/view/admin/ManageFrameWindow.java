package com.maxtree.automotive.dashboard.view.admin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Hr;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ManageFrameWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param storehouse
	 */
	public ManageFrameWindow(FrameNumber storehouse) {
		this.storehouse = storehouse;
		initComponents();
	}
	
	private void initComponents() {
		this.setCaption("库房"+storehouse.getStorehouseName());
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
					FrameNumber frame = (FrameNumber) objects[0];
					FrameComponent component = new FrameComponent(frame);
					add(component);
				}
			};
			EditFrameWindow.open(storehouse, callback);
        });
		
		btnEdit.addClickListener(e -> {
			List<FrameComponent> selected = new ArrayList<FrameComponent>();
			Iterator<Component> iter = clayout.iterator();
			while(iter.hasNext()) {
				Component comp = iter.next();
				if (comp instanceof FrameComponent) {
					FrameComponent dcomp = (FrameComponent) comp;
					if (dcomp.isSelected()) {
						selected.add(dcomp);
					}
				}
			}
			if (selected.size() > 1) {
				Notifications.warning("只能选择一个进行编辑。");
				return;
			}
			
			Callback2 callback = new Callback2() {

				@Override
				public void onSuccessful(Object... objects) {
					clayout.removeComponent(selected.get(0));
					
					FrameNumber frame = (FrameNumber) objects[0];
					FrameComponent component = new FrameComponent(frame);
					add(component);
				}
			};
			
			EditFrameWindow.edit(storehouse, selected.get(0).getFrame(), callback);
        });
        
		btnRemove.addClickListener(e -> {
			List<FrameComponent> selected = new ArrayList<FrameComponent>();
        	Iterator<Component> iter = clayout.iterator();
			while(iter.hasNext()) {
				Component comp = iter.next();
				if (comp instanceof FrameComponent) {
					FrameComponent dcomp = (FrameComponent) comp;
					if (dcomp.isSelected()) {
						selected.add(dcomp);
					}
				}
			}
			
			if (selected.size() == 0) {
				Notifications.warning("复制前至少选择一个密集架作为模板。");
			} else {
				
				
				Callback okEvent = new Callback() {
					@Override
					public void onSuccessful() {
						List<Component> removeableList = new ArrayList<Component>();
						Iterator<Component> iter = clayout.iterator();
						while(iter.hasNext()) {
							Component comp = iter.next();
							if (comp instanceof FrameComponent) {
								FrameComponent dcomp = (FrameComponent) comp;
								if (dcomp.isSelected()) {
									ui.frameService.deleteFrame(dcomp.getFrame().getStorehouseName(), dcomp.getFrame().getFrameCode());
									removeableList.add(comp);
								}
							}
						}
						// remove component on the UI
						removeComponent(removeableList);
					}
				};
				
				MessageBox.showMessage("删除提示", "注意：删除密集架将会删除其所有的单元格和文件夹。<br>请确定是否彻底删除密集架？", MessageBox.INFO, okEvent, "删除");

			}
			
        });
		
		btnCopy.addClickListener(e -> {
        	
        	List<FrameComponent> selected = new ArrayList<FrameComponent>();
        	Iterator<Component> iter = clayout.iterator();
			while(iter.hasNext()) {
				Component comp = iter.next();
				if (comp instanceof FrameComponent) {
					FrameComponent dcomp = (FrameComponent) comp;
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
						for (FrameComponent dcomp : selected) {
							FrameNumber template = dcomp.getFrame();
							
							int newFrameCode = ui.frameService.findNextCodeOfFrame(template.getStorehouseName());
							FrameNumber newFrame = new FrameNumber();
							newFrame.setStorehouseName(template.getStorehouseName());
							newFrame.setFrameCode(newFrameCode);
							newFrame.setMaxColumn(template.getMaxColumn());
							newFrame.setMaxRow(template.getMaxRow());
							int frameId = ui.frameService.insert(newFrame);
							newFrame.setFrameUniqueId(frameId);
							
							int cellCode = 0;//单元格顺序号
							for (int i = 1; i <= newFrame.getMaxRow(); i++) {
								
								for (int j = 1; j <= newFrame.getMaxColumn(); j++) {
									cellCode++;
									FrameNumber cell = new FrameNumber();
									cell.setStorehouseName(newFrame.getStorehouseName());
									cell.setFrameCode(newFrameCode);
									cell.setMaxColumn(newFrame.getMaxColumn());
									cell.setMaxRow(newFrame.getMaxRow());
									cell.setCol(j);
									cell.setRow(i);
									cell.setCellCode(cellCode);
									
									int cellId = ui.frameService.insert(cell);
									cell.setFrameUniqueId(cellId);
									
									List<FrameNumber> batch = new ArrayList<FrameNumber>();
									for(int z=1; z<=100; z++) {
										StringBuilder codes = new StringBuilder();
										codes.append(formatter.format(newFrame.getFrameCode()));
										codes.append("-");
										codes.append(formatter.format(j));
										codes.append("-");
										codes.append(formatter.format(i));
										codes.append("-");
										codes.append(formatter.format(z));
										
										FrameNumber bag = new FrameNumber();
										bag.setStorehouseName(newFrame.getStorehouseName());
										bag.setFrameCode(newFrameCode);
										bag.setMaxColumn(newFrame.getMaxColumn());
										bag.setMaxRow(newFrame.getMaxRow());
										bag.setCol(j);
										bag.setRow(i);
										bag.setCellCode(cellCode);
										bag.setCode(codes.toString());
										bag.setVin(null);
										batch.add(bag);
									}
									ui.frameService.insert(batch);
									
								}
							}
							
							FrameComponent component = new FrameComponent(newFrame);
							
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
				if (comp instanceof FrameComponent) {
					FrameComponent dcomp = (FrameComponent) comp;
					dcomp.sertSelected(e.getValue());
				}
			}
        });
        
        main.addComponents(toolbar, new Hr(), clayout);
        
        mainPane.setContent(main);
        
        this.setContent(mainPane);
        
	}
	
	private void loadAllFrames(String storehouseName) {
		List<FrameNumber> lstFrame = ui.frameService.findAllFrame(storehouseName);
		for (FrameNumber frame : lstFrame) {
			FrameComponent component = new FrameComponent(frame);
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
	private void add(FrameComponent component) {
		clayout.addComponents(component);
	}
	
	/**
	 * 
	 * @param storehouse
	 */
	public static void open(FrameNumber storehouse) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        ManageFrameWindow w = new ManageFrameWindow(storehouse);
        w.loadAllFrames(storehouse.getStorehouseName());
        UI.getCurrent().addWindow(w);
        w.center();
    }
	
	private FrameNumber storehouse;
	private Button btnAdd = new Button("添加密集架");
	private Button btnEdit = new Button("编辑");
	private Button btnRemove = new Button("删除");
	private Button btnCopy = new Button("复制");
	private CheckBox selectAll = new CheckBox("全选/全不选");
	private CssLayout clayout;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private static DecimalFormat formatter = new DecimalFormat("000");
}
