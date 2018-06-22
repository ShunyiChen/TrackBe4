package com.maxtree.automotive.dashboard.component;

import com.maxtree.automotive.dashboard.Callback;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;

public class SwitchButton extends HorizontalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default background with Gray color.
	 * 
	 * @param selected
	 */
	public SwitchButton(boolean selected) {
		this(selected, GRAY);
	}
	
	/**
	 * 
	 * @param selected
	 * @param caption
	 * @param description
	 * @param style
	 */
	public SwitchButton(boolean selected, String caption, String description, int style) {
		this(selected, style);
		img.setCaption(caption);
		img.setDescription(description);
	}
	
	/**
	 * 
	 * @param b
	 */
	public SwitchButton(boolean selected, int style) {
		if (style == GRAY) {
			imgOnPath = "img/components/switchbutton/switchbutton_on.png";
			imgOffPath= "img/components/switchbutton/switchbutton_off.png";
		} else {
			imgOnPath = "img/components/switchbutton/on.png";
			imgOffPath= "img/components/switchbutton/off.png";
		}
		
		this.selected = selected;
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("34px");
		this.setHeightUndefined();
		setSelected(selected);
		this.addStyleName("switchbutton");
		this.addComponent(img);
		this.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
		this.addLayoutClickListener(e -> {
			this.selected = !this.selected;
			if (this.selected) {
				img.setIcon(new ThemeResource(imgOnPath));
				if (onAction != null)
					onAction.onSuccessful();
			} else {
				if (offAction != null)
					offAction.onSuccessful();
				img.setIcon( new ThemeResource(imgOffPath));
			}
		});
	}
	
	public void setSelected(boolean b) {
		if (b) {
			img.setIcon(new ThemeResource(imgOnPath));
		} else {
			img.setIcon(new ThemeResource(imgOffPath));
		}
		this.selected = b;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	private String imgOnPath;
	private String imgOffPath;
	public static int WHITE = 1;
	public static int GRAY = 2;
	public Callback onAction;
	public Callback offAction;
	public boolean selected;
	protected Image img = new Image(null);
}
