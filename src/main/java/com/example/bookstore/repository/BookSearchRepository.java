package com.example.bookstore.repository;

import com.example.bookstore.entity.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {

	Page<BookDocument> findByTitleOrAuthors(String title, String author, Pageable pageable);
}
