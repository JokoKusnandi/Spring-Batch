package com.joko.batchprocessing;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;



@Configuration
public class BatchConfiguration {
	
	// tag::readerwriterprocessor[]
	@Bean
	public FlatFileItemReader<Person> reader() {
		return new FlatFileItemReaderBuilder<Person>()
			.name("personItemReader")
			.resource(new ClassPathResource("sample-data.csv"))
			.delimited().delimiter(";")
			.names("firstName", "lastName")
			.targetType(Person.class)
			.build();
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Person>()
			.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
			.dataSource(dataSource)
			.beanMapped()
			.build();
	}
	
    @Bean
    public FlatFileItemReader<OrderEntity> orderEntityReader() {
        return new FlatFileItemReaderBuilder<OrderEntity>()
            .name("orderEntityReader")
            .resource(new ClassPathResource("orders.csv")) // Adjust the file path as needed
            .delimited().delimiter(";")
            .names("orderId", "orderDate", "shipDate", "shipMode", "customerId", "customerName",
                "segment", "country", "city", "state", "postalCode", "region", "productId",
                "category", "subCategory", "productName", "sales", "quantity", "discount", "profit")
            .fieldSetMapper(orderEntityFieldSetMapper())
            .targetType(OrderEntity.class)
            .build();
    }
    
    @Bean
    public FieldSetMapper<OrderEntity> orderEntityFieldSetMapper() {
        BeanWrapperFieldSetMapper<OrderEntity> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(OrderEntity.class);
        fieldSetMapper.setConversionService(conversionService());
        return fieldSetMapper;
    }
    
    @Bean
    public ConversionService conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter( new DecimalNumberConverter(4));
        return conversionService;
    }
    

    
    @Bean
    public OrderItemProcessor orderProcessor() {
        return new OrderItemProcessor();
    }
    
    @Bean
    public JdbcBatchItemWriter<OrderEntity> orderEntityWriter(DataSource dataSource1) {
        return new JdbcBatchItemWriterBuilder<OrderEntity>()
           // .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO orders (Order_ID, Order_Date, Ship_Date, Ship_Mode, Customer_ID, " +
                "Customer_Name, Segment, Country, City, State, Postal_Code, Region, Product_ID, " +
                "Category, Sub_Category, Product_Name, Sales, Quantity, Discount, Profit) " +
                "VALUES (:orderId, :orderDate, :shipDate, :shipMode, :customerId, :customerName, " +
                ":segment, :country, :city, :state, :postalCode, :region, :productId, :category, " +
                ":subCategory, :productName, :sales, :quantity, :discount, :profit)")
			.dataSource(dataSource1)
			.beanMapped()
			.build();
    }
    
    
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobRepository jobRepository,Step PersonStep,Step OrderStep,  JobCompletionNotificationListener listener) {
		return new JobBuilder("importUserJob", jobRepository)
			.listener(listener)
			.start(PersonStep)
			.next(OrderStep)
			.build();
	}
	
	

	@Bean
	public Step PersonStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
					  FlatFileItemReader<Person> reader, PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
		return new StepBuilder("PersonStep", jobRepository)
			.<Person, Person> chunk(3, transactionManager)
			.reader(reader)
			.processor(processor)
			.writer(writer)
			.build();
	}
	
	@Bean
	public Step OrderStep(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
			  FlatFileItemReader<OrderEntity> orderEntityReader, OrderItemProcessor orderProcessor, JdbcBatchItemWriter<OrderEntity> orderEntityWriter) {
		return new StepBuilder("OrderStep", jobRepository)
			.<OrderEntity, OrderEntity> chunk(21, transactionManager)
			.reader(orderEntityReader)
			.processor(orderProcessor)
			.writer(orderEntityWriter)
			.build();
}
	// end::jobstep[]
	
}
