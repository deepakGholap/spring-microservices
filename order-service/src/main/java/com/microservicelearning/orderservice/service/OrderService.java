package com.microservicelearning.orderservice.service;

import com.microservicelearning.orderservice.dto.InventoryResponse;
import com.microservicelearning.orderservice.dto.OrderLineItemsDto;
import com.microservicelearning.orderservice.dto.OrderRequest;
import com.microservicelearning.orderservice.model.Order;
import com.microservicelearning.orderservice.model.OrderLineItems;
import com.microservicelearning.orderservice.repository.OrderServiceRepository;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderServiceRepository orderServiceRepository;
  private final WebClient webClient;

  private final Tracer tracer;

  public String placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());
    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
        .map(this::mapToOrderLineItems)
        .toList();

    order.setOrderLineItemsList(orderLineItems);
    List<String> skuCodes = order.getOrderLineItemsList()
        .stream()
        .map(OrderLineItems::getSkuCode).toList();
    Span inventoryServiceLookUp = tracer.nextSpan().name("inventoryServiceLookUp");
    try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookUp.start())) {
      InventoryResponse[] inventoryResponseArray = webClient.get()
          .uri("http://localhost:8082/api/inventory",
              uriBuilder -> uriBuilder.queryParam("sku-code", skuCodes).build())
          .retrieve()
          .bodyToMono(InventoryResponse[].class)
          .block();
      boolean isInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
      if (isInStock) {
        orderServiceRepository.save(order);
        return "order placed successfully";
      } else {
        throw new IllegalArgumentException("Product is not in stock, please try again later");
      }
    } finally {
      inventoryServiceLookUp.end();
    }
  }

  private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    return orderLineItems;
  }
}
