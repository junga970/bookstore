package com.example.bookstore.repository;

import com.example.bookstore.entity.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findAllByUserId(Long userId);

	Optional<CartItem> findByUserIdAndBookId(Long userId, Long bookId);
}
