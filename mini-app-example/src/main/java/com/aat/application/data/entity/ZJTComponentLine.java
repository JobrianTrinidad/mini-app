package com.aat.application.data.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="zjt_componentline")
public class ZJTComponentLine implements ZJTPo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column (name ="zjt_componentline_id")
	private int zjt_componentline_id;

	@ManyToOne
	@JoinColumn(name="parent_id", referencedColumnName = "zjt_product_id", nullable = false)
	private ZJTProduct parent;

	@ManyToOne
	@JoinColumn(name="product_id", referencedColumnName = "zjt_product_id", nullable = false)
	private ZJTProduct product;

	@ManyToOne
	@JoinColumn(name="zjt_element_id", referencedColumnName = "zjt_element_id")
	private ZJTElement tripelement;

	@Enumerated(EnumType.STRING)
	private Uom uom;

	@Column
	private BigDecimal qty;

	@Column
	private BigDecimal unitprice;

	@Column
	private BigDecimal totalamt;

	public int getZjt_componentline_id() {
		return zjt_componentline_id;
	}

	public void setZjt_componentline_id(int zjt_componentline_id) {
		this.zjt_componentline_id = zjt_componentline_id;
	}

	public ZJTProduct getParent() {
		return parent;
	}

	public void setParent(ZJTProduct parent) {
		this.parent = parent;
	}

	public ZJTProduct getProduct() {
		return product;
	}

	public void setProduct(ZJTProduct product) {
		this.product = product;
	}

	public ZJTElement getTripelement() {
		return tripelement;
	}

	public void setTripelement(ZJTElement tripelement) {
		this.tripelement = tripelement;
	}

	public Uom getUom() {
		return uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
	}

	public BigDecimal getQty() {
		return qty;
	}

	public void setQty(BigDecimal qty) {
		this.qty = qty;
	}

	public BigDecimal getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(BigDecimal unitprice) {
		this.unitprice = unitprice;
	}

	public BigDecimal getTotalamt() {
		return totalamt;
	}

	public void setTotalamt(BigDecimal totalamt) {
		this.totalamt = totalamt;
	}


}
