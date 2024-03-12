package com.vaadin.componentfactory.tuigrid.model;
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

import elemental.json.Json;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AATContextMenu {
    private boolean openOnClick;
    private List<MenuItem> items;
    private Object target;

    /**
     * This is a constructor for creating a column object with the column base option, editable, type, and maximum length specified.
     */
    public AATContextMenu() {
        this.items = new ArrayList<>();
    }
    public String convertChildrenToJson() {
        return this.getItems() != null
                ? this.getItems().stream().map(MenuItem::toJSON).collect(Collectors.joining(","))
                : "";
    }

    public String toJSON() {
        JsonObject js = Json.createObject();
        Optional.ofNullable(convertChildrenToJson()).ifPresent(v -> js.put("contextmenu", "[" + v + "]"));
        return js.toJson();
    }

    public void setOpenOnClick(boolean openOnClick) {
        this.openOnClick = openOnClick;
    }

    public MenuItem addItem(String caption) {
        MenuItem newItem = new MenuItem(caption);
        newItem.setCommand(caption);
        items.add(newItem);
        return newItem;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void onContextMenuAction(String cmd, List<Cell> row) {
        for (MenuItem item : this.items) {
            checkCaption(item, cmd, row);
        }
    }

    public void checkCaption(MenuItem item, String cmd, List<Cell> row) {
        if (item.getCommand().equals(cmd)) {
            item.onContextMenuAction(row);
        }

        for (MenuItem subItem : item.getSubItems()) {
            checkCaption(subItem, cmd, row);
        }
    }
}