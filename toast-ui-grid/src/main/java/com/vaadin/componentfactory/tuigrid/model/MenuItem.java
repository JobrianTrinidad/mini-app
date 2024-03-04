/*-
 * #%L
 * TuiGrid
 * %%
 * Copyright (C) 2021 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.vaadin.componentfactory.tuigrid.model;

import com.vaadin.componentfactory.tuigrid.event.ContextMenuSelectEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import elemental.json.Json;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuItem extends Div {
    private final String caption;
    private String command;
    private MenuItem parentMenu;
    private final List<MenuItem> subItems;

    public MenuItem(String caption) {
        this.caption = caption;
        this.subItems = new ArrayList<>();
    }

    public String toJSON() {
        JsonObject js = Json.createObject();
        js.put("title", this.caption);
        String command = this.parentMenu != null ? this.parentMenu.getCaption() + "_" + this.caption : this.caption;
        js.put("command", command);
        Optional.ofNullable(convertChildrenToJson()).ifPresent(v -> js.put("menu", "[" + v + "]"));
        return js.toJson();
    }

    private String convertChildrenToJson() {
        return !this.subItems.isEmpty()
                ? this.subItems.stream().map(MenuItem::toJSON).collect(Collectors.joining(","))
                : null;
    }

    public String getCaption() {
        return caption;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public MenuItem getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(MenuItem parentMenu) {
        this.parentMenu = parentMenu;
    }

    public MenuItem addSubItem(String caption) {
        MenuItem subItem = new MenuItem(caption);
        subItem.setParentMenu(this);
        subItem.setCommand(this.getCaption() + "_" + subItem.getCaption());
        subItems.add(subItem);
        return subItem;
    }

    public List<MenuItem> getSubItems() {
        return subItems;
    }

    public void onContextMenuAction(List<Cell> row) {
        // Handle the context menu action

        ContextMenuSelectEvent event = new ContextMenuSelectEvent(this, row, true);
        RuntimeException exception = null;
        try {
            fireEvent(event);
        } catch (RuntimeException e) {
            exception = e;
            event.setCancelled(true);
        }
    }

    /**
     * Adds a listener for {@link ContextMenuSelectEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addContextMenuClickListener(ComponentEventListener<ContextMenuSelectEvent> listener) {
        addListener(ContextMenuSelectEvent.class, listener);
    }
}
