package com.jo.application.views;

import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.entity.ZJTVehicle;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.*;

@Route(value = "vehicle-assignment/:subcategory?/:filter?", layout = MainLayout.class)
public class VehicleAssignmentView extends StandardFormView implements HasUrlParameter<String> {

    TimeLineViewParameter timeLineViewParameter;
    public VehicleAssignmentView(BaseEntityRepository repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);
        timeLineViewParameter = new TimeLineViewParameter(new String[]{"description"}
                , "vehicle"
                , new String[]{"startDate"}
                , new String[]{"endDate"}
                , "colorcode"
                , null);
        timeLineViewParameter.setFromDefinition("ZJTVehicleAssignment");
        timeLineViewParameter.setGroupClass(ZJTVehicle.class);
        timeLineViewParameter.setDateFilterOn("startDate");
        timeLineViewParameter.setSelectDefinition("fleetid");
        timeLineViewParameter.setGroupSelectDefinition("fleetid");
//        timeLineViewParameter.setGroupCSSClass("classname");
        // css class is set to this field
        timeLineViewParameter.setGroupCSSClass("fuelcardname1");
        super.setTimeLineViewParameter(timeLineViewParameter);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null) {

        } else {

        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.START_COG));
        button.setTooltipText("Filter by Depot");
        button.addClickListener(e -> filterByDepot());
        this.addCustomButton(button);
    }

    private void filterByDepot()
    {

    }
}
