package com.example.bookstore.controller;

import static com.example.bookstore.type.ResponseCode.GET_STOCK_BY_STORES;
import static com.example.bookstore.type.ResponseCode.NOW_DREAM_ORDER_SUCCESSFUL;

import com.example.bookstore.dto.NowDreamCartItemIds;
import com.example.bookstore.dto.NowDreamStock;
import com.example.bookstore.dto.response.ApiResponse;
import com.example.bookstore.service.NowDreamService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/now-dream")
public class NowDreamController {

	private final NowDreamService nowDreamService;

	@PostMapping("/{storeId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse orderByNowDream(
		@AuthenticationPrincipal UserDetails user,
		@PathVariable Long storeId,
		@RequestBody @Valid NowDreamCartItemIds request) {

		Long userId = Long.parseLong(user.getUsername());

		nowDreamService.orderByNowDream(userId, storeId, request);

		return new ApiResponse(NOW_DREAM_ORDER_SUCCESSFUL);
	}

	@GetMapping("/stock/{bookId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<List<NowDreamStock>> getStockByStores(@PathVariable Long bookId) {

		List<NowDreamStock> nowDreamStockList = nowDreamService.getStockByStores(bookId);

		return new ApiResponse(nowDreamStockList, GET_STOCK_BY_STORES);
	}
}
