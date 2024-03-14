package com.jo.application.component;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@Tag("signature-dialog")
@JsModule("./js/signature-dialog.tsx")
@JsModule("./js/imageService.tsx")
@NpmPackage(value = "axios", version = "1.6.7")
public class Signature extends Div {

    public Signature() {
        setId("signature-visualization" + this.hashCode());
        setWidthFull();
        setClassName("signature");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        initSignature();
    }

    private void initSignature() {

        this.getElement()
                .callJsFunction("registerSignature",this);
    }
}
