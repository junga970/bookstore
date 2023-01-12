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
public class CartItem extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "book_id")
	Book book;

	@ManyToOne
	@JoinColumn(name = "user_id")
	User user;

	Integer quantity;
}
