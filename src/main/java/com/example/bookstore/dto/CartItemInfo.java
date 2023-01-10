package com.example.bookstore.dto;

import com.example.bookstore.entity.Cart;
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
public class CartItemInfo {

	private BookInfo bookInfo;
	private Integer quantity;
	private Integer totalPrice;

	public static CartItemInfo fromEntity(Cart cart) {

		BookInfo bookInfo = BookInfo.fromEntity(cart.getBook());
		Integer quantity = cart.getQuantity();

		return CartItemInfo.builder()
			.bookInfo(bookInfo)
			.quantity(quantity)
			.totalPrice(bookInfo.getDiscountPrice() * quantity)
			.build();
	}
}