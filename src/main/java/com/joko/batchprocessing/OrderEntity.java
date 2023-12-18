package com.joko.batchprocessing;

import java.math.BigDecimal;
import java.util.Date;

public record OrderEntity(
		//int rowId,
		String orderId,
		Date orderDate,
		Date shipDate,
		String shipMode,
		String customerId,
		String customerName,
		String segment,
		String country,
		String city,
		String state,
		int postalCode,
		String region,
		String productId,
		String category,
		String subCategory,
		String productName,
		BigDecimal sales,
		int quantity,
		BigDecimal discount,
		BigDecimal profit) {

}


