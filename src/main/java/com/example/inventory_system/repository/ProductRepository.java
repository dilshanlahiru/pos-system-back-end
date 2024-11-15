package com.example.inventory_system.repository;

import com.example.inventory_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Additional query methods (if needed)
}
