package com.jo.application.core.form;

import com.jo.application.core.data.entity.ZJTEntity;
import com.jo.application.views.CommonView;
import com.vaadin.componentfactory.tuigrid.TuiGrid;
import com.vaadin.componentfactory.tuigrid.model.AATContextMenu;
import com.vaadin.componentfactory.tuigrid.model.GuiItem;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;

public abstract class CommonForm extends VerticalLayout {

    public TuiGrid grid;
    protected String dateFilterOn;
    protected final DatePicker startDatePicker = new DatePicker("");
    protected final DatePicker endDatePicker = new DatePicker("");
    private final ComboBox<EnumDateFilter> dateFilterComboBox = new ComboBox<>("");
    protected HorizontalLayout dateFilter;

    protected CommonView commonView;

    abstract public void onNewItem(GuiItem item);

    abstract public ZJTEntity onNewItem(ZJTEntity entity, int id);

    abstract public void setDataToGridRow(int rowKey, String data);

    abstract public int onUpdateItem(Object[] objects) throws Exception;

    abstract public int onDeleteItemChecked() throws Exception;

    abstract public void setContextMenu(AATContextMenu contextMenu);

    abstract public void setMessageStatus(String msg);

    abstract public void addCustomComponent(int index, Component component);

    abstract public String getHamburgerText();

    abstract public String getOriginViewText();

    abstract public void onUpdateForm() throws Exception;

    abstract public void onUpdateTimeWindow() throws Exception;

    abstract public void setDatePickerReadonly(boolean readonly);

    public CommonForm() {
        startDatePicker.addValueChangeListener(e -> {
            try {
                if (endDatePicker.getValue() != null) {
                    if (e.getValue() != null && !e.getValue().atStartOfDay().isEqual(endDatePicker.getValue().atStartOfDay())
                            && !e.getValue().atStartOfDay().isBefore(endDatePicker.getValue().atStartOfDay())) {
                        updateEndDateByDateFilter(e.getValue());
                    }
                    this.filterByDate(e.getValue().atStartOfDay(), endDatePicker.getValue().atStartOfDay());
                }
                else
                    this.filterByDate(e.getValue().atStartOfDay(), null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        endDatePicker.addValueChangeListener(e -> {
            try {
                if (startDatePicker.getValue() != null)
                    this.filterByDate(startDatePicker.getValue().atStartOfDay(), e.getValue().atStartOfDay());
                else
                    this.filterByDate(null, e.getValue().atStartOfDay());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        DatePicker.DatePickerI18n i18n = new DatePicker.DatePickerI18n();
        i18n.setDateFormat("dd/MM/yyyy");
        startDatePicker.setI18n(i18n);
        endDatePicker.setI18n(i18n);

        dateFilterComboBox.setItems(EnumDateFilter.values());
        dateFilterComboBox.addValueChangeListener(e -> updateDateFilter());
        dateFilterComboBox.setValue(EnumDateFilter.TM);
        dateFilter = new HorizontalLayout(dateFilterComboBox, startDatePicker, new Span("To"), endDatePicker);
        dateFilter.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    public void updateDateFilter(EnumDateFilter enumDateFilter) throws Exception {
        dateFilterComboBox.setValue(enumDateFilter);
        updateDateFilter();
        onUpdateForm();
    }

    private void updateDateFilter() {
        LocalDate dateFrom = null;
        LocalDate dateTo = null;

        switch (dateFilterComboBox.getValue()) {
            case TD:
                dateFrom = LocalDate.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                dateTo = LocalDate.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                break;
            case TD3:
                dateFrom = LocalDate.from(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
                dateTo = LocalDate.from(LocalDateTime.now().plusDays(3).truncatedTo(ChronoUnit.DAYS));
                break;
            case TW:
                dateFrom = LocalDate.now();
                dateFrom = dateFrom.with(WeekFields.of(Locale.UK).getFirstDayOfWeek());
                dateTo = dateFrom.plusWeeks(1).minusDays(1);
                break;
            case NW:
                dateFrom = LocalDate.now().plusWeeks(1);
                dateFrom = dateFrom.with(WeekFields.of(Locale.UK).getFirstDayOfWeek());
                dateTo = dateFrom.plusWeeks(1).minusDays(1);
                break;
            case TM:
                dateFrom = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
                dateTo = YearMonth.of(LocalDate.now().getYear(), LocalDate.now().getMonth()).atEndOfMonth();
                break;
            case NM:
                dateFrom = LocalDate.now().plusMonths(1);
                dateFrom = LocalDate.of(dateFrom.getYear(), dateFrom.getMonth(), 1);
                dateTo = YearMonth.of(dateFrom.getYear(), dateFrom.getMonth()).atEndOfMonth();
                break;
        }

        if (dateFrom != null)
            startDatePicker.setValue(dateFrom);
        if (dateTo != null)
            endDatePicker.setValue(dateTo);
    }
    /**
     * Updates the end date picker value based on a given starting date and the selected date filter.
     * <p>
     * This method calculates the new end date based on the selected value from the `dateFilterComboBox`.
     * The behavior of each filter is as follows:
     * <ul>
     *     <li><strong>TD:</strong> Sets the end date to the given starting date.</li>
     *     <li><strong>TD3:</strong> Sets the end date to 3 days after the given starting date.</li>
     *     <li><strong>TW:</strong> Sets the end date to the end of the current week starting from the given date (6 days later).</li>
     *     <li><strong>NW:</strong> Sets the end date to the end of the next week starting from the given date.</li>
     *     <li><strong>TM:</strong> Sets the end date to the last day of the current month.</li>
     *     <li><strong>NM:</strong> Sets the end date to the last day of the next month.</li>
     * </ul>
     * The computed date is then set in the `endDatePicker`.
     * <p>
     * Edge Cases:
     * <ul>
     *     <li><strong>dateFrom:</strong> Must not be null. If null, an {@code IllegalArgumentException} is thrown.</li>
     *     <li><strong>Valid Ranges:</strong> Ensure that the provided {@code dateFrom} falls within the acceptable range for the desired filter logic. Out-of-range dates could lead to unexpected results.</li>
     * </ul>
     *
     * @param dateFrom the starting date from which the end date will be calculated. Must be non-null.
     */
    private void updateEndDateByDateFilter(LocalDate dateFrom) {
        if (dateFrom == null) {
            throw new IllegalArgumentException("dateFrom must not be null");
        }

        var filterValue = dateFilterComboBox.getValue();
        if (filterValue == null) {
            throw new IllegalArgumentException("dateFilterComboBox value must not be null");
        }

        LocalDate dateTo = switch (filterValue) {
            case TD -> dateFrom;
            case TD3 -> dateFrom.plusDays(3);
            case TW -> dateFrom.with(java.time.DayOfWeek.SUNDAY);
            case NW -> dateFrom.plusWeeks(1).with(java.time.DayOfWeek.SUNDAY);
            case TM -> YearMonth.from(dateFrom).atEndOfMonth();
            case NM -> YearMonth.from(dateFrom.plusMonths(1)).atEndOfMonth();
        };
        endDatePicker.setValue(dateTo);
    }

    public void filterByDate(LocalDateTime start, LocalDateTime end) throws Exception {
        if (start != null && end != null) {
            if (!start.isEqual(end) && !start.isBefore(end)) {
                Notification.show("End date should be after start date", 5000, Notification.Position.MIDDLE);
                return;
            }
        }
        onUpdateTimeWindow();
        onUpdateForm();
    }

    protected void addConditionWhenFilteringDate(StringBuilder query) {
        if (startDatePicker.getValue() != null
                && endDatePicker.getValue() != null) {
            if (!startDatePicker.getValue().equals(endDatePicker.getValue())) {
                query.append(" DATE(p.").append(dateFilterOn)
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
            } else {
                query.append(" DATE(p.").append(dateFilterOn)
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
            }

        } else {
            boolean addAndStatement = false;
            if (startDatePicker.getValue() != null) {
                query.append(" DATE(p.").append(dateFilterOn)
                        .append(") >= '").append(startDatePicker.getValue().atStartOfDay()).append("'");
                addAndStatement = true;
            }
            if (endDatePicker.getValue() != null) {
                if (addAndStatement) {
                    query.append(" AND ");
                }
                query.append(" AND DATE(p.").append(dateFilterOn)
                        .append(") < '").append(endDatePicker.getValue().plusDays(1).atStartOfDay()).append("'");
            }

        }
    }

    public CommonView getCommonView() {
        return commonView;
    }

    public void setCommonView(CommonView commonView) {
        this.commonView = commonView;
    }
}