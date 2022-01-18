package com.pos.shopifyinventory.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.pos.shopifyinventory.model.Inventory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShipmentWrapper {
    private List<Inventory> inventories;
    private String customer_name;
    private String courier_name;
    private String tracking_no;

    @JsonCreator
    public ShipmentWrapper(List<Inventory> inventories, String customerName, String courierName, String trackingNo) {
        this.inventories = inventories;
        this.customer_name = customerName;
        this.courier_name = courierName;
        this.tracking_no = trackingNo;
    }
}
