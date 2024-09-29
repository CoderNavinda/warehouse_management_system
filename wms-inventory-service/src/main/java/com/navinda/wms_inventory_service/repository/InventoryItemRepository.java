package com.navinda.wms_inventory_service.repository;

import com.navinda.wms_inventory_service.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    // Custom query methods if necessary
}
