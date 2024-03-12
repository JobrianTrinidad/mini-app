package com.vaadin.componentfactory.tuigrid.model;

/*-
 * #%L
 * Timeline
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Representation of the different options that can be configure for a {@link com.vaadin.componentfactory.tuigrid.TuiGrid} component.
 */
public class TuiGridOption {
    public enum SummaryPosition {
        top,
        bottom
    }

    public List<Column> columns = new ArrayList<>();
    public List<ComplexColumn> header = new ArrayList<>();
    public int headerHeight = 0;
    public List<Summary> summaryList = new ArrayList<>();
    public int summaryHeight = 0;
    public SummaryPosition pos = SummaryPosition.bottom;
    public int frozenCount = 0;
    public int frozenBorderWidth = 1;
    public boolean resizable = true;
    public boolean vScroll = false;
    public boolean hScroll = false;
    public int tableWidth = 0;
    public int tableHeight = 0;
    public List<String> rowHeaders = new ArrayList<>();
    public TreeOption treeOption;
    public boolean autoSave = false;
    public AATContextMenu contextMenu;
    public boolean bAllowDelete = true;
    public boolean bAllowInsert = true;
    public  boolean multiSelectStatus = true;

    public String toJSON() {
        JsonObject js = Json.createObject();
        Optional.ofNullable(convertColumnsToJson()).ifPresent(v -> js.put("columns", "[" + v + "]"));
        Optional.ofNullable(convertHeaderToJson()).ifPresent(v -> js.put("header", v));
        js.put("autoSave", autoSave);
        js.put("allowDelete", bAllowDelete);
        js.put("allowInsert",
                bAllowInsert);
        js.put("multiSelectStatus",
                multiSelectStatus);
        if (tableWidth > 0)
            js.put("width", tableWidth);
        if (tableHeight > 0)
            js.put("bodyHeight", tableHeight);
        js.put("scrollX", vScroll);
        js.put("scrollY", hScroll);
        if (treeOption != null && treeOption.isTreeColumnOptions()) {
            js.put("treeColumnOptions", treeOption.toJSON());
        }
        if (!rowHeaders.isEmpty())
//            js.put("rowHeaders", rowHeaders.toString());
            Optional.ofNullable(convertRowHeadersToJson()).ifPresent(v -> js.put("rowHeaders", "[" + v + "]"));
        if (header != null) {
            JsonObject headerJs = Json.createObject();
            if (headerHeight != 0) {
                headerJs.put("height", headerHeight);
            }
//            headerJs.put("height", 100);
            Optional.ofNullable(convertHeaderToJson()).ifPresent(v -> headerJs.put("complexColumns", "[" + v + "]"));
            js.put("header", headerJs);
        }


        JsonObject columnOptionsJs = Json.createObject();
        if (frozenCount > 0) {
            columnOptionsJs.put("frozenCount", frozenCount);
            columnOptionsJs.put("frozenBorderWidth", frozenBorderWidth);
        }
        columnOptionsJs.put("resizable", resizable);
        js.put("columnOptions", columnOptionsJs);


        if (summaryList != null) {
            JsonObject summaryJs = Json.createObject();
            if (summaryHeight != 0)
                summaryJs.put("height", summaryHeight);
            else
                summaryJs.put("height", 40);
            summaryJs.put("position", pos.name());
            Optional.ofNullable(convertSummaryToJson()).ifPresent(v -> summaryJs.put("columnContent", "[" + v + "]"));
            js.put("summary", summaryJs);
        }

        if (this.contextMenu != null) {
            Optional.ofNullable(this.contextMenu.convertChildrenToJson()).ifPresent(v -> js.put("contextmenu", "[" + v + "]"));
        }

        return js.toJson();
    }

    private String convertColumnsToJson() {
        return this.columns != null
                ? this.columns.stream().map(column -> {
            int index = this.columns.indexOf(column);
            if (index < this.columns.size())
                return column.toJSON(true);
            else
                return column.toJSON(false);
        }).collect(Collectors.joining(","))
                : "";
    }

    private String convertHeaderToJson() {
        return this.header != null
                ? this.header.stream().map(ComplexColumn::toJSON).collect(Collectors.joining(","))
                : "";
    }

    private String convertSummaryToJson() {
        return this.summaryList != null
                ? this.summaryList.stream().map(Summary::toJSON).collect(Collectors.joining(","))
                : "";
    }

    private String convertRowHeadersToJson() {
        return this.rowHeaders != null
                ? String.join(",", this.rowHeaders)
                : "";
    }
}