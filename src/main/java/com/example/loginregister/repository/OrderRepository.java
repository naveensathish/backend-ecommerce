package com.example.loginregister.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.loginregister.entity.Order;
import com.example.loginregister.entity.OrderItem;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserIdOrderByOrderDateDesc(String userId);

	List<Order> findByUserIdAndEmail(String userId, String email);

	List<Order> findByUserId(String userId);

	List<Order> findByOrderId(String orderId);

	@Query("SELECT o.orderDate FROM Order o WHERE o.userId = :userId")
	String findOrderDateByUserId(String userId);

//	@Query("SELECT o FROM Order o WHERE CAST(o.orderDate AS timestamp) BETWEEN :startDate AND :endDate") 
//	List<Order> findOrdersByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

//	@Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
//	List<Order> findOrdersByOrderDateBetween(String startDate, String endDate);
	
//	@Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
//	List<Order> findOrdersByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

//	@Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
//	List<Order> findOrdersByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
	
	
	//yea below
//	@Query("SELECT o FROM Order o WHERE TO_TIMESTAMP(o.orderDate, 'yyyy-MM-dd HH:mm:ss') BETWEEN :startDate AND :endDate")
//	List<Order> findOrdersByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


//	@Query("SELECT o FROM Order o WHERE TO_TIMESTAMP(o.orderDate, 'YYYY-MM-DD HH24:MI:SS') BETWEEN :startDate AND :endDate")
//	List<Order> findOrdersByOrderDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
//	
	
	@Query("SELECT o FROM Order o WHERE " +
            "CAST(o.orderDate AS timestamp) BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByOrderDateBetween(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

	List<Order> findOrdersByOrderDateBetween(String startDate, String endDate);
	
	List<OrderItem> findItemsByOrderId(String orderId);

	List<Order> findByUserIdAndEmailAndStatusNot(String userId, String email, String status);

//    List<Order> findOrdersByOrderDateBetween(LocalDateTime start, LocalDateTime end);

}
