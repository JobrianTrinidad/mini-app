package com.aat.application.views;

import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.entity.ZJTVehicle;
import com.aat.application.data.entity.ZJTVehicleBooking;
import com.aat.application.data.repository.BaseEntityRepository;
import com.vaadin.flow.router.Route;

@Route(value = "pricing-type")
public class PricingTypeView extends StandardFormView<ZJTPricingType> {
    public PricingTypeView(BaseEntityRepository<ZJTPricingType> repository) {
        super(repository);
    }
}