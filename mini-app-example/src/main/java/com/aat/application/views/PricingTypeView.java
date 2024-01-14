package com.aat.application.views;

import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.entity.ZJTPricingType;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "pricing-type")
public class PricingTypeView extends StandardFormView<ZJTPricingType> {
    @Autowired
    public PricingTypeView(BaseEntityRepository<ZJTPricingType> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService, new TimeLineViewParameter("timelineItemTitle", "", "planDate", null, null, "ZJTVehicleServiceSchedule"));
    }
}