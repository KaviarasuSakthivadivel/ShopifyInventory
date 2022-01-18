package com.pos.shopifyinventory.api;

import com.pos.shopifyinventory.model.Inventory;
import com.pos.shopifyinventory.model.Shipment;
import com.pos.shopifyinventory.repository.InventoryRepository;
import com.pos.shopifyinventory.service.ShipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryRepository inventoryRepository;
    private final ShipmentService shipmentService;

    @Autowired
    public InventoryController(InventoryRepository inventoryRepository, ShipmentService shipmentService) {
        this.inventoryRepository = inventoryRepository;
        this.shipmentService = shipmentService;
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        logger.debug("Adding inventory into db :: {0} ", new String[]{inventory.getName()});
        inventory.setId(UUID.randomUUID().toString());
        inventory.setCreated_at(LocalDateTime.now());
        inventory.setUpdated_at(LocalDateTime.now());
        this.inventoryRepository.save(inventory);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Inventory> updateInventory(@PathVariable(value="id") String id, @RequestBody Inventory updatedInventory) {
        Optional<Inventory> inventoryOptional = this.inventoryRepository.findById(id);
        if(!inventoryOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Inventory newInventory = this.inventoryRepository.findById(id)
                .map(inventory -> {
                    if(updatedInventory.getName() != null) {
                        inventory.setName(updatedInventory.getName());
                    }
                    if(updatedInventory.getCost_price() != null) {
                        inventory.setCost_price(updatedInventory.getCost_price());
                    }
                    if(updatedInventory.getSelling_price() != null) {
                        inventory.setSelling_price(updatedInventory.getSelling_price());
                    }

                    inventory.setStock(updatedInventory.getStock());
                    return inventoryRepository.save(inventory);
                })
                .orElseGet(() -> {
                    updatedInventory.setId(id);
                    return this.inventoryRepository.save(updatedInventory);
                });
        return new ResponseEntity<>(newInventory, HttpStatus.OK);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<List<Inventory>> listInventory() {
        List<Inventory> inventoryList = this.inventoryRepository.findAll();
        logger.info("Number of items: " + inventoryList.size());
        return new ResponseEntity<>(inventoryList, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<String> deleteInventory(@PathVariable(value="id") String id) {
        Inventory inventory = this.inventoryRepository.getById(id);
        this.inventoryRepository.delete(inventory);
        return new ResponseEntity<>(inventory.getId(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Inventory> getWebsiteMonitor(@PathVariable(value="id") String id) {
        Optional<Inventory> inventoryOptional = this.inventoryRepository.findById(id);
        return inventoryOptional.map(inventory -> new ResponseEntity<>(inventory, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
