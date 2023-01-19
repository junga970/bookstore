package com.example.bookstore.dto;

import com.example.bookstore.entity.CartItem;
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
public class CartItemCondition {

	private Long cartItemId;
	private BookCondition bookCondition;
	private Integer quantity;
	private Integer totalPrice;

	public static CartItemCondition fromEntity(CartItem cartItem) {

		BookCondition bookCondition = BookCondition.fromEntity(cartItem.getBook());
		Integer quantity = cartItem.getQuantity();

		return CartItemCondition.builder()
			.cartItemId(cartItem.getId())
			.bookCondition(bookCondition)
			.quantity(quantity)
			.totalPrice(bookCondition.getDiscountPrice() * quantity)
			.build();
	}
}
