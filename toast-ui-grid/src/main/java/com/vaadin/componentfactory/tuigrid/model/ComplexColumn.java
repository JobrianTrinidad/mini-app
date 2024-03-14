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
import jakarta.persistence.GeneratedValue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ComplexColumn {
    private String headerName;
    private String name;
    private List<String> childNames;

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

    public List<String> getChildNames() {
        return childNames;
    }

    public void setChildNames(List<String> childNames) {
        this.childNames = childNames;
    }

    public String toJSON() {
        JsonObject js = Json.createObject();
        Optional.ofNullable(getName()).ifPresent(v -> js.put("name", v));
        Optional.ofNullable(getHeaderName()).ifPresent(v -> js.put("header", v));
        Optional.ofNullable(getChildNames()).ifPresent(v -> js.put("childNames", String.valueOf(v)));

        return js.toJson();
    }

    public ComplexColumn(String headerName, String name, List<String> childNames) {
        this.headerName = headerName;
        this.name = name;
        this.childNames = childNames;
    }

    public ComplexColumn() {

    }
}
