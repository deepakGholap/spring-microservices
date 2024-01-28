package com.microservicelearning.inventoryservice.repository;

import com.microservicelearning.inventoryservice.model.Inventory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryServiceRepository extends JpaRepository<Inventory, Long> {

  List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
