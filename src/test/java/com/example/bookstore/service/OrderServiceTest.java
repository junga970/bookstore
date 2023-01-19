package com.example.bookstore.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.example.bookstore.dto.OrderDetailCondition;
import com.example.bookstore.dto.OrderInfoCondition;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.OrderDetail;
import com.example.bookstore.entity.OrderInfo;
import com.example.bookstore.entity.Store;
import com.example.bookstore.repository.OrderDetailRepository;
import com.example.bookstore.repository.OrderInfoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@Mock
	private OrderInfoRepository orderInfoRepository;

	@Mock
	private OrderDetailRepository orderDetailRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	@DisplayName("주문 내역 조회 성공")
	void getOrderInfoSuccess() {
		// given
		Long userId = 1L;
		Integer page = 1;
		LocalDate localDate = LocalDate.now();
		LocalDateTime localDateTime = LocalDateTime.now();

		Store store = Store.builder()
			.name("테스트점")
			.build();

		List<OrderInfo> orderInfo = Arrays.asList(
			OrderInfo.builder()
				.id("test1111")
				.store(store)
				.isNowDream(true)
				.totalPrice(10000)
				.orderDate(localDateTime)
				.build()
		);

		Page<OrderInfo> orderInfoPage = new PageImpl<>(orderInfo);

		given(orderInfoRepository.findAllByUserIdAndOrderDateBetween(anyLong(), any(), any(), any()))
			.willReturn(orderInfoPage);

		// when
		Page<OrderInfoCondition> orderInfoConditionPage =
			orderService.getOrderInfo(userId, localDate, localDate, page);

		// then
		assertEquals("test1111", orderInfoConditionPage.getContent().get(0).getOrderInfoId());
		assertEquals("테스트점", orderInfoConditionPage.getContent().get(0).getStoreName());
		assertEquals(true, orderInfoConditionPage.getContent().get(0).isNowDream());
		assertEquals((Integer) 10000, orderInfoConditionPage.getContent().get(0).getTotalPrice());
		assertEquals(localDateTime, orderInfoConditionPage.getContent().get(0).getOrderDate());
	}

	@Test
	@DisplayName("주문 상세내역 조회 성공")
	void getOrderDetailSuccess() {
		// given
		String orderInfoId = "test1111";

		Book book1 = Book.builder()
			.title("타이틀1")
			.build();

		Book book2 = Book.builder()
			.title("타이틀2")
			.build();

		List<OrderDetail> orderDetails = Arrays.asList(
			OrderDetail.builder()
				.book(book1)
				.quantity(10)
				.price(10000)
				.build(),
			OrderDetail.builder()
				.book(book2)
				.quantity(1)
				.price(20000)
				.build()
		);

		given(orderDetailRepository.findAllByOrderInfoId(anyString())).willReturn(orderDetails);

		// when
		List<OrderDetailCondition> orderDetailConditions = orderService.getOrderDetail(orderInfoId);

		// then
		assertEquals("타이틀1", orderDetailConditions.get(0).getBookCondition().getTitle());
		assertEquals((Integer) 10, orderDetailConditions.get(0).getQuantity());
		assertEquals((Integer) 10000, orderDetailConditions.get(0).getPrice());

		assertEquals("타이틀2", orderDetailConditions.get(1).getBookCondition().getTitle());
		assertEquals((Integer) 1, orderDetailConditions.get(1).getQuantity());
		assertEquals((Integer) 20000, orderDetailConditions.get(1).getPrice());
	}
}