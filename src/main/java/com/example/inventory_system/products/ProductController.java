package com.example.inventory_system.products;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.getProductById(id)
                .map(product -> {
                    product.setProductID(productDetails.getProductID());
                    product.setCategory(productDetails.getCategory());
                    product.setBrandOfTheProduct(productDetails.getBrandOfTheProduct());
                    product.setBrandOfTheVehicle(productDetails.getBrandOfTheVehicle());
                    product.setModel(productDetails.getModel());
                    product.setAdditionalInformation(productDetails.getAdditionalInformation());
                    product.setCost(productDetails.getCost());
                    product.setPrice(productDetails.getPrice());
                    product.setAvailableAmount(productDetails.getAvailableAmount());
                    product.setLocation(productDetails.getLocation());
                    product.setReorderThreshold(productDetails.getReorderThreshold());
                    product.setBarcode(productDetails.getBarcode());
                    product.setDiscount(productDetails.getDiscount());
                    product.setTaxRate(productDetails.getTaxRate());
                    return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
