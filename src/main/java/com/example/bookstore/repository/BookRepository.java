package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	@Query(
		value = "SELECT * FROM book b\n"
			+ "LEFT JOIN (SELECT book_id, SUM(quantity) sales_quantity FROM order_book GROUP BY book_id) b\n"
			+ "ON b.id = b.book_id WHERE b.sub_category_id = :id",
		countQuery = "SELECT COUNT(*) FROM book WHERE sub_category_id = :id",
		nativeQuery = true
	)
	Page<Book> findBySubCategoryId(@Param(value = "id") Long subCategoryId, Pageable pageable);
}
