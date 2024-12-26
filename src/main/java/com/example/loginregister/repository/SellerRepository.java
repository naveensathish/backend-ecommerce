package com.example.loginregister.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.loginregister.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {
	Optional<Seller> findByEmail(String email);

	List<Seller> findByRole(String role);

	Optional<Seller> findById(Long sellerId);
	
	List<Seller> findByUsernameContaining(String username);

	List<Seller> findByUserId(Long userId);

	Seller findByUsername(String username);

//	@Query("SELECT new com.example.loginregister.entity.Seller(s.id, s.username, s.email, s.password, s.role, au.role_appuser, s.userId) "
//			+ "FROM Seller s JOIN User au ON s.userId = au.id " + "WHERE s.userId = :userId")
//	List<Seller> findSellersWithRoleByUserId(@Param("userId") Long userId);
	
	@Query("SELECT s FROM Seller s WHERE s.userId = :userId")
    List<Seller> findSellersWithRoleByUserId(@Param("userId") Long userId);

//	@Modifying
//	@Query("UPDATE Seller s SET s.isActive = :isActive WHERE s.id = :sellerId")
//	int updateSellerStatus(@Param("sellerId") Long sellerId, @Param("isActive") boolean isActive);

}
