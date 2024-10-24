package com.jo.application.data.event;

import com.jo.application.data.entity.ZJTItem;
import com.jo.application.events.ZJTContextFormEventHandler;

import java.util.Map;
import java.util.Optional;

public class CustomContextFormEventHandler extends ZJTContextFormEventHandler {

    public CustomContextFormEventHandler(String htmlContent, String htmlStyle, boolean isSubmitForm) {
        super(htmlContent, htmlStyle, isSubmitForm);
    }

    @Override
    public void onInputChange(String key, String value, int itemId) {
        System.out.println("InputOnChange key = " + key + " value = " + value);
    }

    @Override
    public void onClickButton(String key, int itemId) {
        System.out.println("ButtonClick key = " + key);
    }

    @Override
    public String parsedContent(String content, int itemId) {
        Optional<ZJTItem> item = zjtItems.stream().filter(it -> it.getId() == itemId).findFirst();
        return com.vaadin.componentfactory.timeline.util.TimelineUtil.parseString(content, item.get(), null);
    }

    @Override
    public void onSubmitForm(Map<String, Object> formDataMap, int itemId) {
        formDataMap.forEach((k, v) -> System.out.println("FormSubmit key = " + k + " value = " + v));
    }
}
