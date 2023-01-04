package com.example.bookstore.controller;

import com.example.bookstore.dto.BookInfo;
import com.example.bookstore.dto.response.ApiResponse;
import com.example.bookstore.service.BookService;
import com.example.bookstore.type.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookController {

	private final BookService bookService;

	@GetMapping("/book")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<List<BookInfo>> getBooksByCategory(
		@RequestParam(value = "sub-category", defaultValue = "1") Long subCategoryId,
		@RequestParam(value = "order", defaultValue = "bestseller") String orderValue,
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "size", defaultValue = "10") Integer size) {

		List<BookInfo> bookInfos = bookService.getBooksByCategory(
			subCategoryId,
			orderValue,
			page,
			size
		);

		return new ApiResponse(bookInfos, ResponseCode.GET_BOOKS);
	}
}
