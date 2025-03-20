package com.jo.application.data.event;

import com.vaadin.componentfactory.timeline.context.ContextMenuItem;
import com.vaadin.componentfactory.timeline.context.ItemContextMenuEventHandler;
import com.vaadin.componentfactory.timeline.model.ItemGroup;
import com.vaadin.flow.component.UI;

import java.util.List;
import java.util.Optional;

// This for test purpose only, to check the working how to add new group context option.
public class CustomItemContextMenuEventHandler extends ItemContextMenuEventHandler {


    public CustomItemContextMenuEventHandler() {
        super();
    }

    @Override
    protected void loadGroupItemsContextMenu(int left, int top, int selectedGroupID) {
        super.loadGroupItemsContextMenu(left, top, selectedGroupID);
            ContextMenuItem testSelectItem = new ContextMenuItem("las la-check-square", "Test Select Item", event -> {
                contextMenu.hideContextMenu();
            });
            contextMenu.add(testSelectItem);
            contextMenu.showContextMenu(left, top);
    }

    @Override
    protected void loadItemsContextMenu(int left, int top, int selectedItemID) {
        super.loadItemsContextMenu(left, top, selectedItemID);
    }

    @Override
    public void loadContextMenu(int left, int top, int selectedCompID, String componentType) {
        super.loadContextMenu(left, top, selectedCompID, componentType);
    }
}
