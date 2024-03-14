package com.jo.application.core.form;

import com.jo.application.core.data.service.ZJTService;

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
