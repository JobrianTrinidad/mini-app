package com.aat.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import elemental.json.Json;
import elemental.json.JsonArray;

//@Route(value = "test")
@Tag("div")
@JsModule("./js/my-component.js") // Adjust the path accordingly
public class MyCustomComponent extends Div {

    private boolean componentAdded = false;
    public MyCustomComponent() {
        // Create a Div element to hold your custom JavaScript component
        Div container = new Div();
        container.setId("myComponentContainer");

        add(container);
    }

    public void addItems(JsonArray itemsArray) {
        // Check if the component has already been added
        if (!componentAdded) {
            // Create a list of items to pass to the JavaScript component
//            JsonArray itemsArray = Json.createArray();
//            itemsArray.set(0, "Item 1");
//            itemsArray.set(1, "Item 2");
//            itemsArray.set(2, "Item 3");
            // Add more items as needed

            // Convert the JsonArray to a JSON string
            String itemsJson = itemsArray.toJson();

            System.out.println("This is call...");
            // Execute JavaScript code to create the custom component with the list of items
            getElement().executeJs("const myComponent = document.createElement('my-component');" +
                    "myComponent.items = " + itemsJson + ";" + // Pass the items array directly
                    "document.getElementById('myComponentContainer').appendChild(myComponent);");

            // Set the flag to indicate that the component has been added
            componentAdded = true;
        }
    }
}