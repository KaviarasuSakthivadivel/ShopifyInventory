package com.pos.shopifyinventory.repository;

import com.pos.shopifyinventory.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, String> {
}
