package com.jo.application.form;

import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.core.data.service.ZJTService;
import com.jo.application.core.form.CustCompViewParameter;
import com.jo.application.core.form.CustomComponentForm;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;

public class CustomCompCommonForm<T extends ZJTEntity> extends CustomComponentForm<ZJTService> {

    public CustomCompCommonForm(CustCompViewParameter cameraParameter,
                                ZJTService service) {
        super(cameraParameter, service);
        addClassName("demo-app-form");
    }

    @Override
    public void onNewItem(GuiItem item) {

    }

    @Override
    public ZJTEntity onNewItem(ZJTEntity entity, int id) {
        return null;
    }

    @Override
    public void setDataToGridRow(int rowKey, String data) {

    }

    @Override
    public int onUpdateItem(Object[] objects) throws Exception {
        return 0;
    }

    @Override
    public int onDeleteItemChecked() throws Exception {
        return 0;
    }

    @Override
    public void setContextMenu(AATContextMenu contextMenu) {

    }

    @Override
    public void setMessageStatus(String msg) {

    }

    @Override
    public void addCustomComponent(int index, Component component) {

    }

    @Override
    public String getHamburgerText() {
        return null;
    }

    @Override
    public String getOriginViewText() {
        return null;
    }

    @Override
    public void onUpdateForm() throws Exception {

    }
}
