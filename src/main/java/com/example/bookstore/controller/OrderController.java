package com.example.bookstore.controller;

import static com.example.bookstore.type.ResponseCode.GET_ORDER_DETAIL;
import static com.example.bookstore.type.ResponseCode.GET_ORDER_INFO;

import com.example.bookstore.dto.OrderDetailCondition;
import com.example.bookstore.dto.OrderInfoCondition;
import com.example.bookstore.dto.response.ApiResponse;
import com.example.bookstore.service.OrderService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderService;

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<Page<OrderInfoCondition>> getOrderInfo(
		@AuthenticationPrincipal UserDetails user,
		@RequestParam(value = "start-date") @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
		@RequestParam(value = "end-date") @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
		@RequestParam(defaultValue = "0") Integer page) {

		Long userId = Long.parseLong(user.getUsername());

		Page<OrderInfoCondition> orderInfoConditionPage =
			orderService.getOrderInfo(userId, startDate, endDate, page);

		return new ApiResponse(orderInfoConditionPage, GET_ORDER_INFO);
	}


	// 주문 상세 내역 조회, 오더 Id 요청 받기
	@GetMapping("/{orderInfoId}")
	@ResponseStatus(HttpStatus.OK)
	public ApiResponse<List<OrderDetailCondition>> getOrderDetail(
		@AuthenticationPrincipal UserDetails user, @PathVariable String orderInfoId) {

		List<OrderDetailCondition> orderDetailConditions = orderService.getOrderDetail(orderInfoId);

		return new ApiResponse(orderDetailConditions, GET_ORDER_DETAIL);
	}
}
