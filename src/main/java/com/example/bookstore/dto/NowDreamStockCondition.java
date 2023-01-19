package com.example.bookstore.dto;

import com.example.bookstore.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NowDreamStockCondition {

	private Store store;
	private int stock;
}
