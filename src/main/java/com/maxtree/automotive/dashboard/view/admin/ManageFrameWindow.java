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
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * 密集架管理器
 * 
 * @author Chen
 *
 */
public class ManageFrameWindow extends Window {

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param storehouse
//	 */
//	public ManageFrameWindow(FrameNumber storehouse) {
//		this.storehouse = storehouse;
//		initComponents();
//	}
//
//	private void initComponents() {
//		this.setCaption("库房"+storehouse.getStorehouseName());
//		this.setSizeFull();
//		this.setClosable(true);
//		this.setResizable(true);
//		this.setModal(true);
//		VerticalLayout main = new VerticalLayout();
//		main.setSizeFull();
//
//		updateScopeOptions(true);
//		scope.setHeight("28px");
//		scope.setEmptySelectionAllowed(false);
//		scope.setTextInputAllowed(false);
//		scope.setWidth("150px");
//		scope.setDescription("选择查询范围");
//		scope.addValueChangeListener(e->{
//			QueryScope sc = e.getValue();
//			loadAllFrames(sc);
//		});
//		Label scopeTxt = new Label("查询范围:");
//
//
//		gridLayout.setSizeFull();
//		HorizontalLayout toolbar = new HorizontalLayout();
//		toolbar.setSpacing(false);
//		toolbar.setMargin(false);
//		toolbar.setWidthUndefined();
//		toolbar.setHeight("40px");
//		btnAdd.setIcon(VaadinIcons.FILE_ADD);
//		btnAdd.setDescription("添加新密集架");
//		btnAdd.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//
//		btnEdit.setIcon(VaadinIcons.EDIT);
//		btnEdit.setDescription("编辑密集架");
//		btnEdit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//
//		btnDelete.setIcon(VaadinIcons.FILE_REMOVE);
//		btnDelete.setDescription("删除密集架");
//		btnDelete.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//
//		btnCopy.setIcon(VaadinIcons.COPY);
//		btnCopy.setDescription("以此密集架作为模板进行复制");
//		btnCopy.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
//
//
//		toolbar.addComponents(btnAdd,Box.createHorizontalBox(8), btnEdit, Box.createHorizontalBox(8), btnDelete, Box.createHorizontalBox(8), btnCopy, Box.createHorizontalBox(18), scopeTxt,scope,Box.createHorizontalBox(18),checkbox);
//		toolbar.setComponentAlignment(btnAdd, Alignment.MIDDLE_LEFT);
//		toolbar.setComponentAlignment(btnEdit, Alignment.MIDDLE_LEFT);
//		toolbar.setComponentAlignment(btnDelete, Alignment.MIDDLE_LEFT);
//		toolbar.setComponentAlignment(btnCopy, Alignment.MIDDLE_LEFT);
//		toolbar.setComponentAlignment(scope, Alignment.MIDDLE_LEFT);
//		toolbar.setComponentAlignment(scopeTxt, Alignment.MIDDLE_LEFT);
//		toolbar.setComponentAlignment(checkbox, Alignment.MIDDLE_LEFT);
//
//		checkbox.addValueChangeListener(e->{
//			selectAll(e.getValue());
//		});
//
//		scrollPane.setSizeFull();
//
//        VerticalSplitPanel vlayout = new VerticalSplitPanel();
//        vlayout.setSizeFull();
//        vlayout.setFirstComponent(gridLayout);
//        vlayout.setSecondComponent(scrollPane);
//        Hr hr = new Hr();
//        main.addComponents(toolbar, hr, vlayout);
//        main.setExpandRatio(toolbar, 0);
//        main.setExpandRatio(hr, 0);
//        main.setExpandRatio(vlayout, 1);
//
//        this.setContent(main);
//
//		btnAdd.addClickListener(e -> {
//			Callback2 callback = new Callback2() {
//
//				@Override
//				public void onSuccessful(Object... objects) {
//					FrameNumber frame = (FrameNumber) objects[0];
//					ShelfGridComponent gridComponent = new ShelfGridComponent(frame);
//					ShelfComponent component = new ShelfComponent(frame);
//					addListener(component, gridComponent);
//					addShelfComponent(component);
//
//					updateScopeOptions(false);
//				}
//			};
//			EditFrameWindow.open(storehouse, callback);
//        });
//
//		btnEdit.addClickListener(e -> {
//			if (template == null) {
//				Notifications.warning("请选择一个密集架。");
//				return;
//			}
//			Callback2 callback = new Callback2() {
//
//				@Override
//				public void onSuccessful(Object... objects) {
//					FrameNumber updatedFrame = (FrameNumber) objects[0];
//					ShelfGridComponent gridComponent = new ShelfGridComponent(updatedFrame);
//					template.updateTitle(updatedFrame);
//					template.setFrameNumber(updatedFrame);
//					scrollPane.setContent(gridComponent);
//				}
//			};
//
//			EditFrameWindow.edit(storehouse, template.getFrameNumber(), callback);
//        });
//
//		btnDelete.addClickListener(e -> {
//			if (template == null) {
//				Notifications.warning("请选择一个密集架。");
//				return;
//			}
//			// 收集要删除的密集架
//			List<ShelfComponent> removeable = collectAllSelectedShelfComponents();
//
//			Callback okEvent = new Callback() {
//				@Override
//				public void onSuccessful() {
//
//					Callback2 delete = new Callback2() {
//						@Override
//						public void onSuccessful(Object... objects) {
//							FrameNumber f = (FrameNumber) objects[0];
//							ui.frameService.deleteFrame(f.getStorehouseName(), f.getFrameCode());
//						}
//					};
//
//					// 删除
//					removeAllSelectedShelfComponents(removeable,delete);
//
//					scrollPane.setContent(new Label());
//					//更新查询范围选项
//					updateScopeOptions(false);
//					//重新加载密集架
//					loadAllFrames(scope.getValue());
//
//					template = null;
//				}
//			};
//
//			MessageBox.showMessage("提示", "请确定是否删除这 "+removeable.size()+" 个密集架？", MessageBox.INFO, okEvent, "确定");
//
//        });
//
//		btnCopy.addClickListener(e -> {
//			if (template == null) {
//				Notifications.warning("请选择一个密集架作为模板。");
//				return;
//			}
//
//			Callback2 create = new Callback2() {
//
//				@Override
//				public void onSuccessful(Object... objects) {
//
//					int newFrameCode = ui.frameService.findNextCodeOfFrame(template.getFrameNumber().getStorehouseName());
//					FrameNumber newFrame = new FrameNumber();
//					newFrame.setStorehouseName(template.getFrameNumber().getStorehouseName());
//					newFrame.setFrameCode(newFrameCode);
//					newFrame.setMaxColumn(template.getFrameNumber().getMaxColumn());
//					newFrame.setMaxRow(template.getFrameNumber().getMaxRow());
//					newFrame.setMaxfolder(template.getFrameNumber().getMaxfolder());
//					int frameId = ui.frameService.insert(newFrame);
//					newFrame.setFrameUniqueId(frameId);
//
//					int cellCode = 0;//单元格顺序号
//					for (int i = 1; i <= newFrame.getMaxRow(); i++) {
//
//						for (int j = 1; j <= newFrame.getMaxColumn(); j++) {
//							cellCode++;
//							FrameNumber cell = new FrameNumber();
//							cell.setStorehouseName(newFrame.getStorehouseName());
//							cell.setFrameCode(newFrameCode);
//							cell.setMaxColumn(newFrame.getMaxColumn());
//							cell.setMaxRow(newFrame.getMaxRow());
//							cell.setCol(j);
//							cell.setRow(i);
//							cell.setCellCode(cellCode);
//
//							int cellId = ui.frameService.insert(cell);
//							cell.setFrameUniqueId(cellId);
//
//							List<FrameNumber> batch = new ArrayList<FrameNumber>();
//							for(int z=1; z<=newFrame.getMaxfolder(); z++) {
//								StringBuilder codes = new StringBuilder();
//								codes.append(formatter.format(newFrame.getFrameCode()));
//								codes.append("-");
//								codes.append(formatter.format(j));
//								codes.append("-");
//								codes.append(formatter.format(i));
//								codes.append("-");
//								codes.append(formatter.format(z));
//
//								FrameNumber bag = new FrameNumber();
//								bag.setStorehouseName(newFrame.getStorehouseName());
//								bag.setFrameCode(newFrameCode);
//								bag.setMaxColumn(newFrame.getMaxColumn());
//								bag.setMaxRow(newFrame.getMaxRow());
//								bag.setCol(j);
//								bag.setRow(i);
//								bag.setCellCode(cellCode);
//								bag.setCode(codes.toString());
//								bag.setVin(null);
//								batch.add(bag);
//							}
//							ui.frameService.insert(batch);
//
//						}
//					}
//
//					ShelfGridComponent gridComponent = new ShelfGridComponent(newFrame);
//					ShelfComponent component = new ShelfComponent(newFrame);
//					addListener(component, gridComponent);
//
//					addShelfComponent(component);
//				}
//			};
//
//			Callback2 batch = new Callback2() {
//
//				@Override
//				public void onSuccessful(Object... objects) {
//					if(objects.length > 0) {
//						int count = Integer.parseInt(objects[0].toString());
//						for(int i = 0; i < count; i++) {
//							create.onSuccessful();
//						}
//						updateScopeOptions(false);
//					}
//				}
//			};
//			NumberofCopiesWindow.open(batch);
//        });
//
//	}
//
//	/**
//	 *
//	 * @param queryScope
//	 */
//	private void loadAllFrames(QueryScope queryScope) {
//		if(queryScope != null) {
//			template = null;
//			// clean
//			gridLayout.removeAllComponents();
//			List<FrameNumber> lstFrame = ui.frameService.findAllFrame(storehouse.getStorehouseName(), 20, queryScope.getFrom()-1);
//			for(int i = 0; i < lstFrame.size(); i++) {
//				FrameNumber frame = lstFrame.get(i);
//				ShelfGridComponent gridComponent = new ShelfGridComponent(frame);
//				ShelfComponent component = new ShelfComponent(frame);
//				addShelfComponent(component);
//				addListener(component, gridComponent);
//			}
//		}
//	}
//
//	/**
//	 *
//	 * @param component
//	 * @param gridComponent
//	 */
//	private void addListener(ShelfComponent component, ShelfGridComponent gridComponent) {
//		component.addLayoutClickListener(e->{
//			if(component.isSelected()) {
//				component.deselect();
//				scrollPane.setContent(new Label());
//				template = null;
//			}
//			else {
//				component.select();
//				scrollPane.setContent(gridComponent);
//				template = component;
//			}
//		});
//	}
//
//	/**
//	 *
//	 * @param shelf
//	 */
//	private void addShelfComponent(ShelfComponent shelf) {
//		int count = gridLayout.getComponentCount();
//		if(count < rowsPerPage) {
//			gridLayout.addComponent(shelf);
//		}
//	}
//
//	/**
//	 *
//	 * @param selectAll
//	 */
//	private void selectAll(boolean selectAll) {
//		Iterator<Component> iter = gridLayout.iterator();
//		while(iter.hasNext()) {
//			ShelfComponent shelf = (ShelfComponent) iter.next();
//			if(selectAll) {
//				shelf.select();
//			}
//			else {
//				shelf.deselect();
//			}
//		}
//	}
//
//	/**
//	 *
//	 * @param delete
//	 */
//	private List<ShelfComponent> collectAllSelectedShelfComponents() {
//		List<ShelfComponent> removeableList = new ArrayList<>();
//		Iterator<Component> iter = gridLayout.iterator();
//		while(iter.hasNext()) {
//			ShelfComponent shelf = (ShelfComponent) iter.next();
//			if(shelf.isSelected()) {
//				removeableList.add(shelf);
//			}
//		}
//
//		return removeableList;
//	}
//
//	/**
//	 *
//	 * @param removeableList
//	 * @param delete
//	 */
//	private void removeAllSelectedShelfComponents(List<ShelfComponent> removeableList, Callback2 delete) {
//		if (removeableList.size() > 0) {
//
//			ShelfComponent comp = removeableList.get(0);
//
//			gridLayout.removeComponent(comp);
//
//			delete.onSuccessful(comp.getFrameNumber());
//
//			removeableList.remove(0);
//
//			removeAllSelectedShelfComponents(removeableList, delete);
//		}
//	}
//
//	/**
//	 *
//	 * @param selectFirstOption
//	 */
//	private void updateScopeOptions(boolean selectFirstOption) {
//		List<FrameNumber> allShelf = ui.frameService.findAllFrame(storehouse.getStorehouseName());
//		int count = allShelf.size() / 20;
//		if(count == 0) {
//			count = 1;
//		} else if(allShelf.size() == 20) {
//			count = 1;
//		} else {
//			count++;
//		}
//
//		List<QueryScope> items = new ArrayList<QueryScope>();
//		QueryScope first = null;
//		for(int i = 0; i < count; i++) {
//			QueryScope scope = new QueryScope((i*20+1),((i+1)*20));
//			if(i == 0) {
//				first = scope;
//			}
//			items.add(scope);
//		}
//		scope.setItems(items);
//		if(selectFirstOption) {
//			scope.setSelectedItem(first);
//		}
//	}
//
//	/**
//	 *
//	 * @param storehouse
//	 */
//	public static void open(FrameNumber storehouse) {
//        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
//        ManageFrameWindow w = new ManageFrameWindow(storehouse);
//        w.loadAllFrames(w.scope.getValue());
//        UI.getCurrent().addWindow(w);
//        w.center();
//    }
//
//	private FrameNumber storehouse;
//	private Button btnAdd = new Button();
//	private Button btnEdit = new Button();
//	private Button btnDelete = new Button();
//	private Button btnCopy = new Button();
//	private CheckBox checkbox = new CheckBox("全选");
//	private ComboBox<QueryScope> scope = new ComboBox<QueryScope>();
//	private GridLayout gridLayout = new GridLayout(10, 2);
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private static DecimalFormat formatter = new DecimalFormat("000");
//	private Panel scrollPane = new Panel();
//	private ShelfComponent template;
//	private int rowsPerPage = 20;
//}
//
//class QueryScope {
//
//	/**
//	 *
//	 * @param from
//	 * @param to
//	 */
//	public QueryScope(int from, int to) {
//		this.from = from;
//		this.to = to;
//	}
//
//	/**
//	 *
//	 */
//	public String toString() {
//		return from+"~"+to+"行";
//	}
//
//	public int getFrom() {
//		return from;
//	}
//
//	public void setFrom(int from) {
//		this.from = from;
//	}
//
//	public int getTo() {
//		return to;
//	}
//
//	public void setTo(int to) {
//		this.to = to;
//	}
//
//	private int from = 0;
//	private int to = 0;
}
