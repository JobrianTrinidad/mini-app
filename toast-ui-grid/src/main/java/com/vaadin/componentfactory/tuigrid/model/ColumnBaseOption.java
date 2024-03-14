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

import java.util.Optional;

public class ColumnBaseOption {
    private int id;
    private String headerName;
    private String name;
    private int width;
    private String align;
    private String className;

    public ColumnBaseOption(int id, String headerName, String name, int width, String align, String className) {
        this.id = id;
        this.headerName = headerName;
        this.name = name;
        this.width = width;
        this.align = align;
        this.className = className;
    }

    public JsonObject toJSON() {
        JsonObject js = Json.createObject();
        Optional.ofNullable(getId()).ifPresent(v -> js.put("id", v));
        Optional.ofNullable(getName()).ifPresent(v -> js.put("name", v));
        Optional.ofNullable(getHeaderName()).ifPresent(v -> js.put("header", v));
        if(getWidth() > 0)
            js.put("width", getWidth());
        Optional.ofNullable(getAlign()).ifPresent(v -> js.put("align", v));
        Optional.ofNullable(getClassName()).ifPresent(v -> js.put("className", v));
        return js;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


}
