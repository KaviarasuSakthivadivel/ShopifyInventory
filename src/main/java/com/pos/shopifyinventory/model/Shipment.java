package com.pos.shopifyinventory.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Shipment")
public class Shipment {
    @Id
    private String id;

    // This will be user id in real time
    @Column(name = "customer_name", nullable = false)
    private String customer_name;

    // This will be courier id in real time
    @Column(name = "courier_name", nullable = false)
    private String courier_name;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    @CreatedDate
    private LocalDateTime updated_at;

    @Column(name = "tracking_no", nullable = false)
    private String tracking_no;

    @Column(name = "total", nullable = false)
    private Long total;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<ShipmentItems> shipmentItems;
}
