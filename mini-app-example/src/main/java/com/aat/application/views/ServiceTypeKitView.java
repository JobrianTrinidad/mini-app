package com.aat.application.views;

import com.aat.application.core.form.GridViewParameter;
import com.aat.application.data.entity.ZJTServiceTypeKit;
import com.aat.application.data.entity.ZJTVehicleServiceType;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "service-type-kit/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceTypeKitView extends StandardFormView implements HasUrlParameter<String> {

    GridViewParameter gridViewParameter;
    public ServiceTypeKitView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        gridViewParameter = new GridViewParameter(ZJTServiceTypeKit.class, "");
        super.setGridViewParameter(gridViewParameter);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter String parameter) {
    }

}
