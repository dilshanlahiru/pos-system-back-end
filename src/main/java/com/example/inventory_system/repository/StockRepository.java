package com.example.inventory_system.repository;

import com.example.inventory_system.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByProduct_ProductId(String productId);
}
