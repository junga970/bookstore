package com.example.bookstore.service;

import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_SUB_CATEGORY_ID;
import static com.example.bookstore.type.ErrorCode.INVALID_ORDER_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.bookstore.dto.BookCondition;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookDocument;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.BookSearchRepository;
import com.example.bookstore.repository.SubCategoryRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock
	private SubCategoryRepository subCategoryRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BookSearchRepository bookSearchRepository;

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

		List<Book> bookList = Arrays.asList(
			Book.builder()
				.title("테스트제목1")
				.build(),
			Book.builder()
				.title("테스트제목2")
				.build(),
			Book.builder()
				.title("테스트제목3")
				.build()
		);

		Page<Book> books = new PageImpl<>(bookList);

		given(subCategoryRepository.existsById(anyLong())).willReturn(true);
		given(bookRepository.findBySubCategoryId(anyLong(), any())).willReturn(books);

		// when
		Page<BookCondition> pageInfo = bookService.getBooksByCategory(subCategoryId, orderValue,
			page);

		// then
		assertEquals("테스트제목1", pageInfo.getContent().get(0).getTitle());
		assertEquals("테스트제목2", pageInfo.getContent().get(1).getTitle());
		assertEquals("테스트제목3", pageInfo.getContent().get(2).getTitle());
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(3, pageInfo.getTotalElements());
	}

	@Test
	@DisplayName("도서 조회 실패 - 존재하지 않는 서브카테고리")
	void getBooksByCategoryFailure_DoesNotExistSubCategoryId() {
		// given
		Long subCategoryId = 100L;
		String orderValue = "bestseller";
		Integer page = 0;

		given(subCategoryRepository.existsById(anyLong())).willReturn(false);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> bookService.getBooksByCategory(subCategoryId, orderValue, page));

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

		given(subCategoryRepository.existsById(anyLong())).willReturn(true);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> bookService.getBooksByCategory(subCategoryId, orderValue, page));

		// then
		assertEquals(INVALID_ORDER_VALUE, exception.getErrorCode());
		assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
	}

	/*
		도서 or 작가 검색 테스트
	*/
	@Test
	@DisplayName("도서 or 작가 검색 - 성공")
	void searchBooksSuccess() {
		// given
		List<BookDocument> bookDocumentList = Arrays.asList(
			BookDocument.builder()
				.title("자바스프링")
				.authors(Arrays.asList("테스트 작가", "백엔드"))
				.build(),
			BookDocument.builder()
				.title("테스트 DB")
				.authors(Arrays.asList("프론트"))
				.build()
		);

		Page<BookDocument> bookDocuments = new PageImpl<>(bookDocumentList);

		given(bookSearchRepository.findByTitleOrAuthors(anyString(), anyString(), any()))
			.willReturn(bookDocuments);

		// when
		Page<BookCondition> pageInfo = bookService.searchBooks("테스트", 0);

		// then
		assertEquals("자바스프링", pageInfo.getContent().get(0).getTitle());
		assertEquals("테스트 작가", pageInfo.getContent().get(0).getAuthors().get(0));
		assertEquals("백엔드", pageInfo.getContent().get(0).getAuthors().get(1));

		assertEquals("테스트 DB", pageInfo.getContent().get(1).getTitle());
		assertEquals("프론트", pageInfo.getContent().get(1).getAuthors().get(0));
		assertEquals(1, pageInfo.getTotalPages());
		assertEquals(2, pageInfo.getTotalElements());
	}
}