package com.jo.application.data.entity;

import com.jo.application.annotations.ContentDisplayedInSelect;
import com.jo.application.annotations.DisplayName;
import com.jo.application.core.data.entity.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDateTime;


/**
 * Entity implementation class for Entity: ZJTUser
 */
@Entity
@Immutable
@Table(name = "`zjv_tripleg`")
@Subselect("select * from zjv_tripleg")
//@Table(name = "zjt_user")
@PageTitle("User")
public class ZJTTripLeg implements ZJTEntity {


    //equivalent to c_orderline_id
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_tripleg_id")
    private int zjt_tripleg_id;

    @Column(name="itindetail")
    @ContentDisplayedInSelect(value = "ItinDetail")
    @DisplayName(value = "ItinDetail")
    private String itinDetail = "";

    //C_BPartner.Name
    @Column(name="customername")
    @DisplayName(value = "Customer Name")
    private String customerName = "";

    //C_Order.POReference
    @Column
    @DisplayName(value = "Reference")
    private String reference;

    @Column(name="tripstartdate")
    private LocalDateTime tripStartDate;

    @Column(name="tripenddate")
    private LocalDateTime tripEndDate;

    //C_Order.documentno
    @Column(name="tripno")
    private String tripNo;

    //Just the String representation
    @Column(name="vehsize")
    private String vehSize;

    //Tour Director Info and Contact#
    @Column(name="tourdirinfo")
    private String tourDirInfo;

    @Column(name="hasdrivernotes")
    private boolean hasDriverNotes;

    @Column(name="hasdietarynotes")
    private boolean hasDietaryNotes;


    //C_Order.jtt_tripnotes
    @Column(name="hasbookingnotes")
    private  boolean hasBookingNotes;

    //just String representation
    @Column(name="vehname")
    private String vehName;

    @Column(name="drivername")
    private String driverName;

    @Column(name="langspcname")
    private String langSpcName;

    @Column(name="guide1name")
    private String guide1Name;

    @Column(name="guide2name")
    private String guide2Name;


    @Override
    public int getId() {
        return zjt_tripleg_id;
    }


    public String getItinDetail() {
        return itinDetail;
    }

    public void setItinDetail(String itinDetail) {
        this.itinDetail = itinDetail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDateTime getTripStartDate() {
        return tripStartDate;
    }

    public void setTripStartDate(LocalDateTime tripStartDate) {
        this.tripStartDate = tripStartDate;
    }

    public LocalDateTime getTripEndDate() {
        return tripEndDate;
    }

    public void setTripEndDate(LocalDateTime tripEndDate) {
        this.tripEndDate = tripEndDate;
    }

    public String getTripNo() {
        return tripNo;
    }

    public void setTripNo(String tripNo) {
        this.tripNo = tripNo;
    }

    public String getVehSize() {
        return vehSize;
    }

    public void setVehSize(String vehSize) {
        this.vehSize = vehSize;
    }

    public String getTourDirInfo() {
        return tourDirInfo;
    }

    public void setTourDirInfo(String tourDirInfo) {
        this.tourDirInfo = tourDirInfo;
    }

    public boolean isHasDriverNotes() {
        return hasDriverNotes;
    }

    public void setHasDriverNotes(boolean hasDriverNotes) {
        this.hasDriverNotes = hasDriverNotes;
    }

    public boolean isHasDietaryNotes() {
        return hasDietaryNotes;
    }

    public void setHasDietaryNotes(boolean hasDietaryNotes) {
        this.hasDietaryNotes = hasDietaryNotes;
    }

    public boolean isHasBookingNotes() {
        return hasBookingNotes;
    }

    public void setHasBookingNotes(boolean hasBookingNotes) {
        this.hasBookingNotes = hasBookingNotes;
    }

    public String getVehName() {
        return vehName;
    }

    public void setVehName(String vehName) {
        this.vehName = vehName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLangSpcName() {
        return langSpcName;
    }

    public void setLangSpcName(String langSpcName) {
        this.langSpcName = langSpcName;
    }

    public String getGuide1Name() {
        return guide1Name;
    }

    public void setGuide1Name(String guide1Name) {
        this.guide1Name = guide1Name;
    }

    public String getGuide2Name() {
        return guide2Name;
    }

    public void setGuide2Name(String guide2Name) {
        this.guide2Name = guide2Name;
    }
}
