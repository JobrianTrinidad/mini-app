package com.aat.application.views;

import com.aat.application.data.entity.ZJTItem;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;


@Route(value = "timeline/:category?/:subcategory?")
public class SpecificEntityTimelineView extends TimeLineFormView<ZJTItem> implements HasUrlParameter<String>, BeforeEnterObserver {
    private String name;

    public SpecificEntityTimelineView(BaseEntityRepository<ZJTItem> repository) {
        super(repository);
//        super.setGroupName("group");
//        super.setGroupClass(ZJTTimeLineNode.class);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter != null) {
            this.name = parameter;
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
    }
}