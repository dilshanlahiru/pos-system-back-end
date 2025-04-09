package com.example.inventory_system.controller;


import com.example.inventory_system.dto.CategorySearchRequest;
import com.example.inventory_system.entity.Product;
import com.example.inventory_system.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;
//    private final ProductService productService;

//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // --------- end of defult ones

    @PostMapping("/bulk")
    public ResponseEntity<?> createProducts(@RequestBody List<Product> products) {
        if (products == null || products.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "No products provided for bulk insertion.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        // Assuming validation is already handled in the service layer or before this
        try {
            List<Product> savedProducts = productService.createProducts(products);
            return ResponseEntity.ok(savedProducts);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "An error occurred while saving the products.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/distinct-categories")
    public ResponseEntity<?> getDistinctCategories() {
        Map<String, List<String>> categories = productService.getAllDistinctCategories();

        if (categories.isEmpty() || categories.values().stream().allMatch(List::isEmpty)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "No distinct categories found in the system.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        return ResponseEntity.ok(categories);
    }

    @PostMapping("/by-categories")
    public ResponseEntity<?> getProductByCategories(@RequestBody CategorySearchRequest request) {
        Product product = productService.getProductByCategories(
                request.getCategory1(),
                request.getCategory2(),
                request.getCategory3(),
                request.getCategory4()
        );

        if (product == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No product found for the given categories.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(product);
    }

    @PostMapping("/create-if-not-exists")
    public ResponseEntity<?> createProductIfNotExists(@RequestBody Product product) {
        // Check if a matching product exists based on the 4 categories
        Product existingProduct = productService.getProductByCategories(
                product.getCategory1(),
                product.getCategory2(),
                product.getCategory3(),
                product.getCategory4()
        );

        if (existingProduct != null) {
            // If a matching product exists, return a message with the existing product
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Product already exists for the given categories.");
            response.put("existingProduct", existingProduct);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // If no matching product exists, create the new product
        Product createdProduct = productService.createProduct(product);

        // Return the newly created product
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
}
