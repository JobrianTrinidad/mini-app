package com.aat.application.form;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.data.service.ZJTService;
import com.aat.application.core.form.CustCompViewParameter;
import com.aat.application.core.form.CustomComponentForm;
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
    public void addCustomButton(Button button) {

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
