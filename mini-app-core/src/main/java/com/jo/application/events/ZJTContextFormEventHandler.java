package com.jo.application.events;

import com.jo.application.data.entity.ZJTItem;
import com.vaadin.componentfactory.timeline.context.ContextFormEventHandler;
import com.vaadin.componentfactory.timeline.model.ItemGroup;

import java.util.List;

/**
 * Abstract class that extends {@link ContextFormEventHandler} to provide additional functionality
 * for handling context form events with specific ZJT items and item groups.
 * <p>
 * This class is intended to be subclassed for custom handling of events related to ZJT items.
 * It stores a list of {@link ZJTItem} objects and {@link ItemGroup} objects, which can be used
 * in context form operations.
 * </p>
 */
public abstract class ZJTContextFormEventHandler extends ContextFormEventHandler {

    /**
     * List of {@link ZJTItem} objects that this handler will operate on.
     */
    protected List<ZJTItem> zjtItems;

    /**
     * List of {@link ItemGroup} objects that represent item groups in this handler.
     */
    protected List<ItemGroup> itemGroups;

    /**
     * Default constructor that calls the super constructor of {@link ContextFormEventHandler}.
     */
    public ZJTContextFormEventHandler() {
        super();
    }

    /**
     * Constructor that initializes the handler with HTML content, styles, and form submission information.
     *
     * @param htmlContent  the HTML content that defines the structure of the form
     * @param htmlStyle    the CSS styles that apply to the form
     * @param isSubmitForm a flag indicating whether the form has a submit action
     */
    public ZJTContextFormEventHandler(String htmlContent, String htmlStyle, boolean isSubmitForm) {
        super(htmlContent, htmlStyle, isSubmitForm);
    }

    /**
     * Sets the list of {@link ZJTItem} objects to be handled by this event handler.
     *
     * @param zjtItems the list of ZJT items to be set
     */
    public void setZjtItems(List<ZJTItem> zjtItems) {
        this.zjtItems = zjtItems;
    }

    /**
     * Retrieves the list of {@link ZJTItem} objects that this handler is operating on.
     *
     * @return the list of ZJT items
     */
    public List<ZJTItem> getZjtItems() {
        return zjtItems;
    }

    /**
     * Sets the list of {@link ItemGroup} objects to be handled by this event handler.
     *
     * @param itemGroups the list of item groups to be set
     */
    public void setItemGroups(List<ItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }

    /**
     * Retrieves the list of {@link ItemGroup} objects that this handler is operating on.
     *
     * @return the list of item groups
     */
    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }
}

