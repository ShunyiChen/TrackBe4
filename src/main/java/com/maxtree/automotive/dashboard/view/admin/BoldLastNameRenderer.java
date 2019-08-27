package com.maxtree.automotive.dashboard.view.admin;

import com.vaadin.ui.renderers.HtmlRenderer;
import elemental.json.JsonValue;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class BoldLastNameRenderer extends HtmlRenderer {

    @Override
    public JsonValue encode(String value) {
        int lastSpace = value.lastIndexOf(' ');
        if (lastSpace >= 0) {
            value = String
                    .format("<span style=\"pointer-events: none;\">%s <b>%s</b></span>",
                            Jsoup.clean(value.substring(0, lastSpace),
                                    Whitelist.basic()),
                            Jsoup.clean(value.substring(lastSpace),
                                    Whitelist.basic()));
        }
        return super.encode(value);
    }
}
