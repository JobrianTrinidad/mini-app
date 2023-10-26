package com.aat.application.views;

import com.aat.application.data.entity.ZJTTimeLineItem;
import com.aat.application.data.entity.ZJTTimeLineNode;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.router.Route;

@Route(value = "group-timeline")
public class GroupTimeLineView extends TimeLineFormView<ZJTTimeLineItem> {
    public GroupTimeLineView(BaseEntityRepository<ZJTTimeLineItem> repository) {
        super(repository);
        super.setGroupName("group");
        super.setGroupClass(ZJTTimeLineNode.class);
    }
}

