package com.maxtree.automotive.dashboard.view.admin;

import com.google.common.collect.Lists;
import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.jpa.entity.Camera;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Optional;

public class EditCameraWindow extends Window {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public EditCameraWindow() {
        this.setWidth("513px");
        this.setHeightUndefined();
        this.setModal(true);
        this.setResizable(false);
        this.setCaption("添加新高拍仪");
        this.addStyleName("edit-window");
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(false);
        FormLayout form = new FormLayout();
        form.setSpacing(false);
        form.setMargin(false);
        form.setSizeFull();

        nameField.setIcon(VaadinIcons.EDIT);
        //设置焦点
        nameField.focus();
        enableBox.setIcon(VaadinIcons.FOLDER_OPEN);
        enableBox.setEmptySelectionAllowed(false);
        enableBox.setTextInputAllowed(false);
        enableBox.setItems(options);
        upload = new Upload("模板", new Upload.Receiver() {

            @Override
            public OutputStream receiveUpload(final String filename, final String MIMEType) {
                templateFilename = filename;
                uploadSuccessfully.setCaption(templateFilename);
                uploadSuccessfully.setVisible(true);
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream("camera/templates/"+filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return outputStream;
            }
        });
        upload.setIcon(VaadinIcons.UPLOAD_ALT);
        upload.setImmediateMode(true);
        upload.setButtonCaption("上传模板");
        uploadSuccessfully.addStyleName(ValoTheme.BUTTON_LINK);
        uploadSuccessfully.setVisible(false);
        form.addComponents(nameField, enableBox, upload, uploadSuccessfully);

        HorizontalLayout buttonPane = new HorizontalLayout();
        buttonPane.setSizeFull();
        buttonPane.setSpacing(false);
        buttonPane.setMargin(false);
        Button btnCancel = new Button("取消");
        btnAdd = new Button("添加");
        HorizontalLayout subButtonPane = new HorizontalLayout();
        subButtonPane.setSpacing(false);
        subButtonPane.setMargin(false);
        subButtonPane.setWidth("128px");
        subButtonPane.setHeight("100%");
        subButtonPane.addComponents(btnCancel, btnAdd);
        subButtonPane.setComponentAlignment(btnCancel, Alignment.BOTTOM_LEFT);
        subButtonPane.setComponentAlignment(btnAdd, Alignment.BOTTOM_CENTER);
        buttonPane.addComponent(subButtonPane);
        buttonPane.setComponentAlignment(subButtonPane, Alignment.BOTTOM_RIGHT);
        mainLayout.addComponents(form, buttonPane);
        this.setContent(mainLayout);
        btnCancel.addClickListener(e -> {
            close();
        });
        setComponentSize(350, 27);

        // Bind nameField to the Person.name property
        // by specifying its getter and setter
        bindFields();

        // Validating Field Values
        validatingFieldValues();

        // Bind an actual concrete Person instance.
        // After this, whenever the user changes the value
        // of nameField, p.setName is automatically called.
        binder.setBean(camera);
    }

    /**
     *
     * @param w
     * @param h
     */
    private void setComponentSize(int w, int h) {
        nameField.setWidth(w+"px");
        enableBox.setWidth(w+"px");
        nameField.setHeight(h+"px");
        enableBox.setHeight(h+"px");
    }

    /**
     *
     */
    private void bindFields() {
        binder.bind(nameField, Camera::getName, Camera::setName);
//        binder.bind(enableBox, Camera::getEnable, Camera::setEnable);
    }

    /**
     *
     */
    private void validatingFieldValues () {
        // Validating Field Values
        binder.forField(nameField).withValidator(new StringLengthValidator(
                "高拍仪名长度范围在2~20个字符",
                2, 20)) .bind(Camera::getName, Camera::setName);
    }

    /**
     *
     * @return
     */
    private boolean checkEmptyValues() {
        if (StringUtils.isEmpty(camera.getName())) {
            Notifications.warning("高拍仪名不能为空");
            return false;
        }
        if(enableBox.getValue() == null) {
            Notifications.warning("请选择是否启用");
            return false;
        }
        if(templateFilename == null) {
            Notifications.warning("缺少模板");
            return false;
        }
        if (nameField.getErrorMessage() != null) {
            nameField.setComponentError(nameField.getErrorMessage());
            return false;
        }
        return true;
    }

    /**
     *
     * @param callback
     */
    public static void open(Callback2 callback) {
        EditCameraWindow w = new EditCameraWindow();
        w.btnAdd.setCaption("添加");
        w.btnAdd.addClickListener(e -> {
            if (w.checkEmptyValues()) {
                w.camera.setEnable(w.enableBox.getValue().equals("启用")?true:false);
                w.camera.setTemplatePath("camera/templates/"+w.templateFilename);
                Camera c = TB4Application.getInstance().cameraRepository.save(w.camera);
                w.close();
                callback.onSuccessful(c.getId());
            }
        });
        UI.getCurrent().addWindow(w);
        w.center();
    }

    /**
     *
     * @param camera
     * @param callback
     */
    public static void edit(Camera camera, Callback callback) {
        DashboardEventBus.post(new DashboardEvent.BrowserResizeEvent());
        EditCameraWindow w = new EditCameraWindow();
        Optional<Camera> opt = TB4Application.getInstance().cameraRepository.findById(camera.getId());
        if (opt.isPresent()) {
            Camera c = opt.get();
            w.templateFilename = c.getTemplatePath();
            w.nameField.setValue(c.getName());
            w.enableBox.setValue(c.getEnable()?"启用":"禁用");
            w.btnAdd.setCaption("保存");
            w.setCaption("编辑高拍仪");
            w.uploadSuccessfully.setVisible(true);
            w.uploadSuccessfully.setCaption(w.templateFilename);
            w.btnAdd.addClickListener(e -> {
                if (w.checkEmptyValues()) {
                    w.camera.setId(c.getId());
                    w.camera.setEnable(w.enableBox.getValue().equals("启用")?true:false);
                    w.camera.setTemplatePath(c.getTemplatePath());
                    TB4Application.getInstance().cameraRepository.save(w.camera);
                    w.close();
                    callback.onSuccessful();
                }
            });
            UI.getCurrent().addWindow(w);
            w.center();
        }
    }

    private TextField nameField = new TextField("高拍仪");
    private String[] options = {"启用","禁用"};
    private ComboBox<String> enableBox = new ComboBox("是否启用");
    private Upload upload;
    private Button btnAdd;
    private Binder<Camera> binder = new Binder<>();
    private Camera camera = new Camera();
    private String templateFilename;
    private Button uploadSuccessfully = new Button();
}

