package com.example.bookstore.dto.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
public class BookRequest {

	@NotNull
	private Long subCategoryId;

	@NotBlank
	private String title;

	@NotBlank
	private String imageUrl;

	@NotBlank
	private String publisher;

	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate publicationDate;

	@NotEmpty
	private List<@Valid String> authors;

	@NotNull
	private Integer price;

	@NotNull
	private Integer discountPrice;
}
