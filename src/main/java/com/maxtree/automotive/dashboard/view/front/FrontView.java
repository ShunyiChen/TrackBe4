package com.maxtree.automotive.dashboard.view.front;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.LicenseHasExpiredWindow;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.component.NotificationsButton;
import com.maxtree.automotive.dashboard.component.NotificationsPopup;
import com.maxtree.automotive.dashboard.component.Test;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Car;
import com.maxtree.automotive.dashboard.domain.Community;
import com.maxtree.automotive.dashboard.domain.Company;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.maxtree.automotive.dashboard.domain.Notification;
import com.maxtree.automotive.dashboard.domain.Queue;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.Transition;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.event.DashboardEvent;
import com.maxtree.automotive.dashboard.event.DashboardEventBus;
import com.maxtree.automotive.dashboard.servlet.UploadFileServlet;
import com.maxtree.automotive.dashboard.view.DashboardMenu;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.data.Binder;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.UIEvents;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.schlichtherle.license.LicenseContent;

/**
 * 
 * @author chens
 *
 */
@SuppressWarnings("serial")
public final class FrontView extends Panel implements View,InputViewIF {
    
	private static final Logger log = LoggerFactory.getLogger(FrontView.class);
	
    public FrontView() {
        addStyleName(ValoTheme.PANEL_BORDERLESS);
        setSizeFull();
        DashboardEventBus.register(this);
        root = new VerticalLayout();
        root.setWidth("100%");
        root.setHeight("100%");
        root.setSpacing(false);
        root.addStyleName("dashboard-view");
        setContent(root);
        Responsive.makeResponsive(root);
        root.addComponent(buildHeader());
        root.addComponent(buildSparklines());
        main.addStyleName("dynamicallyVLayout");
        main.setWidth("100%");
        main.setHeight("100%");
        main.addComponents(blankLabel);
        main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
        root.addComponents(main);
        root.setExpandRatio(main, 7.0f);
        loggedInUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
        // All the open sub-windows should be closed whenever the root layout
        // gets clicked.
        root.addLayoutClickListener(new LayoutClickListener() {
            @Override
            public void layoutClick(final LayoutClickEvent event) {
                DashboardEventBus.post(new DashboardEvent.CloseOpenWindowsEvent());
            }
        });
        
        // 开启轮询事件提醒
        startPolling();
        
        ui.access(new Runnable() {
            @Override
            public void run() {
            	
            	
            	Callback2 verifyEvent = new Callback2() {
					@Override
					public void onSuccessful(Object... objects) {
						// fails
						if (objects[0].equals("")) {
							
							LicenseHasExpiredWindow.open("您的许可证已经过期，请联系管理员及时更新许可证文件。", new Callback() {

								@Override
								public void onSuccessful() {
									DashboardEventBus.post(new DashboardEvent.UserLoggedOutEvent());
								}
				        	});
							
						} else {
							
							LicenseContent content = (LicenseContent) objects[0];
					        long endTimes = content.getNotAfter().getTime();
					        long times = new Date().getTime();
					        long minusTimes = endTimes - times;
							
					        int remainingDays = (int) (minusTimes / (1000 * 60 * 60 * 24));
					        if (remainingDays <= 15) {
					        	LicenseHasExpiredWindow.open("您的授权证书有效期剩余 "+remainingDays+" 天，请联系管理员及时更新许可文件。", null);
					        }
						}
					}
				};
				new Test().verify(verifyEvent);
            	
            	
            }
        });
    }
    
    /**
     * 
     */
	private void startPolling() {
		SystemConfiguration sc = Yaml.readSystemConfiguration();
		ui.setPollInterval(sc.getInterval());
		ui.addPollListener(new UIEvents.PollListener() {
			@Override
			public void poll(UIEvents.PollEvent event) {
				updateUnreadCount();
			}
		});
	}

    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);
        return sparks;
    }

    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        titleLabel = new Label("扫描录入");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);
        buildFillButton();
        buildPrintButton();
        buildNotificationsButton();
        buildAddButton();
        buildCommitButton();
        HorizontalLayout tools = new HorizontalLayout(btnFill,btnPrint, btnAdd, btnCommit,notificationsButton);
        tools.addStyleName("toolbar");
        header.addComponent(tools);

        return header;
    }

    

    @Override
    public void enter(final ViewChangeEvent event) {
    	updateUnreadCount();
    }
    
    /**
     * 
     */
    private void resetComponents() {
    	main.removeAllComponents();
    	main.setHeightUndefined();
    	
    	fileGrid.removeAllRows();
//    	businessTypePane.setSelectorEnabled(false);
    	
    	spliterSouth.setSizeFull();
    	spliterSouth.addComponents(fileGrid, capturePane);
    	
    	spliterNorth.setSizeFull();
    	spliterNorth.addComponents(basicInfoPane, businessTypePane);
    	spliterNorth.setExpandRatio(basicInfoPane, 3);
    	spliterNorth.setExpandRatio(businessTypePane, 1);
    	main.addComponents(spliterNorth, spliterSouth);
    	
    	main.setExpandRatio(spliterNorth, 1);
    	main.setExpandRatio(spliterSouth, 9);
    }
    
    /**
     * 补充业务流水号
     * 
     * @return
     */
    private void buildFillButton() {
    	btnFill.setEnabled(true);
    	btnFill.setId(EDIT_ID);
    	btnFill.setIcon(VaadinIcons.BARCODE);
    	btnFill.addStyleName("icon-edit");
    	btnFill.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	btnFill.setDescription("补充业务流水号");
    	btnFill.addClickListener(e -> {
    		FillBarcodeWindow.open();
        });
    }
    
    /**
     * 
     */
    private void buildPrintButton() {
    	btnPrint.setEnabled(true);
    	btnPrint.setId(EDIT_ID);
    	btnPrint.setIcon(VaadinIcons.PRINT);
    	btnPrint.addStyleName("icon-edit");
    	btnPrint.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	btnPrint.setDescription("打印标签");
    	btnPrint.addClickListener(e -> {
    		if (!validate()) {
    			return;
    		}
    		Callback2 callback = new Callback2() {
				@Override
				public void onSuccessful(Object... objects) {
				}
    		};
    		SearchAndPrintWindow.open("", callback);
        });
    }
    
    /**
     * 添加按钮
     * 
     * @return
     */
    private void buildAddButton() {
    	btnAdd.setEnabled(true);
    	btnAdd.setId(EDIT_ID);
    	btnAdd.setIcon(VaadinIcons.FILE_ADD);
    	btnAdd.addStyleName("icon-edit");
    	btnAdd.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
    	btnAdd.setDescription("创建新业务");
    	btnAdd.addClickListener(e -> {
    		if(editableTrans == null) {
    			commitMode = "INSERT";
    			startTransaction();
    		}
    		else {
    			Notifications.warning("请确保完成当前任务，再执行下一操作。");
    		}
        });
    }
    
    /**
     * 提交按钮 
     * 
     * @return
     */
    private void buildCommitButton() {
    	btnCommit.setId(EDIT_ID);
        btnCommit.setIcon(VaadinIcons.CLOUD_UPLOAD);
        btnCommit.addStyleName("icon-edit");
        btnCommit.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btnCommit.setDescription("提交业务");
        btnCommit.addClickListener(e -> {
        	// 新建
        	if (commitMode.equals("INSERT")) {
        		newTransaction();
        	} 
        	// 更改
        	else if(commitMode.equals("UPDATE")){
        		updateTransaction();
        	}
        });
    }
    
    /**
     * 提醒按钮
     * 
     * @return
     */
    private void buildNotificationsButton() {
        notificationsButton = new NotificationsButton();
    	notificationsButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
            	popup.open(event);
            }
        });
    }
    
    /**
     * 
     */
    private void startTransaction() {
		if (!validate()) {
			return;
		}
		//创建UUID
    	uuid = UUID.randomUUID().toString();
		//如果站点文件夹装满则提醒用户
    	batch = ui.siteService.updateFolders(editableSite);
    	if (batch == 0) {
    		Notifications.warning("当前站点-"+editableSite.getSiteName()+"已满。请联系管理员进行重新分配。");
    		editableSite = null;
    		return;
    	}
    	
    	editableTrans = new Transaction();
    	editableTrans.setBarcode("");
    	editableTrans.setPlateType("");
    	editableTrans.setPlateNumber("");
    	editableTrans.setVin("");
    	basicInfoPane.populateFields(editableTrans);
    	
    	// validating the transaction information
    	basicInfoPane.validatingFieldValues(binder);
    	binder.setBean(editableTrans);
    	
    	resetComponents();
    	
    	capturePane.displayImage();
    }
    
    /**
     * 对当前用户做有效性验证
     */
    private boolean validate() {
		int companyUniqueId = loggedInUser.getCompanyUniqueId();
    	int communityUniqueId = loggedInUser.getCommunityUniqueId();
    	if (companyUniqueId == 0) {
    		Notifications.warning("当前用户没有加入任何机构，请联系管理员进行分配。");
    		return false;
    	}
    	if (communityUniqueId == 0) {
    		Notifications.warning("当前用户没有加入任何社区，请联系管理员进行分配。");
    		return false;
    	}
    	editableCompany = ui.companyService.findById(companyUniqueId);
    	List<Site> allSites = ui.siteService.getSites(communityUniqueId);
    	for (int i = 0; i < allSites.size(); i++) {
    		if (allSites.get(i).getRunningStatus() == 1) {
    			editableSite = allSites.get(i);
    			break;
    		}
    	}
    	if (editableSite == null) {
    		Notifications.warning("当前用户所在的社区不存在文件站点，或站点已关闭。请联系管理员进行设置。");
    		return false;
    	}
    	return true;
    }
    
    /**
     * 
     * @param transaction
     * @param deletableMessageUniqueId
     * @param callback
     */
    public void openTransaction(Transaction transaction,int deletableMessageUniqueId, Callback callback) {
    	commitMode = "UPDATE";
    	editableTrans = transaction;
    	this.deletableMessageUniqueId = deletableMessageUniqueId;//删除提醒用
    	editableSite = ui.siteService.findByCode(editableTrans.getSiteCode());
    	uuid = editableTrans.getUuid();
    	batch = editableTrans.getBatch();
    	vin = editableTrans.getVin();
    	
    	int companyUniqueId = loggedInUser.getCompanyUniqueId();
    	int communityUniqueId = loggedInUser.getCommunityUniqueId();
    	if (companyUniqueId == 0) {
    		Notifications.warning("当前用户没有加入任何机构，请联系管理员进行分配。");
    		return;
    	}
    	if (communityUniqueId == 0) {
    		Notifications.warning("当前用户没有加入任何社区，请联系管理员进行分配。");
    		return;
    	}
    	editableCompany = ui.companyService.findById(companyUniqueId);
    	List<Site> allSites = ui.siteService.getSites(communityUniqueId);
    	for (int i = 0; i < allSites.size(); i++) {
    		if (allSites.get(i).getRunningStatus() == 1) {
    			editableSite = allSites.get(i);
    			break;
    		}
    	}
    	if (editableSite == null) {
    		Notifications.warning("当前用户所在的社区不存在文件站点，或站点已关闭。请联系管理员进行设置。");
    		return;
    	}
    	
    	// validating the transaction information
    	basicInfoPane.validatingFieldValues(binder);
    	binder.setBean(editableTrans);
    	
    	resetComponents();
    	
    	basicInfoPane.populateFields(editableTrans);
    	businessTypePane.populate(editableTrans.getBusinessCode());
    	
    	callback.onSuccessful();
    }
    
    /**
     * 提交异常
     */
    public void throwException(String exception) {
    	this.exception = exception;
    }
    
    /**
     * 
     */
    public void newTransaction() {
    	if(!StringUtils.isEmpty(exception)) {
    		Notifications.warning(exception);
    		return;
    	}
    	if(editableTrans == null) {
    		Notifications.warning("提交异常。");
    		return;
    	}
    	//如果是新车注册业务，则需验证业务流水号
    	if(!basicInfoPane.emptyChecks(businessTypePane.getSelected().getName().contains("注册登记"))) {
			Notifications.warning("有效性验证失败。");
			return;
    	}
    	if (fileGrid.validationFails()) {
			Notifications.warning("请将必录材料上传完整。");
			return;
    	}
    	//4大流程
    	//新车注册流程
    	if (businessTypePane.getSelected().getName().contains("注册登记")) {
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
        	editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
//        	editableTrans.setLocationCode(editableCompany.getProvince()+","+editableCompany.getCity()+","+editableCompany.getDistrict());
        	editableTrans.setBatch(batch);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	editableTrans.setIndexNumber(1);
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 获取社区内的车管所
        		Company com = ui.communityService.findDMVByCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		if(com == null) {
        			Notifications.warning("当前社区不存在车管所，请联系管理员进行设置。");
        			return;
        		}
        		
        		FrameNumber frame = ui.frameService.getNewCode(com.getStorehouseName());
        		if(StringUtils.isEmpty(frame.getCode())) {
        			Notifications.warning("没有可用的上架号，请联系管理员设置库房（注册登记）。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(frame.getCode()+"");//上架号
        			ui.frameService.updateVIN(basicInfoPane.getVIN(), frame.getCode());
        		}
        		
        		editableTrans.setStatus(ui.state().getName("B2"));
        		ui.transactionService.insert(editableTrans);
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		//操作记录
        		track(ui.state().getName("B2"));
        		
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已完成逻辑上架。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.insert(editableTrans);
        		
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待质检。");
        	}
    	}
    	
    	// 非审档流程
    	else if ("无".equals(businessTypePane.getSelected().getCheckLevel())) {
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
//        	editableTrans.setLocationCode(editableCompany.getProvince()+","+editableCompany.getCity()+","+editableCompany.getDistrict());
        	editableTrans.setBatch(batch);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber + 1);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findTransactionCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号或新车注册业务不存在(非审档)。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(ui.state().getName("B2"));
        		ui.transactionService.insert(editableTrans);
        		
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		//操作记录
        		track(ui.state().getName("B2"));
        		
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已完成逻辑上架。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.insert(editableTrans);
        		
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待质检。");
        	}
    	}
    	
    	// 需要审档（一级）流程
    	else if (businessTypePane.getSelected().getCheckLevel().equals("一级审档")) {
    		Community myCommunity = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	editableTrans.setLocationCode(myCommunity.getProvince()+","+myCommunity.getCity()+","+myCommunity.getDistrict());
        	editableTrans.setBatch(batch);
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber + 1);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findTransactionCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号或新车注册业务不存在(一级审档)。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(ui.state().getName("B4"));
        		ui.transactionService.insert(editableTrans);
        		
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		// 插入待审档队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B4"));
        		
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待审档。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.insert(editableTrans);
        		
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待质检。");
        	}
    	}
    	
    	
    	// 需要审档（二级）流程
    	else if (businessTypePane.getSelected().getCheckLevel().equals("二级审档")) {
    		Community myCommunity = ui.communityService.findById(loggedInUser.getCommunityUniqueId());
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
    		editableTrans.setDateCreated(new Date());
        	editableTrans.setDateModified(new Date());
        	editableTrans.setSiteCode(editableSite.getCode());
        	editableTrans.setBusinessCode(businessTypePane.getSelected().getCode());
        	editableTrans.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        	editableTrans.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        	editableTrans.setLocationCode(myCommunity.getProvince()+","+myCommunity.getCity()+","+myCommunity.getDistrict());
        	editableTrans.setUuid(uuid);
        	editableTrans.setCreator(loggedInUser.getUserName());
        	int indexNumber = ui.transactionService.findIndexNumber(basicInfoPane.getVIN());
        	editableTrans.setIndexNumber(indexNumber + 1);
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		// 新车注册首个上架号
        		String firstCode = ui.transactionService.findTransactionCode(basicInfoPane.getVIN());
        		if(StringUtils.isEmpty(firstCode)) {
        			Notifications.warning("没有可用的上架号或新车注册业务不存在(二级审档)。");
        			return;
        		} else {
        			//跳过质检，完成逻辑上架
        			editableTrans.setCode(firstCode+"");//上架号
        		}
        		editableTrans.setStatus(ui.state().getName("B4"));
        		ui.transactionService.insert(editableTrans);
        		
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		// 判断是不是车管所用户
            	boolean belongsToDMV = ui.companyService.isDMV(loggedInUser.getCompanyUniqueId());
            	// 如果是车管所录入的，则需要车管所审档
            	if(belongsToDMV) {
            		// 插入待审档队列
            		Queue newQueue = new Queue();
            		newQueue.setUuid(editableTrans.getUuid());
            		newQueue.setVin(editableTrans.getVin());
            		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
            		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
            		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
            		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
            		ui.queueService.create(newQueue, serial);
            	}
            	else {
            		Company com = ui.communityService.findDMVByCommunityUniqueId(loggedInUser.getCommunityUniqueId());
            		if(com == null) {
            			Notifications.warning("当前社区不存在车管所，请联系管理员进行设置。");
            			return;
            		}
            		// 插入待审档队列
            		Queue newQueue = new Queue();
            		newQueue.setUuid(editableTrans.getUuid());
            		newQueue.setVin(editableTrans.getVin());
            		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
            		newQueue.setCompanyUniqueId(com.getCompanyUniqueId());
            		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
            		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
            		ui.queueService.create(newQueue, serial);
            	}
        		
        		//操作记录
        		track(ui.state().getName("B4"));
        		
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待审档。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.insert(editableTrans);
        		
        		//更新车辆信息
        		updateCar(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待质检。");
        	}
    	}
    }
    
    /**
     * 
     * @param trans
     */
    private void updateCar(Transaction trans) {
    	ui.carService.delete(trans.getVin());
    	
    	Car car = new Car();
		car.setBarcode(trans.getBarcode());
		car.setPlateType(trans.getPlateType());
		car.setPlateNumber(trans.getPlateNumber());
		car.setVin(trans.getVin());
		ui.carService.insert(car);
    }
    
    /**
     * 
     */
    private void updateTransaction() {
    	if(editableTrans == null) {
    		Notifications.warning("提交异常。");
    		return;
    	}
    	//如果是新车注册业务，则需录入业务流水好吧
    	if(!basicInfoPane.emptyChecks(businessTypePane.getSelected().getName().contains("注册登记"))) {
			Notifications.warning("有效性验证失败。");
			return;
    	}
    	if (fileGrid.validationFails()) {
			Notifications.warning("请将必录材料上传完整。");
			return;
    	}
    	
    	// 非审档流程
    	if (businessTypePane.getSelected().getName().contains("注册登记")) {
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
        	editableTrans.setDateModified(new Date());
    		// 跳过质检
    		if(editableCompany.getIgnoreChecker() == 1) {
    			editableTrans.setStatus(ui.state().getName("B2"));
        		ui.transactionService.update(editableTrans);
        		
        		//操作记录
        		track(ui.state().getName("B2"));
        		
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已完成逻辑上架。");
    			
    		}
    		else {
    			editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.update(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待质检。");
    		}
    		
    	}
    	// 非审档流程
    	else if ("无".equals(businessTypePane.getSelected().getCheckLevel())) {
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
        	editableTrans.setDateModified(new Date());
        	// 跳过质检
    		if(editableCompany.getIgnoreChecker() == 1) {
    			editableTrans.setStatus(ui.state().getName("B2"));
        		ui.transactionService.update(editableTrans);
        		
        		//操作记录
        		track(ui.state().getName("B2"));
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已完成逻辑上架。");
    		}
    		else {
    			editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.update(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列等待质检。");
    		}
    		
    	}
    	// 两大流程
    	// 需要审档（一级）流程
    	else if (businessTypePane.getSelected().getCheckLevel().equals("一级审档")) {
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
        	editableTrans.setDateModified(new Date());
        	
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		editableTrans.setStatus(ui.state().getName("B4"));
        		ui.transactionService.update(editableTrans);
        		
        		// 插入待审档队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B4"));
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已添加到队列中等待审档。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.update(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已提交到队列中等待质检。");
        	}
    	}
    	
    	// 需要审档（二级）流程
    	else if (businessTypePane.getSelected().getCheckLevel().equals("二级审档")) {
    		basicInfoPane.populateTransaction(editableTrans);//赋值基本信息
        	editableTrans.setDateModified(new Date());
        	// 是否跳过质检
        	if(editableCompany.getIgnoreChecker() == 1) {
        		editableTrans.setStatus(ui.state().getName("B4"));
        		ui.transactionService.update(editableTrans);
        		
        		// 插入待审档队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 2;// 1:质检队列，2：审档队列，3：确认审档队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B4"));
        		//清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已添加到队列中等待审档。");
        	}
        	// 提交给质检队列
        	else {
        		editableTrans.setStatus(ui.state().getName("B7"));
        		ui.transactionService.update(editableTrans);
        		
        		// 添加到质检队列
        		Queue newQueue = new Queue();
        		newQueue.setUuid(editableTrans.getUuid());
        		newQueue.setVin(editableTrans.getVin());
        		newQueue.setLockedByUser(0);	// 默认为0标识任何人都可以取，除非被某人锁定
        		newQueue.setCompanyUniqueId(loggedInUser.getCompanyUniqueId());
        		newQueue.setCommunityUniqueId(loggedInUser.getCommunityUniqueId());
        		int serial = 1;// 1:代表质检取队列，2：代表审档取队列
        		ui.queueService.create(newQueue, serial);
        		
        		//操作记录
        		track(ui.state().getName("B7"));
        		// 清空舞台
            	cleanStage();
            	Notifications.bottomWarning("提交成功！已添加到队列中等待质检。");
        	}
    	}
    	System.out.println(deletableMessageUniqueId+","+loggedInUser.getUserUniqueId());
    	messageSys.deleteMessage(deletableMessageUniqueId,loggedInUser.getUserUniqueId(),TB4MessagingSystem.PERMANENTLYDELETE);
    }
    
    /**
     * 
     * @param status
     */
    private void track(String status) {
    	// 插入移行表
		Transition transition = new Transition();
		transition.setTransactionUUID(uuid);
		transition.setVin(basicInfoPane.getVIN());
		transition.setActivity(status);
		transition.setComments(null);
		transition.setOperator(loggedInUser.getUserName());
		transition.setDateCreated(new Date());
		ui.transitionService.insert(transition, basicInfoPane.getVIN());
    }
    
	@Override
	public void cleanStage() {
		main.removeAllComponents();
		main.setHeight("100%");
		main.addComponents(blankLabel);
		main.setComponentAlignment(blankLabel, Alignment.MIDDLE_CENTER);
		//清空当前用户上传缓存
		UploadFileServlet.IN_DTOs.remove(loggedInUser.getUserUniqueId());
		UploadFileServlet.OUT_DTOs.remove(loggedInUser.getUserUniqueId());
		try {
			FileUtils.forceDelete(new File("devices/"+loggedInUser.getUserUniqueId()+".html"));
		} catch (IOException e) {
			log.info("删除"+"devices/"+loggedInUser.getUserUniqueId()+".html失败。");
		}
		
		editableTrans = null;
		exception = "";
	}

	@Override
	public void updateUnreadCount() {
		List<Notification> notifications = CacheManager.getInstance().getNotificationsCache().get(loggedInUser.getUserUniqueId());
		int unreadCount = 0;
		for (Notification n : notifications) {
			if (n.getViewName().equals(DashboardViewType.INPUT.getViewName())) {
				unreadCount++;
			}
		}
		
		DashboardMenu.getInstance().updateNotificationsCount(unreadCount);
		notificationsButton.setUnreadCount(unreadCount);
	}
	
	@Override
	public User loggedInUser() {
		return loggedInUser;
	}
	
	@Override
	public int batch() {
		return batch;
	}
	
	@Override
	public String uuid() {
		return uuid;
	}
	
	@Override
	public String vin() {
		return basicInfoPane.getVIN();//vin;
	}

	@Override
	public Site editableSite() {
		return editableSite;
	}
	
	@Override
	public BasicInfoPane basicInfoPane() {
		return basicInfoPane;
	}
	
	@Override
	public BusinessTypePane businessTypePane() {
		return businessTypePane;
	}
	
	@Override
	public ThumbnailGrid thumbnailGrid() {
		return fileGrid;
	}
	
	@Override
	public CapturePane capturePane() {
		return capturePane;
	}
	
	public static final String EDIT_ID = "dashboard-edit";
	public static final String TITLE_ID = "dashboard-title";
	private String commitMode = "INSERT";
	private Transaction editableTrans; 	//可编辑的编辑transaction
	private Company editableCompany = null;	 	//前台所在机构
	private User loggedInUser;	//登录用户
	private Site editableSite;	//业务站点
	private String uuid;			//UUID业务与原文关联号
	private int batch;			//业务批次号。默认最大1000个批次，每批次最多放5000文件夹。
	private String vin;// = "LGB12YEA9DY001226";			//车辆识别代码。用于分表。
    private Label titleLabel;
    private VerticalLayout root;
    private VerticalLayout main = new VerticalLayout();
    private DashboardUI ui = (DashboardUI) UI.getCurrent();
    private Binder<Transaction> binder = new Binder<>();
    //各个区域面板
    private BasicInfoPane basicInfoPane = new BasicInfoPane(this);
    private BusinessTypePane businessTypePane = new BusinessTypePane(this);
    private ThumbnailGrid fileGrid = new ThumbnailGrid(this);
    private CapturePane capturePane = new CapturePane();
    private Button btnFill = new Button();
    private Button btnPrint = new Button();
    private Button btnAdd = new Button();
    private Button btnCommit = new Button();
    private NotificationsButton notificationsButton;
    private Label blankLabel = new Label("<span style='font-size:24px;color: #8D99A6;font-family: Microsoft YaHei;'>暂无可编辑的信息</span>", ContentMode.HTML);
    private HorizontalLayout spliterNorth = new HorizontalLayout();
    private HorizontalLayout spliterSouth = new HorizontalLayout();
    private NotificationsPopup popup = new NotificationsPopup(DashboardViewType.INPUT.getViewName(), this);
    private int deletableMessageUniqueId;
    private String exception;
    private TB4MessagingSystem messageSys = new TB4MessagingSystem();
}
