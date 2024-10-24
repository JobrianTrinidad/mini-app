package com.jo.application.views;

import com.jo.application.core.form.*;
import com.jo.application.data.repository.BaseEntityRepository;
import com.jo.application.data.service.BaseEntityService;
import com.jo.application.data.service.TableInfoService;
import com.jo.application.form.CustomCompCommonForm;
import com.jo.application.form.GridCommonForm;
import com.jo.application.form.TimeLineCommonForm;
import com.vaadin.componentfactory.tuigrid.event.ItemAddEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemChangeEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemDeleteEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemMultiSelectEvent;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.BeforeEnterEvent;

import java.util.Optional;
import java.util.function.Consumer;

public class StandardFormView extends CommonView {

    protected CommonForm form;
    private final TableInfoService tableInfoService;
    private GridViewParameter gridViewParameter;

    private CustCompViewParameter custCompViewParameter;
    private TimeLineViewParameter timeLineViewParameter;
    protected AATContextMenu contextMenu;
    private String name;
    private boolean bGrid = true;

    public StandardFormView(BaseEntityRepository repository,
                            TableInfoService tableInfoService) {
        super(repository);
        this.tableInfoService = tableInfoService;
    }

    public void setTimeLineViewParameter(TimeLineViewParameter timeLineViewParameter) {
        this.timeLineViewParameter = timeLineViewParameter;
    }

    public void setGridViewParameter(GridViewParameter gridViewParameter) {
        this.gridViewParameter = gridViewParameter;
    }

    private void configureForm(Optional<String> filter) {
        String strFilter = filter.orElse("");
        Integer[] filterObjectIds;
        if (filterObjectId != null && !filterObjectId.isEmpty()) {
            String[] parts = filterObjectId.split("\\.");
            filterObjectIds = new Integer[parts.length];
            for (int i = 0; i < parts.length; i++) {
                filterObjectIds[i] = Integer.parseInt(parts[i]);
            }
        } else {
            filterObjectIds = new Integer[]{-1};
        }

        switch (strFilter) {
            case "timeline":
                bGrid = false;
                this.timeLineViewParameter.setParameters(filterObjectIds);
                form = new TimeLineCommonForm(this.timeLineViewParameter, new BaseEntityService<>(repository), tableInfoService);
                this.initLayout();
                break;
            case "grid":
                bGrid = true;
                gridViewParameter.setParameters(filterObjectIds);
                form = new GridCommonForm<>(gridViewParameter, new BaseEntityService<>(repository), tableInfoService);
                break;
            case "component":
                bGrid = false;
                form = new CustomCompCommonForm<>(custCompViewParameter, new BaseEntityService<>(repository));
                break;
            default:
                if (custCompViewParameter != null){
                    bGrid = false;
                    form = new CustomCompCommonForm<>(custCompViewParameter, new BaseEntityService<>(repository));
                }
                else if (gridViewParameter != null) {
                    bGrid = true;
                    gridViewParameter.setParameters(filterObjectIds);
                    if (gridViewParameter.getGroupClass() != null) {
                        gridViewParameter.setEntityClass(gridViewParameter.getGroupClass());
                    } else
                        gridViewParameter.setGroupClass(gridViewParameter.getEntityClass());
                    gridViewParameter.setWhereDefinition(null);
                    form = new GridCommonForm<>(gridViewParameter, new BaseEntityService<>(repository), tableInfoService);
                    if (this.contextMenu != null) {
                        form.setContextMenu(this.contextMenu);
                    }
                } else {
                    bGrid = false;
                    form = new TimeLineCommonForm(this.timeLineViewParameter, new BaseEntityService<>(repository), tableInfoService);
                    this.initLayout();
                }
                break;
        }
        setForm(form);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        configureForm(event.getRouteParameters().get("filter"));
    }

    protected void setContextMenu(AATContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public void setMessageStatus(String msg) {
        if (form != null && form instanceof StandardForm) {
            form.setMessageStatus(msg);
        }
    }

    public void addCustomComponent(Component component) {
        if (form != null) {
            form.addCustomComponent(-1, component);
        }
    }
    public void addCustomComponent(int index, Component component) {
        if (form != null) {
            form.addCustomComponent(index, component);
        }
    }

    protected void onItemMultiSelectEvent(Consumer<ItemMultiSelectEvent> eventHandler) {
        if (bGrid) {
            form.grid.addItemMultiSelectListener(eventHandler::accept);
        }
    }

    protected void onAddEvent(Consumer<ItemAddEvent> eventHandler) {
        if (bGrid) {
            form.grid.addItemAddListener(eventHandler::accept);
        }
    }

    protected void onUpdateEvent(Consumer<ItemChangeEvent> eventHandler) {
        if (bGrid) {
            form.grid.addItemChangeListener(eventHandler::accept);
        }
    }

    protected void onDeleteEvent(Consumer<ItemDeleteEvent> eventHandler) {
        if (bGrid) {
            form.grid.addItemDeleteListener(eventHandler::accept);
        }
    }

    public boolean isbGrid() {
        return bGrid;
    }

    protected void custCompViewParameter(CustCompViewParameter custCompViewParameter) {
        this.custCompViewParameter = custCompViewParameter;
    }
}