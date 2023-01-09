package com.example.bookstore.controller;

import com.example.bookstore.dto.BookInfo;
import com.example.bookstore.dto.response.ApiResponse;
import com.example.bookstore.service.BookService;
import com.example.bookstore.type.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookController {

	private final BookService bookService;

	@GetMapping("/books")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Page<BookInfo>> getBooksByCategory(
		@RequestParam(value = "sub-category", defaultValue = "1") Long subCategoryId,
		@RequestParam(value = "order", defaultValue = "bestseller") String orderValue,
		@RequestParam(defaultValue = "0") Integer page) {

		Page<BookInfo> pageInfo = bookService.getBooksByCategory(subCategoryId, orderValue, page);

		return new ApiResponse(pageInfo, ResponseCode.GET_BOOKS);
	}

	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Page<BookInfo>> searchBooks(
		@RequestParam(defaultValue = "") String keyword,
		@RequestParam(defaultValue = "0") Integer page) {

		Page<BookInfo> pageInfo = bookService.searchBooks(keyword, page);

		return new ApiResponse(pageInfo, ResponseCode.SEARCH_BOOKS);
	}
}