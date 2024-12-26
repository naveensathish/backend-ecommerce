package com.example.loginregister.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginregister.dto.LoginDto;
import com.example.loginregister.dto.UserRegistrationRequestDto;
import com.example.loginregister.entity.Product;
import com.example.loginregister.entity.Role;
import com.example.loginregister.entity.StoredImage;
import com.example.loginregister.entity.User;
import com.example.loginregister.repository.RoleRepository;
import com.example.loginregister.security.JwtUtil;
import com.example.loginregister.service.AuthService;
import com.example.loginregister.service.ProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = { "http://localhost:5000", "http://localhost:5001", "http://localhost:3000",
		"http://localhost:3001" })
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AuthService authService;

	@Autowired
	private ProductService productService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	 @GetMapping("/getUsernameByEmail")
	    public ResponseEntity<String> getUsernameByEmail(@RequestParam String email) {
	        String username = authService.getUsernameByEmail(email);
	        if (username != null) {
	            return ResponseEntity.ok(username); 
	        } else {
	            return ResponseEntity.status(404).body("No accounts linked to this email. Kindly check.");
	        }
	    }
	 
	 @PostMapping("/updatePassword")
	 public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> payload) {
	     String username = payload.get("username");
	     String newPassword = payload.get("newPassword");
	     
	     boolean isUpdated = authService.updatePassword(username, newPassword);
	     
	     if (isUpdated) {
	         return ResponseEntity.ok("\n\nPassword updated successfully");
	     } else { 
	         return ResponseEntity.status(400).body("\n\nUser not found or failed to update password");
	     }
	 }

	@PostMapping("/login")
	public CompletableFuture<ResponseEntity<Map<String, Object>>> login(@Valid @RequestBody LoginDto loginDTO,
			HttpSession session) {

		return authService.authenticateUser(loginDTO.getPassword(), loginDTO.getUsername())
				.thenApply(authenticatedUser -> {

					if (authenticatedUser != null) {
						session.setAttribute("user", authenticatedUser);

						Role role = roleRepository.findById(authenticatedUser.getRoleId())
								.orElseThrow(() -> new RuntimeException("Role not found"));
						String roleName = role.getRoleName();
						String token = jwtUtil.generateToken(authenticatedUser.getUsername(), roleName);
						jwtUtil.printRole(token); 
						String rolesextractedfromjwutil = jwtUtil.getRoleFromToken(token);
						logger.info("rolesextractedfromjwutil" + rolesextractedfromjwutil);
						Map<String, Object> response = new HashMap<>(); 
						response.put("token", token);
						response.put("username", authenticatedUser.getUsername());
						response.put("id", authenticatedUser.getId());
						response.put("email", authenticatedUser.getEmail());
						response.put("phone", authenticatedUser.getPhone());
//						response.put("role", authenticatedUser.getRole_appuser()); 
						response.put("role", roleName);
						response.put("roles_extracted_from_jwutil", rolesextractedfromjwutil);  
						logger.info("Login data: " + response);
						logger.info("\n\ngenerated token hh: \n" + token);
						jwtUtil.printRole(token);

						String message = "User logged in: " + authenticatedUser.getUsername();
						kafkaTemplate.send("user-login-topic", message);

						return ResponseEntity.ok(response);
					} else {
						Map<String, Object> errorResponse = new HashMap<>();
						errorResponse.put("message", "Invalid username or password");
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
					}
				});
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = authService.getAllUsers(); 
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			logger.error("Error fetching users: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/register")
	public CompletableFuture<ResponseEntity<User>> register(
			@RequestHeader(value = "Authorization", required = false) String authorizationHeader, @Valid @RequestBody UserRegistrationRequestDto registrationRequest) {
		String token = null; 
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		         token = authorizationHeader.substring(7); 
		        logger.info("\n\nToken received in register contr: {}", token);  
		    } else {
		        logger.info("No token received in the request.");
		    }
		return authService.registerUser(registrationRequest, token).thenApply(registeredUser -> {
			String message = "User registered: " + registeredUser.getUsername();
			kafkaTemplate.send("user-registration-topic", message);
			return ResponseEntity.ok(registeredUser);
		}).exceptionally(ex -> {
			logger.error("Registration failed: {}", ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		});
	}

	@GetMapping("/displayProducts")
	public Map<String, Object> displayProducts() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Product> allProducts = productService.getAllProducts();
			List<StoredImage> storedImages = authService.getAllStoredImages();
			List<StoredImage> dellProducts = authService.getProductsByCategory("dell");
			List<StoredImage> acerProducts = authService.getProductsByCategory("acer");
			int totalProductCount = allProducts.size();

			logger.info("\n\ntotalProductCount " + totalProductCount);

			response.put("products", allProducts);
			response.put("dell", dellProducts);
			response.put("acer", acerProducts);
			response.put("totalProductCount", totalProductCount);
			response.put("success", true);

			String message = "Products displayed: " + totalProductCount + " products";
			kafkaTemplate.send("product-display-topic", message);

		} catch (Exception e) {
			logger.error("An error occurred: ", e);
			response.put("success", false);
		}
		return response;
	}

	@GetMapping("/displayProductsAdmin")
	public Map<String, Object> displayProductsAdmin() {
		Map<String, Object> response = new HashMap<>();
		try {
			List<Product> allProducts = productService.getAllProductsRedis();

			List<StoredImage> storedImages = authService.getAllStoredImages();
			List<StoredImage> dellProducts = authService.getProductsByCategory("dell");
			List<StoredImage> acerProducts = authService.getProductsByCategory("acer");
			int totalProductCount = allProducts.size();

			logger.info("\n\ntotalProductCount " + totalProductCount);

			response.put("products", allProducts);
			response.put("dell", dellProducts);
			response.put("acer", acerProducts);
			response.put("totalProductCount", totalProductCount);
			response.put("success", true);

			String message = "Products displayed: " + totalProductCount + " products";
			kafkaTemplate.send("product-display-topic", message);

		} catch (Exception e) {
			logger.error("An error occurred: ", e);
			response.put("success", false);
		}
		return response;
	}

	@GetMapping("/displayProductsByProduct")
	public Map<String, Object> displayProductsByProduct(@RequestParam String product) {
		Map<String, Object> response = new HashMap<>();
		try {

			List<StoredImage> storedImages = authService.getProductsByProduct(product);
			logger.info(product);
			logger.info("stored images are" + storedImages);

			response.put("products", storedImages);
			response.put("success", true);
		} catch (Exception e) {
			logger.error("An error occurred: ", e);
			response.put("success", false);
		}
		return response;
	}

	@GetMapping("/displayProductsByProductAndSubProduct")
	public Map<String, Object> displayProductsByProductAndSubProduct(@RequestParam String product,
			@RequestParam String subProduct) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<StoredImage> storedImages = authService.getProductsByProductAndSubProduct(product, subProduct);
			response.put("products", storedImages);

			logger.info("stored images in prods sub prods are" + storedImages);
			response.put("success", true);
		} catch (Exception e) {
			logger.error("An error occurred: ", e);
			response.put("success", false);
		}
		return response;
	}

	@PostMapping("/stored-image")
	public ResponseEntity<StoredImage> addStoredImage(@RequestBody StoredImage storedImage) {
		StoredImage newStoredImage = authService.addStoredImage(storedImage);
		return ResponseEntity.ok(newStoredImage);
	}

	@GetMapping("/stored-images")
	public ResponseEntity<List<StoredImage>> getStoredImages() {
		List<StoredImage> storedImages = authService.getAllStoredImages();
		return ResponseEntity.ok(storedImages);
	}

	@GetMapping("/check-email/{email}")
	public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
		boolean exists = authService.emailExists(email);
		return ResponseEntity.ok(exists);
	}

	@GetMapping("/displayProductsByCategory")
	public Map<String, Object> displayProductsByCategory(@RequestParam String category) {
		Map<String, Object> response = new HashMap<>();
		try {
			List<StoredImage> storedImages = authService.getProductsByCategory(category);
			response.put("products", storedImages);
			response.put("success", true);
		} catch (Exception e) {
			logger.error("An error occurred: ", e);
			response.put("success", false);
		}
		return response;
	}
}