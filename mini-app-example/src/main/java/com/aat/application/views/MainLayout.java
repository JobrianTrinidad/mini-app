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
        parent.addItem(new AppNavItem("Tui Grid", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(StandardFormView.class, "entityClass", ZJTPricingType.class.getName())
                .withParameter(StandardFormView.class, "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Group TimeLine", GroupTimeLineView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(GroupTimeLineView.class, "entityClass", ZJTTimeLineItem.class.getName())
                .withParameter(GroupTimeLineView.class, "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Vehicle TimeLine", VehicleView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(VehicleView.class, "entityClass", ZJTVehicleBooking.class.getName())
                .withParameter(VehicleView.class, "layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Driver TimeLine", DriverView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(DriverView.class, "entityClass", ZJTVehicleBooking.class.getName())
                .withParameter(DriverView.class, "layout", this.getClass().getName()));
        return nav;
    }
}

