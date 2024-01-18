package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.core.form.CommonForm;
import com.aat.application.core.form.StandardForm;
import com.aat.application.core.form.TimeLineViewParameter;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.data.service.TableInfoService;
import com.aat.application.form.GridCommonForm;
import com.aat.application.form.TimeLineCommonForm;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.BeforeEnterEvent;

import java.util.Optional;

//@Route(value = "common-view/:name", layout = CoreMainLayout.class)
@SuppressWarnings("ALL")
public class StandardFormView<T extends ZJTEntity> extends CommonView<T> {

    protected CommonForm<T> form;
    private final TableInfoService tableInfoService;
    private TimeLineViewParameter timeLineViewParameter;
    protected AATContextMenu contextMenu;
    private String name;

    public StandardFormView(BaseEntityRepository<T> repository,
                            TableInfoService tableInfoService) {
        super(repository);
        this.tableInfoService = tableInfoService;
        this.timeLineViewParameter = timeLineViewParameter;
    }

    public void setTimeLineViewParameter(TimeLineViewParameter timeLineViewParameter){
        this.timeLineViewParameter = timeLineViewParameter;
    }

    private void configureForm(Optional<String> filter) {
        String strFilter = !filter.equals(Optional.empty()) ? filter.get() : "";

        switch (strFilter) {
            case "timeline":
                form = new TimeLineCommonForm<>(entityClass, this.timeLineViewParameter, filteredEntityClass, new BaseEntityService<>(repository), groupName, filterObjectId);
                break;
            case "grid":
            default:
                form = new GridCommonForm<>(entityClass, filteredEntityClass, new BaseEntityService<>(repository), tableInfoService, groupName, filterObjectId);
                if (this.contextMenu != null)
                    ((StandardForm) form).setContextMenu(this.contextMenu);
                break;
        }
        setForm(form);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        if (entityClass != null)
            configureForm(event.getRouteParameters().get("filter"));
    }

    protected void setContextMenu(AATContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public void setMessageStatus(String msg)
    {
        if (form != null && form instanceof StandardForm) {
            ((StandardForm) form).setMessageStatus(msg);
        }
    }

    public void addCustomButton(Button button)
    {
        if (form != null && form instanceof StandardForm) {
            ((StandardForm) form).addCustomButton(button);
        }
    }

}