package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.BaseEntityService;
import com.aat.application.data.service.TableInfoService;
import com.aat.application.form.GridCommonForm;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.flow.router.BeforeEnterEvent;

import java.lang.reflect.Field;

//@Route(value = "common-view/:name", layout = CoreMainLayout.class)
public class StandardFormView<T extends ZJTEntity> extends CommonView<T> {

    protected GridCommonForm<T> form;
    private final TableInfoService tableInfoService;
    protected AATContextMenu contextMenu;
    private String name;

    public StandardFormView(BaseEntityRepository<T> repository, TableInfoService tableInfoService) {
        super(repository);
        this.tableInfoService = tableInfoService;
    }

    private void configureForm() {
        form = new GridCommonForm<>(entityClass, new BaseEntityService<>(repository), tableInfoService, groupName, filterObjectId);
        if (this.contextMenu != null)
            form.setContextMenu(this.contextMenu);
//        if (this.filterObjectId != -1) {
////            String[] arrayFilterTemp = this.filterTemp.split("-");
////            if (arrayFilterTemp.length > 1) {
//            String fieldName = "";
//            for (Field field :
//                    entityClass.getDeclaredFields()) {
//                if (field.getType().getName().equals(filteredEntityClass.getTypeName())) {
////                        fieldName = field.getName() + "." + arrayFilterTemp[0];
//                    form.setFilter(filteredEntityClass, field.getName(), this.filterTemp);
//                    break;
//                }
//            }
////            }
//        }
        setForm(form);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);
        if (entityClass != null)
            configureForm();
    }

    protected void setContextMenu(AATContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

}