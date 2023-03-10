package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_BOOK_ID;
import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_CART_ITEM_ID;
import static com.example.bookstore.type.ErrorCode.USER_NOT_FOUND;

import com.example.bookstore.dto.condition.CartItemCondition;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CartService {

	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final CartItemRepository cartItemRepository;

	public void addBookToCart(Long userId, Long bookId, Integer quantity) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));

		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new CustomException(DOES_NOT_EXIST_BOOK_ID, HttpStatus.NOT_FOUND));

		cartItemRepository.findByUserIdAndBookId(userId, bookId).ifPresentOrElse(
			cartItem -> {
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
				cartItemRepository.save(cartItem);
			},
			() -> cartItemRepository.save(CartItem.builder()
				.user(user)
				.book(book)
				.quantity(quantity)
				.build())
		);
	}

	public List<CartItemCondition> getCart(Long userId) {

		List<CartItem> cartItem = cartItemRepository.findAllByUserId(userId);

		return cartItem.stream().map(CartItemCondition::fromEntity).collect(Collectors.toList());
	}

	public void updateQuantityOfBookInCart(Long userId, Long bookId, Integer quantity) {

		CartItem cartItem = cartItemRepository.findByUserIdAndBookId(userId, bookId)
			.orElseThrow(
				() -> new CustomException(DOES_NOT_EXIST_CART_ITEM_ID, HttpStatus.NOT_FOUND));

		cartItem.setQuantity(quantity);
		cartItemRepository.save(cartItem);
	}

	public void deleteBookInCart(Long userId, Long bookId) {

		CartItem cartItem = cartItemRepository.findByUserIdAndBookId(userId, bookId)
			.orElseThrow(
				() -> new CustomException(DOES_NOT_EXIST_CART_ITEM_ID, HttpStatus.NOT_FOUND));

		cartItemRepository.delete(cartItem);
	}

	public void deleteCart(Long userId) {

		List<CartItem> cartItem = cartItemRepository.findAllByUserId(userId);

		cartItemRepository.deleteAll(cartItem);
	}
}
