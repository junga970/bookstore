package com.example.bookstore.repository;

import com.example.bookstore.entity.Store;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	Optional<Store> findByName(String name);
}
