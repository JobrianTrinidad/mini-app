package com.aat.application.views;


import com.aat.application.data.entity.*;
import com.vaadin.flow.component.html.Div;
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
public class MainLayout extends Div {
    CoreMainLayout coreMainLayout;
    public MainLayout() {
        setClassName("MainLayout");
        coreMainLayout = new CoreMainLayout();
        AppNav nav = getNavigation();
        coreMainLayout.setNavigation(nav);
        add(coreMainLayout);
    }

    public AppNav getNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();
        AppNavItem parent = new AppNavItem("Mini-Core");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Tui Grid", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                        .withParameter(StandardFormView.class, "entityClass", ZJTPricingType.class.getName())
                        .withParameter(StandardFormView.class, "layout", this.getClass().getName()));
//        parent.addItem(new AppNavItem("Trip Element", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter(StandardFormView.class, "entityClass", ZJTElement.class.getSimpleName()));
//        parent.addItem(new AppNavItem("Resource Category", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter(StandardFormView.class, "entityClass", ZJTResourceCategory.class.getSimpleName()));
//        parent.addItem(new AppNavItem("Resource Type", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter(StandardFormView.class, "entityClass", ZJTResourceType.class.getSimpleName()));
        parent.addItem(new AppNavItem("Group TimeLine", TimeLineFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(TimeLineFormView.class, "entityClass", ZJTTimeLineItem.class.getName())
                .withParameter(TimeLineFormView.class, "layout", this.getClass().getName()));
        return nav;
    }
}

