package com.aat.application.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Tag("signature-dialog")
@JsModule("./js/signature-dialog.tsx")
public class Signature extends Div {
    private Dialog dialog;
    private Image sign;
    private SignaturePad signature;


    public Signature() {
        HorizontalLayout vl = new HorizontalLayout();

        signature = new SignaturePad();
        signature.setHeight("100px");
        signature.setWidth("300px");

        Button button = new Button("Undo");
        button.setId("undo-button");
        Button button2 = new Button("Save");
        button2.setId("save-button");

        dialog = new Dialog();
        dialog.setSizeFull();
        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);
        dialog.setResizable(true);

        sign = new Image();

        vl.add(signature, sign);

        Button closeButton = new Button("Close");
        closeButton.setId("close-button");

        dialog.add(vl, button, button2, closeButton);

        Button button3 = new Button("Open Sign Dialog");
        button3.setId("open-dialog-button");
        this.add(dialog, button3);

        button3.addClickListener(event -> {
            openDialog();
        });

        button.addClickListener(event -> {
            undoSignature();
        });

        button2.addClickListener(event -> {
            saveSignature();
        });
        // Create a close button for the dialog

        closeButton.addClickListener(event -> {
            closeDialog();
        });
    }

    // Method to be called from JavaScript
    public void openDialog() {
        dialog.open();
    }

    public void closeDialog() {
        sign.setSrc("");
        signature.clear();
        dialog.close();
    }

    public void saveSignature() {
        sign.setSrc(signature.getImageURI());
    }

    public void undoSignature() {
        signature.undo();
    }

//    public void registerButtonEvents() {
//        getElement().addEventListener("invoke-server-method", event -> {
//            String methodName = event.getEventData().getString("methodName");
//            if (methodName.equals("dilogOpen")) {
//                dilogOpen();
//            }
//            // Add more conditions for other server-side methods if needed
//        });
//    }
//    public Registration addCustomEventListener(ComponentEventListener<ClickEvent<Div>> listener) {
//        return addListener(ClickEvent.class, (ComponentEventListener) listener);
//    }
}
