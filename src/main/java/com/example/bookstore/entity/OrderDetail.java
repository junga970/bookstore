package com.example.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class OrderDetail extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;


	@ManyToOne
	@JoinColumn(name = "order_info_id")
	private OrderInfo orderInfo;

	private Integer quantity;
	private Integer price;
}
