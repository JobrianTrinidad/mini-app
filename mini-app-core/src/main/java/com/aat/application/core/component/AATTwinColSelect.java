package com.aat.application.core.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dnd.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.vaadin.tatu.TwinColSelect;

import java.lang.reflect.Field;
import java.util.*;

public class AATTwinColSelect extends Div {

    private final VerticalLayout parentLayout;
    private TwinColSelect<String> twinColSelect;
    private List<Component> selectedChildren;
    private List<String> totalItems;

    public AATTwinColSelect() {
        VerticalLayout mainLayout = new VerticalLayout();
        twinColSelect = new TwinColSelect<>();
        twinColSelect.addValueChangeListener(event -> addEventInItems());
        mainLayout.add(twinColSelect);

// Now, parentLayout can be initialized as mainLayout
        parentLayout = mainLayout;
        add(parentLayout);
        addClickListener(this::handleClickEvent);
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
        twinColSelect.select(items);
        addEventInItems();
    }

    private void addEventInItems() {
        selectedChildren = findComponentsWithAttribute(twinColSelect, "aria-selected", "true");

        for (Component child : selectedChildren) {
            DragSource<Component> dragSourceWrapper = DragSource.create(child);
            dragSourceWrapper.addDragStartListener(this::onDragStart);
            dragSourceWrapper.addDragEndListener(this::onDragEnd);
            DropTarget<Component> dropTargetWrapper = DropTarget.create(child);
            dropTargetWrapper.addDropListener(event -> onDrop(event, child));
        }
    }

    public static List<Component> findComponentsWithAttribute(Component parent, String attributeName, String attributeValue) {
        List<Component> matchingComponents = new ArrayList<>();

        parent.getChildren().forEach(child -> {
            if (attributeValue.equals(child.getElement().getAttribute(attributeName))) {
                matchingComponents.add(child);
            }

            matchingComponents.addAll(findComponentsWithAttribute(child, attributeName, attributeValue));
        });

        return matchingComponents;
    }

    public void setLabel(String label) {
        twinColSelect.setLabel(label);
    }

    private void handleClickEvent(ClickEvent<Div> event) {
        Notification.show("Component clicked!");
    }

    public void deselectAll() {
        twinColSelect.deselectAll();
    }

    private void onDragStart(DragStartEvent<Component> event) {
    }

    private void onDragEnd(DragEndEvent<Component> event) {
    }

    private void onDrop(DropEvent<Component> event, Component child) {
        Component droppedComponent = event.getDragSourceComponent().orElse(null);
        if (droppedComponent != null) {
            int childIndex = selectedChildren.indexOf(child);
            selectedChildren.remove(droppedComponent);
            selectedChildren.add(childIndex, droppedComponent);
        }
        Set<String> items = new LinkedHashSet<>();
        for (Component selectedChildren :
                selectedChildren) {
            try {
                Field itemField = selectedChildren.getClass().getDeclaredField("item");
                itemField.setAccessible(true);
                items.add((String) itemField.get(selectedChildren));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> itemsList = new ArrayList<>(items);
        updateTwinColSelectOrder(itemsList);
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
        parentLayout.remove(twinColSelect);
        parentLayout.add(newTwinColSelect);
        twinColSelect = newTwinColSelect;
        this.select(newOrder);
    }
}