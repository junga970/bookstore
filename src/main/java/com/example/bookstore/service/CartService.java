package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_BOOK_ID;
import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_CART_ITEM_ID;

import com.example.bookstore.dto.CartItemInfo;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartRepository;
import com.example.bookstore.repository.UserRepository;
import java.util.List;
import java.util.Optional;
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

	public void addBookToCart(String userEmail, Long bookId, Integer quantity) {

		User user = userRepository.findByEmail(userEmail).get();

		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new CustomException(DOES_NOT_EXIST_BOOK_ID, HttpStatus.NOT_FOUND));

		Optional<Cart> optionalCartItem = cartRepository.findByUserAndBookId(user, bookId);
		Cart cartItem;
		if (optionalCartItem.isPresent()) {
			cartItem = optionalCartItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		} else {
			cartItem = Cart.builder()
				.user(user)
				.book(book)
				.quantity(quantity)
				.build();
		}

		cartRepository.save(cartItem);
	}

	public List<CartItemInfo> getCart(String userEmail) {

		User user = userRepository.findByEmail(userEmail).get();
		List<Cart> cart = cartRepository.findAllByUser(user);

		return cart.stream().map(CartItemInfo::fromEntity).collect(Collectors.toList());
	}

	public void updateQuantityOfBookInCart(String userEmail, Long bookId, Integer quantity) {

		User user = userRepository.findByEmail(userEmail).get();

		Cart cartItem = cartRepository.findByUserAndBookId(user, bookId)
			.orElseThrow(() -> new CustomException(DOES_NOT_EXIST_CART_ITEM_ID, HttpStatus.NOT_FOUND));

		cartItem.setQuantity(quantity);
		cartRepository.save(cartItem);
	}

	public void DeleteBookInCart(String userEmail, Long bookId) {

		User user = userRepository.findByEmail(userEmail).get();

		Cart cartItem = cartRepository.findByUserAndBookId(user, bookId)
			.orElseThrow(() -> new CustomException(DOES_NOT_EXIST_CART_ITEM_ID, HttpStatus.NOT_FOUND));

		cartRepository.delete(cartItem);
	}

	public void DeleteCart(String userEmail) {

		User user = userRepository.findByEmail(userEmail).get();
		List<Cart> cart = cartRepository.findAllByUser(user);

		cartRepository.deleteAll(cart);
	}
}
