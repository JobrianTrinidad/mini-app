package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = "service-kit/:subcategory?/:filter?", layout = MainLayout.class)
public class ServiceKitView extends StandardFormView implements HasUrlParameter<String> {

    public ServiceKitView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    }


}
