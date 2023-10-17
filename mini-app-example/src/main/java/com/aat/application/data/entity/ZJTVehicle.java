package com.aat.application.data.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="zjt_vehicle")
public class ZJTVehicle implements ZJTPo {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int zjt_vehicle_id;
	
	@Column
	@NotEmpty
	private String vehicleid;

	@Column
	private String fleetid;

	@Column
	private String description;
	
	@Column
	private String makemodel;
	
	@Column
	private String license;

	@Column
	private String enginemodel;
	
	@Column
	private String engineno;
	
	@Column
	private String chassisno;
	
	@Column
	private LocalDate purchasedate;

	@Column
	private LocalDate disposaldate;
	
	@Column
	private boolean operational;
	
	@Column
	private BigDecimal hubmeter;
	
	@Column
	private BigDecimal kmratedays;
	
	@Column
	private BigDecimal kmrate;

	@Column
	private BigDecimal hubmetertele;
	
	@Column
	private BigDecimal kmratedaystele;
	
	@Column
	private BigDecimal kmratetele;

	@Column
	private int loadadultseat;
	
	public int getZjt_vehicle_id() {
		return zjt_vehicle_id;
	}

	public void setZjt_vehicle_id(int zjt_vehicle_id) {
		this.zjt_vehicle_id = zjt_vehicle_id;
	}

	public String getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(String vehicleid) {
		this.vehicleid = vehicleid;
	}

	public String getFleetid() {
		return fleetid;
	}

	public void setFleetid(String fleetid) {
		this.fleetid = fleetid;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMakemodel() {
		return makemodel;
	}

	public void setMakemodel(String makemodel) {
		this.makemodel = makemodel;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getEnginemodel() {
		return enginemodel;
	}

	public void setEnginemodel(String enginemodel) {
		this.enginemodel = enginemodel;
	}

	public String getEngineno() {
		return engineno;
	}

	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}

	public String getChassisno() {
		return chassisno;
	}

	public void setChassisno(String chassisno) {
		this.chassisno = chassisno;
	}

	public LocalDate getPurchasedate() {
		return purchasedate;
	}

	public void setPurchasedate(LocalDate purchasedate) {
		this.purchasedate = purchasedate;
	}

	public LocalDate getDisposaldate() {
		return disposaldate;
	}

	public void setDisposaldate(LocalDate disposaldate) {
		this.disposaldate = disposaldate;
	}

	public boolean isOperational() {
		return operational;
	}

	public void setOperational(boolean operational) {
		this.operational = operational;
	}

	public BigDecimal getHubmeter() {
		return hubmeter;
	}

	public void setHubmeter(BigDecimal hubmeter) {
		this.hubmeter = hubmeter;
	}

	public BigDecimal getKmratedays() {
		return kmratedays;
	}

	public void setKmratedays(BigDecimal kmratedays) {
		this.kmratedays = kmratedays;
	}

	public BigDecimal getKmrate() {
		return kmrate;
	}

	public void setKmrate(BigDecimal kmrate) {
		this.kmrate = kmrate;
	}

	public BigDecimal getHubmetertele() {
		return hubmetertele;
	}

	public void setHubmetertele(BigDecimal hubmetertele) {
		this.hubmetertele = hubmetertele;
	}

	public BigDecimal getKmratedaystele() {
		return kmratedaystele;
	}

	public void setKmratedaystele(BigDecimal kmratedaystele) {
		this.kmratedaystele = kmratedaystele;
	}

	public BigDecimal getKmratetele() {
		return kmratetele;
	}

	public void setKmratetele(BigDecimal kmratetele) {
		this.kmratetele = kmratetele;
	}

	public int getLoadadultseat() {
		return loadadultseat;
	}

	public void setLoadadultseat(int loadadultseat) {
		this.loadadultseat = loadadultseat;
	}

	public boolean isHirein() {
		return hirein;
	}

	public void setHirein(boolean hirein) {
		this.hirein = hirein;
	}

	public String getFuelcardname1() {
		return fuelcardname1;
	}

	public void setFuelcardname1(String fuelcardname1) {
		this.fuelcardname1 = fuelcardname1;
	}

	@Column
	private boolean hirein;

	@Column
	private String fuelcardname1;


	
}
