package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.TB4Application;
import com.maxtree.automotive.dashboard.jpa.entity.Camera;

import java.util.ArrayList;
import java.util.List;

public class CameraRow extends FlexTableRow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param rootView
	 */
	public CameraRow(AdminMainView rootView) {
		this.rootView = rootView;
		initComponents();
	}
	
	private void initComponents() {
		Iterable<Camera> iterable = TB4Application.getInstance().cameraRepository.findAll();
		List<Camera> list = new ArrayList<>();
		iterable.forEach(single ->{list.add(single);});
		rowItemWithOptions = new RowItemWithOptions("高拍仪设备", list);
		this.addComponents(rowItemWithOptions);
		rowItemWithOptions.arrowIcon.addClickListener(e -> {
			rootView.forward(new CameraView("管理高拍仪", rootView));
		});
		rowItemWithOptions.name.addListener(e -> {
			rootView.forward(new CameraView("管理高拍仪", rootView));
		});
	}

	@Override
	public String getSearchTags() {
		return "选择高拍仪,"+getTitle();
	}

	@Override
	public int getOrderID() {
		return 4;
	}
	
	@Override
	public String getTitle() {
		return "设备";
	}
	
	@Override
	public String getImageName() {
		return "device.png";
	}
	
	private RowItemWithOptions rowItemWithOptions;
	private AdminMainView rootView;
}
