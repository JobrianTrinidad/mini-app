package com.aat.application.data.entity;

import com.aat.application.core.ZJTEntity;
import com.vaadin.flow.router.PageTitle;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Entity
@Table(name = "zjt_element")
@PageTitle("Trip Element")
@NamedQuery(name = "findAllPricingType", query = "SELECT p FROM ZJTPricingType p")
public class ZJTElement implements ZJTPo, ZJTEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "zjt_element_id")
    private int zjt_element_id;

    @Column
    @NotEmpty
    private String name = "";

    @ManyToOne
    @JoinColumn(name = "zjt_pricingtype_id", referencedColumnName = "zjt_pricingtype_id")
//    @NotNull
    private ZJTPricingType pricingType;

    @OneToMany(mappedBy = "tripElement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ZJTProduct> children1;

    @OneToMany(mappedBy = "tripelement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ZJTComponentLine> children2;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Uom uom = Uom.E;

    @Enumerated(EnumType.STRING)
    private ElementList elementlist = ElementList.DH;

    public int getZjt_element_id() {
        return zjt_element_id;
    }

    public ElementList getElementlist() {
        return elementlist;
    }

    public void setElementlist(ElementList elementlist) {
        this.elementlist = elementlist;
    }

    public void setZjt_element_id(int zjt_element_id) {
        this.zjt_element_id = zjt_element_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZJTPricingType getPricingType() {
        return pricingType;
    }

    public void setPricingType(ZJTPricingType pricingType) {
        this.pricingType = pricingType;
    }

    public Uom getUom() {
        return uom;
    }

    public void setUom(Uom uom) {
        this.uom = uom;
    }
}
