package com.navinda.wms_inventory_service.service;

import com.navinda.wms_inventory_service.model.InventoryItem;
import com.navinda.wms_inventory_service.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryItemRepository inventoryRepository;

    public InventoryItem addInventory(InventoryItem item) {
        return inventoryRepository.save(item);
    }

    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }
}
