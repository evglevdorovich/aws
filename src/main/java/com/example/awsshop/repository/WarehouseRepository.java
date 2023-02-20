package com.example.awsshop.repository;

import com.example.awsshop.model.WarehouseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<WarehouseProduct, Long> {

    Optional<WarehouseProduct> findByProductId(Long productId);
}
