package com.example.bookstore.service;


import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_BOOK_ID;
import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_CART_ITEM_ID;
import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_ORDER_INFO_ID;
import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_STORE_ID;
import static com.example.bookstore.type.ErrorCode.FAILED_TO_ACQUIRE_LOCK;
import static com.example.bookstore.type.ErrorCode.SOLD_OUT;
import static com.example.bookstore.type.ErrorCode.USER_NOT_FOUND;

import com.example.bookstore.dto.NowDreamOrderRequest;
import com.example.bookstore.dto.NowDreamStockCondition;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.CartItem;
import com.example.bookstore.entity.OrderDetail;
import com.example.bookstore.entity.OrderInfo;
import com.example.bookstore.entity.Store;
import com.example.bookstore.entity.User;
import com.example.bookstore.exception.CustomException;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.OrderDetailRepository;
import com.example.bookstore.repository.OrderInfoRepository;
import com.example.bookstore.repository.StoreRepository;
import com.example.bookstore.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class NowDreamService {

	private final BookRepository bookRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final UserRepository userRepository;
	private final StoreRepository storeRepository;
	private final OrderInfoRepository orderInfoRepository;
	private final CartItemRepository cartItemRepository;
	private final RedissonClient redissonClient;

	private static final String ROCK_NAME = "nowDreamLock";
	private static final Long WAIT_TIME = 1L;
	private static final Long LEASE_TIME = 3L;
	private static final int EMPTY = 0;

	public List<NowDreamStockCondition> getStockByStores(Long bookId) {

		if (!bookRepository.existsById(bookId)) {
			throw new CustomException(DOES_NOT_EXIST_BOOK_ID, HttpStatus.BAD_REQUEST);
		}

		List<Store> stores = storeRepository.findAll();

		List<NowDreamStockCondition> nowDreamStockConditionList = new ArrayList<>();

		for (Store store : stores) {
			String key = createKey(store.getId(), bookId);
			int stock = (int) redissonClient.getBucket(key).get();
			nowDreamStockConditionList.add(new NowDreamStockCondition(store, stock));
		}

		return nowDreamStockConditionList;
	}

	public void orderByNowDream(Long userId, Long storeId, NowDreamOrderRequest request) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));

		Store store = storeRepository.findById(storeId).orElseThrow(
				() -> new CustomException(DOES_NOT_EXIST_STORE_ID, HttpStatus.BAD_REQUEST));

		String uuid = UUID.randomUUID().toString();
		while (orderInfoRepository.existsById(uuid)) {
			uuid = UUID.randomUUID().toString();
		}

		OrderInfo orderInfo = OrderInfo.builder()
			.id(uuid)
			.user(user)
			.store(store)
			.isNowDream(true)
			.build();

		orderInfoRepository.save(orderInfo);

		orderInfo = orderInfoRepository.findById(uuid).orElseThrow(
			() -> new CustomException(DOES_NOT_EXIST_ORDER_INFO_ID, HttpStatus.BAD_REQUEST));

		// lock
		RLock lock = redissonClient.getLock(ROCK_NAME);

		Integer totalPrice = 0;

		try {
			if (!lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS)) {
				throw new CustomException(FAILED_TO_ACQUIRE_LOCK, HttpStatus.SERVICE_UNAVAILABLE);
			}

			// 재고 확인 및 재고 정보 임시 저장
			List<CurrentOrder> currentOrders = checkStock(storeId, request.getCartItemIds());

			// 재고 감소 처리
			for (CurrentOrder currentOrder : currentOrders) {
				Book book = currentOrder.getBook();
				Integer quantity = currentOrder.getQuantity();
				Integer price = book.getDiscountPrice() * quantity;

				String key = createKey(storeId, book.getId());
				redissonClient.getBucket(key).set(currentOrder.getStock() - quantity);

				// 주문 상세 저장
				orderDetailRepository.save(
					OrderDetail.builder()
						.book(book)
						.orderInfo(orderInfo)
						.quantity(quantity)
						.price(price)
						.build()
				);

				// 장바구니 삭제
				cartItemRepository.deleteById(currentOrder.getCartItemId());

				totalPrice += price;
			}

			// 총 금액 저장
			orderInfo.setTotalPrice(totalPrice);
			orderInfoRepository.save(orderInfo);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (lock != null && lock.isLocked()) {
				lock.unlock();
			}
		}
	}

	public List<CurrentOrder> checkStock(Long storeId, List<Long> cartItemIds) {

		List<CurrentOrder> currentOrders = new ArrayList<>();

		for (Long cartItemId : cartItemIds) {

			CartItem cartItem = cartItemRepository.findById(cartItemId)
				.orElseThrow(() -> new CustomException(
					DOES_NOT_EXIST_CART_ITEM_ID,
					HttpStatus.BAD_REQUEST)
				);

			Integer quantity = cartItem.getQuantity();
			Book book = cartItem.getBook();
			Integer price = book.getDiscountPrice() * quantity;

			String key = createKey(storeId, book.getId());

			int stock = (int) redissonClient.getBucket(key).get();

			if (stock - quantity < EMPTY) {
				throw new CustomException(SOLD_OUT, HttpStatus.BAD_REQUEST);
			}

			currentOrders.add(new CurrentOrder(cartItemId, book, price, quantity, stock));
		}

		return currentOrders;
	}

	public String createKey(Long storeId, Long bookId) {
		return "store:" + storeId + ":book:" + bookId + ":stock";
	}

	@Getter
	private class CurrentOrder {

		private Long cartItemId;
		private Book book;
		private Integer price;
		private Integer quantity;
		private Integer stock;

		public CurrentOrder(
			Long cartItemId, Book book, Integer price, Integer quantity, Integer stock) {

			this.cartItemId = cartItemId;
			this.book = book;
			this.price = price;
			this.quantity = quantity;
			this.stock = stock;
		}
	}
}

