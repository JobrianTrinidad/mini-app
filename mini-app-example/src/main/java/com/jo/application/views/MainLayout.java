package com.jo.application.views;

import com.jo.application.data.entity.*;
import com.jo.application.views.CoreMainLayout;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.jo.application.components.appnav.AppNav;
import com.jo.application.components.appnav.AppNavItem;
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
        parent.addItem(new AppNavItem("User", "user", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Kit", "service-kit", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Type", "service-type", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Type kit", "service-type-kit", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Type Task", "service-type-task", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Job", "service-job", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Job & Service Type", "servicejob-servicetype", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Job Task", "service-job-task", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Job & Service Kit", "servicejob-servicekit", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));

//        parent.addItem(new AppNavItem("Service Type Kit", "service-type-kit", LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter("entityClass", ZJTServiceTypeKit.class.getName())
//                .withParameter("layout", this.getClass().getName()));

        parent.addItem(new AppNavItem("Vehicle", "vehicle", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));


        parent = new AppNavItem("Timeline Test");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        parent.addItem(new AppNavItem("Vehicle Assignment", "vehicle-assignment", LineAwesomeIcon.BUS_SOLID.create())
                .withParameter("layout", this.getClass().getName()));

        nav.addItem(parent);

        parent = new AppNavItem("Transaction");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        parent.addItem(new AppNavItem("Vehicle KM Reading", "vehiclekmreading", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Schedule", "service-schedule", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));
        parent.addItem(new AppNavItem("Service Schedule Overview", "service-schedule-overview", LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter("layout", this.getClass().getName()));


        nav.addItem(parent);

//        parent = new AppNavItem("Custom Component");
//        parent.setIcon(LineAwesomeIcon.FOLDER.create());
//        nav.addItem(parent);
//        parent.addItem(new AppNavItem("mobile  camera", "camera", LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter("layout", this.getClass().getName()));
//        parent.addItem(new AppNavItem("mobile  signature", "signature", LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter("layout", this.getClass().getName()));
        return nav;
    }
}