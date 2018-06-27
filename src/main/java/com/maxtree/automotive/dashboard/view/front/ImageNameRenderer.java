package com.maxtree.automotive.dashboard.view.front;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.vaadin.ui.renderers.HtmlRenderer;

import elemental.json.JsonValue;

public class ImageNameRenderer extends HtmlRenderer {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public JsonValue encode(String value) {
        int lastSpace = value.lastIndexOf(' ');
        if (lastSpace >= 0) {
//            value = String
//                    .format("<span style=\"pointer-events: none;\">%s <b>%s</b></span>",
//                            Jsoup.clean(value.substring(0, lastSpace),
//                                    Whitelist.basic()),
//                            Jsoup.clean(value.substring(lastSpace),
//                                    Whitelist.basic()));
            
            value = String.format("<img src=\"%s\" height=\"30\" width=\"30\">&nbsp;<label>%s</label>",
            		Jsoup.clean(value.substring(0, lastSpace), Whitelist.basic()),
            		Jsoup.clean(value.substring(lastSpace), Whitelist.basic()));
        }
        return super.encode(value);
    }

}
