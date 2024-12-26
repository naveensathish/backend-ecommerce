package com.example.loginregister.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.loginregister.dto.UserRegistrationRequestDto;
import com.example.loginregister.entity.StoredImage;
import com.example.loginregister.entity.User;

@Service
public interface AuthService {

	boolean emailExists(String email);

	List<StoredImage> getAllStoredImages();

	StoredImage addStoredImage(StoredImage storedImage);

	List<StoredImage> getProductsByCategory(String product);

	List<StoredImage> getProductsByProduct(String product);
	 
	List<User> getAllUsers();

	public boolean updatePassword(String username, String newPassword);
	
	List<StoredImage> getProductsByProductAndSubProduct(String product, String subProduct);

	public String getUsernameByEmail(String email);
	
//	CompletableFuture<User> registerUser(UserRegistrationRequestDto registrationRequest);
	
	CompletableFuture<User> registerUser(UserRegistrationRequestDto registrationRequest, String token);
	
	CompletableFuture<User> authenticateUser(String password, String username);
	
	Optional<User> findById(Long userId);

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	UserDetails loadSellerByUsername(String username) throws UsernameNotFoundException;

}