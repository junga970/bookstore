package com.example.bookstore.service;

import static com.example.bookstore.type.ErrorCode.DOES_NOT_EXIST_BOOK_ID;
import static com.example.bookstore.type.ErrorCode.SOLD_OUT;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.bookstore.dto.request.NowDreamOrderRequest;
import com.example.bookstore.dto.condition.NowDreamStockCondition;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.CartItem;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class NowDreamServiceTest {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private OrderDetailRepository orderDetailRepository;

	@Mock
	private CartItemRepository cartItemRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private OrderInfoRepository orderInfoRepository;

	@Mock
	private RedissonClient redissonClient;

	@Mock
	private RBucket rBucket;

	@Mock
	private RLock lock;

	@InjectMocks
	private NowDreamService nowDreamService;


	/*
		???????????? ?????????
	*/
	@Test
	@DisplayName("???????????? ?????? ??????")
	public void orderByNowDreamSuccess() throws InterruptedException {
		// given
		User user = User.builder()
			.name("?????????")
			.build();

		Store store = Store.builder()
			.name("????????????")
			.build();

		Book book = Book.builder()
			.title("test")
			.discountPrice(1000)
			.build();

		CartItem cartItem = CartItem.builder()
			.book(book)
			.user(user)
			.quantity(1)
			.build();

		OrderInfo orderInfo = OrderInfo.builder()
			.id("dasdsfaadsf1289378943718597")
			.build();

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(orderInfoRepository.existsById(anyString())).willReturn(false);
		given(orderInfoRepository.findById(anyString())).willReturn(Optional.of(orderInfo));
		given(redissonClient.getLock(anyString())).willReturn(lock);
		given(lock.tryLock(anyLong(), anyLong(), any())).willReturn(true);
		given(cartItemRepository.findById(anyLong())).willReturn(Optional.of(cartItem));
		given(redissonClient.getBucket(anyString())).willReturn(rBucket);

		// ?????? 100??? ??????
		given(rBucket.get()).willReturn(100);

		NowDreamOrderRequest nowDreamOrderRequest = new NowDreamOrderRequest(Arrays.asList(1L));

		// when
		nowDreamService.orderByNowDream(1L, 1L, nowDreamOrderRequest);

		// then
		verify(userRepository, times(1)).findById(anyLong());
		verify(storeRepository, times(1)).findById(anyLong());
		verify(orderInfoRepository, times(1)).existsById(anyString());
		verify(orderInfoRepository, times(1)).findById(anyString());
		verify(orderInfoRepository, times(2)).save(any());
		verify(cartItemRepository, times(1)).findById(anyLong());
		verify(cartItemRepository, times(1)).deleteById(anyLong());
		verify(orderDetailRepository, times(1)).save(any());

		verify(redissonClient, times(1)).getLock(anyString());
		verify(redissonClient, times(2)).getBucket(anyString());
		verify(rBucket, times(1)).get();
		verify(rBucket, times(1)).set(anyInt());
		verify(lock, times(1)).tryLock(anyLong(), anyLong(), any());
		verify(lock, times(1)).isLocked();
	}


	@Test
	@DisplayName("???????????? ?????? ?????? - ?????? ??????")
	public void orderByNowDreamFailure_SoldOut() throws InterruptedException {
		// given
		User user = User.builder()
			.name("?????????")
			.build();

		Store store = Store.builder()
			.name("????????????")
			.build();

		Book book = Book.builder()
			.title("test")
			.discountPrice(1000)
			.build();

		CartItem cartItem = CartItem.builder()
			.book(book)
			.user(user)
			.quantity(1)
			.build();

		OrderInfo orderInfo = OrderInfo.builder()
			.id("dasdsfaadsf1289378943718597")
			.build();

		given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
		given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));
		given(orderInfoRepository.existsById(anyString())).willReturn(false);
		given(orderInfoRepository.findById(anyString())).willReturn(Optional.of(orderInfo));
		given(redissonClient.getLock(anyString())).willReturn(lock);
		given(lock.tryLock(anyLong(), anyLong(), any())).willReturn(true);
		given(cartItemRepository.findById(anyLong())).willReturn(Optional.of(cartItem));
		given(redissonClient.getBucket(anyString())).willReturn(rBucket);

		// ?????? 0??? ??????
		given(rBucket.get()).willReturn(0);

		NowDreamOrderRequest nowDreamOrderRequest = new NowDreamOrderRequest(Arrays.asList(1L));

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> nowDreamService.orderByNowDream(1L, 1L, nowDreamOrderRequest));

		// then
		assertEquals(SOLD_OUT, exception.getErrorCode());
		assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
	}

	@Test
	@DisplayName("????????? ?????? ?????? ??????")
	void getStockByStoresSuccess() {
		// given
		Long bookId = 4L;

		Store store1 = Store.builder()
			.name("?????????")
			.address("?????? 1234")
			.build();
		store1.setId(1L);

		Store store2 = Store.builder()
			.name("?????????")
			.address("?????? 1234")
			.build();
		store1.setId(2L);

		Store store3 = Store.builder()
			.name("??????????????????")
			.address("??????????????? 1234")
			.build();
		store1.setId(3L);

		List<Store> stores = Arrays.asList(store1, store2, store3);

		given(bookRepository.existsById(anyLong())).willReturn(true);
		given(storeRepository.findAll()).willReturn(stores);
		given(redissonClient.getBucket(anyString())).willReturn(rBucket);
		given(rBucket.get()).willReturn(50);

		// when
		List<NowDreamStockCondition> nowDreamStockConditionList = nowDreamService.getStockByStores(
			bookId);

		// then
		assertEquals(nowDreamStockConditionList.get(0).getStore().getName(), "?????????");
		assertEquals(nowDreamStockConditionList.get(0).getStock(), 50);

		assertEquals(nowDreamStockConditionList.get(1).getStore().getName(), "?????????");
		assertEquals(nowDreamStockConditionList.get(1).getStock(), 50);

		assertEquals(nowDreamStockConditionList.get(2).getStore().getName(), "??????????????????");
		assertEquals(nowDreamStockConditionList.get(2).getStock(), 50);
	}

	@Test
	@DisplayName("????????? ?????? ?????? ?????? - ???????????? ?????? ??????")
	void getStockByStoresFailure_DoesNotExistBookId() {
		// given
		Long bookId = 4L;

		given(bookRepository.existsById(anyLong())).willReturn(false);

		// when
		CustomException exception = assertThrows(CustomException.class,
			() -> nowDreamService.getStockByStores(bookId));

		// then
		assertEquals(DOES_NOT_EXIST_BOOK_ID, exception.getErrorCode());
		assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
	}
}