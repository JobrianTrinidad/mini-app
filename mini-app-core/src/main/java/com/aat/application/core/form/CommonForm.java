package com.aat.application.core.form;

import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class CommonForm extends VerticalLayout {

    public TuiGrid grid;

    abstract public void onNewItem(GuiItem item);

    abstract public int onUpdateItem(Object[] objects) throws Exception;

    abstract public int onDeleteItemChecked() throws Exception;

    abstract public void setContextMenu(AATContextMenu contextMenu);

    abstract public void setMessageStatus(String msg);

    abstract public void addCustomButton(Button button);
}