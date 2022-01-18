package com.pos.shopifyinventory.api;

import com.pos.shopifyinventory.model.Shipment;
import com.pos.shopifyinventory.pojo.ShipmentWrapper;
import com.pos.shopifyinventory.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> createShipment(@RequestBody ShipmentWrapper shipmentWrapper) {
        Shipment shipment = new Shipment();
        shipment.setId(UUID.randomUUID().toString());
        shipment.setCustomer_name(shipmentWrapper.getCustomer_name());
        shipment.setCourier_name(shipmentWrapper.getCourier_name());
        shipment.setTracking_no(shipmentWrapper.getTracking_no());
        shipment.setCreated_at(LocalDateTime.now());
        shipment.setUpdated_at(LocalDateTime.now());
        String responseStr = shipmentService.ship(shipmentWrapper.getInventories(), shipment);
        return new ResponseEntity<>(responseStr, HttpStatus.OK);
    }
}
