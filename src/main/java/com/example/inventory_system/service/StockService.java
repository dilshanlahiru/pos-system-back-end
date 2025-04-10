package com.example.inventory_system.service;

import com.example.inventory_system.entity.Product;
import com.example.inventory_system.entity.Stock;
import com.example.inventory_system.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
