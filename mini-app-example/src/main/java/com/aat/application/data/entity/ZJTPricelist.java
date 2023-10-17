package com.aat.application.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name="zjt_pricelist")
public class ZJTPricelist implements ZJTPo {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int zjt_pricelist_id;
	
	@Column
	@NotEmpty
	private String name;

	public int getZjt_pricelist_id() {
		return zjt_pricelist_id;
	}

	public void setZjt_pricelist_id(int zjt_pricelist_id) {
		this.zjt_pricelist_id = zjt_pricelist_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
