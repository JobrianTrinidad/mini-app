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
public class MainLayout extends CoreMainLayout {
    public MainLayout() {
        super();
        setNavigation(getNavigation());
    }

    @Override
    protected AppNav getNavigation() {
        nav = new AppNav();
        AppNavItem parent = new AppNavItem("Mini-Core");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Tui Grid", "commonview", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("commonview", "entityClass", ZJTPricingType.class.getName())
                .withParameter("commonview", "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Group TimeLine", "group-timeline", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("group-timeline", "entityClass", ZJTTimeLineItem.class.getName())
                .withParameter("group-timeline", "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Vehicle TimeLine", "vehicle-timeline", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("vehicle-timeline", "entityClass", ZJTVehicleBooking.class.getName())
                .withParameter("vehicle-timeline", "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Driver TimeLine", "driver-timeline", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("driver-timeline", "entityClass", ZJTVehicleBooking.class.getName())
                .withParameter("driver-timeline", "layout", this.getClass().getName()));
        return nav;
    }
}

