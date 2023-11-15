package com.aat.application.views;

import com.aat.application.data.entity.*;
import com.vaadin.flow.component.dependency.CssImport;
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
@CssImport(value = "./styles/timeline-items-style.css")
public class MainLayout extends CoreMainLayout {
    public MainLayout() {
        super();
        setNavigation(getNavigation(), "Mini-App");
    }

    @Override
    protected AppNav getNavigation() {
        nav = new AppNav();
        AppNavItem parent = new AppNavItem("Mini-Core");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Tui Grid", "pricing-type", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTPricingType.class.getName())
                .withParameter( "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Type", "servicetype", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTVehicleServiceType.class.getName())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Schedule", "serviceschedule", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("entityClass", ZJTVehicleServiceSchedule.class.getName())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Group TimeLine", "group-timeline", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter( "entityClass", ZJTTimeLineItem.class.getName())
                .withParameter( "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Vehicle TimeLine", "vehicle-timeline", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter( "entityClass", ZJTVehicleBooking.class.getName())
                .withParameter( "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Driver TimeLine", "driver-timeline", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter( "entityClass", ZJTVehicleBooking.class.getName())
                .withParameter( "layout", this.getClass().getName()));
        return nav;
    }
}

