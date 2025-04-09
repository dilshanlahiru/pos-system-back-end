package com.example.inventory_system.repository;

import com.example.inventory_system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductId(String productId);

    @Query("SELECT DISTINCT p.category1 FROM Product p WHERE p.category1 IS NOT NULL")
    List<String> findDistinctCategory1();

    @Query("SELECT DISTINCT p.category2 FROM Product p WHERE p.category2 IS NOT NULL")
    List<String> findDistinctCategory2();

    @Query("SELECT DISTINCT p.category3 FROM Product p WHERE p.category3 IS NOT NULL")
    List<String> findDistinctCategory3();

    @Query("SELECT DISTINCT p.category4 FROM Product p WHERE p.category4 IS NOT NULL")
    List<String> findDistinctCategory4();

    Optional<Product> findByCategory1AndCategory2AndCategory3AndCategory4(
            String category1, String category2, String category3, String category4
    );
}
