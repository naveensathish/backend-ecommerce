package com.example.loginregister.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.loginregister.entity.Seller;
import com.example.loginregister.repository.SellerRepository;
import org.springframework.data.domain.Sort;

@Service
public class SellerService {
	private static final Logger logger = LoggerFactory.getLogger(SellerService.class);

	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	String formattedDateTime = now.format(formatter);

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void createSellerTable(Long sellerId) {
		String tableName = "products_seller_" + sellerId;

		String checkTableExists = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = ?)";
		Boolean tableExists = jdbcTemplate.queryForObject(checkTableExists, new Object[] { tableName }, Boolean.class);

		if (!tableExists) {
			String createTableSql = "CREATE TABLE public." + tableName + " ("
					+ "id INT8 NOT NULL GENERATED BY DEFAULT AS IDENTITY, " + "product VARCHAR(255) NULL, "
					+ "sub_product VARCHAR(255) NULL, " + "category_name VARCHAR(255) NULL, "
					+ "\"name\" VARCHAR(255) NULL, " + "img VARCHAR(50000) NULL, " + "videos varchar(50000) NULL, "
					+ "description VARCHAR(2550) NULL, " + "price FLOAT8 NULL, " + "\"options\" VARCHAR(255) NULL, "
					+ "stocks BIGINT NULL, " + "is_best_seller BOOL NULL DEFAULT false, " + "user_id BIGINT NULL, "
					+ "seller_id BIGINT NULL, " + "CONSTRAINT " + tableName + "_pkey PRIMARY KEY (id))";

			jdbcTemplate.execute(createTableSql);
		}
	}

	public Seller getSellerByEmail(String email) {
		return sellerRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Seller not found with email: " + email));
	}

	public Seller updateSeller(Seller seller) {
	    return sellerRepository.save(seller);
	}

	public Seller saveSeller(Seller seller) {
		seller.setUsername(seller.getUsername());
		seller.setEmail(seller.getEmail());
		seller.setPassword(seller.getPassword());
		seller.setRole(seller.getRole());
		seller.setPhone(seller.getPhone());
		seller.setUserId(seller.getUserId());
		seller.setCreatedAt(formattedDateTime);
		seller.setActive(true);
		seller.setGstin(seller.getGstin());
	    seller.setPancard(seller.getPancard());
	    seller.setAadhaarcard(seller.getAadhaarcard());
	    seller.setVoterid(seller.getVoterid());
	    seller.setGstinValidated(false);
	    seller.setPancardValidated(false);
	    seller.setAadhaarcardValidated(false);
	    seller.setVoteridValidated(false);
	    
		try {
			Seller savedSeller = sellerRepository.save(seller);
			logger.info("Successfully onboarded seller with ID: {}", savedSeller.getId());

			List<Seller> sellers = getSellersByUserId(savedSeller.getUserId());

			String roleValue = null;

			if (!sellers.isEmpty()) {
				roleValue = sellers.get(0).getSellerRolefromuser();
				savedSeller.setSellerRolefromuser(roleValue);
				logger.info("Stored role value here inside if: {}", roleValue);

				savedSeller = sellerRepository.save(savedSeller); 
			} else {
				logger.info("No sellers found for user ID: {}", savedSeller.getUserId());
			}

			logger.info("\n\nStored role value: {}", roleValue);
			logger.debug("\n\nSeller details: {}", savedSeller);

			return savedSeller;
		} catch (DataIntegrityViolationException e) {
			logger.error("Error saving seller: {}", e.getMessage());
			throw new RuntimeException("Email is already in use");
		}
	}

	public List<Seller> getSellersByUserId(Long userId) {
		List<Seller> sellers = sellerRepository.findSellersWithRoleByUserId(userId);

		logger.info("\n\nsellers in meth--" + sellers);

		Set<String> uniqueRoles = new HashSet<>();
		for (Seller seller : sellers) {
			uniqueRoles.add(seller.getSellerRolefromuser());
		}

		logger.info("\n\nuniqueRoles" + uniqueRoles);

		for (String role : uniqueRoles) {
			logger.info("Common Role: " + role);
		}

		return sellers;
	}
	
	 public String getSellerRole(Long sellerId) {
	        Optional<Seller> sellerOptional = sellerRepository.findById(sellerId);

	        if (sellerOptional.isPresent()) {
	            Seller seller = sellerOptional.get();
	            return seller.getRole(); 
	        } else {
	            return "ROLE_UNKNOWN";
	        }
	    }

	public List<Seller> getAllSellers() {
		return sellerRepository.findAll(Sort.by(Sort.Order.asc("id")));
	}

	public Seller getSellerById(Long id) {
		return sellerRepository.findById(id).orElse(null);
	}

	public List<Seller> getSellersByRole(String authorityGiven) {
		return sellerRepository.findByRole(authorityGiven);
	}

	public Seller updateSeller(Long id, Seller seller) {
		seller.setId(id); 
		return sellerRepository.save(seller);
	}

	public void deleteSeller(Long id) {
		sellerRepository.deleteById(id);
	}
}
