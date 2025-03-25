package com.hbatch.HBatch.gateway.entity.product;


import com.hbatch.HBatch.core.domain.Product;

public class ProductEntityMapper {

    public static ProductEntity toEntity(Product product){
        return new ProductEntity(product.getId(),product.getName(),product.getPrice().toString(),product.getQuantity());
    }
}
