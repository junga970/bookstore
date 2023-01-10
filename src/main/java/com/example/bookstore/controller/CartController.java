package com.example.bookstore.controller;

import static com.example.bookstore.type.ResponseCode.ADD_BOOK_TO_CART;
import static com.example.bookstore.type.ResponseCode.DELETE_BOOK_IN_CART;
import static com.example.bookstore.type.ResponseCode.DELETE_CART;
import static com.example.bookstore.type.ResponseCode.GET_CART;
import static com.example.bookstore.type.ResponseCode.UPDATE_QUANTITY_OF_BOOK_IN_CART;

import com.example.bookstore.dto.CartItemInfo;
import com.example.bookstore.dto.CartQuantity;
import com.example.bookstore.dto.response.ApiResponse;
import com.example.bookstore.service.CartService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {

	private final CartService cartService;

	@PostMapping("/{bookId}")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse addBookToCart(
		@AuthenticationPrincipal UserDetails user,
		@PathVariable Long bookId,
		@RequestBody @Valid CartQuantity request) {

		String userEmail = user.getUsername();
		Integer quantity = request.getQuantity();

		cartService.addBookToCart(userEmail, bookId, quantity);

		return new ApiResponse(ADD_BOOK_TO_CART);
	}

	@PatchMapping("/{bookId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse updateQuantityOfBookInCart(
		@AuthenticationPrincipal UserDetails user,
		@PathVariable Long bookId,
		@RequestBody @Valid CartQuantity request) {

		String userEmail = user.getUsername();
		Integer quantity = request.getQuantity();

		cartService.updateQuantityOfBookInCart(userEmail, bookId, quantity);

		return new ApiResponse(UPDATE_QUANTITY_OF_BOOK_IN_CART);
	}

	@DeleteMapping("/{bookId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse DeleteBookInCart(
		@AuthenticationPrincipal UserDetails user, @PathVariable Long bookId) {

		String userEmail = user.getUsername();

		cartService.DeleteBookInCart(userEmail, bookId);

		return new ApiResponse(DELETE_BOOK_IN_CART);
	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<List<CartItemInfo>> getCart(@AuthenticationPrincipal UserDetails user) {

		String userEmail = user.getUsername();

		List<CartItemInfo> cart = cartService.getCart(userEmail);

		return new ApiResponse(cart, GET_CART);
	}

	@DeleteMapping()
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse DeleteCart(@AuthenticationPrincipal UserDetails user) {

		String userEmail = user.getUsername();

		cartService.DeleteCart(userEmail);

		return new ApiResponse(DELETE_CART);
	}
}