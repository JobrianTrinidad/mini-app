package com.aat.application.data.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="zjt_pricelist_item")
public class ZJTPriceListItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int zjt_pricelist_item_id;
	

	@ManyToOne
	@JoinColumn(name="zjt_product_id")
	private ZJTProduct product;

	@ManyToOne
	@JoinColumn(name="zjt_pricelist_id")
	private ZJTPricelist pricelist;
	
	@Column
	private BigDecimal price;

	public int getZjt_pricelist_item_id() {
		return zjt_pricelist_item_id;
	}

	public void setZjt_pricelist_item_id(int zjt_pricelist_item_id) {
		this.zjt_pricelist_item_id = zjt_pricelist_item_id;
	}

	public ZJTProduct getProduct() {
		return product;
	}

	public void setProduct(ZJTProduct product) {
		this.product = product;
	}

	public ZJTPricelist getPricelist() {
		return pricelist;
	}

	public void setPricelist(ZJTPricelist pricelist) {
		this.pricelist = pricelist;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	

}
