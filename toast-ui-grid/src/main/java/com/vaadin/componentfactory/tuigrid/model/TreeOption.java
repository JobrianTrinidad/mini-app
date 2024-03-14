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

public class TreeOption {
    private boolean treeColumnOptions = false;
    private String baseFieldForTree;
    private boolean useCascadingCheckbox = false;

    public boolean isTreeColumnOptions() {
        return treeColumnOptions;
    }

    public void setTreeColumnOptions(boolean treeColumnOptions) {
        this.treeColumnOptions = treeColumnOptions;
    }

    public String getBaseFieldForTree() {
        return baseFieldForTree;
    }

    public void setBaseFieldForTree(String baseFieldForTree) {
        this.baseFieldForTree = baseFieldForTree;
    }

    public boolean isUseCascadingCheckbox() {
        return useCascadingCheckbox;
    }

    public void setUseCascadingCheckbox(boolean useCascadingCheckbox) {
        this.useCascadingCheckbox = useCascadingCheckbox;
    }

    public TreeOption(boolean treeColumnOptions, String baseFieldForTree, boolean useCascadingCheckbox) {
        this.treeColumnOptions = treeColumnOptions;
        this.baseFieldForTree = baseFieldForTree;
        this.useCascadingCheckbox = useCascadingCheckbox;
    }

    public String toJSON() {
        JsonObject js = Json.createObject();
        if (isTreeColumnOptions()) {
            Optional.ofNullable(getBaseFieldForTree()).ifPresent(v -> js.put("name", v));
            Optional.ofNullable(isUseCascadingCheckbox()).ifPresent(v -> js.put("useCascadingCheckbox", useCascadingCheckbox));
            return js.toJson();
        } else
            return "";

    }
}
