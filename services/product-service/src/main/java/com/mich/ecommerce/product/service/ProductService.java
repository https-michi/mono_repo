package com.mich.ecommerce.product.service;

import com.mich.ecommerce.product.dto.ProductPurchaseRequest;
import com.mich.ecommerce.product.dto.ProductPurchaseResponse;
import com.mich.ecommerce.product.dto.ProductRequest;
import com.mich.ecommerce.product.dto.ProductResponse;
import com.mich.ecommerce.product.exception.ProductPurchaseException;
import com.mich.ecommerce.product.mapper.ProductMapper;
import com.mich.ecommerce.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest productRequest) {
        var product = productMapper.toProduct(productRequest);
        return productRepository.save(product).getId();
    }

    public ProductResponse findById(Integer productId) {
        return productRepository.findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID::" + productId));
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests) {
        var productIds = requests.stream()
                .map(ProductPurchaseRequest::productId)
                .distinct()
                .toList();

        var storedProducts = productRepository.findAllByIdInOrderById(productIds);

        if (productIds.size() != storedProducts.size()) {
            throw new ProductPurchaseException("One or more products does not exist");
        }

        var requestsByProductId = requests.stream()
                .collect(Collectors.toMap(ProductPurchaseRequest::productId, r -> r));

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for (var product : storedProducts) {
            var request = requestsByProductId.get(product.getId());

            if (product.getAvailableQuantity() < request.quantity()) {
                log.error("Stock insufficient for ID {}: Available {}, Requested {}",
                        product.getId(), product.getAvailableQuantity(), request.quantity());
                throw new ProductPurchaseException("Insufficient stock for product ID: " + product.getId());
            }
            double newQuantity = product.getAvailableQuantity() - request.quantity();
            product.setAvailableQuantity(newQuantity);

            purchasedProducts.add(productMapper.toProductPurchaseResponse(product, request.quantity()));
        }

        productRepository.saveAll(storedProducts);

        log.info("Successfully processed purchase for {} items", purchasedProducts.size());
        return purchasedProducts;
    }
}
