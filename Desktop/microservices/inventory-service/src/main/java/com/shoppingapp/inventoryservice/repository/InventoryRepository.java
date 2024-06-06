package com.shoppingapp.inventoryservice.repository;

import com.shoppingapp.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//JpaRepo= pe masura ce spring 
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
