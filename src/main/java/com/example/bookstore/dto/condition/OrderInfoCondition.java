package com.example.bookstore.dto.condition;

import com.example.bookstore.entity.OrderInfo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfoCondition {

	private String orderInfoId;
	private String storeName;
	private boolean isNowDream;
	private Integer totalPrice;
	private LocalDateTime orderDate;

	public static OrderInfoCondition fromEntity(OrderInfo orderInfo) {

		return OrderInfoCondition.builder()
			.orderInfoId(orderInfo.getId())
			.storeName(orderInfo.getStore().getName())
			.isNowDream(orderInfo.isNowDream())
			.totalPrice(orderInfo.getTotalPrice())
			.orderDate(orderInfo.getOrderDate())
			.build();
	}
}
