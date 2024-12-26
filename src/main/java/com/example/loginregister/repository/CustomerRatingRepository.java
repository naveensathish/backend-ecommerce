package com.example.loginregister.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.loginregister.entity.CustomerRating;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CustomerRatingRepository extends JpaRepository<CustomerRating, Long> {

	@Query("SELECT r FROM CustomerRating r WHERE r.productId = :productId")
	List<CustomerRating> findByProductId(@Param("productId") String productId);

	CustomerRating findByUserIdAndProductId(String userId, String productId);

	Optional<CustomerRating> findByUserId(String userId);

	@Query("SELECT r FROM CustomerRating r WHERE r.ratingId = :ratingId")
	List<CustomerRating> findRatingsByRatingId(@Param("ratingId") String ratingId);

}
