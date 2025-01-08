package com.jo.application.core.form;

import com.jo.application.events.ZJTContextFormEventHandler;
import com.jo.application.util.GlobalData;
import com.vaadin.componentfactory.timeline.context.ContextFormEventHandler;
import com.vaadin.componentfactory.timeline.context.ItemContextMenuEventHandler;
import com.vaadin.componentfactory.timeline.model.AxisOrientation;
import com.vaadin.flow.router.PageTitle;

/**
 * Class definition to create the mapping for the timeline
 */
public class TimeLineViewParameter {

    /**
     * Field name for the ID
     */
    private String idFieldName = null;
    /**
     * Name of the column to display in the content
     * e.g  e.employeename
     */
    private String[] titleFieldName = null;
    private ItemContextMenuEventHandler ItemContextMenuEventHandler;
    private ZJTContextFormEventHandler contextFormEventHandler;

    private int groupZoomTableID = -1;

    public String getToolTipFieldName() {
        return toolTipFieldName;
    }

    public void setToolTipFieldName(String toolTipFieldName) {
        this.toolTipFieldName = toolTipFieldName;
    }

    /**
     * Name of the column to display in the tooltip
     * e.g,  e.empid || ' ' || e.employeename
     */
    private String toolTipFieldName = null;
    /**
     * Name of the column for the group ID
     * Note : separate definition on how group hieararchies are defined
     * e.g.  e.departmentid
     */
    private String groupIDFieldName = null;

    private String subgroupIDFieldName = null;
    /**
     * Name of the column for the startdate
     * e.g pd.birthdate
     */
    private String[] startDateFieldNames = null;

    /**
     * Name of the column for the enddate
     * e. pd.birthdate +  interval '1' day  (based on postgres datetime function)
     */
    private String[] endDateFieldName = null;

    /**
     * Name of the column for the class
     * Classname is the CSS name to control the appearance of the item
     *
     */
    private String classNameFieldName = null;

    /**
     * In simple 1 table definition, it's the name of the table
     * but could be more complex if you use a join statement
     * e.g.
     * employee e
     * inner join personaldetails pd on e.empid = pd.empid
     * inner join department d on e.departmentid = d.departmentid
     */
    private String fromDefinition = null;

    /**
     * Add where condition to load the items
     * e.g. d.orgid = ?
     */
    private String[] whereDefinitions = null;

    /**
     * Where condition to load group items
     */
    private String[] groupWhereDefinitions = null;
    private String selectDefinition = null;
    private String groupSelectDefinition = null;

    private String itemCSSClassField = null;

    private Class<?> groupClass;
    private String groupClassPKField;
    String groupName;
    private String pageName;
    private Object[] parameters;
    private Object[] groupParameters;
    private String dateFilterOn;

    private String groupCSSClass = null;

    /**
     * true = items are displayed as stacked
     *      =  items will be stacked on top of each other such that they do not overlap
     */
    private boolean stack = false;

    /**
     * true = items are displayed as stacked by subgroup
     *      =  items will be stacked on top of each other such that they do not overlap
     */
    private  boolean stackSubgroups = false;

    private AxisOrientation axisOrientation = AxisOrientation.TOP;

    /**
     * This is to test if the definition is valid or not
     * this assists the developer if the call is valid
     *
     * @return
     */

    public boolean isValid() throws Exception {
        boolean valid = true;

        if (titleFieldName == null) {
            throw new Exception("Title FieldName is null");
        }
        if (idFieldName == null) {
            throw new Exception("ID Field name is null");
        }
        if (fromDefinition == null) {
            throw new Exception("from Definition is null");
        }

        return valid;

    }

    /**
     * This check if it requires filter parameter
     *
     * @return
     */
    public boolean isRequireParameter() {
        return whereDefinitions.length > 0;
    }

    /**
     * Will display another horizontal layout below date parameter
     * Comprise of a combox for the Timeline item and a combobox for timeline group
     * User may be able assign item to another group by selecting the combobox
     */
    private boolean isShowItemSelector = false;

    public TimeLineViewParameter(String[] titleFieldName, String groupIDFieldName, String[] startDateFieldNames, String[] endDateFieldName, String classNameFieldName, String fromDefinition) {
        this.titleFieldName = titleFieldName;
        this.groupIDFieldName = groupIDFieldName;
        this.startDateFieldNames = startDateFieldNames;
        this.endDateFieldName = endDateFieldName;
        this.classNameFieldName = classNameFieldName;
        this.fromDefinition = fromDefinition;
    }

    public TimeLineViewParameter(String[] titleFieldName, String groupIDFieldName, String[] startDateFieldNames) {
        this.titleFieldName = titleFieldName;
        this.groupIDFieldName = groupIDFieldName;
        this.startDateFieldNames = startDateFieldNames;
    }

    public String getIdFieldName() {
        return idFieldName;
    }

    public void setIdFieldName(String idFieldName) {
        this.idFieldName = idFieldName;
    }

    public String[] getTitleFieldName() {
        return titleFieldName;
    }

    public void setTitleFieldName(String[] titleFieldName) {
        this.titleFieldName = titleFieldName;
    }

    public String getGroupIDFieldName() {
        return groupIDFieldName;
    }

    public void setGroupIDFieldName(String groupIDFieldName) {
        this.groupIDFieldName = groupIDFieldName;
    }

    public String getSubgroupIDFieldName() {
        return subgroupIDFieldName;
    }

    public void setSubgroupIDFieldName(String subgroupIDFieldName) {
        this.subgroupIDFieldName = subgroupIDFieldName;
    }

    public String[] getStartDateFieldNames() {
        return startDateFieldNames;
    }

    public void setStartDateFieldNames(String[] startDateFieldNames) {
        this.startDateFieldNames = startDateFieldNames;
    }

    public String[] getEndDateFieldName() {
        return endDateFieldName;
    }

    public void setEndDateFieldName(String[] endDateFieldName) {
        this.endDateFieldName = endDateFieldName;
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

    public String[] getWhereDefinitions() {
        return whereDefinitions;
    }

    public void setWhereDefinitions(String[] whereDefinitions) {
        this.whereDefinitions = whereDefinitions;
    }


    public String getSelectDefinition() {
        return selectDefinition;
    }

    public void setSelectDefinition(String selectDefinition) {
        this.selectDefinition = selectDefinition;
    }

    public String getGroupSelectDefinition() {
        return groupSelectDefinition;
    }

    public void setGroupSelectDefinition(String groupSelectDefinition) {
        this.groupSelectDefinition = groupSelectDefinition;
    }

    public Class<?> getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(Class<?> groupClass) {
        this.groupClass = groupClass;
        pageName = groupClass.getAnnotation(PageTitle.class).value();
        groupClassPKField = GlobalData.getPrimaryKeyField(groupClass).getName();
    }

    public String getGroupClassPKField() {
        return groupClassPKField;
    }

    public void setGroupClassPKField(String groupClassPKField) {
        this.groupClassPKField = groupClassPKField;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getDateFilterOn() {
        return dateFilterOn;
    }

    public void setDateFilterOn(String dateFilterOn) {
        this.dateFilterOn = dateFilterOn;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getGroupCSSClass() {
        return groupCSSClass;
    }

    public void setGroupCSSClass(String groupCSSClass) {
        this.groupCSSClass = groupCSSClass;
    }

    public boolean isShowItemSelector() {
        return isShowItemSelector;
    }

    public void setShowItemSelector(boolean showItemSelector) {
        isShowItemSelector = showItemSelector;
    }

    public String[] getGroupWhereDefinitions() {
        return groupWhereDefinitions;
    }

    public void setGroupWhereDefinitions(String[] groupWhereDefinitions) {
        this.groupWhereDefinitions = groupWhereDefinitions;
    }

    public Object[] getGroupParameters() {
        return groupParameters;
    }

    public void setGroupParameters(Object[] groupParameters) {
        this.groupParameters = groupParameters;
    }

    public boolean isStack() {
        return stack;
    }

    public void setStack(boolean stack) {
        this.stack = stack;
    }

    public AxisOrientation getAxisOrientation() {
        return axisOrientation;
    }

    public void setAxisOrientation(AxisOrientation axisOrientation) {
        this.axisOrientation = axisOrientation;
    }

    /**
     * This is the name of the field to control if item is editable or not
     * User will not be able to reassign this item to another group nor change start and end date
     */
    private String itemEditableField;

    /**
     * Sometimes the editable flag is opposite the intended  purpose,
     * this is flag should be able to address this
     */
    private  boolean isEditableFlagInverted = false;
    /**
     * This is the name of the field to control the border color of the item
     * The border color of the item adds another dimension for the user to see
     * some distinction e.g.
     */
    private String itemBorderClassField;

    public String getItemCSSClassField() {
        return itemCSSClassField;
    }

    public void setItemCSSClassField(String itemCSSClassField) {
        this.itemCSSClassField = itemCSSClassField;
    }

    public String getItemEditableField() {
        return itemEditableField;
    }

    public void setItemEditableField(String itemEditableField) {
        this.itemEditableField = itemEditableField;
    }

    public String getItemBorderClassField() {
        return itemBorderClassField;
    }

    public void setItemBorderClassField(String itemBorderClassField) {
        this.itemBorderClassField = itemBorderClassField;
    }

    public boolean isEditableFlagInverted() {
        return isEditableFlagInverted;
    }

    public void setEditableFlagInverted(boolean editableFlagInverted) {
        isEditableFlagInverted = editableFlagInverted;
    }

    public ItemContextMenuEventHandler getItemContextMenuEventHandler() {
        return this.ItemContextMenuEventHandler;
    }

    public void setItemContextMenuEventHandler(com.vaadin.componentfactory.timeline.context.ItemContextMenuEventHandler itemContextMenuEventHandler) {
        ItemContextMenuEventHandler = itemContextMenuEventHandler;
    }

    public ZJTContextFormEventHandler getContextFormEventHandler() {
        return this.contextFormEventHandler;
    }

    public void setContextFormEventHandler(ZJTContextFormEventHandler contextFormEventHandler) {
        this.contextFormEventHandler = contextFormEventHandler;
    }

    public int getGroupZoomTableID() {
        return groupZoomTableID;
    }

    public void setGroupZoomTableID(int groupZoomTableID) {
        this.groupZoomTableID = groupZoomTableID;
    }

    public boolean isStackSubgroups() {
        return stackSubgroups;
    }

    public void setStackSubgroups(boolean stackSubgroups) {
        this.stackSubgroups = stackSubgroups;
    }
}