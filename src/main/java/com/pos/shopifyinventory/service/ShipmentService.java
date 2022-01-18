package com.pos.shopifyinventory.service;

import com.pos.shopifyinventory.model.Inventory;
import com.pos.shopifyinventory.model.Shipment;
import com.pos.shopifyinventory.model.ShipmentItems;
import com.pos.shopifyinventory.repository.InventoryRepository;
import com.pos.shopifyinventory.repository.ShipmentItemRepository;
import com.pos.shopifyinventory.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class ShipmentService {
    private final InventoryRepository inventoryRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentItemRepository shipmentItemRepository;

    @Autowired
    public ShipmentService(InventoryRepository inventoryRepository, ShipmentRepository shipmentRepository, ShipmentItemRepository shipmentItemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentItemRepository = shipmentItemRepository;
    }

    public String ship(List<Inventory> inventories, Shipment shipment) {
        long totalPrice = 0L;
        shipment.setTotal(0L);
        this.shipmentRepository.save(shipment);
        // Checking stock availability.
        for(Inventory inventory: inventories) {
            ShipmentItems shipmentItems = new ShipmentItems();
            Inventory inventoryInDB = this.inventoryRepository.getById(inventory.getId());

            if(inventoryInDB.getStock() < inventory.getStock()) {
                // Not possible to ship, out of stock
                return "Out of stock item: " + inventoryInDB.getName();
            } else {
                totalPrice += inventoryInDB.getSelling_price() * inventory.getStock();
                shipmentItems.setInventory(inventoryInDB);
                shipmentItems.setQuantity(inventory.getStock());
                shipmentItems.setShipment(shipment);
                this.shipmentItemRepository.save(shipmentItems);

                inventoryInDB.setStock(inventoryInDB.getStock() - inventory.getStock());
                this.inventoryRepository.save(inventoryInDB);
            }
        }

        shipment.setTotal(totalPrice);
        this.shipmentRepository.save(shipment);
        return "success";
    }

}
