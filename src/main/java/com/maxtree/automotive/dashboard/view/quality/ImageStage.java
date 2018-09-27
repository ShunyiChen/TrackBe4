package com.maxtree.automotive.dashboard.view.quality;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

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

public class ImageStage extends VerticalLayout implements ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * 
	 * @param imgView
	 */
	public ImageStage(ImageViewIF imgView) {
		this(imgView,false);
	}
	
	/**
	 * 
	 * @param imgView
	 * @param isHeaderHidden
	 */
	public ImageStage(ImageViewIF imgView, boolean isHeaderHidden) {
		this.imgView = imgView;
		this.isHeaderHidden = isHeaderHidden;
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
		if(isHeaderHidden) {
			this.addComponents(scroll,footer);
		}
		else {
			this.addComponents(header,scroll,footer);
		}
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
	public void fittedSize() {
		
		Callback2 callback2 = new Callback2() {
			@Override
			public void onSuccessful(Object... objects) {
				double conWidth = Double.parseDouble(objects[0].toString()) - 42;
				double conHeight = Double.parseDouble(objects[1].toString());
		        //默认的边框间距
		        final double SMALL_SCALE = 0.95;
		 
		        double imgWidth = pictureActualWidth;
		        double imgHeight = pictureActualHeight;
		        //原图的宽长比
		        double imgRatio = imgWidth/imgHeight;
		        //最终输出宽和长
		        double reImgWidth = 0;
		        double reImgHeight = 0;
		 
		 
		        //若原图的宽小于控件宽
		        if(imgWidth < conWidth){
		            if(imgHeight < conHeight){
		                reImgWidth = conWidth*SMALL_SCALE;
		                reImgHeight = reImgWidth/imgRatio;
		            }
		            else {
		                reImgHeight = conHeight*SMALL_SCALE;
		                reImgWidth = reImgHeight*imgRatio;
		            }
		        }
		        //若原图的宽大于控件宽
		        else {
		            if(imgHeight < conHeight){
		                reImgWidth = conWidth*SMALL_SCALE;
		                reImgHeight = reImgWidth/imgRatio;
		            }
		            //若原图的长宽同时大于控件的长宽，最复杂的情况
		            else {
		                //控件的长比宽大
		                double conRatio = conWidth/conHeight;
		 
		                if (imgRatio < conRatio){
		                    reImgHeight = conHeight*SMALL_SCALE;
		                    reImgWidth = reImgHeight*imgRatio;
		                }
		                else {
		                    reImgWidth = conWidth*SMALL_SCALE;
		                    reImgHeight = reImgWidth/imgRatio;
		                }
		            }
		        }
			 
		        picture.setWidth((int)reImgWidth+"px");
		        picture.setHeight((int)reImgHeight+"px");
				pictureFrame.setSizeFull();
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
	 * 调整为实际大小
	 */
	public void actualSize() {
		
		Callback2 callback2 = new Callback2() {
			@Override
			public void onSuccessful(Object... objects) {
				double fw = Double.parseDouble(objects[0].toString()) - 42;
				double fh = Double.parseDouble(objects[1].toString());
				
				picture.setSizeUndefined();
				
				if(pictureActualWidth >= fw) {
					pictureFrame.setWidthUndefined();
				}
				else {
					pictureFrame.setWidth("100%");
				}
				
				if(pictureActualHeight >= fh) {
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
			
			if(pictureSizes.get(document.getDocumentUniqueId()) == null) {
				// 获取图像实际大小
				InputStream is = fileObj.getContent().getInputStream();
			    java.awt.Image img = ImageIO.read(is);
			    pictureSizes.put(document.getDocumentUniqueId(), new int[] {img.getWidth(null), img.getHeight(null)});
			}
			
			int[] size = pictureSizes.get(document.getDocumentUniqueId());
		    pictureActualWidth = size[0];
		    pictureActualHeight = size[1];
 
		} catch (FileException e) {
			Notifications.warning(e.getMessage());
		} catch (FileSystemException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	private boolean isHeaderHidden;
	private Panel scroll = new Panel();
	private VerticalLayout pictureFrame = new VerticalLayout();
	private Image picture = new Image();
	private Button fittedSize = new Button();
	private Button actualSize = new Button();
	private Button left = new Button();
	private Button right = new Button();
	private ImageViewIF imgView;
	private double pictureActualWidth = 0;
	private double pictureActualHeight = 1;
	private Map<Integer, int[]> pictureSizes = new HashMap<Integer, int[]>();//key-documentId, value-picture width and height
}
