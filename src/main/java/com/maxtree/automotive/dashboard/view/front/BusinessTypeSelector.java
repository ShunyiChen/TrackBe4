package com.maxtree.automotive.dashboard.view.front;

import com.vaadin.ui.FormLayout;

/**
 * 业务类型选择器
 * 
 * @author Chen
 *
 */
public class BusinessTypeSelector extends FormLayout {
//
//	/**
//	 *
//	 */
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 *
//	 * @param view
//	 */
//	public BusinessTypeSelector(InputViewIF view) {
//		this.view = view;
//		this.setSpacing(false);
//		this.setMargin(false);
//		this.setSizeFull();
//		String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
//		loggedinUser = ui.userService.getUserByUserName(username);
////		data = ui.userService.findAssignedBusinesses(loggedinUser.getUserUniqueId());
//		selector = new ComboBox<Business>("业务类型:", data);
//		// Disallow null selections
//		selector.setEmptySelectionAllowed(true);
//		selector.setTextInputAllowed(true);
//		selector.setPlaceholder("请选择一个业务类型");
//		selector.setWidth("100%");
//		selector.setHeight("27px");
////		selector.setEnabled(false);
//		this.addComponent(selector);
//
//		this.initPollListener();
//		// 解决1秒内选不完问题
//		selector.addFocusListener(e->{
//			ui.setPollInterval(-1);
//		});
//		selector.addBlurListener(e->{
//			SystemConfiguration sc = Yaml.readSystemConfiguration();
//			ui.setPollInterval(sc.getInterval());
//		});
//
//		selector.addSelectionListener(this);
//	}
//
//	@Override
//	public void selectionChange(SingleSelectionEvent<Business> e) {
//		//没有异常
//		view.throwException(null);
//
//		// 如果业务类型为null,则直接返回
//		if(StringUtils.isEmpty(view.businessTypePane().getSelected())) {
//			//业务类型为空异常
//			view.throwException("业务类型不能选择空选项");
//			return;
//		}
//		if (StringUtils.isEmpty(view.vin())) {
//			Notifications.warning("车辆识别代码不能空。");
//			view.throwException("车辆识别代码不能空");
//			return;
//		}
//		// 支持影像化检测
//		if (view instanceof FrontView) {
//			Optional<Business> opt = e.getSelectedItem();
//			if(!opt.isPresent()) {
//				view.throwException("业务类型不能空");
//				return;
//			}
//			//注册检查
//			if(registrationCheck(view.vin(), e.getSelectedItem().get())) {
//				Notifications.warning(e.getSelectedItem().get().getName()+"业务已经存在，不能重复录入相同业务。");
//				view.throwException(e.getSelectedItem().get().getName()+"业务已经存在，不能重复录入相同业务");
//				return;
//			}
//			//处理中检查
//			if(!beingProcessedCheck(view.vin())) {
//				Notifications.warning("当前车辆存在尚未办结的业务，无法继续。");
//				view.throwException("当前车辆存在尚未办结的业务，无法继续");
//				return;
//			}
//
//			// 影像化检测
//			if (imaginationCheck()) {
//
//				if (opt.isPresent() && opt.get() != e.getOldValue()) {
//					//删除旧原文1
//					List<Document> document1List = ui.documentService.findAllDocument1(view.vin(), view.uuid());
//					for(Document doc : document1List) {
//						try {
//							fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
//						} catch (FileException fe) {
//							fe.printStackTrace();
//						}
//					}
//					//删除数据库记录
//					ui.documentService.deleteByUUID(view.uuid(), view.vin());
//					loadMaterials(opt.get().getCode());
//				}
//				else {
//					view.thumbnailGrid().removeAllRows();
//				}
//			}
//			else {
//				insertImaging();
//			}
//		}
//		// 不支持影像化检查
//		else {
//			Optional<Business> opt = e.getSelectedItem();
//			if(!opt.isPresent()) {
//				return;
//			}
//			if (opt.isPresent() && opt.get() != e.getOldValue()) {
//				//删除旧原文1
//				List<Document> document1List = ui.documentService.findAllDocument1(view.vin(), view.uuid());
//				for(Document doc : document1List) {
//					try {
//						fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
//					} catch (FileException fe) {
//						fe.printStackTrace();
//					}
//				}
//				//删除数据库记录
//				ui.documentService.deleteByUUID(view.uuid(), view.vin());
//				loadMaterials(opt.get().getCode());
//			}
//			else {
//				view.thumbnailGrid().removeAllRows();
//			}
//		}
//	}
//
//	/**
//	 * 插入影像化记录
//	 *
//	 */
//	private void insertImaging() {
//		//1.插入影像化记录
//		Imaging imaging = new Imaging();
//		imaging.setCreator(view.loggedInUser().getUserName());
//		imaging.setDateCreated(new Date());
//		imaging.setDateModified(new Date());
//		imaging.setPlateNumber(view.basicInfoPane().getPlateNumber());
//		imaging.setPlateType(view.basicInfoPane().getPlateType());
//		imaging.setVin(view.basicInfoPane().getVIN());
//		imaging.setStatus(ui.state().getName("B8"));
//		int imaginguniqueid = ui.imagingService.insert(imaging);
//		if (imaginguniqueid != 0) {
//			//2.发信给影像化管理员
//			User receiver = ui.userService.findImagingAdmin(view.loggedInUser().getCommunityUniqueId());
//			if (receiver.getUserUniqueId() == 0) {
//				Notifications.warning("该社区不存在影像化管理员，请联系系统管理员进行设置。");
//				return;
//			}
//			String matedata = "{\"UUID\":\""+view.uuid()+"\",\"VIN\":\""+imaging.getVin()+"\",\"STATE\":\""+imaging.getStatus()+"\",\"CHECKLEVEL\":\"\",\"POPUPAUTOMATICALLY\":\"TRUE\"}";
//			String subject = loggedinUser.getUserName()+"提交了影像化申请";
//			String content = "请补充车牌号"+imaging.getPlateNumber()+"的全部历史影像化记录。";
//			Message newMessage = messageSystem.createNewMessage(loggedinUser,subject, content, matedata);
//
//			Set<Name> names = new HashSet<Name>();
//			Name target = new Name(receiver.getUserUniqueId(), Name.USER, receiver.getProfile().getLastName()+receiver.getProfile().getFirstName(), receiver.getProfile().getPicture());
//			names.add(target);
//			messageSystem.sendMessageTo(newMessage.getMessageUniqueId(),names,DashboardViewType.IMAGING_MANAGER.getViewName());
//			//3.更新消息轮询的缓存
//			CacheManager.getInstance().getNotificationsCache().refresh(receiver.getUserUniqueId());
//		}
//
//		Callback callback = new Callback() {
//			@Override
//			public void onSuccessful() {
//				view.cleanStage();
//			}
//		};
//		MessageBox.showMessage("提示","缺少历史影像化记录，已提交申请。等待补充完整后再重新开始本次业务登记。",callback);
//	}
//
//
//    /**
//     * 注册检查
//     *
//     * @param vin
//     * @param business
//     * @return
//     */
//	private boolean registrationCheck(String vin, Business business) {
//		if(business.getName().contains("注册登记")) {
//			int count = ui.transactionService.getCount(vin, business.getCode());
//			return (count > 0);
//		}
//		return false;
//	}
//
//	/**
//     * 影像化检测
//     *
//     * @return
//     */
//    private boolean imaginationCheck() {
//    	//没有历史档暂停采集，不是注册，没有任何记录（走印象化检测流程）
//    	if(!selector.getSelectedItem().get().getName().contains("注册登记")) {
//    		List<Transaction> result = ui.transactionService.findForList(view.vin(),null,0);
//    		if(result.size() == 0) {
//    			return false;
//    		}
//    	}
//    	return true;
//    }
//
//    /**
//     * 处理中检查
//     *
//     * @param vin
//     * @return
//     */
//    private boolean beingProcessedCheck(String vin) {
//    	if(!selector.getSelectedItem().get().getName().contains("注册登记")) {
//    		List<Transaction> lst = ui.transactionService.findForList(vin,null,0);
//        	for(Transaction trans : lst) {
//
//        		if(trans.getStatus().equals("待上架")
//        				|| trans.getStatus().equals("待补充")
//        				|| trans.getStatus().equals("待复审")
//        				|| trans.getStatus().equals("待审核")
//        				|| trans.getStatus().equals("待质检")) {
//        			return false;
//        		}
//        	}
//    	}
//    	return true;
//    }
//
//	/**
//	 * 根据业务类型加载材料
//	 *
//	 * @param businessCode
//	 */
//	private void loadMaterials(String businessCode) {
////		// 加载文件上传表格
////		view.thumbnailGrid().removeAllRows();
////		List<DataDictionary> list = ui.businessService.getRequiredDataDictionaries(businessCode);
////		int i = 0;
////		for (DataDictionary dd : list) {
////			i++;
////			ThumbnailRow row = new ThumbnailRow(i+"."+dd.getItemName());
////			row.setDataDictionary(dd);
////			view.thumbnailGrid().addRow(row);
////			// 选中第一个
////			if (i == 1) {
////				row.selected();
////				UploadInDTO inDto = new UploadInDTO(view.loggedInUser().getUserUniqueId(), view.vin(), view.batch()+"", view.editableSite().getSiteUniqueId(),view.uuid(),dd.getCode());
////				UploadFileServlet.IN_DTOs.put(view.loggedInUser().getUserUniqueId(), inDto);
////			}
////		}
////		List<DataDictionary> list2 = ui.businessService.getOptionalDataDictionaries(businessCode);
////		for (DataDictionary dd : list2) {
////			i++;
////			ThumbnailRow row = new ThumbnailRow(i+"."+dd.getItemName()+" (可选项)", false);
////			row.setDataDictionary(dd);
////			view.thumbnailGrid().addRow(row);
////			// 选中第一个
////			if (i == 1) {
////				row.selected();
////				UploadInDTO inDto = new UploadInDTO(view.loggedInUser().getUserUniqueId(), view.vin(), view.batch()+"", view.editableSite().getSiteUniqueId(),view.uuid(),dd.getCode());
////				UploadFileServlet.IN_DTOs.put(view.loggedInUser().getUserUniqueId(), inDto);
////			}
////		}
////		view.thumbnailGrid().focus();
////		// 加载拍照影像
//////		view.capturePane().displayImage();
//	}
//
//	/**
//	 * 上传图像回显
//	 */
//	private void initPollListener() {
//
//		pollListener = new UIEvents.PollListener() {
//			/**
//			 *
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void poll(UIEvents.PollEvent event) {
////				// 定期获取上传文件表格焦点，从而可以按上下键控制
////				view.thumbnailGrid().focus();
////
////				List<UploadOutDTO> list = UploadFileServlet.OUT_DTOs.get(view.loggedInUser().getUserUniqueId());
////				if (list != null) {
////
////					Iterator<UploadOutDTO> iter = list.iterator();
////					while(iter.hasNext()) {
////						UploadOutDTO ufq = iter.next();
////						if (ufq.getRemovable() == 0) {
////
////							Thumbnail thumbnail = new Thumbnail(ufq.thumbnail);
////							// 右键菜单
////							com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(thumbnail, true);
////							menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
////								/**
////								 *
////								 */
////								private static final long serialVersionUID = 1L;
////
////								@Override
////								public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
////									ImageViewerWindow.open(view, ufq.getDocumentUniqueId());
////								}
////							});
////							menu.addSeparator();
////							menu.addItem("删除", new com.vaadin.contextmenu.Menu.Command() {
////								/**
////								 *
////								 */
////								private static final long serialVersionUID = 1L;
////
////								@Override
////								public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
////									//从文件系统删除
////									try {
////										fileSystem.deleteFile(view.editableSite(), ufq.getFileFullPath());
////									} catch (FileException e) {
////										e.printStackTrace();
////									}
////
////									//从数据库删除
////									ui.documentService.deleteById(ufq.getDocumentUniqueId(),view.vin());
////
////									//从UI删除
////									ThumbnailRow row = view.thumbnailGrid().mapRows.get(ufq.getDictionaryCode());
////									row.removeThumbnail(thumbnail);
////								}
////							});
//////							System.out.println("view="+ufq.getDictionaryCode()+"    thumbnail="+thumbnail);
////							view.thumbnailGrid().mapRows.get(ufq.getDictionaryCode()).addThumbnail(thumbnail);
////							ufq.setRemovable(1);
////						}
////						else {
////							// 已经回显过的从缓存清空
////							iter.remove();
////						}
////					}
////				}
//			}
//		};
//		// 注册监听
//		ui.addPollListener(pollListener);
//	}
//
//	/**
//	 *
//	 * @return
//	 */
//	public Business getValue() {
//		return selector.getValue();
//	}
//
//	/**
//	 *
//	 */
//	public void setEmpty() {
//		selector.setValue(null);
//	}
//
//	/**
//	 * 业务编码
//	 * @param businessCode
//	 */
//	public void populate(String businessCode) {
//		// 删除
//		selector.removeListener(SingleSelectionEvent.class, this);
//
//		for(Business business : data) {
//			if(business.getCode().equals(businessCode)) {
//				selector.setSelectedItem(business);
//				break;
//			}
//		}
//		// 重新加载Row
//		loadMaterials(businessCode);
//		/// 为每个Row添加缩略图
//		List<Document> documentList1 = ui.documentService.findAllDocument1(view.vin(), view.uuid());
//
//		for (Document doc : documentList1) {
//
////			Thumbnail thumbnail = new Thumbnail(new ByteArrayInputStream(doc.getThumbnail()));
////			// 右键菜单
////			com.vaadin.contextmenu.ContextMenu menu = new com.vaadin.contextmenu.ContextMenu(thumbnail, true);
////			menu.addItem("查看", new com.vaadin.contextmenu.Menu.Command() {
////				/**
////				 *
////				 */
////				private static final long serialVersionUID = 1L;
////
////				@Override
////				public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
////					ImageViewerWindow.open(view, doc.getDocumentUniqueId());
////				}
////			});
////			menu.addSeparator();
////			menu.addItem("删除", new com.vaadin.contextmenu.Menu.Command() {
////				/**
////				 *
////				 */
////				private static final long serialVersionUID = 1L;
////
////				@Override
////				public void menuSelected(com.vaadin.contextmenu.MenuItem selectedItem) {
////					//从文件系统删除
////					try {
////						fileSystem.deleteFile(view.editableSite(), doc.getFileFullPath());
////					} catch (FileException e) {
////						e.printStackTrace();
////					}
////
////					//从数据库删除
////					ui.documentService.deleteById(doc.getDocumentUniqueId(),view.vin());
////
////					//从UI删除
////					ThumbnailRow row = view.thumbnailGrid().mapRows.get(doc.getDictionarycode());
////					row.removeThumbnail(thumbnail);
////				}
////			});
////			ThumbnailRow row = view.thumbnailGrid().mapRows.get(doc.getDictionarycode());
////			row.addThumbnail(thumbnail);
//		}
//	}
//
//	private User loggedinUser;
//	private InputViewIF view;
//	private List<Business> data;
//	private ComboBox<Business> selector;
//	private DashboardUI ui = (DashboardUI) UI.getCurrent();
//	private VFSUtils fileSystem = new VFSUtils();
//	private UIEvents.PollListener pollListener;
//	private TB4MessagingSystem messageSystem = new TB4MessagingSystem();
}
