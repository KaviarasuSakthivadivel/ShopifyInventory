package com.pos.shopifyinventory.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Shipment_Items")
public class ShipmentItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    @JsonBackReference
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    @JsonBackReference
    private Inventory inventory;

    @Column(name = "quantity", nullable = false)
    private int quantity;
}
