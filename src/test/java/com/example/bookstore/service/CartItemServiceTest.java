package com.example.bookstore.service;

import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_BOOK_ID;
import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_CART_ITEM_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.bookstore.dto.CartItemInfo;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class CartItemServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private CartItemRepository cartItemRepository;

	@InjectMocks
	private CartService cartService;

	/*
		장바구니 테스트
	*/
	@Test
	@DisplayName("장바구니 도서 추가 성공 - 장바구니에 존재하지 않는 도서")
	void addBookToCartSuccess_Add() {
		// given
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Book book = Book.builder()
			.title("자바 스프링")
			.build();
		book.setId(10L);

		Integer quantity = 10;

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));
		given(cartItemRepository.findByUserIdAndBookId(any(), anyLong())).willReturn(Optional.empty());

		// when
		cartService.addBookToCart(user.getId(), book.getId(), quantity);

		// then
		verify(userRepository, times(1)).findById(anyLong());
		verify(bookRepository, times(1)).findById(anyLong());
		verify(cartItemRepository, times(1)).findByUserIdAndBookId(anyLong(), anyLong());
		verify(cartItemRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("장바구니 도서 추가 성공 - 장바구니에 이미 존재하는 도서")
	void addBookToCartSuccess_Update() {
		// given
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Book book = Book.builder()
			.title("자바 스프링")
			.build();
		book.setId(10L);

		CartItem cartItem = CartItem.builder()
			.user(user)
			.book(book)
			.quantity(5)
			.build();

		Integer quantity = 10;

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));
		given(cartItemRepository.findByUserIdAndBookId(anyLong(), anyLong())).willReturn(
			Optional.of(cartItem));

		// when
		cartService.addBookToCart(user.getId(), book.getId(), quantity);

		// then
		assertEquals((Integer) 15, cartItem.getQuantity());
	}

	@Test
	@DisplayName("장바구니 도서 추가 실패 - 존재하지 않는 도서")
	void addBookToCartFailure_DoesNotExistBookId() {
		// given
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Long bookId = 10L;
		Integer quantity = 10;

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(bookRepository.findById(anyLong())).willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.addBookToCart(user.getId(), bookId, quantity));

		// then
		assertEquals(DOES_NOT_EXIST_BOOK_ID, exception.getErrorCode());
		assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
	}

	@Test
	@DisplayName("장바구니 조회 성공")
	void getCartSuccess() {
		// given
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Book book1 = Book.builder()
			.title("자바 스프링1")
			.discountPrice(1000)
			.build();

		Book book2 = Book.builder()
			.title("자바 스프링2")
			.discountPrice(2000)
			.build();

		Book book3 = Book.builder()
			.title("자바 스프링3")
			.discountPrice(3000)
			.build();

		List<CartItem> cartItemList = Arrays.asList(
			CartItem.builder()
				.user(user)
				.book(book1)
				.quantity(5)
				.build(),
			CartItem.builder()
				.user(user)
				.book(book2)
				.quantity(5)
				.build(),
			CartItem.builder()
				.user(user)
				.book(book3)
				.quantity(5)
				.build()
		);

		given(cartItemRepository.findAllByUserId(anyLong())).willReturn(cartItemList);

		// when
		List<CartItemInfo> cart = cartService.getCart(user.getId());

		// then
		assertEquals("자바 스프링1", cart.get(0).getBookInfo().getTitle());
		assertEquals((Integer) 1000, cart.get(0).getBookInfo().getDiscountPrice());
		assertEquals((Integer) 5, cart.get(0).getQuantity());
		assertEquals((Integer) 5000, cart.get(0).getTotalPrice());

		assertEquals("자바 스프링2", cart.get(1).getBookInfo().getTitle());
		assertEquals((Integer) 2000, cart.get(1).getBookInfo().getDiscountPrice());
		assertEquals((Integer) 5, cart.get(1).getQuantity());
		assertEquals((Integer) 10000, cart.get(1).getTotalPrice());

		assertEquals("자바 스프링3", cart.get(2).getBookInfo().getTitle());
		assertEquals((Integer) 3000, cart.get(2).getBookInfo().getDiscountPrice());
		assertEquals((Integer) 5, cart.get(2).getQuantity());
		assertEquals((Integer) 15000, cart.get(2).getTotalPrice());
	}

	@Test
	@DisplayName("장바구니 도서 수량 업데이트 성공")
	void updateQuantityOfBookInCartSuccess() {
		// given
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Book book = Book.builder()
			.title("자바 스프링1")
			.discountPrice(1000)
			.build();
		book.setId(10L);

		CartItem cartItem = CartItem.builder()
			.user(user)
			.book(book)
			.quantity(5)
			.build();

		Integer quantity = 10;

		given(cartItemRepository.findByUserIdAndBookId(anyLong(), anyLong())).willReturn(Optional.of(cartItem));

		// when
		cartService.updateQuantityOfBookInCart(user.getId(), book.getId(), quantity);

		// then
		assertEquals((Integer) 10, cartItem.getQuantity());
	}

	@Test
	@DisplayName("장바구니 도서 수량 업데이트 실패 - 존재하지 않는 장바구니 상품")
	void updateQuantityOfBookInCartFailure_DoesNotExistCartItemId() {
		// given
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Long bookId = 10L;
		Integer quantity = 10;

		given(cartItemRepository.findByUserIdAndBookId(anyLong(), anyLong())).willReturn(Optional.empty());

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> cartService.updateQuantityOfBookInCart(user.getId(), bookId, quantity));

		// then
		assertEquals(DOES_NOT_EXIST_CART_ITEM_ID, exception.getErrorCode());
		assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
	}

	@Test
	@DisplayName("장바구니 도서 삭제 성공")
	void DeleteBookInCartSuccess() {
		// given
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Book book = Book.builder()
			.title("자바 스프링1")
			.discountPrice(1000)
			.build();
		book.setId(10L);

		CartItem cartItem = CartItem.builder()
			.user(user)
			.book(book)
			.quantity(5)
			.build();

		Integer quantity = 10;

		given(cartItemRepository.findByUserIdAndBookId(anyLong(), anyLong())).willReturn(Optional.of(cartItem));

		// when
		cartService.deleteBookInCart(user.getId(), book.getId());

		// then
		verify(cartItemRepository, times(1)).findByUserIdAndBookId(anyLong(), anyLong());
		verify(cartItemRepository, times(1)).delete(any());
	}

	@Test
	@DisplayName("장바구니 상품 전체 삭제 성공")
	void DeleteCartSuccess() {
		User user = User.builder()
			.email("user@test.com")
			.build();
		user.setId(1L);

		Book book1 = Book.builder()
			.title("자바 스프링1")
			.discountPrice(1000)
			.build();

		Book book2 = Book.builder()
			.title("자바 스프링2")
			.discountPrice(2000)
			.build();

		Book book3 = Book.builder()
			.title("자바 스프링3")
			.discountPrice(3000)
			.build();

		List<CartItem> cartItemList = Arrays.asList(
			CartItem.builder()
				.user(user)
				.book(book1)
				.quantity(5)
				.build(),
			CartItem.builder()
				.user(user)
				.book(book2)
				.quantity(5)
				.build(),
			CartItem.builder()
				.user(user)
				.book(book3)
				.quantity(5)
				.build()
		);

		Integer quantity = 10;

		given(cartItemRepository.findAllByUserId(anyLong())).willReturn(cartItemList);

		// when
		cartService.deleteCart(user.getId());

		// then
		verify(cartItemRepository, times(1)).findAllByUserId(anyLong());
		verify(cartItemRepository, times(1)).deleteAll(anyList());
	}
}