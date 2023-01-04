package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	@Query(value = "SELECT * FROM book b \n"
		+ "left join sub_category sc on b.sub_category_id = sc.id   \n"
		+ "left join (SELECT book_id  , SUM(quantity) sales_quantity FROM order_book ob \n"
		+ "GROUP BY book_id) b on b.id = b.book_id \n"
		+ "where sc.id = :id", nativeQuery = true)
	List<Book> findBySubCategoryId(@Param(value = "id") Long subCategoryId, Pageable pageable);
}
