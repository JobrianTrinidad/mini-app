package com.aat.application.views;

import com.aat.application.data.entity.ZJTItem;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.repository.TimelineRepository;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.*;


@Route(value = "timeline/:category?/:subcategory?")
public class SpecificEntityTimelineView extends TimeLineFormView implements HasUrlParameter<String>, BeforeEnterObserver {
    private String name;

    public SpecificEntityTimelineView(TimelineRepository repository) {
        super(repository);
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