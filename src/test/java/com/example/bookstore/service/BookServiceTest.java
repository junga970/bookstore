package com.example.bookstore.service;

import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_SUB_CATEGORY_ID;
import static com.example.bookstore.type.ErrorCode.INVALID_ORDER_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.example.bookstore.dto.BookInfo;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.SubCategoryRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock
	private SubCategoryRepository subCategoryRepository;

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookService bookService;

	/*
		도서 조회 테스트

		orderValue
		new : 최신순
	    high : 높은 가격순
	    row : 낮은 가격순
	    bestseller : 판매순
	*/
	@Test
	@DisplayName("도서 조회 성공")
	void getBooksByCategorySuccess() {

		// given
		Long subCategoryId = 1L;
		String orderValue = "bestseller";
		Integer page = 0;
		Integer size = 10;

		List<Book> books = Arrays.asList(
			Book.builder()
				.title("테스트제목1")
				.image(null)
				.publisher(null)
				.publicationDate(null)
				.author(null)
				.price(null)
				.discountPrice(null)
				.build(),
			Book.builder()
				.title("테스트제목2")
				.image(null)
				.publisher(null)
				.publicationDate(null)
				.author(null)
				.price(null)
				.discountPrice(null)
				.build(),
			Book.builder()
				.title("테스트제목3")
				.image(null)
				.publisher(null)
				.publicationDate(null)
				.author(null)
				.price(null)
				.discountPrice(null)
				.build()
		);

		given(subCategoryRepository.existsById(anyLong())).willReturn(true);
		given(bookRepository.findBySubCategoryId(anyLong(), any())).willReturn(books);

		// when
		List<BookInfo> bookInfos = bookService.getBooksByCategory(
			subCategoryId,
			orderValue,
			page,
			size
		);

		// then
		assertEquals("테스트제목1", bookInfos.get(0).getTitle());
		assertEquals("테스트제목2", bookInfos.get(1).getTitle());
		assertEquals("테스트제목3", bookInfos.get(2).getTitle());
	}

	@Test
	@DisplayName("도서 조회 실패 - 존재하지 않는 서브카테고리")
	void getBooksByCategoryFailure_DoesNotExistSubCategoryId() {

		// given
		Long subCategoryId = 100L;
		String orderValue = "bestseller";
		Integer page = 0;
		Integer size = 10;

		given(subCategoryRepository.existsById(anyLong())).willReturn(false);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> bookService.getBooksByCategory(subCategoryId, orderValue, page, size));

		// then
		assertEquals(DOES_NOT_EXIST_SUB_CATEGORY_ID, exception.getErrorCode());
		assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
	}

	@Test
	@DisplayName("도서 조회 실패 - 잘못된 정렬 값")
	void getBooksByCategoryFailure_InvalidOrderValue() {

		// given
		Long subCategoryId = 1L;
		String orderValue = "TEST";
		Integer page = 0;
		Integer size = 10;

		given(subCategoryRepository.existsById(anyLong())).willReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> bookService.getBooksByCategory(subCategoryId, orderValue, page, size));

		// then
		assertEquals(INVALID_ORDER_VALUE, exception.getErrorCode());
		assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
	}
}