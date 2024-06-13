package com.jo.application.views;


import com.jo.application.components.appnav.AppNav;
import com.jo.application.core.event.EventBus;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.apache.commons.lang3.StringUtils;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
//@Route(value = "")
@CssImport("./styles/styles.css")
@JsModule("./js/signature-widget.js")
@JsModule("./js/camera-component.tsx")
@JsModule("./js/signature-dialog.tsx")
@JsModule("./js/imageService.tsx")
@NpmPackage(value = "signature_pad", version = "4.0.4")
public class CoreMainLayout extends AppLayout implements RouterLayout, BeforeEnterObserver {
    private H2 viewTitle;
    protected AppNav nav;
    private Div content;
    Span filterText = new Span("");
    Button btnGoOriginView = new Button("");

    public CoreMainLayout() {
        // Set context path in JavaScript
        String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
        if(!StringUtils.isBlank(contextPath)) {
            Page page = UI.getCurrent().getPage();
            page.executeJs("window.contextPath = $0;", contextPath);
        }
        setPrimarySection(Section.DRAWER);
        addHeaderContent();
    }

    protected AppNav getNavigation() {
        return nav;
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        btnGoOriginView.addClickListener(e -> {
            if (!btnGoOriginView.getText().isEmpty()) {
                String previousView = (String) VaadinSession.getCurrent().getAttribute("previousView");
                if (previousView != null) {
                    UI.getCurrent().navigate(previousView);
                }
            }
        });
        btnGoOriginView.getElement().setAttribute("theme", "tertiary-inline");
        btnGoOriginView.addClassName("link-button");

        HorizontalLayout layout = new HorizontalLayout(toggle, btnGoOriginView, filterText);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClickListener(event -> {
            EventBus.getInstance().post("DrawerToggleClicked");
        });

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        addToNavbar(false, layout, viewTitle);
    }

    private void addDrawerContent(String strAppName) {
        H1 appName = new H1(strAppName);
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);
        addToDrawer(header, createNavigation(), createFooter());
        content = new Div();
        content.setId("content");
        content.setWidth("100%");
        setContent(content);
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

    protected void setHamburgerTitle(String title) {
        filterText.setText(title);
    }

    protected void setGoOriginText(String originText) {
        btnGoOriginView.setText(originText);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        if (getContent() != null) {
            PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
            return title == null ? "" : title.value();
        } else
            return "";
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