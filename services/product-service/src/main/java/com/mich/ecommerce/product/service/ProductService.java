package com.mich.ecommerce.product.service;

import com.mich.ecommerce.product.dto.ProductRequest;
import com.mich.ecommerce.product.entity.Product;
import com.mich.ecommerce.product.mapper.ProductMapper;
import com.mich.ecommerce.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer createProduct(ProductRequest productRequest) {
        var product = productMapper.toProduct(productRequest);
        return productRepository.save(product).getId();
    }
}
