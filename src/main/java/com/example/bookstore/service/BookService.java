package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_SUB_CATEGORY_ID;
import static com.example.bookstore.type.ErrorCode.INVALID_ORDER_VALUE;

import com.example.bookstore.dto.BookCondition;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookDocument;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.BookSearchRepository;
import com.example.bookstore.repository.SubCategoryRepository;
import com.example.bookstore.type.Order;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
	private final BookSearchRepository bookSearchRepository;

	private final int SIZE = 10;

	/*
		orderValue
		new : 최신순
	    high : 높은 가격순
	    row : 낮은 가격순
	    bestseller : 판매순
	*/
	public Page<BookCondition> getBooksByCategory(
		Long subCategoryId, String orderValue, Integer page) {

		if (!subCategoryRepository.existsById(subCategoryId)) {
			throw new CustomException(DOES_NOT_EXIST_SUB_CATEGORY_ID, HttpStatus.NOT_FOUND);
		}

		Optional<Order> optionalOrder = Arrays.stream(Order.values())
			.filter(order -> order.getValue().equals(orderValue)).findFirst();

		if (optionalOrder.isEmpty()) {
			throw new CustomException(INVALID_ORDER_VALUE, HttpStatus.BAD_REQUEST);
		}

		Sort sort = Order.getSort(optionalOrder.get());
		Pageable pageable = PageRequest.of(page, SIZE, sort);
		Page<Book> books = bookRepository.findBySubCategoryId(subCategoryId, pageable);

		return books.map(BookCondition::fromEntity);
	}

	public Page<BookCondition> searchBooks(String keyword, Integer page) {

		Pageable pageable = PageRequest.of(page, SIZE);

		Page<BookDocument> books =
			bookSearchRepository.findByTitleOrAuthors(keyword, keyword, pageable);

		return books.map(BookCondition::fromDocument);
	}
}
