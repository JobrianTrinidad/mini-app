package com.aat.application.views;

import com.aat.application.core.data.entity.ZJTEntity;
import com.aat.application.data.repository.BaseEntityRepository;
import com.aat.application.data.service.TableInfoService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.*;

@Route(value = "vehiclekmreading/:subcategory?/:filter?", layout = MainLayout.class)
public class VehicleKMReadingView extends StandardFormView<ZJTEntity> implements HasUrlParameter<String> {

    public VehicleKMReadingView(BaseEntityRepository<ZJTEntity> repository, TableInfoService tableInfoService) {
        super(repository, tableInfoService);


    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        super.beforeEnter(event);

        this.setMessageStatus("This is a KM reading message status");

        Button button = new Button();
        button.setIcon(new Icon(VaadinIcon.INBOX));
        button.setTooltipText("Import Tele");

        button.addClickListener(e -> importTele());

        this.addCustomButton(button);
    }

    private void importTele()
    {
        this.setMessageStatus("Import tele successful!!!");
    }
}
