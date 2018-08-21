package com.maxtree.automotive.dashboard.view.quality;

import java.io.InputStream;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.Notifications;
import com.maxtree.automotive.dashboard.domain.Document;
import com.maxtree.automotive.dashboard.domain.Site;
import com.maxtree.automotive.dashboard.exception.FileException;
import com.maxtree.automotive.dashboard.view.ImageViewIF;
import com.maxtree.trackbe4.filesystem.TB4FileSystem;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import elemental.json.JsonArray;

public class ImageStage extends VerticalLayout implements ClickListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param imgView
	 */
	public ImageStage(ImageViewIF imgView) {
		this.imgView = imgView;
		initComponents();
	}
	
	private void initComponents() {
		fittedSize.setIcon(VaadinIcons.EXPAND_FULL);
		fittedSize.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		fittedSize.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		fittedSize.setWidth("18px");
		fittedSize.setHeight("18px");
		fittedSize.setId("fittedSize");
		fittedSize.setDescription("适应窗口大小");
		fittedSize.addClickListener(this);
		
		actualSize.setIcon(VaadinIcons.BULLSEYE);
		actualSize.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		actualSize.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		actualSize.setWidth("18px");
		actualSize.setHeight("18px");
		actualSize.setId("actualSize");
		actualSize.setDescription("原图");
		actualSize.addClickListener(this);

		left.focus();
		left.setIcon(VaadinIcons.CHEVRON_CIRCLE_LEFT);
		left.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		left.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		left.setWidth("18px");
		left.setHeight("18px");
		left.setId("left");
		left.setDescription("上一张");
		left.addClickListener(this);
		
		right.setIcon(VaadinIcons.CHEVRON_CIRCLE_RIGHT);
		right.addStyleName(ValoTheme.BUTTON_BORDERLESS);
		right.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		right.setWidth("18px");
		right.setHeight("18px");
		right.setId("right");
		right.setDescription("下一张");
		right.addClickListener(this);
		
		HorizontalLayout header = new HorizontalLayout();
		header.setSpacing(false);
		header.setMargin(false);
		header.setHeightUndefined();
		header.setWidth("100%");
		header.addStyleName("imagestage-header");
		HorizontalLayout subheader = new HorizontalLayout();
		subheader.setSpacing(false);
		subheader.setMargin(false);
		subheader.setWidthUndefined();
		subheader.addComponents(fittedSize,Box.createHorizontalBox(5),actualSize);
		header.addComponents(subheader);
		
		HorizontalLayout footer = new HorizontalLayout();
		footer.setSpacing(false);
		footer.setMargin(false);
		footer.setHeight("22px");
		footer.setWidth("100%");
		footer.addStyleName("imagestage-header");
		HorizontalLayout subfooter = new HorizontalLayout();
		subfooter.setSpacing(false);
		subfooter.setMargin(false);
		subfooter.setWidthUndefined();
		subfooter.addComponents(left,Box.createHorizontalBox(10),right);
		footer.addComponents(subfooter);
		footer.setComponentAlignment(subfooter, Alignment.TOP_CENTER);
		
		scroll.setSizeFull();
		scroll.setStyleName("reminder-scrollpane");
		pictureFrame.setSizeFull();
		pictureFrame.setSpacing(false);
		pictureFrame.setMargin(false);
		scroll.setContent(pictureFrame);
		
		this.setSizeFull();
		this.setSpacing(false);
		this.setMargin(false);
		this.addComponents(header,scroll,footer);
		this.setExpandRatio(scroll,1);
		
//		 UI.getCurrent().addClickListener(e->{
//			 System.out.println(e.getClientX()+","+e.getClientY()+","+e.getRelativeX()+","+e.getRelativeY());
//		 });
	}
	
	/**
	 * 
	 * @param site
	 * @param doc
	 */
	public void display(Site site, Document doc) {
		document2Image(site, doc);
		fittedSize();
	}
	
	public void clean() {
		scroll.setContent(new Label(""));
	}
	
	/**
	 * 调整为适应窗体大小
	 */
	private void fittedSize() {
		picture.setWidth("100%");
		pictureFrame.setSizeFull();
		pictureFrame.removeAllComponents();
		pictureFrame.addComponent(picture);
		pictureFrame.setComponentAlignment(picture, Alignment.MIDDLE_CENTER);
		scroll.setContent(pictureFrame);
	}
	
	/**
	 * 调整为实际大小
	 */
	private void actualSize() {
		
		Callback2 callback2 = new Callback2() {
			@Override
			public void onSuccessful(Object... objects) {
				double fw = Double.parseDouble(objects[0].toString()) - 42;
				double fh = Double.parseDouble(objects[1].toString());
				
				picture.setSizeUndefined();
				
				if(pictureWidth >= fw) {
					pictureFrame.setWidthUndefined();
				}
				else {
					pictureFrame.setWidth("100%");
				}
				
				if(pictureHeight >= fh) {
					pictureFrame.setHeightUndefined();
				}
				else {
					pictureFrame.setHeight("100%");
				}
				
				pictureFrame.removeAllComponents();
				pictureFrame.addComponent(picture);
				pictureFrame.setComponentAlignment(picture, Alignment.MIDDLE_CENTER);
				scroll.setContent(pictureFrame);
			}
		};
		
		scroll.setId("MyPanel");
		JavaScript.getCurrent().addFunction("myGetPanelSize", new JavaScriptFunction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void call(JsonArray arguments) {
				double w = arguments.getNumber(0);
				double h = arguments.getNumber(1);
				callback2.onSuccessful(w,h);
			}
		});
		JavaScript.getCurrent().execute("myGetPanelSize(document.getElementById('" + scroll.getId() + "').clientWidth,document.getElementById('" + scroll.getId() + "').clientHeight);");
	}
	
	
	/**
	 * 
	 * @param site
	 * @param document
	 */
	private void document2Image(Site site, Document document) {
		try {
			FileObject fileObj = new TB4FileSystem().resolveFile(site, document.getFileFullPath());
			com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public InputStream getStream() {
					try {
						return fileObj.getContent().getInputStream();
					} catch (FileSystemException e) {
						Notifications.warning("读取文件失败。"+e.getMessage());
					}
					return null;
				}
			};
			StreamResource streamResource = new StreamResource(streamSource, fileObj.getName().getBaseName());
			streamResource.setCacheTime(0);
			picture = new Image(null, streamResource);
			
			// Create and attach extension
//            DragSourceExtension<Image> dragSource = new DragSourceExtension<>(picture);
//            dragSource.addDragStartListener( event -> {
//            		System.out.println(event.getComponent().getHeight());
//            });
			picture.setId("mypicture");
			JavaScript.getCurrent().addFunction("myGetPictureSize", new JavaScriptFunction() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public void call(JsonArray arguments) {
					pictureWidth = arguments.getNumber(0);
					pictureHeight = arguments.getNumber(1);
				}
			});
			JavaScript.getCurrent().execute("myGetPictureSize(document.getElementById('" + picture.getId() + "').clientWidth,document.getElementById('" + picture.getId() + "').clientHeight);");
			
		} catch (FileException e) {
			Notifications.warning(e.getMessage());
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if(event.getButton().getId().equals("actualSize")) {
			actualSize();
			
		} else if(event.getButton().getId().equals("fittedSize")) {
			fittedSize();
			
		} else if(event.getButton().getId().equals("left")) {
			imgView.previous();
			
		} else if(event.getButton().getId().equals("right")) {
			imgView.next();
			
		}
	}
	
	
	private Panel scroll = new Panel();
	private VerticalLayout pictureFrame = new VerticalLayout();
	private Image picture = new Image();
	private Button fittedSize = new Button();
	private Button actualSize = new Button();
	private Button left = new Button();
	private Button right = new Button();
	private ImageViewIF imgView;
	private double pictureWidth = 0;
	private double pictureHeight = 1;
}
