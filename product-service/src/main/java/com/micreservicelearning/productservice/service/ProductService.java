package com.micreservicelearning.productservice.service;

import com.micreservicelearning.productservice.dto.ProductRequest;
import com.micreservicelearning.productservice.dto.ProductResponse;
import com.micreservicelearning.productservice.model.Product;
import com.micreservicelearning.productservice.repository.ProductServiceRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

  private final ProductServiceRepository productServiceRepository;

  public void createProduct(ProductRequest productRequest){
    Product product = Product.builder()
        .id(productRequest.getId())
        .price(productRequest.getPrice())
        .description(productRequest.getDescription())
        .name(productRequest.getName())
        .build();
    productServiceRepository.save(product);
    log.info("Product {} is saved", product.getId());
  }

  public List<ProductResponse> getProducts(){
    List<Product> productList = productServiceRepository.findAll();
    return productList.stream().map(this::mapToProductResponse).collect(Collectors.toList());
  }

  private ProductResponse mapToProductResponse(Product product){
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .build();
  }
}
