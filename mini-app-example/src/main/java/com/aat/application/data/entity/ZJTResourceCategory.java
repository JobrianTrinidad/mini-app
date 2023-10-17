package com.aat.application.data.entity;

import com.aat.application.core.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


/**
 * Entity implementation class for Entity: ZJTResourceCategory
 *
 */
@Entity
@Table(name="zjt_resourcecategory")
@PageTitle("Resource Category")
public class ZJTResourceCategory implements ZJTPo, ZJTEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column (name="zjt_resourcecategory_id")
	private int zjt_resourcecategory_id;
	
	@Column
	@NotEmpty
	private String name;
	
	@Column
	private String description;
	@OneToMany(mappedBy = "resourceCategory", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ZJTResourceType> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
   
}
