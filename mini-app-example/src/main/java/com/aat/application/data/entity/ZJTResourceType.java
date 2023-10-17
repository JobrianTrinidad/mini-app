package com.aat.application.data.entity;

import com.aat.application.core.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name="zjt_resourcetype")
@PageTitle("Resource Type")
@NamedQuery(name = "findAllResourceCategory", query = "SELECT p FROM ZJTResourceCategory p")
public class ZJTResourceType implements ZJTPo, ZJTEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column (name="zjt_resourcetype_id")
	private int zjt_resourcetype_id;
	
	@Column
	@NotEmpty
	private String name = "";

	@Column
	private String description = "";

	@ManyToOne
	@JoinColumn(name="zjt_resourcecategory_id", referencedColumnName = "zjt_resourcecategory_id")
//	@NotNull
	private ZJTResourceCategory resourceCategory;

	@OneToMany(mappedBy = "resourceType", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ZJTProduct> children;

//	@Transient
//	@Column
//	private String cname;
	
	public int getZjt_resourcetype_id() {
		return zjt_resourcetype_id;
	}

	public void setZjt_resourcetype_id(int zjt_resourcetype_id) {
		this.zjt_resourcetype_id = zjt_resourcetype_id;
	}

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

	public ZJTResourceCategory getResourceCategory() {
		return resourceCategory;
	}

	public void setResourceCategory(ZJTResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

//	public String getCname() {
//		return cname;
////		return resourceCategory.getName();
//	}
//
//	public void setCname(String cname) {
//		this.cname = cname;
//	}

	


	
}
