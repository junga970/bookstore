package com.example.bookstore.service;


import com.example.bookstore.dto.OrderDetailCondition;
import com.example.bookstore.dto.OrderInfoCondition;
import com.example.bookstore.entity.OrderDetail;
import com.example.bookstore.entity.OrderInfo;
import com.example.bookstore.repository.OrderDetailRepository;
import com.example.bookstore.repository.OrderInfoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderInfoRepository orderInfoRepository;
	private final OrderDetailRepository orderDetailRepository;

	private final int SIZE = 10;

	public Page<OrderInfoCondition> getOrderInfo(
		Long userId, LocalDate startDate, LocalDate endDate, Integer page) {

		Pageable pageable = PageRequest.of(page, SIZE);

		Page<OrderInfo> orderInfo = orderInfoRepository.findAllByUserIdAndOrderDateBetween(
				userId, startDate.atStartOfDay(), endDate.atStartOfDay(), pageable);

		return orderInfo.map(OrderInfoCondition::fromEntity);
	}

	public List<OrderDetailCondition> getOrderDetail(String orderInfoId) {

		List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderInfoId(orderInfoId);

		return orderDetails.stream()
			.map(OrderDetailCondition::fromEntity).collect(Collectors.toList());
	}
}
