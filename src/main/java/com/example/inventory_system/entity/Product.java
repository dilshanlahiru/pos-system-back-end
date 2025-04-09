package com.example.inventory_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productId;

    private String category1;
    private String category2;
    private String category3;
    private String category4;

    @Column(length = 1000)
    private String additionalInfo;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getCategory1() { return category1; }
    public void setCategory1(String category1) { this.category1 = category1; }

    public String getCategory2() { return category2; }
    public void setCategory2(String category2) { this.category2 = category2; }

    public String getCategory3() { return category3; }
    public void setCategory3(String category3) { this.category3 = category3; }

    public String getCategory4() { return category4; }
    public void setCategory4(String category4) { this.category4 = category4; }

    public String getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }
}

