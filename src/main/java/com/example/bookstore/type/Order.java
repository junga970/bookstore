package com.example.bookstore.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Order {

	NEW("new", "publication_date", "desc"),
	HIGH("high", "price", "desc"),
	LOW("low", "price", "asc"),
	BESTSELLER("bestseller", "sales_quantity", "desc");

	private final String value;
	private final String properties;
	private final String order;
}
