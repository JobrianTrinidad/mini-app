package com.aat.application.views;


import com.aat.application.components.appnav.AppNav;
import com.aat.application.core.event.EventBus;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
//@Route(value = "")
@CssImport("./styles/styles.css")
public class CoreMainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    private H2 viewTitle;
    protected AppNav nav;
    private Div content;

    public CoreMainLayout() {
        setPrimarySection(Section.DRAWER);
        addHeaderContent();

    }

    protected AppNav getNavigation() {
        return nav;
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClickListener(event -> {
            EventBus.getInstance().post("DrawerToggleClicked");
        });
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        content = new Div();
        content.setId("content");
        addToNavbar(true, toggle, viewTitle);

        setContent(content);
    }

    private void addDrawerContent(String strAppName) {
        H1 appName = new H1(strAppName);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    protected AppNav createNavigation() {
        return nav;
    }

    protected void setNavigation(AppNav nav, String strAppName) {
        this.nav = nav;
        addDrawerContent(strAppName);
    }

    protected Footer createFooter() {
        return new Footer();
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

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        UI.getCurrent().getPage().executeJs(
                "var contentElement = document.querySelector('[content]');" +
                        "contentElement.style.overflow = 'hidden';");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

    }
}