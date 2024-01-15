package com.aat.application.views;

import com.aat.application.data.entity.*;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.aat.application.components.appnav.AppNav;
import com.aat.application.components.appnav.AppNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
@Route(value = "")
//@CssImport(value = "./styles/timeline-items-style.css")
public class MainLayout extends CoreMainLayout {
    public MainLayout() {
        super();
        setNavigation(getNavigation(), "AAT Apps");
    }

    @Override
    protected AppNav getNavigation() {
        nav = new AppNav("Vehicle Maintenance");
        AppNavItem parent = new AppNavItem("Data");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Pricing Type", "pricing-type", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTPricingType.class.getName())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Type", "service-type", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTVehicleServiceType.class.getName())
                .withParameter("layout", this.getClass().getName()));

        parent.addItem(new AppNavItem("Vehicle", "vehicle", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTVehicle.class.getName())
                .withParameter("layout", this.getClass().getName()));

        parent.addItem(new AppNavItem("Vehicle Part", "vehicle-parts", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTVehiclePart.class.getName())
                .withParameter("layout", this.getClass().getName()));

        parent = new AppNavItem("Transaction");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        parent.addItem(new AppNavItem("Service Schedule", "service-schedule", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTVehicleServiceSchedule.class.getName())
                .withParameter("layout", this.getClass().getName()));

        nav.addItem(parent);
        return nav;
    }
}