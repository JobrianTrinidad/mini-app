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

public class Theme {
    private int maxLength;
    private String backgroundColor;
    private double opacity;
    private String width;
    private String height;
    private String size;
    private String border;
    private String outline;
    private String butBackground;

    public String toJSON() {
        JsonObject js = Json.createObject();
        if (getMaxLength() != 0) {
            js.put("maxLength", getMaxLength());
        }
        Optional.ofNullable(getBackgroundColor()).ifPresent(v -> js.put("backgroundColor", getBackgroundColor()));
        Optional.of(getOpacity()).ifPresent(v -> js.put("opacity", getOpacity()));
        Optional.ofNullable(getWidth()).ifPresent(v -> js.put("width", getWidth()));
        Optional.ofNullable(getHeight()).ifPresent(v -> js.put("height", getHeight()));
        Optional.ofNullable(getSize()).ifPresent(v -> js.put("size", getSize()));
        Optional.ofNullable(getBorder()).ifPresent(v -> js.put("border", getBorder()));
        Optional.ofNullable(getOutline()).ifPresent(v -> js.put("outline", getOutline()));
        Optional.ofNullable(getButBackground()).ifPresent(v -> js.put("butBackground", getButBackground()));

        return js.toJson();
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getButBackground() {
        return butBackground;
    }

    public void setButBackground(String butBackground) {
        this.butBackground = butBackground;
    }

    public Theme() {

    }
}
