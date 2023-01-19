package com.example.bookstore.type;

import static com.example.bookstore.type.ErrorCode.INVALID_ORDER_PROPERTY;

import com.example.bookstore.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

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

	public static Sort getSort(Order order) {

		Sort sort;

		if (order.getOrder().equals("desc")) {
			sort = Sort.by(order.getProperties()).descending();
		} else if (order.getOrder().equals("asc")) {
			sort = Sort.by(order.getProperties()).ascending();
		} else {
			throw new CustomException(INVALID_ORDER_PROPERTY, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return sort;
	}
}
