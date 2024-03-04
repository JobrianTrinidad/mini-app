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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Column {
    private ColumnBaseOption columnBaseOption;
    private boolean editable;
    private String type;
    private int maxLength;
    private DateOption dateOption;
    private List<RelationOption> relationOptions;
    private String target;
    private boolean root;
    private String sortingType;
    private boolean sortable;
    private Theme inputTheme;
    private Theme selectTheme;
    private boolean multiline = false;

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getSortingType() {
        return sortingType;
    }

    public void setSortingType(String sortingType) {
        this.sortingType = sortingType;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public DateOption getDateOption() {
        return dateOption;
    }

    public void setDateOption(DateOption dateOption) {
        this.dateOption = dateOption;
    }

    public ColumnBaseOption getColumnBaseOption() {
        return columnBaseOption;
    }

    public void setColumnBaseOption(ColumnBaseOption columnBaseOption) {
        this.columnBaseOption = columnBaseOption;
    }

    public List<RelationOption> getRelationOptions() {
        return relationOptions;
    }

    public void setRelationOptions(List<RelationOption> relationOptions) {
        this.relationOptions = relationOptions;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Theme getInputTheme() {
        return inputTheme;
    }

    public void setInputTheme(Theme inputTheme) {
        this.inputTheme = inputTheme;
    }

    public Theme getSelectTheme() {
        return selectTheme;
    }

    public void setSelectTheme(Theme selectTheme) {
        this.selectTheme = selectTheme;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public String toJSON(boolean bResizable) {
        JsonObject js = columnBaseOption.toJSON();
        if (!bResizable) js.put("resizable", false);
        if (this.multiline)
            js.put("whiteSpace", "normal");
        else
            js.put("whiteSpace", "nowrap");
        if (!Objects.equals(getSortingType(), "")) {
            Optional.ofNullable(getSortingType()).ifPresent(v -> js.put("sortingType", v));
            Optional.of(isSortable()).ifPresent(v -> js.put("sortable", v));
        }
        if (this.isEditable()) {
            JsonObject editableJs = Json.createObject();
            Optional.ofNullable(getType()).ifPresent(v -> editableJs.put("type", v));
            if (Objects.equals(getType(), "input") ||
                    Objects.equals(getType(), "check")) {
                editableJs.put("options", this.inputTheme.toJSON());
            } else if (Objects.equals(getType(), "select")) {
                this.toRelationJSON(js, this.relationOptions);
                js.put("targetNames", this.getTarget());
                editableJs.put("options", this.selectTheme.toJSON());
            } else {
                editableJs.put("options", dateOption.toJSON());
            }
            js.put("editor", editableJs);
        }
//        }
        return js.toJson();
    }

    String toRelationJSON(JsonObject js, List<RelationOption> relationOptions) {
        RelationOption select = new RelationOption("Select", "");
        if (!relationOptions.isEmpty() && !relationOptions.get(0).getChildren().isEmpty()) {
            JsonObject tempJs = Json.createObject();

            for (RelationOption relationOption : relationOptions) {
                if (!Objects.equals(relationOption.getName(), "Select"))
                    tempJs.put(relationOption.getValue(), "[" + select.toSelfJSON() + "," + relationOption.toJSON() + "]");
            }

            js.put("depth1", tempJs);
        } else {
            js.put("depth1", "[]");
        }

        js.put("depth0", isRoot() ?
                (this.convertRelationOptionsToJson().isEmpty() ?
                        "[" + select.toSelfJSON() + "]"
                        : "[" + select.toSelfJSON() + "," + this.convertRelationOptionsToJson() + "]")
                : "[]");

        return js.toJson();
    }

    private String convertRelationOptionsToJson() {
        return this.relationOptions != null ? this.relationOptions.stream().map(RelationOption::toSelfJSON).filter(Objects::nonNull).collect(Collectors.joining(",")) : "";
    }

    /**
     * This is a constructor for creating a column object with the column base option, editable, type, and maximum length specified.
     *
     * @param columnBaseOption the ColumnBaseOption to be set
     * @param editable         the boolean value whether cell is edit enabled
     * @param type             the string value whether cell is type of input, or date...
     * @param root             the boolean whether cell is root of relation
     * @param target           the string value to be set child column of this column
     * @param relationOptions  the RelationOption List value to be set list of RelationOption
     */
    public Column(ColumnBaseOption columnBaseOption, boolean editable, String type, boolean root, String target, List<RelationOption> relationOptions) {
        this.columnBaseOption = columnBaseOption;
        this.editable = editable;
        this.type = type;
        this.root = root;
        this.target = target;
        this.relationOptions = relationOptions;
    }

    /**
     * This is a constructor for creating a column object with only the column base option specified.
     *
     * @param columnBaseOption the ColumnBaseOption to be set
     */
    public Column(ColumnBaseOption columnBaseOption) {
        this(columnBaseOption, false, "input");
    }

    /**
     * This is a constructor for creating a column object with the column base option, editable, type, and maximum length specified.
     *
     * @param columnBaseOption the ColumnBaseOption to be set
     * @param editable         the boolean value whether cell is edit enabled
     * @param type             the string value whether cell is type of input, or date...
     */
    public Column(ColumnBaseOption columnBaseOption, boolean editable, String type) {
        this(columnBaseOption, editable, type, null, "", false);
    }

    /**
     * This is a constructor for creating a column object with the column base option, editable, type, and DateOption specified.
     *
     * @param columnBaseOption the ColumnBaseOption to be set
     * @param editable         the boolean value whether cell is edit enabled
     * @param type             the string value whether cell is type of input, or date...
     * @param option           the DateOption value when cell is type of date
     */
    public Column(ColumnBaseOption columnBaseOption, boolean editable, String type, DateOption option) {
        this(columnBaseOption, editable, type, option, "", false);
    }

    /**
     * This is a constructor for creating a column object with the column base option, sorting type, and sortable specified.
     *
     * @param columnBaseOption the ColumnBaseOption to be set
     * @param sortingType      the string value whether is asc or dec
     * @param sortable         the boolean value whether cell is sortable
     */
    public Column(ColumnBaseOption columnBaseOption, String sortingType, boolean sortable) {
        this(columnBaseOption, false, "", null, sortingType, sortable);
    }

    /**
     * This is a constructor for creating a column object with all the specified properties.
     *
     * @param columnBaseOption the ColumnBaseOption to be set
     * @param editable         the boolean value whether cell is edit enabled
     * @param type             the string value whether cell is type of input, or date...
     * @param dateOption       the DateOption value when cell is type of date
     * @param sortingType      the string value whether is asc or dec
     * @param sortable         the boolean value whether cell is sortable
     */
    public Column(ColumnBaseOption columnBaseOption, boolean editable, String type, DateOption dateOption, String sortingType, boolean sortable) {
        this.columnBaseOption = columnBaseOption;
        this.editable = editable;
        this.type = type;
//        this.maxLength = maxLength;
        this.dateOption = dateOption;
        this.sortingType = sortingType;
        this.sortable = sortable;
    }
}
