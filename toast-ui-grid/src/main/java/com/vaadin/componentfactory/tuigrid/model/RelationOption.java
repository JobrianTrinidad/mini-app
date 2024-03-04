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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RelationOption {
    private String name;
    private String value;
    private List<RelationOption> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {

        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<RelationOption> getChildren() {
        return children;
    }

    public void setChildren(List<RelationOption> children) {
        this.children.addAll(children);
    }

    public int getDepth() {
        return this.value.split("-", -1).length;
    }

    public boolean isRoot() {
        if (this.value.split("-", -1).length == 1)
            return true;
        else
            return false;
    }

    public RelationOption(String name, String value) {
//        this.children.add(new RelationOption("Select", ""));
        this.name = name;
        this.value = value;
    }

    public String toSelfJSON() {
        if (this.getName() != null) {
            JsonObject js = Json.createObject();
            Optional.ofNullable(getName()).ifPresent(v -> js.put("text", v));

            Optional.ofNullable(getValue()).ifPresent(v -> js.put("value", v));
            return js.toJson();
        }
        return null;
    }

    public String toJSON() {
        return this.children != null
                ? this.children.stream().map(RelationOption::toSelfJSON).filter(Objects::nonNull).collect(Collectors.joining(","))
                : "";
    }
}