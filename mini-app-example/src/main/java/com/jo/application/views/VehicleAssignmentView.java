package com.jo.application.views;

import com.jo.application.core.form.EnumDateFilter;
import com.jo.application.core.form.TimeLineViewParameter;
import com.jo.application.data.entity.*;
import com.jo.application.data.event.CustomContextFormEventHandler;
import com.jo.application.data.event.CustomItemContextMenuEventHandler;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.TableInfoService;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Route(value = "vehicle-assignment/:subcategory?/:filter?", layout = MainLayout.class)
public class VehicleAssignmentView extends StandardFormView implements HasUrlParameter<String> {

    TimeLineViewParameter timeLineViewParameter;
    CustomContextFormEventHandler eventHandler;
    private MultiSelectComboBox<ZJTDepot> depotComboBox = new MultiSelectComboBox<ZJTDepot>();

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
        timeLineViewParameter.setItemBorderClassField("bordercolor");
        timeLineViewParameter.setItemEditableField("isFinal");
        timeLineViewParameter.setEditableFlagInverted(true);
        timeLineViewParameter.setGroupSelectDefinition("fleetid");
        timeLineViewParameter.setGroupWhereDefinitions(new String[]{"p.depot.zjt_depot_id = 1000003 OR p.depot.zjt_depot_id"});
        timeLineViewParameter.setParameters(new Object[]{0});
        timeLineViewParameter.setGroupParameters(new Object[]{0});
        timeLineViewParameter.setWhereDefinitions(new String[]{"p.vehicle.zjt_vehicle_id = 1000003 OR p.depot.zjt_depot_id"});
        timeLineViewParameter.setStack(true);
        timeLineViewParameter.setSubgroupIDFieldName("colorcode");
//        timeLineViewParameter.setGroupCSSClass("classname");
        // css class is set to this field
        timeLineViewParameter.setGroupCSSClass("fuelcardname1");
// TODO: Set the group table ID for enabling the zoom option on the timeline., This ID will be used to create zoom url with This ID as table and selected group id as id.
        this.timeLineViewParameter.setGroupZoomTableID(539);
// TODO: Retrieve the HTML content and styles from the database for rendering dynamic forms for item
        ZJTTableInfo tableInfo = tableInfoService.findByTableName(this.timeLineViewParameter.getFromDefinition());
        eventHandler = new CustomContextFormEventHandler(tableInfo.getHtmlContent(), tableInfo.getHtmlStyle(), tableInfo.isSubmitForm());
        timeLineViewParameter.setContextFormEventHandler(eventHandler);
// TODO Extend the context menu by adding a custom event handler for additional options.
        this.timeLineViewParameter.setItemContextMenuEventHandler(new CustomItemContextMenuEventHandler());

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
    public void initLayout() {
        super.initLayout();
        //        Button button = new Button();
//        button.setIcon(new Icon(VaadinIcon.START_COG));
//        button.setTooltipText("Filter by Depot");
//        button.addClickListener(e -> filterByDepot());
//        this.addCustomComponent(button);
        //TODO allowed ids coming from depot - user roles
        int[] allowedDepot = {1000002, 1000003};
        List<ZJTDepot> depots = (List<ZJTDepot>) this.repository.findEntitiesByIds(ZJTDepot.class, allowedDepot);

        depotComboBox.setWidth("300px");
        depotComboBox.setItems(depots);
        depotComboBox.setItemLabelGenerator(ZJTDepot::getName);

        depotComboBox.addValueChangeListener( e-> {
            try {
                filterByDepot();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        if (!depots.isEmpty()) {
            depotComboBox.setValue(depots.get(0));
        }
        this.addCustomComponent(0, depotComboBox);

        try {
            form.updateDateFilter(EnumDateFilter.TD3);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void filterByDepot() throws Exception {
        Object[] depotID = {(depotComboBox.getValue() != null ? (depotComboBox.getValue().stream().filter(Objects::nonNull).map(ZJTDepot::getId).collect(Collectors.toList())) : 0) };
        timeLineViewParameter.setParameters(depotID);
        timeLineViewParameter.setGroupParameters(depotID);
        form.onUpdateForm();
    }

    @Override
    public void onTimelineItemUpdate(ZJTItem item, boolean isMultipleSelection) {
        //Update ResourceAssignment here
        //TODO findEntityById seems not working
        ZJTVehicleAssignment assignment = (ZJTVehicleAssignment) this.repository.findEntityById(ZJTVehicleAssignment.class, item.getId());
        ZJTVehicle vehicle = (ZJTVehicle) this.repository.findEntityById(ZJTVehicle.class, Integer.parseInt(item.getGroupId()));
        if (!isMultipleSelection) {
            assignment.setStartDate(item.getStartTime());
            assignment.setEndDate(item.getEndTime());
        }
//        List<ZJTVehicleAssignment> assignments = (List<ZJTVehicleAssignment>) this.repository.findEntitiesByIds(ZJTVehicleAssignment.class, new int[]{item.getId()});
//        List<ZJTVehicle> vehicles = (List<ZJTVehicle>) this.repository.findEntitiesByIds(ZJTVehicle.class, new int[]{ Integer.parseInt(item.getGroupId())});
//        ZJTVehicleAssignment assignment = assignments.get(0);
//        ZJTVehicle vehicle = vehicles.get(0);
        assignment.setVehicle(vehicle);
        this.repository.updateEntity(assignment);

        Notification.show(assignment.getDescription() + " has been assigned to " + vehicle.getFleetid());
    }
}
