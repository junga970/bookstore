package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_BOOK_ID;
import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_CART_ITEM_ID;
import static com.example.bookstore.type.ErrorCode.USER_NOT_FOUND;

import com.example.bookstore.dto.CartItemInfo;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
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
	private final CartRepository cartRepository;

	public void addBookToCart(Long userId, Long bookId, Integer quantity) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));

		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new CustomException(DOES_NOT_EXIST_BOOK_ID, HttpStatus.NOT_FOUND));

		cartRepository.findByUserIdAndBookId(userId, bookId).ifPresentOrElse(
			cartItem -> {
				cartItem.setQuantity(cartItem.getQuantity() + quantity);
				cartRepository.save(cartItem);
			},
			() -> cartRepository.save(CartItem.builder()
				.user(user)
				.book(book)
				.quantity(quantity)
				.build())
		);
	}

	public List<CartItemInfo> getCart(Long userId) {

		List<CartItem> cartItem = cartRepository.findAllByUserId(userId);

		return cartItem.stream().map(CartItemInfo::fromEntity).collect(Collectors.toList());
	}

	public void updateQuantityOfBookInCart(Long userId, Long bookId, Integer quantity) {

		CartItem cartItem = cartRepository.findByUserIdAndBookId(userId, bookId)
			.orElseThrow(() -> new CustomException(DOES_NOT_EXIST_CART_ITEM_ID, HttpStatus.NOT_FOUND));

		cartItem.setQuantity(quantity);
		cartRepository.save(cartItem);
	}

	public void deleteBookInCart(Long userId, Long bookId) {

		CartItem cartItem = cartRepository.findByUserIdAndBookId(userId, bookId)
			.orElseThrow(() -> new CustomException(DOES_NOT_EXIST_CART_ITEM_ID, HttpStatus.NOT_FOUND));

		cartRepository.delete(cartItem);
	}

	public void deleteCart(Long userId) {

		List<CartItem> cartItem = cartRepository.findAllByUserId(userId);

		cartRepository.deleteAll(cartItem);
	}
}
