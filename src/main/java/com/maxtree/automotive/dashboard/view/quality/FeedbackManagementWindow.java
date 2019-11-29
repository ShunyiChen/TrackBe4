package com.maxtree.automotive.dashboard.view.quality;

import java.util.ArrayList;
import java.util.List;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.PermissionCodes;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.maxtree.automotive.dashboard.view.admin.CustomGrid;
import com.maxtree.automotive.dashboard.view.admin.CustomGridRow;
import com.maxtree.automotive.dashboard.view.admin.EditCommunityWindow;
import com.maxtree.automotive.dashboard.view.admin.GridColumn;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu.Command;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author Chen
 *
 */
public class FeedbackManagementWindow extends Window{

//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param cb
//	 */
//	public FeedbackManagementWindow(Callback cb) {
//		this.cb = cb;
//		initComponents();
//	}
//
//	private void initComponents() {
//		this.setCaption("管理批改意见");
//		this.setWidth("600px");
//		this.setHeight("540px");
//		this.setModal(true);
//		this.setClosable(true);
//		this.setResizable(false);
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		loggedInUser = ui.userService.getUserByUserName(username);
//		GridColumn[] columns = {new GridColumn("批改意见",490),new GridColumn("", 20)};
//		List<CustomGridRow> data = new ArrayList<>();
//		List<Feedback> list = ui.feedbackService.findByUserName(loggedInUser.getUserName());
//		for (Feedback f : list) {
//			Object[] rowData = generateOneRow(f);
//			data.add(new CustomGridRow(rowData));
//		}
//		grid = new CustomGrid("意见列表",columns, data);
//		Callback addEvent = new Callback() {
//
//			@Override
//			public void onSuccessful() {
//				Callback2 callback = new Callback2() {
//
//					@Override
//					public void onSuccessful(Object... objects) {
//
//						int feedbackUniqueId = (int) objects[0];
//						Feedback feedback = ui.feedbackService.findById(feedbackUniqueId);
//						Object[] rowData = generateOneRow(feedback);
//						grid.insertRow(new CustomGridRow(rowData));
//
//						cb.onSuccessful();
//					}
//				};
//				EditFeedbackWindow.open(callback);
//			}
//		};
//		grid.setAddEvent(addEvent);
//		grid.setScrollPaneHeight(300);
//		main.setWidth("100%");
//		main.setHeightUndefined();
//		main.setSpacing(false);
//		main.setMargin(false);
//
//		main.addComponents(grid);
//		main.setComponentAlignment(grid, Alignment.TOP_CENTER);
//
//		this.setContent(main);
//	}
//
//	/**
//	 *
//	 * @param feedback
//	 * @return
//	 */
//	private Object[] generateOneRow(Feedback feedback) {
//		Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
//		img.addStyleName("PeopleView_menuImage");
//		img.addClickListener(e->{
//			ContextMenu menu = new ContextMenu(img, true);
//			menu.addItem("编辑", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//					Callback callback = new Callback() {
//
//						@Override
//						public void onSuccessful() {
//							Feedback f = ui.feedbackService.findById(feedback.getFeedbackUniqueId());
//							Object[] rowData = generateOneRow(f);
//							grid.setValueAt(new CustomGridRow(rowData),feedback.getFeedbackUniqueId());
//
//							cb.onSuccessful();
//						}
//					};
//					EditFeedbackWindow.edit(feedback, callback);
//
//				}
//			});
//			menu.addItem("从列表删除", new Command() {
//				/**
//				 *
//				 */
//				private static final long serialVersionUID = 1L;
//
//				@Override
//				public void menuSelected(MenuItem selectedItem) {
//
//					Callback event = new Callback() {
//
//						@Override
//						public void onSuccessful() {
//
//							ui.feedbackService.delete(feedback.getFeedbackUniqueId());
//							grid.deleteRow(feedback.getFeedbackUniqueId());
//
//							cb.onSuccessful();
//						}
//					};
//
//					MessageBox.showMessage("提示", "请确定是否删除该意见。", MessageBox.WARNING, event, "确定");
//				}
//			});
//
//			menu.open(e.getClientX(), e.getClientY());
//		});
//
//		return new Object[] {feedback.getSuggestion(),img,feedback.getFeedbackUniqueId()};
//	}
//
//	/**
//	 *
//	 * @param callback
//	 */
//	public static void open(Callback callback) {
//		FeedbackManagementWindow fmw = new FeedbackManagementWindow(callback);
//        UI.getCurrent().addWindow(fmw);
//        fmw.center();
//    }
//
//	private Callback cb;
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private HorizontalLayout footer = new HorizontalLayout();
//	private VerticalLayout main = new VerticalLayout();
//	private User loggedInUser;
//	private CustomGrid grid;
}
