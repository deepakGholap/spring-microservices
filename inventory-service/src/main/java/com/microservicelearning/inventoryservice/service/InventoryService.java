package com.microservicelearning.inventoryservice.service;

import com.microservicelearning.inventoryservice.dto.InventoryResponse;
import com.microservicelearning.inventoryservice.repository.InventoryServiceRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

  private final InventoryServiceRepository inventoryServiceRepository;

  @Transactional(readOnly = true)
  @SneakyThrows
  public List<InventoryResponse> isInStock(List<String> skuCode){
    log.info("wait started");
    Thread.sleep(10000);
    log.info("wait ended");
   return inventoryServiceRepository.findBySkuCodeIn(skuCode).stream()
       .map(inventory -> InventoryResponse.builder()
           .skuCode(inventory.getSkuCode())
           .isInStock(inventory.getQuantity() > 0)
           .build())
       .collect(Collectors.toList());
  }
}
