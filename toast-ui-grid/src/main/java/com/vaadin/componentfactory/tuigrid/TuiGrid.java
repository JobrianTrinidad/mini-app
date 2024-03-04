/*-
 * #%L
 * Toast-ui-grid
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

package com.vaadin.componentfactory.tuigrid;

import com.vaadin.componentfactory.tuigrid.event.*;
import com.vaadin.componentfactory.tuigrid.model.*;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Div;
import elemental.json.JsonArray;
import elemental.json.JsonNull;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import elemental.json.impl.JreJsonArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Toast-ui-grid component definition. Toast-ui-grid uses vis-timeline component to display data in time (see
 * more at https://github.com/toast-ui-grid).
 */
@NpmPackage(value = "react", version = "^18.2.0")
@NpmPackage(value = "react-dom", version = "^18.2.0")
//@NpmPackage(value = "@types/react", version = "^18.2.0")
//@NpmPackage(value = "@types/react-dom", version = "^18.2.0")
@NpmPackage(value = "@chakra-ui/react", version = "^2.8.0")
@NpmPackage(value = "@chakra-ui/icons", version = "^2.1.0")
@NpmPackage(value = "tui-grid", version = "^4.21.22")
@NpmPackage(value = "tui-context-menu", version = "^2.1.9")
@JsModule("./src/views/toastuigrid/toast-ui-grid-view.tsx")
@JsModule("./src/views/components/Table/FeaturesTable.tsx")
@JsModule("./src/views/components/Table/ExcelSheet.tsx")
@JsModule("./src/views/components/Table/CustomeEditor.tsx")
@JsModule("./src/views/components/checkbox/ada-checkbox.tsx")
@CssImport("tui-grid/dist/tui-grid.css")
@CssImport("tui-context-menu/dist/tui-context-menu.css")
@CssImport("tui-date-picker/dist/tui-date-picker.css")
@CssImport("tui-time-picker/dist/tui-time-picker.css")
@CssImport("./styles/styles.css")
public class TuiGrid extends Div {
    private List<Item> items = new ArrayList<>();
    protected TuiGridOption tuiGridOption = new TuiGridOption();
    private List<Column> columns = new ArrayList<>();
    private int filterId = -1;
    private List<Integer> checkedItems = new ArrayList<>();
    List<String> headers = new ArrayList<>();
    Theme inputTheme;
    Theme selectTheme;

    public TuiGrid() {
        setId("visualization" + this.hashCode());
        setWidthFull();
        setClassName("grid");
    }

    public TuiGrid(List<Item> items, List<Column> columns) {
        this();
        this.items = items;
        tuiGridOption.columns = columns;
    }

    public TuiGrid(List<Item> items, List<Column> columns, List<Summary> summaries) {
        this();
        this.items = items;
        tuiGridOption.columns = columns;
        tuiGridOption.summaryList = summaries;
    }

    public TuiGrid(List<ComplexColumn> customHeader, List<Item> items, List<Column> columns, List<Summary> summaries) {
        this();
        this.items = items;
        tuiGridOption.columns = columns;
        tuiGridOption.summaryList = summaries;
        tuiGridOption.header = customHeader;
    }

    /**
     * Method called when the component is attached to the UI.
     * It initializes the TuiGrid component.
     */
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        initTuiGrid();
    }

    /**
     * Sets the items of the grid with the provided list of items.
     *
     * @param items the items to be set
     */
    public void setItems(List<Item> items) {
        this.items = items;
        if (this.getElement().getNode().isAttached()) {
            this.getElement()
                    .executeJs(
                            "toastuigrid.setTableData($0, $1);",
                            this, "[" + convertItemsToJson(items) + "]");
        }
    }

    public List<Item> getItems() {
        return items;
    }

    /**
     * Adds the provided list of items to the existing items in the grid.
     *
     * @param items the items to be added
     */
    public void addItem(List<Item> items) {
        if (this.getElement().getNode().isAttached())
            this.getElement()
                    .executeJs(
                            "toastuigrid.addTableData($0);",
                            this);
    }

    @ClientCallable
    public void addItem() {
        if (this.getElement().getNode().isAttached())
            this.getElement()
                    .executeJs(
                            "toastuigrid.addTableData($0);",
                            this);
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }

    public void setContextMenu(AATContextMenu contextMenu) {
        tuiGridOption.contextMenu = contextMenu;
        this.updateTuiGridOptions();

    }

    public void setAutoSave(boolean autoSave) {
        tuiGridOption.autoSave = autoSave;
        this.updateTuiGridOptions();
    }

    public void setInputTheme(Theme inputTheme) {
        this.inputTheme = inputTheme;
        for (Column col :
                tuiGridOption.columns) {
            col.setInputTheme(inputTheme);
        }
        this.updateTuiGridOptions();
    }

    public void setSelectTheme(Theme selectTheme) {
        this.selectTheme = selectTheme;
        for (Column col :
                tuiGridOption.columns) {
            col.setSelectTheme(selectTheme);
        }
        this.updateTuiGridOptions();
    }

    public void setbAllowDelete(boolean bAllowDelete) {
        this.tuiGridOption.bAllowDelete = bAllowDelete;
    }

    public void setbAllowInsert(boolean bAllowInsert) {
        this.tuiGridOption.bAllowInsert = bAllowInsert;
    }

    /**
     * Sets the columns of the grid with the provided list of columns.
     *
     * @param columns the columns to be set
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
        tuiGridOption.columns.addAll(columns);
        this.updateTuiGridOptions();
    }

    /**
     * Sets the columns of the grid with the provided list of summaries.
     *
     * @param summaries the summaries to be set
     */
    public void setSummaries(List<Summary> summaries) {
        tuiGridOption.summaryList.addAll(summaries);
        this.updateTuiGridOptions();
    }

    /**
     * Sets the complex columns of the grid with the provided list of complex columns.
     *
     * @param complexColumns the complexColumns to be set
     */
    public void setComplexColumns(List<ComplexColumn> complexColumns) {
        tuiGridOption.header.addAll(complexColumns);
        this.updateTuiGridOptions();
    }

    /**
     * Returns the header height of the grid.
     */
    public int getHeaderHeight() {
        return tuiGridOption.headerHeight;
    }

    /**
     * Sets the header height of the grid with the provided value.
     *
     * @param headerHeight the headerHeight to be set
     */
    public void setHeaderHeight(int headerHeight) {
        tuiGridOption.headerHeight = headerHeight;
        this.updateTuiGridOptions();
    }

    /**
     * Sets the summary height of the grid with the provided value.
     *
     * @param summaryHeight the summaryHeight to be set
     */
    public void setSummaryHeight(int summaryHeight) {
        tuiGridOption.summaryHeight = summaryHeight;
        this.updateTuiGridOptions();
    }

    /**
     * Sets whether vertical scrolling is enabled in the grid.
     *
     * @param vScroll the boolean value to be set
     */
    public void setvScroll(boolean vScroll) {
        tuiGridOption.vScroll = vScroll;
        this.updateTuiGridOptions();
    }

    /**
     * Sets whether horizontal scrolling is enabled in the grid.
     *
     * @param hScroll the boolean value to be set
     */
    public void sethScroll(boolean hScroll) {
        tuiGridOption.hScroll = hScroll;
        this.updateTuiGridOptions();
    }

    /**
     * Sets the number of frozen columns in the grid.
     *
     * @param frozenCount the int value to be set
     */
    public void setFrozenCount(int frozenCount) {
        tuiGridOption.frozenCount = frozenCount;
        this.updateTuiGridOptions();
    }

    /**
     * Sets the border width of frozen columns in the grid.
     *
     * @param frozenBorderWidth the int value to be set
     */
    public void setFrozenBorderWidth(int frozenBorderWidth) {
        tuiGridOption.frozenBorderWidth = frozenBorderWidth;
        this.updateTuiGridOptions();
    }

    /**
     * Sets the width of the grid table.
     *
     * @param tableWidth the int value to be set
     */
    public void setTableWidth(int tableWidth) {
        tuiGridOption.tableWidth = tableWidth;
        this.updateTuiGridOptions();
    }

    /**
     * Sets the height of the grid table.
     *
     * @param tableHeight the int value to be set
     */
    public void setTableHeight(int tableHeight) {
        tuiGridOption.tableHeight = tableHeight;
        this.updateTuiGridOptions();
    }

    /**
     * Sets the row headers of the grid with the provided list of row headers.
     *
     * @param rowHeaders the rowHeaders to be set
     */
    public void setRowHeaders(List<String> rowHeaders) {
        tuiGridOption.rowHeaders = rowHeaders;
        this.updateTuiGridOptions();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    /**
     * Sets the tree option for the grid.
     *
     * @param treeOption the TreeOption to be set
     */
    public void setTreeOption(TreeOption treeOption) {
        tuiGridOption.treeOption = treeOption;
    }

    /**
     * Sets whether the grid is resizable.
     *
     * @param resizable the boolean value to be set
     */
    public void setResizable(boolean resizable) {
        tuiGridOption.resizable = resizable;
    }

    public int[] getCheckedItems() {
        int[] array = new int[checkedItems.size()];

        for (int i = 0; i < checkedItems.size(); i++) {
            array[i] = checkedItems.get(i);
        }
        return array;
    }

    /**
     * Updates tuigrid options after tuigrid creation.
     */
    public void setUnCheckAll() {
        this.getElement().executeJs("toastuigrid.uncheckAll($0)", this);
    }

    /**
     * Initializes the TuiGrid component.
     */
    private void initTuiGrid() {
        this.getElement()
                .executeJs(
                        "toastuigrid.create($0, $1, $2, $3);",
                        this, "[" + convertItemsToJson() + "]",
                        tuiGridOption.toJSON(), this.getFilterId());
    }

    /**
     * Updates tuigrid options after tuigrid creation.
     */
    private void updateTuiGridOptions() {
        if (this.getElement().getNode().isAttached()) {
            this.getElement().executeJs("toastuigrid.setOptions($0, $1)", this, tuiGridOption.toJSON());
        }
    }

    /**
     * Converts the items in the grid to JSON format.
     */
    private String convertItemsToJson() {
        return this.items != null
                ? this.items.stream().map(Item::toJSON).collect(Collectors.joining(","))
                : "";
    }

    /**
     * Converts the provided list of items to JSON format.
     */
    private String convertItemsToJson(List<Item> items) {
        return items != null
                ? items.stream().map(Item::toJSON).collect(Collectors.joining(","))
                : "";
    }

    /**
     * Sets the selected item in the grid based on the provided column name.
     *
     * @param colName the String value to be set
     */
    @ClientCallable
    public void setSelectItem(String colName, int rowKey) {
        fireItemSelectEvent(colName, rowKey, true);
    }

    /**
     * Fires an item select event with the provided column name and client information.
     */
    protected void fireItemSelectEvent(
            String colName, int rowKey, boolean fromClient) {
        SelectionEvent event = new SelectionEvent(this, colName, rowKey, fromClient);
        RuntimeException exception = null;

        try {
            fireEvent(event);
        } catch (RuntimeException e) {
            exception = e;
            event.setCancelled(true);
        }
    }//

    /**
     * Sets the selected item in the grid based on the provided column name.
     *
     * @param rows the String value to be set
     */
    @ClientCallable
    public void deleteItems(int[] rows) {
        checkedItems = new ArrayList<>();
        for (int num : rows) {
            checkedItems.add(num);
        }

        fireDeleteItemsEvent(checkedItems, true);
    }

    /**
     * Fires an item select event with the provided column name and client information.
     */
    protected void fireDeleteItemsEvent(
            List<Integer> rows, boolean fromClient) {
        ItemDeleteEvent event = new ItemDeleteEvent(this, rows, fromClient);
        RuntimeException exception = null;
        this.getElement()
                .executeJs(
                        "toastuigrid.removeRows($0);",
                        this);

        try {
            fireEvent(event);
        } catch (RuntimeException e) {
            exception = e;
            event.setCancelled(true);
        }
    }//

    /**
     * Sets the selected item in the grid based on the provided column name.
     *
     * @param colName  the String value to be set
     * @param colWidth the int value to be set
     */
    @ClientCallable
    public void resizeColumn(String colName, int colWidth) {
        fireColumnResizeEvent(colName, colWidth, true);
    }

    /**
     * Fires an item select event with the provided column name and client information.
     */
    protected void fireColumnResizeEvent(
            String colName, int colWidth, boolean fromClient) {
        ColumnResizeEvent event = new ColumnResizeEvent(this, colName, colWidth, fromClient);

        try {
            fireEvent(event);
        } catch (RuntimeException e) {
            event.setCancelled(true);
        }
    }//

    /**
     * Returns the list of items in the grid.
     */
    public List<Item> getData() {
        return this.items;
    }

    /**
     * Refreshes the data in the grid by updating the value at the specified row and column.
     *
     * @param row    the grid's row to be changed
     * @param record the grid's value to be changed
     */
    private void refreshData(int row, JsonObject record) {
        List<Item> tempItems = new ArrayList<>(this.items);
        GuiItem temp = (GuiItem) this.items.get(row);
        List<String> tempRecord = new ArrayList<>(temp.getRecordData());
        for (Column colName :
                this.columns) {

            String columnName = colName.getColumnBaseOption().getName();
            String columnValue = "";

            if (record.hasKey(columnName)) {
                JsonValue jsonValue = record.get(columnName);
                if (jsonValue == null || jsonValue instanceof JsonNull) {
                    columnValue = "";
                } else {
                    columnValue = jsonValue.asString();
                }
            }
            tempRecord.set(temp.getHeaders().indexOf(columnName), columnValue);
        }
        tempItems.set(row, new GuiItem((int) record.getNumber("id"), tempRecord, temp.getHeaders()));
        this.items = tempItems;
    }

    public void refreshGrid() {
        this.getElement()
                .executeJs(
                        "toastuigrid.refreshLayout($0);",
                        this);
    }

    public void reloadData() {
        this.getElement()
                .executeJs(
                        "toastuigrid.modifiedData($0);",
                        this);
    }
    public void resetOriginData() {
        this.getElement()
                .executeJs(
                        "toastuigrid.resetOriginData($0);",
                        this);
    }

    public void restoreData() {
        this.getElement()
                .executeJs(
                        "toastuigrid.restoreData($0);",
                        this);
    }

    public void onEnable() {
        this.getElement()
                .executeJs(
                        "toastuigrid.onEnable($0);",
                        this);
    }

    public void onDisable() {
        this.getElement()
                .executeJs(
                        "toastuigrid.onDisable($0);",
                        this);
    }

    public void setRowCountOnElement(String elementId) {
        if (this.getElement().getNode().isAttached()) {
            this.getElement()
                    .executeJs(
                            "toastuigrid.setRowCount($0, $1);",
                            this, elementId);
        }
    }

    @ClientCallable
    public void modifiedData(JsonObject modifiedData) {
        JsonArray updatedRows = modifiedData.getArray("updatedRows");
        JsonArray createdRows = modifiedData.getArray("createdRows");
        JsonArray deletedRows = modifiedData.getArray("deletedRows");
        ItemSaveEvent event = new ItemSaveEvent(this, createdRows, updatedRows, deletedRows,
                this.tuiGridOption.columns, headers, true);

        RuntimeException exception = null;
        try {
            fireEvent(event);
            this.getElement()
                    .executeJs(
                            "toastuigrid.resetOriginData($0);",
                            this);
        } catch (RuntimeException e) {
            exception = e;
            event.setCancelled(true);
        }
    }

    public void setFilter(String colName, String filter) {
        this.getElement()
                .executeJs(
                        "toastuigrid.setFilter($0, $1, $2);",
                        colName, filter, this);
    }

    public void setDateFilter(String colName, String startDate, String endDate) {
        this.getElement()
                .executeJs(
                        "toastuigrid.setDateFilter($0, $1, $2, $3);",
                        colName, startDate, endDate, this);
    }

    /**
     * Handles the click event in the grid for the specified column name and row.
     */
    @ClientCallable
    public void onClick(String colName, int row) {
        this.getElement()
                .executeJs(
                        "toastuigrid.setTest($0, $1);",
                        this, colName);
    }

    @ClientCallable
    public void onContextMenuAction(String cmd, JsonObject focusedCell) {
        List<Cell> row = new ArrayList<>();
        for (String header :
                this.headers) {
            if (focusedCell.hasKey(header)) {
                Cell cell = new Cell(header,
                        (int) focusedCell.getNumber("id"),
                        focusedCell.get(header).asString());
                row.add(cell);
            }
        }
        switch (cmd) {
            case "copy":
            case "copyColumns":
            case "copyRows":
            case "csvExport":
            case "excelExport":
            case "txtExport":
                this.getElement()
                        .executeJs(
                                "toastuigrid.defaultContextMenu($0, $1);",
                                cmd, this);
                break;
            default:
                this.tuiGridOption.contextMenu.onContextMenuAction(cmd, row);
                break;
        }
    }

    /**
     * Handles the onCheck event in the grid.
     */
    @ClientCallable
    public void onCheck(int[] rows) {
        fireItemMultiSelectEvent(rows, true);
    }

    /**
     * Fires an item select event with the provided column name and client information.
     */
    protected void fireItemMultiSelectEvent(
            int[] itemIds, boolean fromClient) {
        ItemMultiSelectEvent event = new ItemMultiSelectEvent(this, itemIds, fromClient);
        RuntimeException exception = null;

        try {
            fireEvent(event);
        } catch (RuntimeException e) {
            exception = e;
            event.setCancelled(true);
        }
    }

    /**
     * Handles the onUncheck event in the grid.
     */
    @ClientCallable
    public void onUncheck(int[] rows) {
        checkedItems = new ArrayList<>();
        for (int num : rows) {
            checkedItems.add(num);
        }
    }

    /**
     * Handles the onCheckAll event in the grid.
     */
    @ClientCallable
    public void onCheckAll(int[] rows) {
        checkedItems = new ArrayList<>();
        for (int num : rows) {
            checkedItems.add(num);
        }
    }

    /**
     * Handles the onUnCheckAll event in the grid.
     */
    @ClientCallable
    public void onUncheckAll(JsonObject eventData) {
    }

    /**
     * Handles the editing finish event in the grid.
     */
    @ClientCallable
    public void onUpdateData(JsonObject eventData) {
        JsonObject itemChanged = eventData.getArray("changes").get(0);
        JsonObject record = eventData.get("record");

        ItemChangeEvent event = new ItemChangeEvent(
                this, itemChanged.getString("columnName"),
                itemChanged.getString("value"),
                (int) record.get("id").asNumber(), true);

        RuntimeException exception = null;
        try {
            fireEvent(event);
        } catch (RuntimeException e) {
            exception = e;
            event.setCancelled(true);
        }
    }

    @ClientCallable
    public void onAddRecord(JsonObject eventData) {
        GuiItem item = convertJsonToItem(eventData);
        this.items = new ArrayList<>(this.items);
        this.items.add(item);

        ItemAddEvent addEvent = new ItemAddEvent(this, item, true);

        RuntimeException exception = null;
        try {
            fireEvent(addEvent);
        } catch (RuntimeException e) {
            exception = e;
            addEvent.setCancelled(true);
        }
    }

    public void setIDToGridRow(int rowKey, int id) {
        this.getElement()
                .executeJs(
                        "toastuigrid.setIdToRow($0, $1, $2);",
                        this, rowKey, id);
    }

    private GuiItem convertJsonToItem(JsonObject eventData) {
        JsonObject data = eventData.getObject("data");
        int index = (int) eventData.getNumber("rowIndex");
        GuiItem item = new GuiItem();
        item.setId(index);
        List<String> record = new ArrayList<>();

        for (Column column :
                this.tuiGridOption.columns) {
            String colName = column.getColumnBaseOption().getName();
            if (data.hasKey(colName)
                    && !data.get(colName).toString().equals("All"))
                record.add(data.get(colName).asString());
            else
                record.add("");
        }

        item.setHeaders(headers);
        item.setRecordData(record);
        return item;
    }

    /**
     * Handles the event to get data from the grid.
     */
    @ClientCallable
    public void onGetData(JreJsonArray data) {
        JsonObject temp = data.getObject(0);
        List<String> headers = new ArrayList<>(List.of(temp.keys()));
        for (int i = 0; i < data.length(); i++) {
            List<String> recordData = new ArrayList<>();
            JsonObject jsonValue = data.getObject(i);
            for (String header :
                    headers) {

                recordData.add(jsonValue.get(header).toString());
            }
            this.items = new ArrayList<>();
            this.items.add(new GuiItem((int) jsonValue.getNumber("id"), recordData, headers));
        }
    }

    /**
     * Adds a listener for {@link SelectionEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addItemSelectListener(ComponentEventListener<SelectionEvent> listener) {
        addListener(SelectionEvent.class, listener);
    }

    /**
     * Adds a listener for {@link ItemMultiSelectEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addItemMultiSelectListener(ComponentEventListener<ItemMultiSelectEvent> listener) {
        addListener(ItemMultiSelectEvent.class, listener);
    }

    /**
     * Adds a listener for {@link ItemChangeEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addItemChangeListener(ComponentEventListener<ItemChangeEvent> listener) {
        addListener(ItemChangeEvent.class, listener);
    }

    /**
     * Adds a listener for {@link ItemAddEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addItemAddListener(ComponentEventListener<ItemAddEvent> listener) {
        addListener(ItemAddEvent.class, listener);
    }

    /**
     * Adds a listener for {@link ItemChangeEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addColumnResizeListener(ComponentEventListener<ColumnResizeEvent> listener) {
        addListener(ColumnResizeEvent.class, listener);
    }

    /**
     * Adds a listener for {@link ItemSaveEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addItemSaveListener(ComponentEventListener<ItemSaveEvent> listener) {
        addListener(ItemSaveEvent.class, listener);
    }

    /**
     * Adds a listener for {@link ItemDeleteEvent} to the component.
     *
     * @param listener the listener to be added
     */
    public void addItemDeleteListener(ComponentEventListener<ItemDeleteEvent> listener) {
        addListener(ItemDeleteEvent.class, listener);
    }

    public void setFieldsAsReadOnly(String[] fieldsAsReadOnly) {
        String columnsJson = convertColumnsToJson(fieldsAsReadOnly);

        this.getElement().executeJs("toastuigrid.onDisableFieldsAsReadOnly($0, $1);", this, columnsJson);

    }

    public  void setMultiSelect(Boolean multiSelectStatus) {
        this.tuiGridOption.multiSelectStatus = multiSelectStatus;
    }
    private String convertColumnsToJson(String[] fieldsAsReadOnly) {
        if (fieldsAsReadOnly != null && fieldsAsReadOnly.length > 0) {
            // Assuming each field in fieldsAsReadOnly corresponds to a column in your columns list
            return "[" + Arrays.stream(fieldsAsReadOnly)
                    .map(field -> "{\"name\": \"" + field + "\", \"readOnly\": true}")
                    .collect(Collectors.joining(",")) + "]";
        } else {
            return "[]"; // Empty array if fieldsAsReadOnly is null or empty
        }
    }
}