package com.aat.application.core.form;

import com.aat.application.core.data.service.ZJTService;
import com.vaadin.flow.component.Component;

public abstract class CustomComponentForm<S extends ZJTService> extends CommonForm {

    protected CustCompViewParameter custCompViewParameter;
    protected S service;

    public CustomComponentForm(CustCompViewParameter custCompViewParameter, S service) {
        this.custCompViewParameter = custCompViewParameter;
        this.service = service;
        addClassName("demo-app-form");
        add(custCompViewParameter.getComponent());
    }
}
