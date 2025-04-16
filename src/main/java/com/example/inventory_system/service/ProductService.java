package com.example.inventory_system.service;


import com.example.inventory_system.entity.Product;
import com.example.inventory_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        product.setProductId(generateNextProductId());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = getProductById(id);

        existing.setProductId(updatedProduct.getProductId());
        existing.setCategory1(updatedProduct.getCategory1());
        existing.setCategory2(updatedProduct.getCategory2());
        existing.setCategory3(updatedProduct.getCategory3());
        existing.setCategory4(updatedProduct.getCategory4());
        existing.setAdditionalInfo(updatedProduct.getAdditionalInfo());

        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // --------- end of defult ones

    public Product getProductByProductId(String productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with productId: " + productId));
    }

    public List<Product> createProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public Map<String, List<String>> getAllDistinctCategories() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("category1List", productRepository.findDistinctCategory1());
        map.put("category2List", productRepository.findDistinctCategory2());
        map.put("category3List", productRepository.findDistinctCategory3());
        map.put("category4List", productRepository.findDistinctCategory4());
        return map;
    }

    public Product getProductByCategories(String cat1, String cat2, String cat3, String cat4) {
        return productRepository
                .findByCategory1AndCategory2AndCategory3AndCategory4(cat1, cat2, cat3, cat4)
                .orElse(null);
    }

    private String generateNextProductId() {
        String prefix = "PROD-";
        String lastId = productRepository.findTopByOrderByIdDesc()
                .map(Product::getProductId)
                .orElse(null);

        int nextNumber = 1;

        if (lastId != null && lastId.startsWith(prefix)) {
            try {
                nextNumber = Integer.parseInt(lastId.substring(prefix.length())) + 1;
            } catch (NumberFormatException e) {
                // log a warning and keep nextNumber = 1
            }
        }

        return prefix + String.format("%03d", nextNumber);
    }
}
