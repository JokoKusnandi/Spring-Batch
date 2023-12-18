package com.joko.batchprocessing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;



public class OrderItemProcessor implements ItemProcessor<OrderEntity, OrderEntity>{
	
	private static final Logger log = LoggerFactory.getLogger(OrderItemProcessor.class);
	
	@Override
	public OrderEntity process(OrderEntity order) throws Exception {
        //final int rowId = order.rowId();
        final String orderId = order.orderId();
        final Date orderDate = order.orderDate();
        final Date shipDate = order.shipDate();
        final String shipMode = order.shipMode().toUpperCase();
        final String customerId = order.customerId();
        final String customerName = order.customerName().toUpperCase();
        final String segment = order.segment().toUpperCase();
        final String country = order.country().toUpperCase();
        final String city = order.city().toUpperCase();
        final String state = order.state().toUpperCase();
        final int postalCode = order.postalCode();
        final String region = order.region().toUpperCase();
        final String productId = order.productId();
        final String category = order.category().toUpperCase();
        final String subCategory = order.subCategory().toUpperCase();

        // Add the missing fields or adjust as needed
        final String productName = order.productName();
        final BigDecimal sales = order.sales().setScale(4, RoundingMode.HALF_UP);
        final int quantity = order.quantity();
        final BigDecimal discount = order.discount().setScale(4, RoundingMode.HALF_UP);
        final BigDecimal profit = order.profit().setScale(4, RoundingMode.HALF_UP);

        
		
		final OrderEntity transformedOrder = new OrderEntity(
                //rowId,
                orderId,
                orderDate,
                shipDate,
                shipMode,
                customerId,
                customerName,
                segment,
                country,
                city,
                state,
                postalCode,
                region,
                productId,
                category,
                subCategory,
                productName,
                sales,
                quantity,
                discount,
                profit
				);
		 log.info("Converting (" + order + ") into (" + transformedOrder + ")");
		return transformedOrder;
	}
}