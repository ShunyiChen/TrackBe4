package com.maxtree.automotive.dashboard.view.admin;

import java.util.Iterator;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.domain.FrameNumber;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;

/**
 * 密集架封装类
 * 
 * @author Chen
 *
 */
public class Shelf {

	/**
	 * 
	 * @param shelfComponent
	 * @param callback
	 */
	public Shelf(ShelfComponent shelfComponent, Callback2 callback) {
		this.shelfComponent = shelfComponent;
		this.callback = callback;
		Resource icon = new ThemeResource("img/adminmenu/bookshelf_46.08px_1164538_easyicon.net.png");
		this.icon = new Image("密集架:"+shelfComponent.getFrame().getFrameCode()+"("+shelfComponent.getFrame().getMaxColumn()+"列 x "+shelfComponent.getFrame().getMaxRow()+"行)", icon);
		this.icon.addStyleName("Thumbnail");
		this.icon.addClickListener(e->{
			view();
			
			restoreCSS();
			
			this.icon.addStyleName("Thumbnail-selected");
		});
		
	}
	
	private void restoreCSS() {
		GridLayout gridLayout = (GridLayout) this.icon.getParent();
		Iterator<Component> iter = gridLayout.iterator();
		while(iter.hasNext()) {
			Component comp = iter.next();
			if(comp instanceof Image) {
				comp.removeStyleName("Thumbnail-selected");
				comp.addStyleName("Thumbnail");
			}
		}
		
	}
	
	/**
	 * 显示密集架结构图
	 */
	public void view() {
		this.getIcon().addStyleName("Thumbnail-selected");
		callback.onSuccessful(this);
	}
	
	/**
	 * 修改密集架图标
	 * 
	 * @param fn
	 */
	public void changeIconTitle(FrameNumber fn) {
		this.icon.setCaption("密集架:"+fn.getFrameCode()+"("+fn.getMaxColumn()+"列 x "+fn.getMaxRow()+"行)");
	}
	
	
	public Image getIcon() {
		return icon;
	}
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	public ShelfComponent getShelfComponent() {
		return shelfComponent;
	}
	public void setShelfComponent(ShelfComponent shelfComponent) {
		this.shelfComponent = shelfComponent;
	}

	private Image icon;
	private ShelfComponent shelfComponent;
	private Callback2 callback;
}
