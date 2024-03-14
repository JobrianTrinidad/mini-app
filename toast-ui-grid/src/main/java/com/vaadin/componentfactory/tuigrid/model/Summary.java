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

import java.util.Objects;
import java.util.Optional;

public class Summary {

    public enum OperationType {
        sum,
        avg,
        max,
        min,
        rowcount
    }

    private String colName;
    private OperationType operationType;

    public Summary(String colName, OperationType operationType) {
        this.colName = colName;
        this.operationType = operationType;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String toJSON() {
        JsonObject js = Json.createObject();
        Optional.ofNullable(getColName()).ifPresent(v -> js.put(v, getOperationType().name()));

        return js.toJson();
    }
}