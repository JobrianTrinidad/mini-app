package com.aat.application.views;

import com.aat.application.component.Signature;
import com.aat.application.core.form.CustCompViewParameter;
import com.aat.application.data.entity.AdImage;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "signature-ap/:subcategory?/:filter?", layout = MainLayout.class)
public class SignatureView extends StandardFormView implements HasUrlParameter<String> {
    private String subcategory;
    private String filter;

    public SignatureView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        Signature signature = new Signature();
        CustCompViewParameter custCompViewParameter = new CustCompViewParameter(AdImage.class, "", signature);
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
