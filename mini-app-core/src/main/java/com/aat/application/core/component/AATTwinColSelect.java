package com.aat.application.core.component;

import com.aat.application.util.GlobalData;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.tatu.TwinColSelect;

import java.lang.reflect.Field;
import java.util.*;

public class AATTwinColSelect extends Div {

    private final HorizontalLayout parentLayout;
    private TwinColSelect<String> twinColSelect;
    private List<Component> childrenDisplayed;
    private List<String> totalItems;
    private Set<String> orderedItems;
    private int selectedItemCount = 0;
    private boolean bDrop = false;

    public AATTwinColSelect() {
        HorizontalLayout mainLayout = new HorizontalLayout();
        twinColSelect = new TwinColSelect<>();
        twinColSelect.addValueChangeListener(event -> addEventInItems(twinColSelect));

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.add(twinColSelect, moveToolbar());

// Now, parentLayout can be initialized as mainLayout
        parentLayout = mainLayout;
        DragSource<Component> dragSourceWrapper = DragSource.create(parentLayout);
        dragSourceWrapper.addDragStartListener(this::onDragStart);
        dragSourceWrapper.addDragEndListener(this::onDragEnd);

        add(parentLayout);
        addClickListener(this::handleClickEvent);
        addDoubleClickListener(this::handleDbClickEvent);
    }

    private VerticalLayout moveToolbar() {
        VerticalLayout updownLayout = new VerticalLayout();
        updownLayout.setSizeFull();
        Button upBtn = new Button(VaadinIcon.ARROW_UP.create());
        Button downBtn = new Button(VaadinIcon.ARROW_DOWN.create());
        updownLayout.add(upBtn, downBtn);
        return updownLayout;
    }

    public Set getValue() {
        return Collections.singleton(twinColSelect.getValue());
    }

    public Set<String> getSelectedItems() {
        return twinColSelect.getSelectedItems();
    }

    public void setItems(List<String> items) {
        totalItems = items;
        twinColSelect.setItems(items);
    }

    public void select(Iterable<String> items) {
        if (items != null) {
            twinColSelect.select(items);
            selectedItemCount = twinColSelect.getSelectedItems().size();
            addEventInItems(twinColSelect);
        }
    }

    private void addEventInItems(TwinColSelect<String> twinColSelect) {
        List<Component> liveParent = GlobalData.findComponentsWithAttribute(twinColSelect, "aria-live");
        childrenDisplayed = GlobalData.findComponentsWithAttribute(liveParent.get(0), "aria-selected");

        for (Component child : childrenDisplayed) {
            DropTarget<Component> dropTargetWrapper = DropTarget.create(child);
            dropTargetWrapper.addDropListener(this::onDrop);
        }
    }

    public void setLabel(String label) {
        twinColSelect.setLabel(label);
    }

    private void handleClickEvent(ClickEvent<Div> event) {
        Notification.show("Component clicked!");
    }

    private void handleDbClickEvent(ClickEvent<Div> event) {
        if(selectedItemCount < twinColSelect.getSelectedItems().size()){
            addEventInItems(twinColSelect);
        }
        selectedItemCount = twinColSelect.getSelectedItems().size();
        Notification.show("Component clicked!");
    }


    public void deselectAll() {
        twinColSelect.deselectAll();
    }

    private void onDragStart(DragStartEvent<Component> event) {
    }

    private void onDragEnd(DragEndEvent<Component> event) {
        if (bDrop) {
            bDrop = false;
            List<String> itemsList = new ArrayList<>(orderedItems);
            this.updateTwinColSelectOrder(itemsList);
        }
    }

    private void onDrop(DropEvent<Component> event) {
        Component droppedComponent = event.getDragSourceComponent().orElse(null);
        if (droppedComponent != null) {
            int childIndex = childrenDisplayed.indexOf(event.getSource());
            if (childIndex < 0)
                return;
            childrenDisplayed.remove(droppedComponent);
            childrenDisplayed.add(childIndex, droppedComponent);
        }
        orderedItems = new LinkedHashSet<>();
        for (Component child :
                childrenDisplayed) {
            try {
                Field itemField = child.getClass().getDeclaredField("item");
                itemField.setAccessible(true);
                orderedItems.add((String) itemField.get(child));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        bDrop = true;
    }

    private void updateTwinColSelectOrder(List<String> newOrder) {
        TwinColSelect<String> newTwinColSelect = new TwinColSelect<>();
        List<String> tempTotalItems = new ArrayList<>();
        for (String item :
                totalItems) {
            if (!newOrder.contains(item))
                tempTotalItems.add(item);
        }
        tempTotalItems.addAll(newOrder);
        newTwinColSelect.setItems(tempTotalItems);
        newTwinColSelect.select(newOrder);
        addEventInItems(newTwinColSelect);

        parentLayout.removeAll();
        parentLayout.add(newTwinColSelect, moveToolbar());
        twinColSelect = newTwinColSelect;
        twinColSelect.clearTicks(TwinColSelect.ColType.RIGHT);
    }
}