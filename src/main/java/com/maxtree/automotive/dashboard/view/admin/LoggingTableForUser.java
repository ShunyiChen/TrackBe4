package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.jpa.entity.Logging;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class LoggingTableForUser extends VerticalLayout {

    private Grid<Logging> grid = new Grid<Logging>();
    private int pageCount = 220;
    private int i=0;
    private HorizontalLayout pagingLayout = new HorizontalLayout();
    private ComboBox pages;


    public LoggingTableForUser() {
        this.setSizeFull();
        this.setWidth("100%");
        this.setHeight("95%");

        grid.addColumn(Logging::getId).setCaption("##").setExpandRatio(0);

        grid.addColumn(Logging::getFormattedDatetime)
                .setCaption("操作日期").setWidth(210);

        grid.addColumn(Logging::getUserName, new BoldLastNameRenderer())
                .setCaption("用户名").setWidth(127);

        grid.addColumn(Logging::getIpAddress, new BoldLastNameRenderer())
                .setCaption("IP地址").setWidth(210);

        grid.addColumn(Logging::getActionName, new BoldLastNameRenderer())
                .setCaption("事件名").setWidth(410);

        grid.addColumn(Logging::getDetails, new BoldLastNameRenderer())
                .setCaption("详细内容").setWidth(820);
        setMargin(false);

        grid.setWidth("99%");
        grid.setHeight("99%");
        pages = createPageSizeBox();
        HorizontalLayout searchFields = searchFields();
        HorizontalLayout pagingFooter = pagingFooter(1);


        this.addComponents(searchFields, grid, pagingFooter);
        this.setComponentAlignment(searchFields, Alignment.TOP_LEFT);
        this.setComponentAlignment(pagingFooter, Alignment.BOTTOM_CENTER);
        this.setExpandRatio(searchFields, 0f);
        this.setExpandRatio(grid, 10f);
        this.setExpandRatio(pagingFooter, 1f);
    }

    private HorizontalLayout searchFields() {
        float h = 27.0f;
        float w = 150f;
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthUndefined();
        layout.setMargin(new MarginInfo(true, false,false,false));
        Label title1 = new Label("日期", ContentMode.HTML);
        DateTimeField dateTimeField = new DateTimeField();
        dateTimeField.setHeight(h, Unit.PIXELS);

        Label title2 = new Label("用户名", ContentMode.HTML);
        TextField userNameField = new TextField();
        userNameField.setHeight(h, Unit.PIXELS);
        userNameField.setWidth(w, Unit.PIXELS);

        Label title3 = new Label("IP地址", ContentMode.HTML);
        TextField ipField = new TextField();
        ipField.setHeight(h, Unit.PIXELS);
        ipField.setWidth(w, Unit.PIXELS);

        Label title4 = new Label("事件名称", ContentMode.HTML);
        TextField actionNameField = new TextField();
        actionNameField.setHeight(h, Unit.PIXELS);
        actionNameField.setWidth(w, Unit.PIXELS);

        Label title5 = new Label("详细内容", ContentMode.HTML);
        TextField detailsField = new TextField();
        detailsField.setHeight(h, Unit.PIXELS);
        detailsField.setWidth(w, Unit.PIXELS);

        Button btnClear = new Button("清除", VaadinIcons.DEL_A);
        btnClear.setHeight(h, Unit.PIXELS);
        btnClear.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        btnClear.addClickListener(e -> {
            dateTimeField.clear();
            userNameField.clear();
            ipField.clear();
            actionNameField.clear();
            detailsField.clear();
        });

        Button btnSearch = new Button("搜索", VaadinIcons.SEARCH);
        btnSearch.setHeight(h, Unit.PIXELS);
        btnSearch.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
        btnSearch.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnSearch.addClickListener(e -> {
            List<Logging> list = new ArrayList<>();
            for(int i = 0; i < 30; i++) {
                Logging log = new Logging(new Date(),"chens","192.168.91.12","登录","登录了前台界面");
                log.setId(i*1L);
                list.add(log);
            }
            grid.setDataProvider(new ListDataProvider(list));
        });

        layout.addComponents(title1,dateTimeField,title2,userNameField,title3,ipField,title4,actionNameField,title5,detailsField,btnClear,btnSearch);
        layout.setComponentAlignment(title1, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(title2, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(title3, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(title4, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(title5, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(dateTimeField, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(userNameField, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(ipField, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(actionNameField, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(detailsField, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(btnClear, Alignment.MIDDLE_LEFT);
        layout.setComponentAlignment(btnSearch, Alignment.MIDDLE_LEFT);
        return layout;
    }

    private ComboBox createPageSizeBox() {
        Collection<String> data = new ArrayList<>();
        data.add("10");
        data.add("20");
        data.add("30");
        data.add("50");
        data.add("100");
        ComboBox<String> pages = new ComboBox(null,data);
        pages.setWidth("100px");
        pages.setTextInputAllowed(false);
        // Disallow null selections
        pages.setEmptySelectionAllowed(false);
        pages.setValue("50");
        pages.addValueChangeListener(e -> {
            System.out.println("pageSize="+pages.getValue());
        });
        return pages;
    }

    private HorizontalLayout pagingFooter(int page) {
        pagingLayout.setWidthUndefined();
        pagingLayout.setHeight("40px");
        Button previous = new Button("<");
        previous.setStyleName("link");
        previous.addClickListener(e -> {
            pagingLayout.removeAllComponents();
            pagingLayout = pagingFooter(page -1);
        });
        pagingLayout.addComponents(previous);
        int from = page - 2;
        int to = page + 2;
        if(from < 1) {
            from = 1;
            to = 5;
            if(to > pageCount) {
                to = pageCount;
            }
        }
        if(to > pageCount) {
            to = pageCount;
            from = pageCount - 4;
            if(from < 1) {
                from  = 1;
            }
        }
        for(i = from; i <= to; i++) {
            final Button num = new Button(""+i);
            num.setStyleName("link");
            num.addClickListener(e -> {
                pagingLayout.removeAllComponents();
                pagingLayout = pagingFooter(Integer.parseInt(num.getCaption()));

            });
            pagingLayout.addComponents(num);
        }
        Button next = new Button(">");
        next.setStyleName("link");
        next.addClickListener(e -> {
            pagingLayout.removeAllComponents();
            pagingLayout = pagingFooter(page + 1);
        });

        Label lebel = new Label("每页显示行数：");
        pagingLayout.addComponents(next,lebel, pages);
        pagingLayout.setComponentAlignment(lebel, Alignment.MIDDLE_LEFT);
        pagingLayout.setComponentAlignment(pages, Alignment.MIDDLE_LEFT);
        return pagingLayout;
    }

}
