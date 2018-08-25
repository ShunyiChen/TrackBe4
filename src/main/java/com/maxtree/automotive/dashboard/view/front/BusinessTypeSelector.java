package com.maxtree.automotive.dashboard.view.front;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.maxtree.automotive.dashboard.BusinessState;
import com.maxtree.automotive.dashboard.Callback;
import com.maxtree.automotive.dashboard.DashboardUI;
import com.maxtree.automotive.dashboard.Openwith;
import com.maxtree.automotive.dashboard.Popup;
import com.maxtree.automotive.dashboard.cache.CacheManager;
import com.maxtree.automotive.dashboard.component.MessageBox;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.data.SystemConfiguration;
import com.maxtree.automotive.dashboard.data.Yaml;
import com.maxtree.automotive.dashboard.domain.Business;
import com.maxtree.automotive.dashboard.domain.DataDictionary;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Imaging;
import com.maxtree.automotive.dashboard.domain.Message;
import com.maxtree.automotive.dashboard.domain.Transaction;
import com.maxtree.automotive.dashboard.domain.User;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.servlet.UploadFileServlet;
import com.maxtree.automotive.dashboard.servlet.UploadInDTO;
import com.maxtree.automotive.dashboard.servlet.UploadOutDTO;
import com.maxtree.automotive.dashboard.view.DashboardViewType;
import com.maxtree.automotive.dashboard.view.InputViewIF;
import com.maxtree.automotive.dashboard.view.imaging.ImagingInputView;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.maxtree.trackbe4.messagingsystem.MessageBodyParser;
import com.maxtree.trackbe4.messagingsystem.Name;
import com.maxtree.trackbe4.messagingsystem.TB4MessagingSystem;
import com.vaadin.event.UIEvents;
import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.EventId;
import com.vaadin.shared.Registration;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.UI;

/**
 * 业务类型选择器
 * 
 * @author Chen
 *
 */
public class BusinessTypeSelector extends FormLayout implements SingleSelectionListener<Business>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param view
	 */
	public BusinessTypeSelector(InputViewIF view) {
		this.view = view;
		this.setSpacing(false);
		this.setMargin(false);
		this.setSizeFull();
		loggedinUser = (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
		data = ui.userService.findAssignedBusinesses(loggedinUser.getUserUniqueId());
		selector = new ComboBox<Business>("业务类型:", data);
		// Disallow null selections
		selector.setEmptySelectionAllowed(true);
		selector.setTextInputAllowed(false);
		selector.setPlaceholder("请选择一个业务类型");
		selector.setWidth("100%");
		selector.setHeight("27px");
		selector.setEnabled(false);
		this.addComponent(selector);
		
		this.initPollListener();
		// 解决1秒内选不完问题
		selector.addFocusListener(e->{
			ui.setPollInterval(-1);
		});
		selector.addBlurListener(e->{
			SystemConfiguration sc = Yaml.readSystemConfiguration();
			ui.setPollInterval(sc.getInterval());
		});
		
		selector.addSelectionListener(this);
	}
	
	@Override
	public void selectionChange(SingleSelectionEvent<Business> e) {
		if (view.vin() == null) {
			Notifications.warning("车辆识别代码不能空。");
			return;
		}
		// 支持影像化检测
		if (view instanceof FrontView) {
			// 影像化检测
			if (imagingCheck()) {
				Optional<Business> opt = e.getSelectedItem();
				if (opt.isPresent() && existCheck(view.vin(), opt.get().getCode())) {
					view.thumbnailGrid().removeAllRows();
					Notifications.warning("此业务已经办理过了。");
					return;
				}
				
				if (opt.isPresent() && opt.get() != e.getOldValue()) {
					//删除旧原文1
					List<Document> document1List = ui.documentService.findAllDocument1(view.vin(), view.uuid());
					for(Document doc : document1List) {
						try {
							fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
						} catch (FileException fe) {
							fe.printStackTrace();
						}
					}
					//删除旧原文2
					List<Document> document2List = ui.documentService.findAllDocument2(view.vin(), view.uuid());
					for(Document doc : document2List) {
						try {
							fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
						} catch (FileException fe) {
							fe.printStackTrace();
						}
					}
					//删除数据库记录
					ui.documentService.deleteByUUID(view.uuid(), view.vin());
					loadMaterials(opt.get().getCode());
				}
				else {
					view.thumbnailGrid().removeAllRows();
				}
			}
			else {
				insertImaging();
			}
		}
		// 不支持影像化检查
		else {
			Optional<Business> opt = e.getSelectedItem();
			if (opt.isPresent() && existCheck(view.vin(), opt.get().getCode())) {
				Notifications.warning("此业务已经办理过。");
				return;
			}
			if (opt.isPresent() && opt.get() != e.getOldValue()) {
				//删除旧原文1
				List<Document> document1List = ui.documentService.findAllDocument1(view.vin(), view.uuid());
				for(Document doc : document1List) {
					try {
						fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
					} catch (FileException fe) {
						fe.printStackTrace();
					}
				}
				//删除旧原文2
				List<Document> document2List = ui.documentService.findAllDocument2(view.vin(), view.uuid());
				for(Document doc : document2List) {
					try {
						fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
					} catch (FileException fe) {
						fe.printStackTrace();
					}
				}
				//删除数据库记录
				ui.documentService.deleteByUUID(view.uuid(), view.vin());
				loadMaterials(opt.get().getCode());
			}
			else {
				view.thumbnailGrid().removeAllRows();
			}
		}
	}
	
	/**
	 * 
	 */
	private void insertImaging() {
		//1.插入影像化记录
		Imaging imaging = new Imaging();
		imaging.setCreator(view.loggedInUser().getUserName());
		imaging.setDateCreated(new Date());
		imaging.setDateModified(new Date());
		imaging.setPlateNumber(view.basicInfoPane().getPlateNumber());
		imaging.setPlateType(view.basicInfoPane().getPlateType());
		imaging.setVin(view.basicInfoPane().getVIN());
		imaging.setStatus(BusinessState.B8.name);
		int imaginguniqueid = ui.imagingService.insert(imaging);
		if (imaginguniqueid != 0) {
			//2.发信给影像化管理员
			User receiver = ui.userService.findImagingAdmin(view.loggedInUser().getCommunityUniqueId());
			if (receiver.getUserUniqueId() == 0) {
				Notifications.warning("无法找到影像化管理员，请联系系统管理员进行设置。");
				return;
			}
			String matedata = "{\"openwith\":\""+Openwith.MESSAGE+"\",\"imaginguniqueid\":\""+imaginguniqueid+"\",\"popup\":\""+Popup.YES+"\"}";
			TB4MessagingSystem messageSystem = new TB4MessagingSystem();
			Message newMessage = messageSystem.createNewMessage(loggedinUser, view.basicInfoPane().getPlateNumber()+"影像化补充", "请补充车牌号"+imaging.getPlateNumber()+"的全部历史影像化记录。", matedata);
			Set<Name> names = new HashSet<Name>();
			Name target = new Name(receiver.getUserUniqueId(), Name.USER, receiver.getProfile().getLastName()+receiver.getProfile().getFirstName(), receiver.getProfile().getPicture());
			names.add(target);
			messageSystem.sendMessageTo(newMessage.getMessageUniqueId(),names,DashboardViewType.IMAGING_MANAGER.getViewName());
			//3.更新消息轮询的缓存
			CacheManager.getInstance().getSendDetailsCache().refresh(receiver.getUserUniqueId());
		}
		
		Callback callback = new Callback() {
			@Override
			public void onSuccessful() {
				view.cleanStage();
			}
		};
		MessageBox.showMessage("提示","缺少历史影像化记录，已提交申请。等待补充完整后再重新开始本次业务登记。",callback);
	}
	
	/**
     * 影像化检测
     * 
     * @return
     */
    private boolean imagingCheck() {
    	/*
    	 需求：
	    	 设立一个影响化管理人，有单独角色单独界面，界面有列表，一行一个车牌号。全程自己手动更改业务状态（待查看，待提档，待归档，完成）
	    	 影像化录入，单独角色单独界面，根据纸质录入车辆信息，上传原文，提交给质检。
	    	 影响化质检，单独角色单独界面，根据纸质录入车辆信息，查看原文，退回质检或完成后将纸质档案放回。
    	 */
    	if(StringUtils.isEmpty(view.businessTypePane().getSelected())) {
    		return true;
    	}
    	else if(view.businessTypePane().getSelected().getName().equals("注册登记")) {
    		return true;
    	}
    	else if (view instanceof ImagingInputView){
    		return true;
    	}
    	else {
    		List<Transaction> result = ui.transactionService.findForList(view.vin(),0);
    		return (result.size() != 0);
    	}
    }
	
    /**
     * 
     * @param vin
     * @param businessCode
     * @return
     */
	private boolean existCheck(String vin, String businessCode) {
		int count = ui.transactionService.getCount(vin, businessCode);
		return (count > 0);
	}
    
    
	/**
	 * 根据业务类型加载材料
	 * 
	 * @param businessCode
	 */
	private void loadMaterials(String businessCode) {
		// 加载文件上传表格
		view.thumbnailGrid().removeAllRows();
		List<DataDictionary> list = ui.businessService.getDataDictionaries(businessCode,3);
		int i = 0;
		for (DataDictionary dd : list) {
			i++;
			ThumbnailRow row = new ThumbnailRow(i+"."+dd.getItemName());
			row.setDataDictionary(dd);
			
			view.thumbnailGrid().addRow(row);
			// 选中第一个
			if (i == 1) {
				row.selected();
				
				UploadInDTO inDto = new UploadInDTO(view.loggedInUser().getUserUniqueId(), view.vin(), view.batch()+"", view.editableSite().getSiteUniqueId(),view.uuid(),dd.getCode());
				UploadFileServlet.IN_DTOs.put(view.loggedInUser().getUserUniqueId(), inDto);
			}
		}
		i++;
		ThumbnailRow row = new ThumbnailRow(i+"."+"其它材料");
		DataDictionary dict = new DataDictionary();
		dict.setCode("$$$$"); // $$$$为辅助材料code
		row.setDataDictionary(dict);
		view.thumbnailGrid().addRow(row);
		view.thumbnailGrid().focus();
		// 加载拍照影像
		view.capturePane().displayImage(view.uuid());
	}
	
	/**
	 * 上传图像回显
	 */
	private void initPollListener() {
		
		pollListener = new UIEvents.PollListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void poll(UIEvents.PollEvent event) {
				// 定期获取上传文件表格焦点，从而可以按上下键控制
				view.thumbnailGrid().focus();
				
				List<UploadOutDTO> list = UploadFileServlet.OUT_DTOs.get(view.loggedInUser().getUserUniqueId());
				if (list != null) {
 
					Iterator<UploadOutDTO> iter = list.iterator();
					while(iter.hasNext()) {
						UploadOutDTO ufq = iter.next();
						if (ufq.getRemovable() == 0) {
							
							Thumbnail thumbnail = new Thumbnail(ufq.thumbnail);
							// 右键菜单
							com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(thumbnail, true);
							menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
								@Override
								public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
									ImageViewerWindow.open(view, ufq.getDocumentUniqueId());
								}
							});
							menu.addSeparator();
							menu.addItem("删除", new com.vaadin.contextmenu.Menu.Command() {
								@Override
								public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
									//从文件系统删除
									try {
										fileSystem.deleteFile(view.editableSite(), ufq.getFileFullPath());
									} catch (FileException e) {
										e.printStackTrace();
									}
									
									//从数据库删除
									ui.documentService.deleteById(ufq.getDocumentUniqueId(), ufq.getLocation() ,view.vin());
									
									//从UI删除
									ThumbnailRow row = view.thumbnailGrid().mapRows.get(ufq.getDictionaryCode());
									row.removeThumbnail(thumbnail);
								}
							});
							System.out.println("view="+ufq.getDictionaryCode()+"    thumbnail="+thumbnail);
							view.thumbnailGrid().mapRows.get(ufq.getDictionaryCode()).addThumbnail(thumbnail);
							ufq.setRemovable(1);
						}
						else {
							// 已经回显过的从缓存清空
							iter.remove();
						}
					}
				}
			}
		};
		// 注册监听
		ui.addPollListener(pollListener);
	}

	/**
	 * 
	 * @return
	 */
	public Business getValue() {
		return selector.getValue();
	}
	
	/**
	 * 
	 * @param enabled
	 */
	public void setEnabled2(boolean enabled) {
		selector.setEnabled(enabled);
	}
	
	/**
	 * 业务编码
	 * @param businessCode
	 */
	public void populate(String businessCode) {
		// 删除
		selector.removeListener(SingleSelectionEvent.class, this);
		
		for(Business business : data) {
			if(business.getCode().equals(businessCode)) {
				selector.setSelectedItem(business);
				break;
			}
		}
		// 重新加载Row
		loadMaterials(businessCode);
		/// 为每个Row添加缩略图
		List<Document> documentList1 = ui.documentService.findAllDocument1(view.vin(), view.uuid());
		
		System.out.println("shunyi--"+view.vin() +"," +view.uuid()+"  "+documentList1.size());
		
		int i = 1;
		for (Document doc : documentList1) {
			
			Thumbnail thumbnail = new Thumbnail(new ByteArrayInputStream(doc.getThumbnail()));
			// 右键菜单
			com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(thumbnail, true);
			menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
				@Override
				public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
					ImageViewerWindow.open(view, doc.getDocumentUniqueId());
				}
			});
			menu.addSeparator();
			menu.addItem("删除", new com.vaadin.contextmenu.Menu.Command() {
				@Override
				public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
					//从文件系统删除
					try {
						fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
					} catch (FileException e) {
						e.printStackTrace();
					}
					
					//从数据库删除
					ui.documentService.deleteById(doc.getDocumentUniqueId(),1,view.vin());
					
					//从UI删除
					ThumbnailRow row = view.thumbnailGrid().mapRows.get(doc.getDictionarycode());
					row.removeThumbnail(thumbnail);
				}
			});
			
			
			ThumbnailRow row = view.thumbnailGrid().mapRows.get(doc.getDictionarycode());
			row.addThumbnail(thumbnail);
		}
		List<Document> documentList2 = ui.documentService.findAllDocument2(view.vin(), view.uuid());
		for (Document doc : documentList2) {
			
			Thumbnail thumbnail = new Thumbnail(new ByteArrayInputStream(doc.getThumbnail()));
			// 右键菜单
			com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(thumbnail, true);
			menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
				@Override
				public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
					ImageViewerWindow.open(view, doc.getDocumentUniqueId());
				}
			});
			menu.addSeparator();
			menu.addItem("删除", new com.vaadin.contextmenu.Menu.Command() {
				@Override
				public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
					//从文件系统删除
					try {
						fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
					} catch (FileException e) {
						e.printStackTrace();
					}
					
					//从数据库删除
					ui.documentService.deleteById(doc.getDocumentUniqueId(),2,view.vin());
					
					//从UI删除
					ThumbnailRow row = view.thumbnailGrid().mapRows.get(doc.getDictionarycode());
					row.removeThumbnail(thumbnail);
				}
			});
			
			ThumbnailRow row = view.thumbnailGrid().mapRows.get("$$$$");// $$$$为辅助材料code
			row.addThumbnail(thumbnail);
		}
	}

	private InputViewIF view;
	private List<Business> data;
	private ComboBox<Business> selector;
	private DashboardUI ui = (DashboardUI) UI.getCurrent();
	private TB4FileSystem fileSystem = new TB4FileSystem();
	private UIEvents.PollListener pollListener;
	private MessageBodyParser jsonHelper = new MessageBodyParser();
	private User loggedinUser = null;

}
