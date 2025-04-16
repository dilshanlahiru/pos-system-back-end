package com.example.inventory_system.controller;

import com.example.inventory_system.dto.CategorySearchRequest;
import com.example.inventory_system.entity.Stock;
import com.example.inventory_system.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = "*")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping
    public ResponseEntity<?> getAllStocks() {
        List<Stock> stocks = stockService.getAllStock();
        return ResponseEntity.ok(stocks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStockById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.getStockById(id));
    }

    @PostMapping
    public ResponseEntity<?> createStock(@RequestBody Stock stock) {
        return stockService.createStock(stock);
//        try {
//            Stock created = stockService.createStock(stock);
//            return ResponseEntity.status(201).body(created);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "success", false,
//                    "message", "Invalid input or request",
//                    "error", e.getMessage()
//            ));
//        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody Stock stock) {
        try {
            Stock updated = stockService.updateStock(id, stock);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid input or request",
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable Long id) {
        try {
            stockService.deleteStock(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Stock deleted successfully"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<?> getStockByProductId(@PathVariable String productId) {
        return stockService.getStocksByProductId(productId);
    }

    @PostMapping("/by-product-categories")
    public ResponseEntity<?> getStockByProductCategories(@RequestBody CategorySearchRequest request) {
        return stockService.getStocksByProductCategories(request);
    }

    @PutMapping("/safe-update")
    public ResponseEntity<?> safeUpdateStock(@RequestBody Stock updatedStock) {
        return stockService.updateStockSafely(updatedStock);
    }
}
