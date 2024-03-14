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
package com.vaadin.componentfactory.tuigrid.event;

import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.Column;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.componentfactory.tuigrid.model.Item;
import com.vaadin.flow.component.ComponentEvent;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Event thrown when an item is resized.
 */
public class ItemSaveEvent extends ComponentEvent<TuiGrid> {

    private final List<Column> columns;
    private final List<String> headers;
    JsonArray updatedRows;
    JsonArray createdRows;
    JsonArray deletedRows;
    private boolean cancelled = false;

    public ItemSaveEvent(TuiGrid source,
                         JsonArray createdRows,
                         JsonArray updatedRows,
                         JsonArray deletedRows,
                         List<Column> columns,
                         List<String> headers,
                         boolean fromClient) {
        super(source, fromClient);
        this.createdRows = createdRows;
        this.updatedRows = updatedRows;
        this.deletedRows = deletedRows;
        this.columns = columns;
        this.headers = headers;

        List<GuiItem> updateItems = new ArrayList<>();
        List<GuiItem> createdItems = new ArrayList<>();
        List<GuiItem> deletedItems = new ArrayList<>();

        for (int i = 0; i < updatedRows.length(); i++) {
            JsonObject jsonObject = updatedRows.getObject(i);
            GuiItem item = convertJsonToItem(jsonObject);
            updateItems.add(item);
        }

        for (int i = 0; i < createdRows.length(); i++) {
            JsonObject jsonObject = createdRows.getObject(i);
            GuiItem item = convertJsonToItem(jsonObject);
            createdItems.add(item);
        }

        for (int i = 0; i < deletedRows.length(); i++) {
            JsonObject jsonObject = deletedRows.getObject(i);
            GuiItem item = convertJsonToItem(jsonObject);
            deletedItems.add(item);
        }
    }

    private GuiItem convertJsonToItem(JsonObject eventData) {
        GuiItem item = new GuiItem();
        List<String> record = new ArrayList<>();

        for (Column column :
                this.columns) {
            String colName = column.getColumnBaseOption().getName();
            if (eventData.hasKey(colName)
                    && !eventData.get(colName).toString().equals("All"))
                record.add(eventData.get(colName).asString());
            else
                record.add("");
        }

        item.setHeaders(headers);
        item.setRecordData(record);
        return item;
    }

    public JsonArray getUpdatedRows() {
        return updatedRows;
    }

    public void setUpdatedRows(JsonArray updatedRows) {
        this.updatedRows = updatedRows;
    }

    public JsonArray getCreatedRows() {
        return createdRows;
    }

    public void setCreatedRows(JsonArray createdRows) {
        this.createdRows = createdRows;
    }

    public JsonArray getDeletedRows() {
        return deletedRows;
    }

    public void setDeletedRows(JsonArray deletedRows) {
        this.deletedRows = deletedRows;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public TuiGrid getTuiGrid() {
        return (TuiGrid) source;
    }
}