package com.aat.application.core.data.entity;

public interface ZJTEntity {
    int getId();


    /**
     * id = tablename + _id  e.g zjt_vehicle = zjt_vehicle_id
     *
     * date use LocalDateTime
     *
     * foreign key use classname of the parent table
     *      e.g.
     *      @ManyToOne
     *      @JoinColumn(name="zjt_user_id")
     *      @DisplayName(value="Logged By")
     *      private ZJTUser user
     *
     * If this Entity will be used as dropdown - add this annotation
     *     @Column
     *     @ContentDisplayedInSelect(value = "name")
     *     @DisplayName(value = "Name")
     *     private String name = "";
     */
}