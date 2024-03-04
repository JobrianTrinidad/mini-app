package com.aat.application.core.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.router.PageTitle;

public class CustCompViewParameter {

    /**
     * Name of the column for the class
     * Classname is the CSS name to control the appearance of the item
     */
    private String classNameFieldName;

    /**
     * In simple 1 table definition, it's the name of the table
     * but could be more complex if you use a join statement
     * e.g.
     * employee e
     * inner join personalDetails pd on e.empId = pd.empId
     * inner join department d on e.departmentId = d.departmentId
     */
    private String fromDefinition = null;

    private Class<?> entityClass;
    private String pageName;

    private Component component;


    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getClassNameFieldName() {
        return classNameFieldName;
    }

    public void setClassNameFieldName(String classNameFieldName) {
        this.classNameFieldName = classNameFieldName;
    }

    public String getFromDefinition() {
        return fromDefinition;
    }

    public void setFromDefinition(String fromDefinition) {
        this.fromDefinition = fromDefinition;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }


    public CustCompViewParameter(Class<?> entityClass, String classNameFieldName, Component component) {
        this.classNameFieldName = classNameFieldName;
        this.setEntityClass(entityClass);
        this.setComponent(component);
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
        pageName = entityClass.getAnnotation(PageTitle.class).value();
        this.fromDefinition = entityClass.getSimpleName();
    }
}
