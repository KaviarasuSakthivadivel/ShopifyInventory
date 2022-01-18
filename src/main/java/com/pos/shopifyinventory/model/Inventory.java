package com.pos.shopifyinventory.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "selling_price", nullable = false)
    private Long selling_price;

    @Column(name = "cost_price", nullable = false)
    private Long cost_price;

    @Column(name = "opening_stock", nullable = false)
    private int stock;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime created_at;

    @Column(name = "updated_at")
    @CreatedDate
    private LocalDateTime updated_at;
}
