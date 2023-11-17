package com.aat.application.views;

import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.Route;

@Route(value="vehicle")
public class VehicleView extends  StandardFormView<ZJTVehicle> {

    public VehicleView(BaseEntityRepository<ZJTVehicle> repository, TableInfoService tableInfoService){
        super(repository, tableInfoService);

    }

//    Override
//    void initWindow()
//    {
//        .getColumn("column1").setHeaderName("Column 1").setWidth("200px");
//        .getColumn("column2").setVisible(false);
//        .getColumn("column3").setReadonly(true);
//    }

}
