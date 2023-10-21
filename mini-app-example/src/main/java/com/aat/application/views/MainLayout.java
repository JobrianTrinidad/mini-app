package com.aat.application.views;


import com.aat.application.data.entity.*;
import org.vaadin.lineawesome.LineAwesomeIcon;

import com.aat.application.components.appnav.AppNav;
import com.aat.application.components.appnav.AppNavItem;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
@Route(value = "")
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("JTT Scheduler");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        AppNav nav = new AppNav();
        AppNavItem parent = new AppNavItem("Mini-Core");
        parent.setIcon(LineAwesomeIcon.FOLDER.create());
        nav.addItem(parent);
        parent.addItem(new AppNavItem("Tui Grid", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(StandardFormView.class, "entityClass", ZJTPricingType.class.getSimpleName()));
//        parent.addItem(new AppNavItem("Trip Element", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter(StandardFormView.class, "entityClass", ZJTElement.class.getSimpleName()));
//        parent.addItem(new AppNavItem("Resource Category", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter(StandardFormView.class, "entityClass", ZJTResourceCategory.class.getSimpleName()));
//        parent.addItem(new AppNavItem("Resource Type", StandardFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
//                .withParameter(StandardFormView.class, "entityClass", ZJTResourceType.class.getSimpleName()));
        parent.addItem(new AppNavItem("Group TimeLine", TimeLineFormView.class, LineAwesomeIcon.PRODUCT_HUNT.create())
                .withParameter(TimeLineFormView.class, "entityClass", ZJTTimeLineItem.class.getSimpleName()));
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
