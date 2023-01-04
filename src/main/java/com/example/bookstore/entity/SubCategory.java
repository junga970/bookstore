package com.example.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubCategory extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	private String name;
}
