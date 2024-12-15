package com.navinda.wms_inventory_service.controller;

import com.navinda.wms_inventory_service.model.InventoryItem;
import com.navinda.wms_inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryItemController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<InventoryItem> addInventory(@RequestBody InventoryItem item) {
        InventoryItem createdItem = inventoryService.addInventory(item);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryItem>> getAllInventory() {
        List<InventoryItem> inventory = inventoryService.getAllInventory();
        return new ResponseEntity<>(inventory, HttpStatus.OK);
    }

    @GetMapping("/check/{sku}/{quantity}")
    public ResponseEntity<Boolean> checkInventory(@PathVariable String sku,
            @PathVariable int quantity) {
        boolean isAvailable = inventoryService.isStockAvailable(sku, quantity);
        return ResponseEntity.ok(isAvailable);
    }

    @PutMapping("/reduce")
    public ResponseEntity<Void> reduceInventory(@RequestBody Map<String, Object> stockRequest) {
        String sku = String.valueOf(stockRequest.get("sku"));
        int quantity = (int) stockRequest.get("quantity");
        inventoryService.reduceStock(sku, quantity);
        return ResponseEntity.ok().build();
    }
}
