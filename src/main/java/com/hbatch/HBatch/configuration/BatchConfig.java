package com.hbatch.HBatch.configuration;

import com.hbatch.HBatch.entrypoint.processors.ProductItemProcessor;
import com.hbatch.HBatch.gateway.entity.product.ProductEntity;
import com.hbatch.HBatch.gateway.entity.product.ProductRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.Resource;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {


    @Bean
    public Job job(JobRepository jobRepository, Step sampleStep) {
        return new JobBuilder("productJob", jobRepository)
                .start(sampleStep)
                .build();
    }

    @Bean
    public Step step(JobRepository jobRepository,
                     PlatformTransactionManager transactionManager, ItemReader<ProductEntity> itemReader, ItemWriter<ProductEntity> itemWriter) {
        return new StepBuilder("productStep", jobRepository)
                .<ProductEntity, ProductEntity>chunk(100, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .processor(itemProcessor())
                .build();
    }

    @Bean
    public ItemWriter<ProductEntity> writer(ProductRepository productRepository) {
        return productRepository::saveAll;
    }

    @Bean
    public ItemProcessor<ProductEntity, ProductEntity> itemProcessor() {
        return new ProductItemProcessor();
    }

    @Bean
    public FlatFileItemReader<ProductEntity> flatFileItemReader(@Value("${inputFile}") Resource inputFile) {
        FlatFileItemReader<ProductEntity> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setName("productItemReader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setResource(inputFile);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<ProductEntity> lineMapper() {
        DefaultLineMapper<ProductEntity> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames("id", "name", "price", "quantity");
        lineTokenizer.setStrict(false);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper<ProductEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ProductEntity.class);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);
        return defaultLineMapper;
    }


}
