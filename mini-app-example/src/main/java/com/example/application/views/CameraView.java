package com.example.application.views;

import com.example.application.data.entity.ADImage;
import com.example.application.component.Camera;
import com.jo.application.core.form.CustCompViewParameter;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.jo.application.views.StandardFormView;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.*;

@Route(value = "camera/:subcategory?/:filter?", layout = MainLayout.class)
public class CameraView extends StandardFormView implements HasUrlParameter<String> {
    private String subcategory;
    private String filter;

    public CameraView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        Camera camera = new Camera();
        CustCompViewParameter custCompViewParameter = new CustCompViewParameter(ADImage.class, "", camera);
        super.custCompViewParameter(custCompViewParameter);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {
            String[] parts = parameter.split("/");
            if (parts.length >= 1) {
                subcategory = parts[0];
            }
            if (parts.length >= 2) {
                filter = parts[1];
            }
        }
        // Use subcategory and filter as needed in your view
        // For example:
        // updateContent(subcategory, filter);
    }

    // You can define additional methods to update the content of your view based on the parameters
    // private void updateContent(String subcategory, String filter) {}
}
