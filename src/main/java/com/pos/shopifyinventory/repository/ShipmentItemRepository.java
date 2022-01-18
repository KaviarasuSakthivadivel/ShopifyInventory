package com.pos.shopifyinventory.repository;

import com.pos.shopifyinventory.model.ShipmentItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentItemRepository extends JpaRepository<ShipmentItems, Long> {
}
