package com.maxtree.automotive.dashboard.view.check;

import com.maxtree.automotive.dashboard.Callback2;
import com.maxtree.automotive.dashboard.component.Box;
import com.maxtree.automotive.dashboard.component.DoubleField;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.ShortcutListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

public class SliderWithTextField extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SliderWithTextField(String caption, double min, double max, double defaultVal) {
		this.setSpacing(false);
		this.setMargin(false);
		this.setWidth("100%");
		this.setHeightUndefined();
		slider = new Slider(min, max, 2);
		slider.setCaption(caption);
		slider.setValue(defaultVal);
		slider.setWidth("100%");
		
		note.setValue("  "+min+"~"+max);
		numField.setWidth("80px");
		numField.setHeight("23px");
		numField.setValue(defaultVal+"");
		
		HorizontalLayout hlayout = new HorizontalLayout();
		hlayout.setSpacing(false);
		hlayout.setMargin(false); 
		hlayout.setWidthUndefined();
		
		hlayout.addComponents(numField, note);
		hlayout.setComponentAlignment(numField, Alignment.MIDDLE_CENTER);
		hlayout.setComponentAlignment(note, Alignment.MIDDLE_CENTER);
		this.addComponents(slider, hlayout, Box.createVerticalBox(1));
		
		this.setComponentAlignment(slider, Alignment.TOP_CENTER);
		this.setComponentAlignment(hlayout, Alignment.TOP_CENTER);
		
		slider.addValueChangeListener(event -> {
			 if (valueChangedCallback != null)
				 valueChangedCallback.onSuccessful(event.getValue().doubleValue());

			 if (!changed) {
				 numField.setValue(event.getValue().doubleValue()+"");
			 }
			 changed = false;
		});
		
		numField.addValueChangeListener(e -> {
			try {
				double val = Double.parseDouble(numField.getValue());
				if (val >= min && val <= max) {
					changed = true;
					slider.setValue(val);
				}
			} catch (NumberFormatException e2) {
				Notification.show("请输入一个浮点数");
			}
		});
 
		// 键盘左方向快捷键
		ShortcutListener leftListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_LEFT, null) {
	        @Override
	        public void handleAction(Object sender, Object target) {
	        	double val = Double.parseDouble(numField.getValue());
	        	if (val > min)
	        		numField.setValue((val-1) + "");
	        }
	    };
	    // 键盘右方向快捷键
	    ShortcutListener rightListener = new ShortcutListener(null, com.vaadin.event.ShortcutAction.KeyCode.ARROW_RIGHT, null) {
	        @Override
	        public void handleAction(Object sender, Object target) {
	        	double val = Double.parseDouble(numField.getValue());
	        	if (val < max)
	        		numField.setValue((val+1) + "");
	        }
	    };
	    
	    r1 = numField.addShortcutListener(leftListener);
	    r2 = numField.addShortcutListener(rightListener);
		numField.addBlurListener(new BlurListener() {
	        @Override
	        public void blur(BlurEvent event) {
	        	r1.remove();
	        	r2.remove();
	        }
	    });
		numField.addFocusListener(e -> {
			r1 = numField.addShortcutListener(leftListener);
		    r2 = numField.addShortcutListener(rightListener);
		});
	}
	
	public void update(double val) {
		slider.setValue(val);
	}
	
	public void setValueChangedCallback(Callback2 valueChangedCallback) {
		this.valueChangedCallback = valueChangedCallback;
	}

	private Callback2 valueChangedCallback;
	private Slider slider;
	private DoubleField numField = new DoubleField();
	private Label note = new Label();
	private Registration r1;
	private Registration r2;
	private boolean changed = false;
}
