package com.aat.application.views;

import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.entity.ZJTVehicleServiceType;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.Route;

@Route(value="servicetype")
public class ServiceTypeView extends  StandardFormView<ZJTVehicleServiceType> {

    public ServiceTypeView(BaseEntityRepository<ZJTVehicleServiceType> repository, TableInfoService tableInfoService){
        super(repository, tableInfoService);

    }
}
