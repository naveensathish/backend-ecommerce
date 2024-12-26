package com.example.loginregister.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.loginregister.entity.StoredImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<StoredImage, Long> {
	Optional<StoredImage> findById(Long id);

	@Query("SELECT p.id FROM StoredImage p")
	List<String> findAllIds();
}