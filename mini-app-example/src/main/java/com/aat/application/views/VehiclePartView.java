package com.aat.application.views;

import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.entity.ZJTVehiclePart;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.Route;

@Route(value="vehicle-parts")
public class VehiclePartView extends  StandardFormView<ZJTVehiclePart> {

    public VehiclePartView(BaseEntityRepository<ZJTVehiclePart> repository, TableInfoService tableInfoService){
        super(repository, tableInfoService);

    }


}
