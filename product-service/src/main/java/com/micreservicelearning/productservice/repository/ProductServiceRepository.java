package com.micreservicelearning.productservice.repository;

import com.micreservicelearning.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductServiceRepository extends JpaRepository<Product, String> {
}
