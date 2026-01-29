package com.mich.ecommerce.product.mapper;

import com.mich.ecommerce.product.dto.ProductRequest;
import com.mich.ecommerce.product.dto.ProductResponse;
import com.mich.ecommerce.product.entity.Category;
import com.mich.ecommerce.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .availableQuantity(request.availableQuantity())
                .price(request.price())
                .category(
                        Category.builder()
                                .id(request.categoryId())
                                .build()
                )
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        var category = product.getCategory();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : "No Category",
                category != null ? category.getDescription() : null
        );
    }
}
