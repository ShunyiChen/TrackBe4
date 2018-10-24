package com.maxtree.automotive.dashboard.view.admin;

import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 * 密集架缩略图形式
 * 
 * @author Chen
 *
 */
public class ShelfComponent extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param frameNumber
	 */
	public ShelfComponent(FrameNumber frameNumber) {
		this.frameNumber = frameNumber;
		this.setSizeUndefined();
		this.setMargin(false);
		this.setSpacing(false);
		Resource icon = new ThemeResource("img/adminmenu/bookshelf_46.08px_1164538_easyicon.net.png");
		image = new Image("密集架:"+frameNumber.getFrameCode()+"("+frameNumber.getMaxColumn()+"列 x "+frameNumber.getMaxRow()+"行)", icon);// x "+frameNumber.getMaxfolder()+"个文件夹
		image.addStyleName("ShelfComponent");
		image.setDescription(frameNumber.getMaxColumn()+"列 x "+frameNumber.getMaxRow()+"行 x"+ frameNumber.getMaxfolder()+"个文件夹");
		this.addComponent(image);
		this.setComponentAlignment(image, Alignment.TOP_CENTER);
	}
	
	public FrameNumber getFrameNumber() {
		return frameNumber;
	}

	public void setFrameNumber(FrameNumber frameNumber) {
		this.frameNumber = frameNumber;
	}

	/**
	 * 修改密集架图标
	 * 
	 * @param fn
	 */
	public void updateTitle(FrameNumber fn) {
		image.setCaption("密集架:"+fn.getFrameCode()+"("+fn.getMaxColumn()+"列 x "+fn.getMaxRow()+"行)");///x "+fn.getMaxfolder()+"个文件夹
		image.setDescription(fn.getMaxColumn()+"列 x "+fn.getMaxRow()+"行 x"+ fn.getMaxfolder()+"个文件夹");
	}
	
	public void select() {
		selected = true;
		image.addStyleName("ShelfComponent-selected");
	}
	
	public void deselect() {
		selected = false;
		image.removeStyleName("ShelfComponent-selected");
		image.addStyleName("Shelf");
	}
	
	public boolean isSelected() {
		return selected;
	}

	private Image image;
	private boolean selected;
	private FrameNumber frameNumber;
	
}
