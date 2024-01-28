package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.CommonForm;
import com.aat.application.core.form.GridViewParameter;
import com.aat.application.core.form.StandardForm;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.data.service.TableInfoService;
import com.aat.application.form.GridCommonForm;
import com.aat.application.form.TimeLineCommonForm;
import com.vaadin.componentfactory.tuigrid.event.ItemAddEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemChangeEvent;
import com.vaadin.componentfactory.tuigrid.event.ItemDeleteEvent;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.BeforeEnterEvent;

import java.util.Optional;
import java.util.function.Consumer;

//@Route(value = "common-view/:name", layout = CoreMainLayout.class)
@SuppressWarnings("ALL")
public class StandardFormView<T extends ZJTEntity> extends CommonView<T> {

    protected CommonForm<T> form;
    private final TableInfoService tableInfoService;
    private GridViewParameter gridViewParameter;
    private TimeLineViewParameter timeLineViewParameter;
    protected AATContextMenu contextMenu;
    private String name;
    private boolean bGrid = true;

    public StandardFormView(BaseEntityRepository<T> repository,
                            TableInfoService tableInfoService) {
        super(repository);
        this.tableInfoService = tableInfoService;
    }

    public void setTimeLineViewParameter(TimeLineViewParameter timeLineViewParameter) {
        this.timeLineViewParameter = timeLineViewParameter;
        this.timeLineViewParameter.setParameters(new Integer[]{this.filterObjectId});
    }

    public void setGridViewParameter(GridViewParameter gridViewParameter) {
        this.gridViewParameter = gridViewParameter;
    }

    private void configureForm(Optional<String> filter) {
        String strFilter = !filter.equals(Optional.empty()) ? filter.get() : "";

        switch (strFilter) {
            case "timeline":
                bGrid = false;
                form = new TimeLineCommonForm<>( this.timeLineViewParameter, new BaseEntityService<>(repository));
                break;
            case "grid":
                bGrid = true;
                gridViewParameter.setParameters(new Integer[]{filterObjectId});
                form = new GridCommonForm<>(gridViewParameter, new BaseEntityService<>(repository), tableInfoService);
                break;
            default:
                bGrid = true;
                if (gridViewParameter.getGroupClass() != null) {
                    gridViewParameter.setEntityClass(gridViewParameter.getGroupClass());
                } else
                    gridViewParameter.setGroupClass(gridViewParameter.getEntityClass());
                gridViewParameter.setWhereDefinition(null);
                form = new GridCommonForm<>(gridViewParameter, new BaseEntityService<>(repository), tableInfoService);
                if (this.contextMenu != null) {
                    ((StandardForm) form).setContextMenu(this.contextMenu);
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
            ((StandardForm) form).setMessageStatus(msg);
        }
    }

    public void addCustomButton(Button button) {
        if (form != null && form instanceof StandardForm) {
            ((StandardForm) form).addCustomButton(button);
        }
    }

    protected void onAddEvent(Consumer<ItemAddEvent> eventHandler) {
        if (bGrid) {
            ((GridCommonForm) this.form).grid.addItemAddListener(eventHandler::accept);
        }
    }

    protected void onUpdateEvent(Consumer<ItemChangeEvent> eventHandler) {
        if (bGrid) {
            ((GridCommonForm) this.form).grid.addItemChangeListener(eventHandler::accept);
        }
    }

    protected void onDeleteEvent(Consumer<ItemDeleteEvent> eventHandler) {
        if (bGrid) {
            ((GridCommonForm) this.form).grid.addItemDeleteListener(eventHandler::accept);
        }
    }

    public boolean isbGrid() {
        return bGrid;
    }
}