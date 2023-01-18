package com.example.bookstore.dto;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.OrderDetail;
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
public class OrderDetailCondition {

	private BookInfo bookInfo;
	private Integer quantity;
	private Integer price;

	public static OrderDetailCondition fromEntity(OrderDetail orderDetail) {

		Book book = orderDetail.getBook();

		return OrderDetailCondition.builder()
			.bookInfo(BookInfo.fromEntity(book))
			.quantity(orderDetail.getQuantity())
			.price(orderDetail.getPrice())
			.build();
	}
}
