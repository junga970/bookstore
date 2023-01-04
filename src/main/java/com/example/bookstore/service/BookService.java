package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_SUB_CATEGORY_ID;
import static com.example.bookstore.type.ErrorCode.INVALID_ORDER_VALUE;

import com.example.bookstore.dto.BookInfo;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.SubCategoryRepository;
import com.example.bookstore.type.Order;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

	private final SubCategoryRepository subCategoryRepository;
	private final BookRepository bookRepository;

	/*
		orderValue
		new : 최신순
	    high : 높은 가격순
	    row : 낮은 가격순
	    bestseller : 판매순
	*/
	public List<BookInfo> getBooksByCategory(Long subCategoryId, String orderValue, Integer page,
		Integer size) {

		if (!subCategoryRepository.existsById(subCategoryId)) {
			throw new CustomException(DOES_NOT_EXIST_SUB_CATEGORY_ID, HttpStatus.NOT_FOUND);
		}

		Order order = getOrder(orderValue);
		Sort sort = null;
		if (order.getOrder().equals("desc")) {
			sort = Sort.by(order.getProperties()).descending();
		} else if (order.getOrder().equals("asc")) {
			sort = Sort.by(order.getProperties()).ascending();
		}

		Pageable pageable = PageRequest.of(page, size, sort);

		List<Book> books = bookRepository.findBySubCategoryId(subCategoryId, pageable);

		return books.stream().map(BookInfo::fromEntity).collect(Collectors.toList());
	}

	public static Order getOrder(String orderValue) {

		for (Order order : Order.values()) {
			if (order.getValue().equals(orderValue)) {
				return order;
			}
		}

		throw new CustomException(INVALID_ORDER_VALUE, HttpStatus.BAD_REQUEST);
	}
}
