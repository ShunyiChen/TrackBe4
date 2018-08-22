package com.maxtree.automotive.dashboard.view.check;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.maxtree.imageprocessor.services.Command;
import com.maxtree.imageprocessor.services.ImageProcessorAPI;
import com.maxtree.imageprocessor.services.ImageProcessorException;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.Page;
import com.vaadin.server.Page.Styles;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author chens
 *
 */
public class ImageWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param alias
	 * @param file
	 * @param opacity
	 */
	public ImageWindow(String alias, FileObject file, float opacity) {
		super(alias);
		this.file = file;
		this.addStyleName("v-window-translucent-" + this.hashCode());
		this.addStyleName("foo");
		this.setResizable(true);
		this.setWidth("800px");
		this.setHeight("600px");
		adjustTransparency(opacity);
		image = new Image(null, getStreamByFileObject(file));
		vl.addStyleName("v-content-translucent-" + this.hashCode());
		vl.setSpacing(false);
		vl.setMargin(false);
		vl.setWidthUndefined();
		vl.setHeightUndefined();
		vl.addComponents(image);
		vl.setComponentAlignment(image, Alignment.TOP_CENTER);

		this.setContent(vl);

		UI.getCurrent().addWindow(this);

		this.focus();

		this.addFocusListener(e -> {
			this.bringToFront();
		});
		
		ShortcutListener upListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_UP,
				null) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				if (getPositionY() > 0)
					setPositionY(getPositionY() - 1);
			}
		};
		ShortcutListener downListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_DOWN,
				null) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				setPositionY(getPositionY() + 1);
			}
		};
		ShortcutListener leftListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_LEFT,
				null) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				if (getPositionX() > 0)
					setPositionX(getPositionX() - 1);
			}
		};
		ShortcutListener rightListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_RIGHT,
				null) {
			/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			@Override
			public void handleAction(Object sender, Object target) {
				setPositionX(getPositionX() + 1);
			}
		};
		this.addShortcutListener(upListener);
		this.addShortcutListener(downListener);
		this.addShortcutListener(rightListener);
		this.addShortcutListener(leftListener);
	}
	
	public void original() {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				
 				lstCommands.clear();
 				removedCommands.clear();
				try {
					return file.getContent().getInputStream();
				} catch (FileSystemException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	public void fit() {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
				try {
					return file.getContent().getInputStream();
				} catch (FileSystemException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	/**
	 * Scale image
	 * 
	 * @param xscale
	 * @param yscale
	 */
	public void scale(double xscale, double yscale) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				removeCommand("scale");
 				Command command = new Command("scale", xscale, yscale);
 				lstCommands.add(command);
 				try {
					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	/**
	 * Rotate image
	 * 
	 * @param radians
	 */
	public void rotate(double radians) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				removeCommand("rotate");
 				Command command = new Command("rotate", radians);
 				lstCommands.add(command);
 				try {
					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	/**
	 * Sharpen
	 */
	public void sharpen() {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				Command command = new Command("sharpen");
 				lstCommands.add(command);
 				try {
 					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	/**
	 * Find edges
	 */
	public void findEdges() {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				Command command = new Command("findEdges");
 				lstCommands.add(command);
 				try {
 					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	/**
	 * Adjust transparency
	 * 
	 * @param d
	 */
	public void adjustTransparency(double d) {
		Styles styles = Page.getCurrent().getStyles();
    	String css1 = ".v-content-translucent-"+this.hashCode()+" { opacity:" + (d) + " !important; }";
		String css2 = ".v-window-translucent-"+this.hashCode()+" { background-color:rgba(255,255,255,0.0) !important; }";
		
		styles.add(css1);
		styles.add(css2);
	}
	
	/**
	 * Adjust contrast
	 * 
	 * @param cvalue
	 */
	public void adjustContrast(double cvalue) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				
 				removeCommand("adjustContrast");
 				Command command = new Command("adjustContrast", cvalue);
 				lstCommands.add(command);
 				try {
 					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
 				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	/**
	 * Adjust brightness
	 * 
	 * @param cvalue
	 */
	public void adjustBrightness(double cvalue) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				
 				removeCommand("adjustBrightness");
 				Command command = new Command("adjustBrightness", cvalue);
 				lstCommands.add(command);
 				try {
 					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
 				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	public void shadows(String arg) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				
 				Command command = new Command("shadows", arg);
 				lstCommands.add(command);
 				try {
 					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
 				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	public void undo() {
		if (lstCommands.size() == 0) {
			return;
		} else {
			Command removeCommand = lstCommands.get(lstCommands.size() - 1);
			lstCommands.remove(removeCommand);
			removedCommands.add(removeCommand);
		}
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				try {
 					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
 				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	public void redo() {
		if (removedCommands.size() == 0) {
			return;
		} else {
			Command resumeCommand = removedCommands.get(removedCommands.size() - 1);
			removedCommands.remove(resumeCommand);
			lstCommands.add(resumeCommand);
		}
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				try {
 					return api.executeCommands(file.getContent().getInputStream(), lstCommands);
 				} catch (FileSystemException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ImageProcessorException e) {
					e.printStackTrace();
				}
				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		reloadImage(streamResource);
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public StreamResource getStreamByFileObject(FileObject file) {
		com.vaadin.server.StreamResource.StreamSource streamSource = new com.vaadin.server.StreamResource.StreamSource() {
 			@Override
 			public InputStream getStream() {
 				try {
 					return file.getContent().getInputStream();
 				} catch (FileSystemException e) {
 					e.printStackTrace();
 				}
 				return null;
 			}
 		}; 
 		StreamResource streamResource = new StreamResource(streamSource, file.getName().getBaseName());
 		streamResource.setCacheTime(0);
 		return streamResource;
	}
	
	/**
	 * 
	 * @param streamResource
	 */
	private void reloadImage(StreamResource streamResource) {
		vl.removeComponent(image);
 		image = new Image(null, streamResource);
 		vl.addComponent(image);
 		vl.setComponentAlignment(image, Alignment.TOP_CENTER);
	}
	
	/**
	 * 
	 * @param name
	 */
	public void removeCommand(String name) {
		Iterator<Command> iter = lstCommands.iterator();
		while (iter.hasNext()) {
			Command c = iter.next();
			if (c.option.equals(name)) {
				iter.remove();
			}
		}
	}
	
	public ImageParameter getParameters() {
		return parameters;
	}

	private VerticalLayout vl = new VerticalLayout();
	private FileObject file;
	private Image image;
	private ImageProcessorAPI api = new ImageProcessorAPI();
	private List<Command> lstCommands = new ArrayList<Command>();
	private List<Command> removedCommands = new ArrayList<Command>();
	private ImageParameter parameters = new ImageParameter();
}
