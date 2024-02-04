package com.aat.application.core.form;

/**
 * Class definition to create the mapping for the timeline
 */
public class TimeLineViewParameter {

    /**
     * Name of the column to display in the content
     * e.g  e.employeename
     */
    private String titleFieldName = null;

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

    /**
     * Name of the column for the startdate
     * e.g pd.birthdate
     */
    private String startDateFieldName = null;

    /**
     * Name of the column for the enddate
     * e. pd.birthdate +  interval '1' day  (based on postgres datetime function)
     */
    private String endDateFieldName = null;

    /**
     * Name of the column for the class
     * Classname is the CSS name to control the appearance of the item
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
     * Add where condition if applicable
     * e.g. d.orgid = ?
     */
    private String whereDefinition = null;
    private String selectDefinition = null;

    private Class<?> groupClass;
    String groupName;
    private Object[] parameters;

    /**
     * This is to test if the definition is valid or not
     * this assists the developer if the call is valid
     *
     * @return
     */

    public boolean isValid() {
        boolean valid = titleFieldName != null;
        //TODO - add some logging or notification to developer for missing definition

        if (fromDefinition == null) {
            valid = false;
            //TODO - add some logging or notification to developer for missing definition
        }

        return valid;

    }

    /**
     * This check if it requires filter parameter
     *
     * @return
     */
    public boolean isRequireParameter() {
        return whereDefinition != null;
    }


    public TimeLineViewParameter(String titleFieldName, String groupIDFieldName, String startDateFieldName, String endDateFieldName, String classNameFieldName, String fromDefinition) {
        this.titleFieldName = titleFieldName;
        this.groupIDFieldName = groupIDFieldName;
        this.startDateFieldName = startDateFieldName;
        this.endDateFieldName = endDateFieldName;
        this.classNameFieldName = classNameFieldName;
        this.fromDefinition = fromDefinition;
    }
    public TimeLineViewParameter(String titleFieldName, String groupIDFieldName, String startDateFieldName) {
        this.titleFieldName = titleFieldName;
        this.groupIDFieldName = groupIDFieldName;
        this.startDateFieldName = startDateFieldName;
    }

    public String getTitleFieldName() {
        return titleFieldName;
    }

    public void setTitleFieldName(String titleFieldName) {
        this.titleFieldName = titleFieldName;
    }

    public String getGroupIDFieldName() {
        return groupIDFieldName;
    }

    public void setGroupIDFieldName(String groupIDFieldName) {
        this.groupIDFieldName = groupIDFieldName;
    }

    public String getStartDateFieldName() {
        return startDateFieldName;
    }

    public void setStartDateFieldName(String startDateFieldName) {
        this.startDateFieldName = startDateFieldName;
    }

    public String getEndDateFieldName() {
        return endDateFieldName;
    }

    public void setEndDateFieldName(String endDateFieldName) {
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

    public String getWhereDefinition() {
        return whereDefinition;
    }

    public void setWhereDefinition(String whereDefinition) {
        this.whereDefinition = whereDefinition;
    }

    public String getSelectDefinition() {
        return selectDefinition;
    }

    public void setSelectDefinition(String selectDefinition) {
        this.selectDefinition = selectDefinition;
    }

    public Class<?> getGroupClass() {
        return groupClass;
    }

    public void setGroupClass(Class<?> groupClass) {
        this.groupClass = groupClass;
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
}