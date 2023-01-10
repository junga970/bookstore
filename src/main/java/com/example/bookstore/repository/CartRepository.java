package com.example.bookstore.repository;

import com.example.bookstore.entity.Cart;
import com.example.bookstore.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

	List<Cart> findAllByUser(User user);

	Optional<Cart> findByUserAndBookId(User user, Long bookId);
}
