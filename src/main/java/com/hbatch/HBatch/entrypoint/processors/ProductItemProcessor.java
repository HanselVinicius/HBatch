package com.hbatch.HBatch.entrypoint.processors;

import com.hbatch.HBatch.core.domain.Product;
import com.hbatch.HBatch.gateway.entity.product.ProductEntity;
import com.hbatch.HBatch.gateway.entity.product.ProductEntityMapper;
import io.micrometer.common.lang.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, ProductEntity> {

    @Override
    public ProductEntity process(@NonNull Product item) {
        System.out.println(item);
        return ProductEntityMapper.toEntity(item);
    }
}
