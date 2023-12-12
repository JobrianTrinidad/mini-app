package com.aat.application.views;

import com.aat.application.data.entity.ZJTTimeLineItem;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.router.Route;

@Route(value = "view/timeline")
public class SpecificEntityTimelineView extends TimeLineFormView<ZJTTimeLineItem> {
    public SpecificEntityTimelineView(BaseEntityRepository<ZJTTimeLineItem> repository) {
        super(repository);
//        super.setGroupName("group");
//        super.setGroupClass(ZJTTimeLineNode.class);
    }
}