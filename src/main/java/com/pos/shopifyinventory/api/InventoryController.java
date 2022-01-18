package com.pos.shopifyinventory.api;

import com.pos.shopifyinventory.model.Inventory;
import com.pos.shopifyinventory.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/inventory")
public class InventoryController {

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        logger.debug("Adding inventory into db :: {0} ", new String[]{inventory.getName()});
        inventory.setId(UUID.randomUUID().toString());
        this.inventoryRepository.save(inventory);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Inventory> updateInventory(@RequestBody Inventory inventory) {
        logger.debug("Updating inventory into db :: {0} ", new String[]{inventory.getName()});
        this.inventoryRepository.save(inventory);
        return new ResponseEntity<>(inventory, HttpStatus.OK);
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