package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.*;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.DataException;
import com.maxtree.automotive.dashboard.jpa.entity.Camera;
import com.maxtree.automotive.dashboard.service.AuthService;
import com.vaadin.contextmenu.ContextMenu;
import com.vaadin.contextmenu.Menu;
import com.vaadin.contextmenu.MenuItem;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeviceView extends ContentView {

    public DeviceView(String parentTitle, AdminMainView rootView) {
        super(parentTitle, rootView);
        this.setHeight((Page.getCurrent().getBrowserWindowHeight()-58)+"px");
        String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
        loggedInUser = ui.userService.getUserByUserName(username);
        main.setWidth("100%");
        main.setHeightUndefined();
        main.setSpacing(false);
        main.setMargin(false);

        GridColumn[] columns = {new GridColumn("高拍仪",240), new GridColumn("描述",240), new GridColumn("使用中",80),new GridColumn("", 20)};
        List<CustomGridRow> data = new ArrayList<>();
        Iterable<Camera> iter = TB4Application.getInstance().cameraRepository.findAll();
        iter.forEach(c -> {
            Object[] rowData = generateOneRow(c);
            data.add(new CustomGridRow(rowData));
        });
        grid = new CustomGrid("高拍仪列表",columns, data);
        Callback addEvent = new Callback() {

            @Override
            public void onSuccessful() {
                if (loggedInUser.isPermitted(PermissionCodes.G1)) {
                    Callback2 callback = new Callback2() {

                        @Override
                        public void onSuccessful(Object... objects) {
                            Long cameraId = (Long) objects[0];
                            Optional<Camera> opt = TB4Application.getInstance().cameraRepository.findById(cameraId);
                            if(opt.isPresent()) {
                                Camera c = opt.get();
                                Object[] rowData = generateOneRow(c);
                                grid.insertRow(new CustomGridRow(rowData));
                            }
                        }
                    };
                    EditCameraWindow.open(callback);

                } else {
                    Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
                }
            }
        };
        grid.setAddEvent(addEvent);
        main.addComponents(grid);
        main.setComponentAlignment(grid, Alignment.TOP_CENTER);

        this.addComponent(main);
        this.setComponentAlignment(main, Alignment.TOP_CENTER);
        this.setExpandRatio(main, 1);
    }

    private Object[] generateOneRow(Camera camera) {
        Image img = new Image(null, new ThemeResource("img/adminmenu/menu.png"));
        img.addStyleName("PeopleView_menuImage");
        img.addClickListener(e->{
            ContextMenu menu = new ContextMenu(img, true);
             
            menu.addSeparator();

            menu.addItem("编辑", new Menu.Command() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void menuSelected(MenuItem selectedItem) {

                    if (loggedInUser.isPermitted(PermissionCodes.G2)) {
                        Callback callback = new Callback() {

                            @Override
                            public void onSuccessful() {
                                Optional<Camera> opt = TB4Application.getInstance().cameraRepository.findById(camera.getId());
                                if(opt.isPresent()) {
                                    Camera c = opt.get();
                                    Object[] rowData = generateOneRow(c);
                                    grid.setValueAtLongId(new CustomGridRow(rowData), camera.getId());
                                }
                            }
                        };
                        EditCameraWindow.edit(camera, callback);
                    }
                    else {
                        Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
                    }
                }
            });

            menu.addItem("从列表删除", new Menu.Command() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void menuSelected(MenuItem selectedItem) {

                    if (loggedInUser.isPermitted(PermissionCodes.G3)) {

                        Callback event = new Callback() {

                            @Override
                            public void onSuccessful() {
                                TB4Application.getInstance().cameraRepository.delete(camera);
                                grid.deleteRowByLong(camera.getId());
                            }
                        };
                        MessageBox.showMessage("提示", "请确定是否删除该高拍仪。", MessageBox.WARNING, event, "删除");

                    } else {
                        Notifications.warning(TB4Application.PERMISSION_DENIED_MESSAGE);
                    }
                }
            });
            menu.open(e.getClientX(), e.getClientY());
        });
        return new Object[] {camera.getName(),camera.getDescription(), camera.getInUse()?"√":"", img, camera.getId()};
    }

    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private VerticalLayout main = new VerticalLayout();
    private User loggedInUser;
    private CustomGrid grid;
}
