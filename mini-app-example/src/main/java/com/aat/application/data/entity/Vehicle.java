package com.aat.application.data.entity;

public class Vehicle {
	
	String VIN;
	String plateNo;
	int maxSeatingCapacity;
	
	
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public int getMaxSeatingCapacity() {
		return maxSeatingCapacity;
	}
	public void setMaxSeatingCapacity(int maxSeatingCapacity) {
		this.maxSeatingCapacity = maxSeatingCapacity;
	}
	
	
}
