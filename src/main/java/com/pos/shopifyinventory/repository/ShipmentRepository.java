package com.pos.shopifyinventory.repository;

import com.pos.shopifyinventory.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, String> {
}
