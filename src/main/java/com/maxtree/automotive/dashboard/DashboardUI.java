package com.maxtree.automotive.dashboard;

import java.util.Locale;

import javax.servlet.annotation.WebServlet;

import com.maxtree.automotive.dashboard.service.*;
import com.vaadin.shared.ui.ui.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.maxtree.automotive.dashboard.component.LoggingWrapper;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.security.PasswordSecurity;
import com.maxtree.automotive.dashboard.view.LoginView;
import com.maxtree.automotive.dashboard.view.MainView;
import com.maxtree.automotive.dashboard.view.admin.AdminMainView;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

//@Push(PushMode.MANUAL)
@Push(transport = Transport.WEBSOCKET_XHR)
@Theme("dashboard")
@Title(TB4Application.NAME+" "+TB4Application.VERSION)
@SuppressWarnings("serial")
@SpringUI
public final class DashboardUI extends UI {
	// define the logger
	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardUI.class);
	@Autowired
	public RoleService roleService;
	@Autowired
	public UserService userService;
	@Autowired
	public AuthService authService;
	@Autowired
	public BusinessService businessService;
	@Autowired
	public TransactionService transactionService;
	@Autowired
	public SiteService siteService;
	@Autowired
	public PermissionService permissionService;
	@Autowired
	public PermissionCategoryService permissionCategoryService;
	@Autowired
	public DocumentService documentService;
	@Autowired
	public QueueService queueService;
	@Autowired
	public CommunityService communityService;
	@Autowired
	public CompanyService companyService;
	@Autowired
	public DataItemService dataItemService;
	@Autowired
	public MessagingService messagingService;
	@Autowired
	public FrameNumberService frameService;
	@Autowired
	public TransitionService transitionService;
	@Autowired
	public FeedbackService feedbackService;
	@Autowired
	public ImagingService imagingService;
	@Autowired
	public SystemSettingsService settingsService;
	@Autowired
	public BusinessStateService businessStateService;
	@Autowired
	public EmbeddedServerService embeddedServerService;
	@Autowired
	public CarService carService;
	@Autowired
	public LocationService locationService;
	@Autowired
	public CompanyCategoryService companyCategoryService;
	@Autowired
	public LoggingService loggingService;
	
	
	private StateHelper state = null;
	/*
	 * This field stores an access to the dummy backend layer. In real applications
	 * you most likely gain access to your beans trough lookup or injection; and not
	 * in the UI but somewhere closer to where they're actually accessed.
	 */
//	private final DataProvider dataProvider = new DummyDataProvider();
	private final DashboardEventBus dashboardEventbus = new DashboardEventBus();

	@Override
	protected void init(final VaadinRequest request) {
		
		setLocale(Locale.US);

		DashboardEventBus.register(this);
		Responsive.makeResponsive(this);
//		addStyleName(ValoTheme.UI_WITH_MENU);

		updateContent();

		// Some views need to be aware of browser resize events so a
		// BrowserResizeEvent gets fired to the event bus on every occasion.
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			@Override
			public void browserWindowResized(final BrowserWindowResizeEvent event) {
				DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
			}
		});
	}

	/**
	 * Updates the correct content for this UI based on the current user status. If
	 * the user is logged in with appropriate privileges, main view is shown.
	 * Otherwise login view is shown.
	 */
	private void updateContent() {
		if (!AuthService.isAuthenticated()) {
			setContent(new LoginView());
			addStyleName("loginview");
		} else {
			String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
			User user = userService.getUserByUserName(username);
			if(user.isPermitted(PermissionCodes.A1)
			   || user.isPermitted(PermissionCodes.A2)) {
				setContent(new AdminMainView());
				removeStyleName("loginview");
				
			} else if (user.isPermitted(PermissionCodes.A3)
					|| user.isPermitted(PermissionCodes.A4)
					|| user.isPermitted(PermissionCodes.A5)
					|| user.isPermitted(PermissionCodes.A6)
					|| user.isPermitted(PermissionCodes.A7)
					|| user.isPermitted(PermissionCodes.A8)
					|| user.isPermitted(PermissionCodes.A9)
					|| user.isPermitted(PermissionCodes.A10)
					|| user.isPermitted(PermissionCodes.A11)
					|| user.isPermitted(PermissionCodes.A12)) {
				MainView mainView = new MainView();
				setContent(mainView);
				removeStyleName("loginview");
				String firstViewName = (String) VaadinSession.getCurrent().getAttribute(user.getUserUniqueId()+"");
				getNavigator().navigateTo(firstViewName);
				
			} else {
				Notification notification = new Notification("提示：", "当前用户没有登录权限,请联系管理员进行设置。", Type.WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				notification.show(Page.getCurrent());
				
				notification.addCloseListener(e -> {
					DashboardEventBus.post(new DashboardEvent.UserLoggedOutEvent());
				});
				
			}
		}
	}

	@Subscribe
	public void userLoginRequested(final DashboardEvent.UserLoginRequestedEvent event) {
		if (StringUtils.isEmpty(event.getUserName()) || StringUtils.isEmpty(event.getPassword())) {
			LOGGER.info(event.getUserName(),LoggingWrapper.LOGIN,"Incorrect username or password.");
			smoothNotification("用户名或密码不能为空", "请重新输入用户名和密码。");
		} else {
			User user = userService.getUserByUserName(event.getUserName());
			if (user.getUserName() != null) {
				
				if (user.getActivated() == 0) {
					LOGGER.info(event.getUserName(),LoggingWrapper.LOGIN,"User["+event.getUserName()+"] not found.");
					smoothNotification("该账号尚未激活", "当前用户没有被激活，请联系管理员进行设置。");
					
				} else {
					boolean successful = authService.login(event.getUserName(), event.getPassword(), event.isRememberMe());// PasswordSecurity.check(event.getPassword(), user.getHashed());
					if (successful) {
						LOGGER.info(user.getUserName(), LoggingWrapper.LOGIN, "Login is sucessful.");
						updateContent();
					} else {
						LOGGER.info(user.getUserName(), LoggingWrapper.LOGIN, "Incorrect password.");
						smoothNotification("密码错误", "请重新输入密码，如果忘记密码请联系管理员重置密码。");
					}
				}
			} else {
				LOGGER.info(event.getUserName(),LoggingWrapper.LOGIN,"The username["+event.getUserName()+"] does not exist.");
				smoothNotification("用户不存在", "用户名"+event.getUserName()+"不存在，请重新输入用户名和密码。");
			}
		}

		// user = authenticationService.authenticate(event.getUserName(),
		// event.getPassword());
		// if (user != null) {
		// VaadinSession.getCurrent().setAttribute(Usr.class.getName(), user);
		// updateContent();
		// } else {
		//
//		 Notification notification = new Notification(
//		 "UserName or Password is wrong.");
//		 notification
//		 .setDescription("<span> That account doesn't exist. Enter a different account
//		 or get a new one..</span>");
//		 notification.setHtmlContentAllowed(true);
//		 notification.setStyleName("tray dark small closable login-help");
//		 notification.setPosition(Position.BOTTOM_CENTER);
//		 notification.setDelayMsec(5000);
//		 notification.show(Page.getCurrent());
		//
		// }

		// Usr user = getDataProvider().authenticate(event.getUserName(),
		// event.getPassword());
		// VaadinSession.getCurrent().setAttribute(Usr.class.getName(), user);
		// updateContent();
	}
	
	private void smoothNotification(String title, String message) {
		 Notification notification = new Notification(title);//"UserName or Password is wrong."
		 notification.setDescription("<span>"+message+"</span>");//("<span> That account doesn't exist. Enter a different account or get a new one..</span>");
		 notification.setHtmlContentAllowed(true);
		 notification.setStyleName("tray dark small closable login-help");
		 notification.setPosition(Position.BOTTOM_CENTER);
		 notification.setDelayMsec(5000);
		 notification.show(Page.getCurrent());
	}

	@Subscribe
	public void userLoggedOut(final DashboardEvent.UserLoggedOutEvent event) {
		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
		LOGGER.info(username, LoggingWrapper.LOGIN,"You have logged out successfully.");
		
		// When the user logs out, current VaadinSession gets closed and the
		// page gets reloaded on the login screen. Do notice the this doesn't
		// invalidate the current HttpSession.
//		VaadinSession.getCurrent().close();
//		Page.getCurrent().reload();

		authService.logOut();
	}

	@Subscribe
	public void closeOpenWindows(final DashboardEvent.CloseOpenWindowsEvent event) {
		for (Window window : getWindows()) {
			window.close();
		}
	}

//	/**
//	 * @return An instance for accessing the (dummy) services layer.
//	 */
//	public static DataProvider getDataProvider() {
//		return ((DashboardUI) getCurrent()).dataProvider;
//	}

	public static DashboardEventBus getDashboardEventbus() {
		return ((DashboardUI) getCurrent()).dashboardEventbus;
	}

	@WebServlet(urlPatterns = "/*", name = "DashboardUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = DashboardUI.class, productionMode = false)
	public static class DashboardUIServlet extends SpringVaadinServlet implements SessionInitListener {

		@Override
		public void sessionInit(SessionInitEvent event) throws ServiceException {
			event.getSession().addBootstrapListener(new MyBootstrapListener());
		}

		private static class MyBootstrapListener implements BootstrapListener {
			private static final long serialVersionUID = 1L;

			@Override
			public void modifyBootstrapPage(BootstrapPageResponse response) {
				// ...
			}

			@Override
			public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
				// ...
			}
		};
	}
	
	/**
	 * 返回业务状态
	 * 
	 * @return
	 */
	public StateHelper state() {
		if(state == null) {
			state = new StateHelper();
		}
		return state;
	}
	
	private LoggingWrapper loggingWrapper = new LoggingWrapper(DashboardUI.class);
}
