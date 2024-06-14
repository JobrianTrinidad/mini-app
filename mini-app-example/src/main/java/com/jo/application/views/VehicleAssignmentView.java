package com.jo.application.views;

import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.entity.ZJTDepot;
import com.jo.application.data.entity.ZJTItem;
import com.jo.application.data.entity.ZJTVehicle;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;

import java.util.List;

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
        timeLineViewParameter.setIdFieldName("zjt_vehicleassignment_id");
        timeLineViewParameter.setFromDefinition("ZJTVehicleAssignment");
        timeLineViewParameter.setGroupClass(ZJTVehicle.class);
        timeLineViewParameter.setDateFilterOn("startDate");
        timeLineViewParameter.setSelectDefinition("fleetid");
        timeLineViewParameter.setGroupSelectDefinition("fleetid");
//        timeLineViewParameter.setGroupCSSClass("classname");
        // css class is set to this field
        timeLineViewParameter.setGroupCSSClass("fuelcardname1");
        timeLineViewParameter.setShowItemSelector(true);  //to test this feature
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
//        Button button = new Button();
//        button.setIcon(new Icon(VaadinIcon.START_COG));
//        button.setTooltipText("Filter by Depot");
//        button.addClickListener(e -> filterByDepot());
//        this.addCustomComponent(button);

        //TODO allowed ids coming from depot - user roles
        int[] allowedDepot = {1000003, 1000002};
        List<ZJTDepot> depots = (List<ZJTDepot>) this.repository.findEntitiesByIds(ZJTDepot.class, allowedDepot);

        ComboBox<ZJTDepot> depotComboBox = new ComboBox<>();
        depotComboBox.setWidth("300px");
        depotComboBox.setItems(depots);
        depotComboBox.setItemLabelGenerator(ZJTDepot::getName);
        if (!depots.isEmpty()) {
            depotComboBox.setValue(depots.get(0));
        }
        this.addCustomComponent(0, depotComboBox);

        //TODO - add itemUpdated event listener to save entity
        //form.addl
    }

    private void filterByDepot()
    {

    }
}
