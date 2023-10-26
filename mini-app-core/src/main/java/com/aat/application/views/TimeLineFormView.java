package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.form.TimeLineCommonForm;
import com.aat.application.util.GlobalData;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;

//@Route(value = "timeline")
public class TimeLineFormView<T extends ZJTEntity> extends CommonView<T> {

    protected TimeLineCommonForm<T> form;
    private String groupName = "group";
    Class<? extends ZJTEntity> groupClass = null;

    public TimeLineFormView(BaseEntityRepository<T> repository) {
        super(repository);
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupClass(Class<? extends ZJTEntity> groupClass) {
        this.groupClass = groupClass;
    }

    private void configureForm() {
        form = new TimeLineCommonForm<>(entityClass, new BaseEntityService<>(repository), groupName, groupClass);
        setForm(form);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);

        if (entityClass != null) {
            configureForm();
        }
    }
}