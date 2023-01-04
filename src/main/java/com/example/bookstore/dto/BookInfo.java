package com.example.bookstore.dto;

import com.example.bookstore.entity.Book;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
public class BookInfo {

	private String title;
	private String image;
	private String publisher;
	private LocalDate publicationDate;
	private Map<String, List<String>> author;
	private Integer price;
	private Integer discountPrice;

	public static BookInfo fromEntity(Book book) {

		return BookInfo.builder()
			.title(book.getTitle())
			.image(book.getImage())
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.author(book.getAuthor())
			.price(book.getPrice())
			.discountPrice(book.getDiscountPrice())
			.build();
	}
}
