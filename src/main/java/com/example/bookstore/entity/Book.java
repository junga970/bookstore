package com.example.bookstore.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
public class Book extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "sub_category_id")
	private SubCategory subCategory;

	private String title;
	private String imageUrl;
	private String publisher;
	private LocalDate publicationDate;

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private Map<String, List<String>> author;

	private Integer price;
	private Integer discountPrice;
}
