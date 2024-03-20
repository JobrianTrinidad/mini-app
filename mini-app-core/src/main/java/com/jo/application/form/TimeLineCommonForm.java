package com.jo.application.form;

import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.core.data.service.ZJTService;
import com.jo.application.core.form.TimeLineForm;
import com.jo.application.core.form.TimeLineViewParameter;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.button.Button;

public class TimeLineCommonForm extends TimeLineForm<ZJTService> {
    public TimeLineCommonForm(TimeLineViewParameter timeLineViewParameter,
                              ZJTService service) {
        super(timeLineViewParameter, service);
        addClassName("demo-app-form");
    }

    @Override
    public void onNewItem(GuiItem item) {
    }

    @Override
    public ZJTEntity onNewItem(ZJTEntity entity, int id) {

        return entity;
    }

    @Override
    public void setDataToGridRow(int rowKey, String data) {

    }

    @Override
    public int onUpdateItem(Object[] objects) {
        return 0;
    }

    @Override
    public int onDeleteItemChecked() {
        return 0;
    }

    @Override
    public void setContextMenu(AATContextMenu contextMenu) {

    }

    @Override
    public void setMessageStatus(String msg) {

    }

    @Override
    public void addCustomButton(Button button) {

    }
}