package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.domain.UserProfile;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.vaadin.annotations.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.*;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.Arrays;
import java.util.Date;

@SuppressWarnings("serial")
public class ProfilePreferencesWindow extends Window implements UploadFinishedHandler {

    public static final String ID = "profilepreferenceswindow";

    /*
     * Fields for editing the User object are defined here as class members.
     * They are later bound to a FieldGroup by calling
     * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
     * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
     * the fields with the user object.
     */
    @PropertyId("firstName")
    private TextField firstNameField;
    @PropertyId("lastName")
    private TextField lastNameField;
    @PropertyId("title")
    private ComboBox<String> titleField;
    @PropertyId("male")
    private RadioButtonGroup<Boolean> sexField;
    @PropertyId("email")
    private TextField emailField;
    @PropertyId("location")
    private TextField locationField;
    @PropertyId("phone")
    private TextField phoneField;
    @PropertyId("newsletterSubscription")
    private OptionalSelect<Integer> newsletterField;
    @PropertyId("website")
    private TextField websiteField;
    @PropertyId("bio")
    private TextArea bioField;

    private ProfilePreferencesWindow(final User user,
            final boolean preferencesTabOpen) {
        addStyleName("profile-window");
        setId(ID);
        Responsive.makeResponsive(this);

        setModal(true);
        setCloseShortcut(KeyCode.ESCAPE, null);
        setResizable(false);
        setClosable(false);
        setHeight(90.0f, Unit.PERCENTAGE);

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.setMargin(new MarginInfo(true, false, false, false));
        content.setSpacing(false);
        setContent(content);

        TabSheet detailsWrapper = new TabSheet();
        detailsWrapper.setSizeFull();
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_ICONS_ON_TOP);
        detailsWrapper.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        content.addComponent(detailsWrapper);
        content.setExpandRatio(detailsWrapper, 1f);

        detailsWrapper.addComponent(buildProfileTab());
        detailsWrapper.addComponent(buildPreferencesTab());

        if (preferencesTabOpen) {
            detailsWrapper.setSelectedTab(1);
        }

        content.addComponent(buildFooter());

    }

    private Component buildPreferencesTab() {
        VerticalLayout root = new VerticalLayout();
        root.setCaption("首选项");
        root.setIcon(FontAwesome.COGS);
        root.setSpacing(true);
        root.setMargin(true);
        root.setSizeFull();

        Label message = new Label("Not implemented in this demo");
        message.setSizeUndefined();
        message.addStyleName(ValoTheme.LABEL_LIGHT);
        root.addComponent(message);
        root.setComponentAlignment(message, Alignment.MIDDLE_CENTER);

        return root;
    }

    private Component buildProfileTab() {
        HorizontalLayout root = new HorizontalLayout();
        root.setCaption("用户资料");
        root.setIcon(FontAwesome.USER);
        root.setWidth(100.0f, Unit.PERCENTAGE);
        root.setMargin(true);
        root.addStyleName("profile-form");

        VerticalLayout pic = new VerticalLayout();
        pic.setSizeUndefined();
        pic.setSpacing(true);
        String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
        currentUser = ui.userService.getUserByUserName(username);
        filePath = currentUser.getProfile().getPicture();
        Resource res = new ThemeResource(currentUser.getProfile().getPicture());
        profilePic = new Image(null, res);
        profilePic.setWidth(100.0f, Unit.PIXELS);
        pic.addComponent(profilePic);
        UploadStateWindow uploadStateWindow = new UploadStateWindow();
    	MultiFileUpload multi = new MultiFileUpload(this, uploadStateWindow, false);
		multi.setEnabled(true);
   		multi.setUploadButtonCaptions("更改", "选择文件");
   		multi.setUploadButtonIcon(VaadinIcons.FOLDER_OPEN);
        
        pic.addComponent(multi);

        root.addComponent(pic);

        FormLayout details = new FormLayout();
        details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        root.addComponent(details);
        root.setExpandRatio(details, 1);
        lastNameField = new TextField("姓");
        lastNameField.setValue(currentUser.getProfile().getLastName());
        details.addComponent(lastNameField);
        firstNameField = new TextField("名");
        firstNameField.setValue(currentUser.getProfile().getFirstName());
        details.addComponent(firstNameField);

        titleField = new ComboBox<>("职称", Arrays.asList("主任", "科长", "职员"));
        titleField.setPlaceholder("请指定一个职称");
        titleField.setValue(currentUser.getProfile().getTitle());
        details.addComponent(titleField);

        sexField = new RadioButtonGroup<>("性别", Arrays.asList(true, false));
        sexField.setItemCaptionGenerator(item -> item ? "男" : "女");
        
        boolean bool = false;
        if (currentUser.getProfile().getSex() != null && currentUser.getProfile().getSex().equals("男")) {
        	bool = true;
        }
        sexField.setValue(bool);
        sexField.addStyleName("horizontal");
        details.addComponent(sexField);

        Label section = new Label("联系方式");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
        details.addComponent(section);

        emailField = new TextField("邮箱");
        emailField.setWidth("100%");
        emailField.setValue(currentUser.getProfile().getEmail());
        emailField.setRequiredIndicatorVisible(true);
        // TODO add validation that not empty, use binder
        details.addComponent(emailField);

        locationField = new TextField("办公地址");
        locationField.setValue(currentUser.getProfile().getLocation());
        locationField.setWidth("100%");
        locationField
                .setComponentError(new UserError("This address doesn't exist"));
        details.addComponent(locationField);

        phoneField = new TextField("电话");
        phoneField.setValue(currentUser.getProfile().getPhone());
        phoneField.setWidth("100%");
        details.addComponent(phoneField);

        newsletterField = new OptionalSelect<Integer>();
//        newsletterField.addOption(0, "Daily");
//        newsletterField.addOption(1, "Weekly");
//        newsletterField.addOption(2, "Monthly");
//        details.addComponent(newsletterField);

        section = new Label("Additional Info");
        section.addStyleName(ValoTheme.LABEL_H4);
        section.addStyleName(ValoTheme.LABEL_COLORED);
//        details.addComponent(section);

        websiteField = new TextField("Website");
        websiteField.setPlaceholder("http://");
        websiteField.setWidth("100%");
//        details.addComponent(websiteField);

        bioField = new TextArea("Bio");
        bioField.setWidth("100%");
        bioField.setRows(4);
//        details.addComponent(bioField);

        return root;
    }

    private Component buildFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
        footer.setWidth(100.0f, Unit.PERCENTAGE);
        footer.setSpacing(false);

        Button ok = new Button("OK");
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
        ok.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                try {
                    // Updated user should also be persisted to database. But
                    // not in this demo.
                	
                	saveAll();

                    Notification success = new Notification("用户资料更新成功。");
                    success.setDelayMsec(2000);
                    success.setStyleName("bar success small");
                    success.setPosition(Position.BOTTOM_CENTER);
                    success.show(Page.getCurrent());

                    DashboardEventBus.post(new DashboardEvent.ProfileUpdatedEvent());
                    close();
                } catch (Exception e) {
                    Notification.show("Error while updating profile",  Type.ERROR_MESSAGE);
                }

            }
        });
        ok.focus();
        footer.addComponent(ok);
        footer.setComponentAlignment(ok, Alignment.TOP_RIGHT);
        return footer;
    }

    private void saveAll() {
    	UserProfile myProfile = currentUser.getProfile();
    	myProfile.setDateLastModified(new Date());
    	myProfile.setTitle(titleField.getValue());
    	myProfile.setPicture(filePath);
    	myProfile.setSex(sexField.getValue()?"男":"女");
    	myProfile.setEmail(emailField.getValue());
    	myProfile.setLocation(locationField.getValue());
    	myProfile.setPhone(phoneField.getValue());
    	myProfile.setFirstName(firstNameField.getValue());
    	myProfile.setLastName(lastNameField.getValue());
    	ui.userService.updateProfile(myProfile);
    }
    
    public static void open(final User user,
            final boolean preferencesTabActive) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        Window w = new ProfilePreferencesWindow(user, preferencesTabActive);
        UI.getCurrent().addWindow(w);
        w.focus();
    }

	@Override
	public void handleFile(InputStream stream, String fileName, String mimeType, long length, int filesLeftInQueue) {
		if (fileName.length() > 20) {
			Notification.show("上传文件名不能超出20个字符。",
					Notification.Type.WARNING_MESSAGE);
		} else {
			try {
				 
				ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
				filePath = "img/users/"+currentUser.getUserUniqueId()+"/"+fileName;
				File picture = new File(servletContext.getRealPath("/")+"/VAADIN/themes/dashboard/"+filePath);
				if (!picture.getParentFile().exists()) {
					picture.getParentFile().mkdirs();
				}
				
				OutputStream out = new FileOutputStream(picture);//new TB4FileSystem().receiveUpload(site, filePath);
				IOUtils.copy(stream, out);
				
				out.close();
				stream.close();
				
				
				// 更新头像
				FileResource res = new FileResource(picture);
				profilePic.setSource(res);
				FileResource res2 = new FileResource(picture);
				DashboardMenu.getInstance().updateUserPicture(res2);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private String filePath;
	private User currentUser;
	private Image profilePic;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private static final Logger log = LoggerFactory.getLogger(ProfilePreferencesWindow.class);
}
