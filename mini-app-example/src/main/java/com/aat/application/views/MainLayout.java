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
        parent.addItem(new AppNavItem("Group TimeLine", TimeLineFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(TimeLineFormView.class, "entityClass", ZJTTimeLineItem.class.getName())
                .withParameter(TimeLineFormView.class, "layout", this.getClass().getName()));
        return nav;
    }
}

