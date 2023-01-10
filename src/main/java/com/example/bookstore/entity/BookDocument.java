package com.example.bookstore.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "book")
@Mapping(mappingPath = "elastic/book-mapping.json")
@Setting(settingPath = "elastic/book-setting.json")
public class BookDocument {

	@Id
	private Long id;

	private String title;
	private String imageUrl;
	private String publisher;
	private LocalDate publicationDate;
	private List<String> authors;
	private Integer price;
	private Integer discountPrice;
}
