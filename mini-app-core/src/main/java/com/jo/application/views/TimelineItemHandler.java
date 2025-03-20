package com.jo.application.views;

import com.jo.application.data.entity.ZJTItem;

/**
 * Interface to handle timeline item events.
 * <p>
 * This interface provides default implementations for handling different
 * timeline item events such as group updates, time updates, and removal.
 * Classes implementing this interface can override these methods
 * if specific behavior is required.
 * </p>
 */
public interface TimelineItemHandler {

    /**
     * Handles updates when a timeline item changes its group.
     *
     * @param item               The timeline item being updated.
     * @param isMultipleSelection Indicates whether the item is part of multiple selections.
     */
    default void onTimelineItemUpdateGroup(ZJTItem item, boolean isMultipleSelection) {
    }

    /**
     * Handles updates when a timeline item's time is modified.
     *
     * @param item               The timeline item being updated.
     */
    default void onTimelineUpdateItem(ZJTItem item) {
    }

    /**
     * Handles the removal of a timeline item.
     *
     * @param item               The timeline item being removed.
     */
    default void onTimelineRemoveItem(ZJTItem item) {
    }

    /**
     * Handles the Adding of a timeline item.
     *
     * @param item The timeline new item being added.
     */
    default void onTimelineAddItem(ZJTItem item) {
    }

    /**
     * Initializes the layout.
     * <p>
     * This method can be overridden by implementing classes to perform
     * layout-specific initialization if needed.
     * </p>
     */
    default void initLayout() {
    }
}


