package com.maxtree.automotive.dashboard.view.check;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.component.Box;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ToolbarWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ToolbarWindow() {
		initComponents();
	}
	
	private void initComponents() {
		this.setClosable(false);
		this.setResizable(false);
		this.setCaption("工具栏");
		this.setWidthUndefined();
		this.setHeight("680px");
		
		VerticalLayout vl = new VerticalLayout();
		vl.setSpacing(false);
		vl.setMargin(false);
		vl.setWidth("100%");
		vl.setHeightUndefined();
		Button undo = new Button();
		Button redo = new Button();
		Button sharpen = new Button();
		Button magic = new Button();
		// Shadow buttons
		Button shadowUp = new Button();
		Button shadowDown = new Button();
		Button shadowLeft = new Button();
		Button shadowRight = new Button();
		
		Button original = new Button();
		
		undo.setIcon(new ThemeResource("img/imgcomparator/reply-all.png"));
		undo.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		undo.setDescription("撤销");
		redo.setIcon(new ThemeResource("img/imgcomparator/reply.png"));
		redo.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		redo.setDescription("恢复");
		sharpen.setIcon(new ThemeResource("img/imgcomparator/sharpen.png"));
		sharpen.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		sharpen.setDescription("锐化");
		magic.setIcon(new ThemeResource("img/imgcomparator/magic.png"));
		magic.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		magic.setDescription("查找边缘");
		
		shadowUp.setIcon(VaadinIcons.PADDING_TOP);
		shadowUp.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		shadowUp.setDescription("上阴影");
		shadowDown.setIcon(VaadinIcons.PADDING_BOTTOM);
		shadowDown.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		shadowDown.setDescription("下阴影");
		shadowLeft.setIcon(VaadinIcons.PADDING_LEFT);
		shadowLeft.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		shadowLeft.setDescription("左阴影");
		shadowRight.setIcon(VaadinIcons.PADDING_RIGHT);
		shadowRight.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		shadowRight.setDescription("右阴影");
		
		original.setIcon(VaadinIcons.BULLSEYE);
		original.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		original.setDescription("原始图像");
		
		scaleSlider = new SliderWithTextField("缩小放大:", 0d, 200d, 100d);
		scaleSlider.setValueChangedCallback(new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				if (editWindow != null) {
					double val = (double) objects[0];
					editWindow.scale((val * 0.01), (val * 0.01));
					editWindow.getParameters().setScale(val);
				}
			}
		});
		
		rotateSlider = new SliderWithTextField("旋转:", -180d, 180d, 0d);
		rotateSlider.setValueChangedCallback(new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				if (editWindow != null) {
					double val = (double) objects[0];
					editWindow.rotate(Math.toRadians(val));
					editWindow.getParameters().setRotate(val);
				}
			}
		});
		
		transparencySlider = new SliderWithTextField("透明度:", 10d, 100d, 100d);
		transparencySlider.setValueChangedCallback(new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				if (editWindow != null) {
					editWindow.adjustTransparency((double)objects[0]/ 100d);
					editWindow.getParameters().setTransparency((double) objects[0]);
				}
			}
		});
		
		
		brightnessSlider = new SliderWithTextField("亮度:", 0d, 255d, 130d);
		brightnessSlider.setValueChangedCallback(new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				if (editWindow != null) {
					double val = (double) objects[0];
					editWindow.adjustBrightness(val);
					editWindow.getParameters().setBrightness(val);
				}
			}
		});
		
		contrastSlider = new SliderWithTextField("对比度:", 0d, 255d, 130d);
		contrastSlider.setValueChangedCallback(new Callback2() {

			@Override
			public void onSuccessful(Object... objects) {
				if (editWindow != null) {
					double val = (double) objects[0];
					editWindow.adjustContrast(val);
					editWindow.getParameters().setContrast(val);
				}
			}
		});
		
		HorizontalLayout row1 = createRow(original, undo, redo, sharpen, magic);
		HorizontalLayout row2 = createRow(shadowUp, shadowDown, shadowLeft, shadowRight);
		vl.addComponents(row1, row2, scaleSlider, rotateSlider, transparencySlider, brightnessSlider, contrastSlider);
		vl.setComponentAlignment(row1, Alignment.MIDDLE_LEFT);
		vl.setComponentAlignment(row2, Alignment.MIDDLE_LEFT);
		vl.setComponentAlignment(scaleSlider, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(rotateSlider, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(transparencySlider, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(brightnessSlider, Alignment.MIDDLE_CENTER);
		vl.setComponentAlignment(contrastSlider, Alignment.MIDDLE_CENTER);
		
		setContent(vl);
		addCloseListener(e -> {
			tx = getPositionX();
			ty = getPositionY();
			UI.getCurrent().removeWindow(this);
		});
		// Undo
		undo.addClickListener(e -> {
			if (editWindow != null) {
				editWindow.undo();
			}
		});
		// redo
		redo.addClickListener(e -> {
			if (editWindow != null) {
				editWindow.redo();
			}
		});
		// 锐化
		sharpen.addClickListener(e -> {
			if (editWindow != null) {
				editWindow.sharpen();
			}
		});
		// 寻找边缘
		magic.addClickListener(e -> {
			if (editWindow != null) {
				editWindow.findEdges();
			}
		});
		shadowUp.addClickListener(e ->{
			if (editWindow != null) {
				editWindow.shadows("north");
			}
		});
		shadowDown.addClickListener(e ->{
			if (editWindow != null) {
				editWindow.shadows("south");
			}
		});
		shadowLeft.addClickListener(e ->{
			if (editWindow != null) {
				editWindow.shadows("west");
			}
		});
		shadowRight.addClickListener(e ->{
			if (editWindow != null) {
				editWindow.shadows("east");
			}
		});
		original.addClickListener(e ->{
			if (editWindow != null) {
				editWindow.getParameters().reset();
				setEditingWindow(editWindow);
				editWindow.original();
			}
		});
	}
	
	public void setEditingWindow(ImageWindow editWindow) {
		this.editWindow = editWindow;
		
		scaleSlider.update(editWindow.getParameters().getScale());
		rotateSlider.update(editWindow.getParameters().getRotate());
		transparencySlider.update(editWindow.getParameters().getTransparency());
		brightnessSlider.update(editWindow.getParameters().getBrightness());
		contrastSlider.update(editWindow.getParameters().getContrast());
	}
	
	/**
	 * 
	 * @param btns
	 * @return
	 */
	private HorizontalLayout createRow(Button... btns) {
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(false);
		hlayout.setMargin(false);
		hlayout.setWidthUndefined();
		hlayout.setHeight("40px");
		for (int i = 0; i < btns.length; i++) {
			hlayout.addComponents(Box.createHorizontalBox(5), btns[i]);
			hlayout.setComponentAlignment(btns[i], Alignment.MIDDLE_CENTER);
		}
		return hlayout;
	} 
	
	public void center2(boolean centered) {
		UI.getCurrent().addWindow(this);
		if (centered) {
			this.center();
		} else {
			this.setPosition(tx, ty);
		}
	}
	
	private int tx = 0;
	private int ty = 0;
	private ImageWindow editWindow;
	private SliderWithTextField scaleSlider;
	private SliderWithTextField rotateSlider;
	private SliderWithTextField transparencySlider;
	private SliderWithTextField brightnessSlider;
	private SliderWithTextField contrastSlider;
}
