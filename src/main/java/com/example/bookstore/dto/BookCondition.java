package com.example.bookstore.dto;

import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.BookDocument;
import java.time.LocalDate;
import java.util.List;
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
public class BookCondition {

	private Long bookId;
	private String title;
	private String imageUrl;
	private String publisher;
	private LocalDate publicationDate;
	private List<String> authors;
	private Integer price;
	private Integer discountPrice;

	public static BookCondition fromEntity(Book book) {

		return BookCondition.builder()
			.bookId(book.getId())
			.title(book.getTitle())
			.imageUrl(book.getImageUrl())
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.authors(book.getAuthors())
			.price(book.getPrice())
			.discountPrice(book.getDiscountPrice())
			.build();
	}

	public static BookCondition fromDocument(BookDocument bookDocument) {

		return BookCondition.builder()
			.bookId(bookDocument.getId())
			.title(bookDocument.getTitle())
			.imageUrl(bookDocument.getImageUrl())
			.publisher(bookDocument.getPublisher())
			.publicationDate(bookDocument.getPublicationDate())
			.authors(bookDocument.getAuthors())
			.price(bookDocument.getPrice())
			.discountPrice(bookDocument.getDiscountPrice())
			.build();
	}
}
