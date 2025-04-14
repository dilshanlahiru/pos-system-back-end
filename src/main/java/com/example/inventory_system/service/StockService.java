package com.example.inventory_system.service;

import com.example.inventory_system.entity.Product;
import com.example.inventory_system.entity.Stock;
import com.example.inventory_system.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductService productService;

    public List<Stock> getAllStock() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    public ResponseEntity<?> createStock(Stock stockRequest) {
        if (stockRequest.getProduct() == null || stockRequest.getProduct().getProductId() == null || stockRequest.getProduct().getId() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "ProductID and Id is required"
            ));
        }
        Optional<Product> productOpt = Optional.ofNullable(productService.getProductByProductId(stockRequest.getProduct().getProductId()));
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Product not found for ProductID: " + stockRequest.getProduct().getProductId()
            ));
        }

        Stock stock = new Stock();
        stock.setProduct(productOpt.get());
        stock.setAvailableAmount(stockRequest.getAvailableAmount());
        stock.setReorderThreshold(stockRequest.getReorderThreshold());
        stock.setBarcode(stockRequest.getBarcode());
        stock.setLastUpdated(stockRequest.getLastUpdated());
        stock.setCreated(stockRequest.getCreated());
        stock.setCost(stockRequest.getCost());
        stock.setPricingRates(stockRequest.getPricingRates());
        stock.setDiscounts(stockRequest.getDiscounts());
        stock.setPrice(stockRequest.getPrice());
        stock.setUnit(stockRequest.getUnit());
        stock.setSupplier(stockRequest.getSupplier());

        Stock saved = stockRepository.save(stock);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Stock created",
                "data", saved
        ));
    }

    public Stock updateStock(Long id, Stock updatedStock) {
        return stockRepository.findById(id)
                .map(existing -> {
                    existing.setProduct(updatedStock.getProduct());
                    existing.setAvailableAmount(updatedStock.getAvailableAmount());
                    existing.setReorderThreshold(updatedStock.getReorderThreshold());
                    existing.setBarcode(updatedStock.getBarcode());
                    existing.setLastUpdated(updatedStock.getLastUpdated());
                    existing.setCreated(updatedStock.getCreated());
                    existing.setCost(updatedStock.getCost());
                    existing.setPricingRates(updatedStock.getPricingRates());
                    existing.setDiscounts(updatedStock.getDiscounts());
                    existing.setPrice(updatedStock.getPrice());
                    existing.setUnit(updatedStock.getUnit());
                    existing.setSupplier(updatedStock.getSupplier());
                    return stockRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("Stock with ID " + id + " not found"));
    }

    public void deleteStock(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new IllegalArgumentException("Stock with ID " + id + " not found");
        }
        stockRepository.deleteById(id);
    }

    public ResponseEntity<?> getStocksByProductId(String productId) {
        List<Stock> stockList = stockRepository.findByProduct_ProductId(productId);

        if (!stockList.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "type", "stockList",
                    "data", stockList
            ));
        }

        Optional<Product> productOpt = Optional.ofNullable(productService.getProductByProductId(productId));
        if (productOpt.isPresent()) {
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "type", "productOnly",
                    "data", productOpt.get()
            ));
        }

        return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", "No stock or product found for productId: " + productId
        ));
    }

    public ResponseEntity<?> updateStockSafely(Stock updatedStock) {
        // Check for essential identifiers
        if (updatedStock.getId() == null ||
                updatedStock.getProduct() == null ||
                updatedStock.getProduct().getId() == null ||
                updatedStock.getProduct().getProductId() == null ||
                updatedStock.getProduct().getProductId().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Not enough identification data to update the stock. Must include: stock.id, product.id, product.productId"
            ));
        }

        Optional<Stock> stockOpt = stockRepository.findById(updatedStock.getId());
        if (stockOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Stock not found for ID: " + updatedStock.getId()
            ));
        }

        Stock existing = stockOpt.get();

        if (updatedStock.getAvailableAmount() != null)
            existing.setAvailableAmount(updatedStock.getAvailableAmount());
        if (updatedStock.getReorderThreshold() != null)
            existing.setReorderThreshold(updatedStock.getReorderThreshold());
        if (updatedStock.getBarcode() != null && !updatedStock.getBarcode().trim().isEmpty())
            existing.setBarcode(updatedStock.getBarcode());
        existing.setLastUpdated(LocalDateTime.now());
        if (updatedStock.getCost() != null)
            existing.setCost(updatedStock.getCost());
        if (updatedStock.getPricingRates() != null )
            existing.setPricingRates(updatedStock.getPricingRates());
        if (updatedStock.getDiscounts() != null )
            existing.setDiscounts(updatedStock.getDiscounts());
        if (updatedStock.getPrice() != null)
            existing.setPrice(updatedStock.getPrice());
        if (updatedStock.getUnit() != null && !updatedStock.getUnit().trim().isEmpty())
            existing.setUnit(updatedStock.getUnit());
        if (updatedStock.getSupplier() != null && !updatedStock.getSupplier().trim().isEmpty())
            existing.setSupplier(updatedStock.getSupplier());

        Stock saved = stockRepository.save(existing);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Stock updated successfully",
                "data", saved
        ));
    }

}
