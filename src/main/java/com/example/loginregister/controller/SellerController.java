package com.example.loginregister.controller;

import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.loginregister.entity.Product;
import com.example.loginregister.entity.Seller;
import com.example.loginregister.security.JwtUtil;
import com.example.loginregister.service.AuthService;
import com.example.loginregister.service.FileExportService;
import com.example.loginregister.service.ProductService;
import com.example.loginregister.service.SellerService;

@RestController
@RequestMapping("/api/sellers")
@CrossOrigin(origins = { "http://localhost:5000", "http://localhost:5001", "http://localhost:3000", "http://localhost:3001" })
public class SellerController {

	private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

	@Autowired
	private SellerService sellerService;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private final JwtUtil jwtUtil;

	@Autowired
	ProductService productService;

	@Autowired
	private FileExportService fileExportService;

	@Autowired
	private AuthService authService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	public SellerController(SellerService sellerService, JwtUtil jwtUtil) {
		this.sellerService = sellerService;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;

	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginDetails) {
		String email = loginDetails.get("email");
		String password = loginDetails.get("password");

		logger.info("\n\n email -" + email + "\n\n");
		logger.info("\n\n password -" + password + "\n\n");

		try {
			Seller seller = sellerService.getSellerByEmail(email);

			if (seller != null && passwordMatches(password, seller.getPassword())) {
				String token = jwtUtil.generateToken(seller.getUsername(), seller.getRole());
				String rolesExtractedFromJwtUtil = jwtUtil.getRoleFromToken(token);
				logger.info("rolesExtractedFromJwtUtil: " + rolesExtractedFromJwtUtil);
				jwtUtil.printRole(token); 
				Map<String, Object> response = new HashMap<>();
				response.put("sellertoken", token);
				response.put("sellerId", seller.getId());
				response.put("role", rolesExtractedFromJwtUtil);
				response.put("sellerName", seller.getUsername());

				String message = "Seller logged in: " + seller.getUsername();
				kafkaTemplate.send("seller-login-topic", message);

				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(Map.of("message", "Invalid email or password"));
			}
		} catch (Exception e) {
			logger.error("An error occurred while processing the request: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "An error occurred while processing the request"));
		}
	}
	
	@PostMapping("/deactivate/{sellerId}")
	@PreAuthorize("hasRole('SUPERADMIN') or hasRole('ADMIN')")
	public ResponseEntity<Map<String, Object>> deactivateSeller(@PathVariable Long sellerId) {
	    try {
	        Seller seller = sellerService.getSellerById(sellerId);
	        if (seller == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Seller not found"));
	        }
	        seller.setActive(false); 
	        Seller updatedSeller = sellerService.updateSeller(seller); 
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", "success");
	        response.put("message", "Seller deactivated successfully");
	        response.put("seller", updatedSeller);
	        kafkaTemplate.send("seller-deactivation-topic", "Seller deactivated: " + updatedSeller.getUsername());
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        logger.error("Error deactivating seller: {}", e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("message", "Error occurred while deactivating the seller"));
	    }
	}
	
	@PostMapping("/activate/{sellerId}")
	public ResponseEntity<Map<String, Object>> activateSeller(@PathVariable Long sellerId) {
	    try {
	        Seller seller = sellerService.getSellerById(sellerId);
	        if (seller == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Seller not found"));
	        }
	        seller.setActive(true);
	        Seller updatedSeller = sellerService.updateSeller(seller);
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", "success");
	        response.put("message", "Seller activated successfully");
	        response.put("seller", updatedSeller);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("message", "Error occurred while activating the seller"));  
	    }
	}


	private boolean passwordMatches(String rawPassword, String storedPassword) {
		return storedPassword.equals(rawPassword);
	}

	@GetMapping("/test-generate-report")
	public String testGenerateReport() {
		return generateReport();
	}

	private String generateReport() {
		logger.info("Generating report...");
		long sizeLimitInBytes = 50000;
		String format = "excel";
		String reportPath = System.getProperty("user.home") + "/reports";

		try {
			logger.info("Calling fileExportService to export data...");
			byte[] report = fileExportService.exportDataIfExceedsLimit(sizeLimitInBytes, format, "seller");

			if (report.length > 0) {
				String filename = reportPath + "/sellers_report." + format;
				try {
					Files.createDirectories(Paths.get(reportPath));
					try (FileOutputStream fos = new FileOutputStream(filename)) {
						fos.write(report);
						logger.info("Report generated successfully! Size: {} bytes. Saved at: {}", report.length,
								filename);
						return "Report generated successfully! Saved at: " + filename;
					}
				} catch (Exception e) {
					logger.error("Error saving report: {}", e.getMessage());
					return "Error saving report: " + e.getMessage();
				}
			} else {
				logger.info("No report generated. Data does not exceed the size limit.");
				return "No report generated. Data does not exceed the size limit.";
			}
		} catch (Exception e) {
			logger.error("Error generating report: {}", e.getMessage());
			return "Error generating report: " + e.getMessage();
		}
	}

	@GetMapping("/export")
	public ResponseEntity<byte[]> exportData(@RequestParam long sizeLimitInBytes, @RequestParam String format) {
		String repoName = "seller";

		try {
			byte[] fileContent = fileExportService.exportDataIfExceedsLimit(sizeLimitInBytes, format, repoName);

			if (fileContent.length == 0) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			}
			String filename = fileExportService.generateFilename(repoName + "_report", format);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");

			MediaType contentType = format.equalsIgnoreCase("excel") ? MediaType.APPLICATION_OCTET_STREAM
					: MediaType.valueOf("text/csv");

			return ResponseEntity.ok().headers(headers).contentType(contentType).body(fileContent);
		} catch (Exception e) {
			logger.error("Error during export: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/onboard")
	public ResponseEntity<Map<String, Object>> onboardSeller(@RequestBody Seller seller) {
		logger.info("\n\nReceived Seller: " + seller);

		Long userId = seller.getUserId();

		List<Seller> sellersWithUserRole = sellerService.getSellersByUserId(userId);

		try {
			String reportMessage = generateReport();
			logger.info("Report Generation Message: {}", reportMessage);
		} catch (Exception e) {
			logger.error("Error while generating report: {}", e.getMessage()); 
		}

		logger.info("\n\n" + sellersWithUserRole + "  --sellerWithuserRolejoin\n");
		if (sellersWithUserRole == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("message", "Seller not found for the given user ID"));
		}

		logger.info("\n\nReceived Seller 2222222222: " + seller);

		Seller savedSeller = sellerService.saveSeller(seller);

		logger.info("\n\n" + savedSeller +"\n");

		Map<String, Object> response = new HashMap<>();
		response.put("status", "success");
		response.put("data", sellersWithUserRole);
		response.put("message", "Seller onboarded successfully");
		response.put("role", seller.getRole());

		String message = "New seller onboarded: " + savedSeller.getUsername() + " with ID: " + savedSeller.getId();
		kafkaTemplate.send("seller-onboard-topic", message);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/sellersget")
	public ResponseEntity<List<Seller>> getAllSellers() {
		List<Seller> sellers = sellerService.getAllSellers();
		if (sellers.isEmpty()) {
			return ResponseEntity.noContent().build(); 
		}
		return ResponseEntity.ok(sellers);
	}

	@PostMapping("/addProduct/{sellerId}")
	public ResponseEntity<Map<String, Object>> addProductToSellerTable(@RequestBody Product productDTO,
			@PathVariable Long sellerId) {

		try {

			logger.info("\n\nselleridddddddd" + sellerId);

			productDTO.setSellerId(sellerId);

			Long userId = productDTO.getUserId();
			logger.info("\n\nReceived userId from request body: {}", userId);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			String role = authentication.getAuthorities().stream()
					.map(grantedAuthority -> grantedAuthority.getAuthority()).findFirst().orElse("ROLE_UNKNOWN");
			
			String sellerRole = sellerService.getSellerRole(sellerId);

	        logger.info("\n\nSeller role from database: {}", sellerRole);

			logger.info("\n\nAuthenticated seller role: {}", role);

			logger.info("Received product for sellerId {}: {}", sellerId, productDTO);

			sellerService.createSellerTable(sellerId);
			logger.info("Seller table for sellerId {} created successfully", sellerId);

			boolean success = productService.addProductToSellerTable(sellerId, productDTO);

			if (success) {
				logger.info("\n\nProduct added successfully to seller table for sellerId {}", sellerId);

				Map<String, Object> response = new HashMap<>();
				response.put("status", "success");
				response.put("message", "Product added to seller table successfully.");
				response.put("sellerId", sellerId);
				response.put("userId", userId);
				response.put("role", role);
				response.put("product", productDTO);

				kafkaTemplate.send("product-addition-topic", String.format(
						"Product added successfully for sellerId %d, productName: %s", sellerId, productDTO.getName()));

				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				Map<String, Object> response = new HashMap<>();
				response.put("status", "failure");
				response.put("message", "Failed to add product to seller table.");

				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Error while adding product to seller table: {}", e.getMessage());

			Map<String, Object> response = new HashMap<>();
			response.put("status", "error");
			response.put("message", "An error occurred while adding the product.");

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
		Seller seller = sellerService.getSellerById(id);
		return seller != null ? ResponseEntity.ok(seller) : ResponseEntity.notFound().build();
	}

	@GetMapping("/role/{role}")
	public ResponseEntity<List<Seller>> getSellersByRole(@PathVariable String role) {
		List<Seller> sellers = sellerService.getSellersByRole(role);
		return ResponseEntity.ok(sellers);
	}
}