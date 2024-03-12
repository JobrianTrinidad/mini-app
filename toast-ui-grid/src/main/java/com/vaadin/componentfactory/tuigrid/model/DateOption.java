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

public class DateOption {
    private String format;
    private boolean timepicker = false;
    private int fromYear;
    private int fromMonth;
    private int fromDay;
    private int toYear;
    private int toMonth;
    private int toDay;
    private String layoutType;
    private String inputType;
    private String optionType;

    public String toJSON() {
        JsonObject js = Json.createObject();

        Optional.ofNullable(getFormat()).ifPresent(v -> js.put("format", v));

        if (getFromYear() != 0) {
            Optional.ofNullable(getFromYear()).ifPresent(v -> js.put("fromYear", v));
            Optional.ofNullable(getFromMonth()).ifPresent(v -> js.put("fromMonth", v));
            Optional.ofNullable(getFromDay()).ifPresent(v -> js.put("fromDay", v));
            Optional.ofNullable(getToYear()).ifPresent(v -> js.put("toYear", v));
            Optional.ofNullable(getToMonth()).ifPresent(v -> js.put("toMonth", v));
            Optional.ofNullable(getToDay()).ifPresent(v -> js.put("toDay", v));
        }

        if (getLayoutType() == "")
            Optional.ofNullable(isTimepicker()).ifPresent(v -> js.put("timepicker", v));
        else {
            JsonObject timepickerJs = Json.createObject();
            Optional.ofNullable(getLayoutType()).ifPresent(v -> timepickerJs.put("layoutType", v));
            Optional.ofNullable(getInputType()).ifPresent(v -> timepickerJs.put("inputType", v));
            js.put("timepicker", timepickerJs);
        }
        if (getOptionType() != "")
            Optional.ofNullable(getOptionType()).ifPresent(v -> js.put("type", v));

        return js.toJson();
    }

    public DateOption(String format, boolean timepicker) {
        this.format = format;
        this.timepicker = timepicker;
    }

    public DateOption(String format, String optionType) {
        this.format = format;
        this.optionType = optionType;
    }

    public DateOption(String format, boolean timepicker, String layoutType, String inputType) {
        this.format = format;
        this.timepicker = timepicker;
        this.layoutType = layoutType;
        this.inputType = inputType;
    }

    public DateOption(int fromYear, int fromMonth, int fromDay, int toYear, int toMonth, int toDay) {
        this.fromYear = fromYear;
        this.fromMonth = fromMonth;
        this.fromDay = fromDay;
        this.toYear = toYear;
        this.toMonth = toMonth;
        this.toDay = toDay;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isTimepicker() {
        return timepicker;
    }

    public void setTimepicker(boolean timepicker) {
        this.timepicker = timepicker;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getFromDay() {
        return fromDay;
    }

    public void setFromDay(int fromDay) {
        this.fromDay = fromDay;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public int getToMonth() {
        return toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public int getToDay() {
        return toDay;
    }

    public void setToDay(int toDay) {
        this.toDay = toDay;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }
}
