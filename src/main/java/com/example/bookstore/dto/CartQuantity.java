package com.example.bookstore.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartQuantity {

	@NotNull()
	@Min(1)
	private Integer quantity;
}
