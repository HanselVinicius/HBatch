package com.hbatch.HBatch.entrypoint.processors;

import com.hbatch.HBatch.gateway.entity.product.ProductEntity;
import io.micrometer.common.lang.NonNull;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductItemProcessor implements ItemProcessor<ProductEntity, ProductEntity> {

    @Override
    public ProductEntity process(@NonNull ProductEntity item) {
        //call the core to made business logic validation
        System.out.println(item);
        return item;

    }
}
