package com.aat.application.data.entity;

import java.math.BigDecimal;

public class PriceListRow {

	String name;
	BigDecimal vehicleHours;
	BigDecimal vehicleKM;
	BigDecimal driverHours;
	BigDecimal overHead;
	BigDecimal profitMargin;
	ZJTPricelist	pricelist;
	
	ZJTResourceType resourceType;
	ZJTPriceListItem vehicleHourItem;
	ZJTPriceListItem vehicleKMItem;
	ZJTPriceListItem driverHourItem;
	ZJTPriceListItem overHeadItem;
	ZJTPriceListItem profitMarginItem;
	
	public PriceListRow(ZJTResourceType resourceType, ZJTPricelist pricelist) {
		super();
		this.name = resourceType.getName();
		this.resourceType = resourceType;
		this.pricelist = pricelist;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getVehicleHours() {
		return vehicleHours;
	}
	public void setVehicleHours(BigDecimal vehicleHours) {
		this.vehicleHours = vehicleHours;
	}
	public BigDecimal getVehicleKM() {
		return vehicleKM;
	}
	public void setVehicleKM(BigDecimal vehicleKM) {
		this.vehicleKM = vehicleKM;
	}
	public BigDecimal getDriverHours() {
		return driverHours;
	}
	public void setDriverHours(BigDecimal driverHours) {
		this.driverHours = driverHours;
	}
	public BigDecimal getOverHead() {
		return overHead;
	}
	public void setOverHead(BigDecimal overHead) {
		this.overHead = overHead;
	}
	public BigDecimal getProfitMargin() {
		return profitMargin;
	}
	public void setProfitMargin(BigDecimal profitMargin) {
		this.profitMargin = profitMargin;
	}

	public ZJTPricelist getPricelist() {
		return pricelist;
	}

	public void setPricelist(ZJTPricelist pricelist) {
		this.pricelist = pricelist;
	}

	public ZJTPriceListItem getVehicleHourItem() {
		return vehicleHourItem;
	}

	public ZJTResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ZJTResourceType resource) {
		name = resource.getName();
		this.resourceType = resource;
	}

	public void setVehicleHourItem(ZJTPriceListItem vehicleHourItem) {
		if(vehicleHourItem != null) {
			vehicleHours = vehicleHourItem.getPrice();
			this.vehicleHourItem = vehicleHourItem;
		}
	}

	public ZJTPriceListItem getVehicleKMItem() {
		return vehicleKMItem;
	}

	public void setVehicleKMItem(ZJTPriceListItem vehicleKMItem) {
		if(vehicleKMItem != null) {
			vehicleKM = vehicleKMItem.getPrice();
			this.vehicleKMItem = vehicleKMItem;
		}
	}

	public ZJTPriceListItem getDriverHourItem() {
		return driverHourItem;
	}

	public void setDriverHourItem(ZJTPriceListItem driverHourItem) {
		if(driverHourItem != null) {
			driverHours = driverHourItem.getPrice();
			this.driverHourItem = driverHourItem;
		}
	}

	public ZJTPriceListItem getOverHeadItem() {
		return overHeadItem;
	}

	public void setOverHeadItem(ZJTPriceListItem overHeadItem) {
		if(overHeadItem != null) {
			overHead = overHeadItem.getPrice();
			this.overHeadItem = overHeadItem;
		}
	}

	public ZJTPriceListItem getProfitMarginItem() {
		return profitMarginItem;
	}

	public void setProfitMarginItem(ZJTPriceListItem profitMarginItem) {
		if(profitMarginItem != null) {
			profitMargin = profitMarginItem.getPrice();
			this.profitMarginItem = profitMarginItem;
		}
	}
	
}
