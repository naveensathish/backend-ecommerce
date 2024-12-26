package com.example.loginregister.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.loginregister.dto.UserRegistrationRequestDto;
import com.example.loginregister.entity.AdminUser;
import com.example.loginregister.entity.Product;
import com.example.loginregister.entity.Role;
import com.example.loginregister.entity.Seller;
import com.example.loginregister.entity.StoredImage;
import com.example.loginregister.entity.User;
import com.example.loginregister.exception.UserLockedException;
import com.example.loginregister.repository.AdminUserRepository;
import com.example.loginregister.repository.OrderRepository;
import com.example.loginregister.repository.RoleRepository;
import com.example.loginregister.repository.SellerRepository;
import com.example.loginregister.repository.StoredImageRepository;
import com.example.loginregister.repository.UserRepository;
import com.example.loginregister.security.JwtUtil;
import com.example.loginregister.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	private static final String ACTIVE_USERS_KEY = "active_users_count";

	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	String formattedDateTime = now.format(formatter);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AdminUserRepository adminUserRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private StoredImageRepository storedImageRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OrderRepository orderRepository;

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, Product> redisTemplateProduct;

	@Autowired
	public AuthServiceImpl(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate,
			@Qualifier("redisTemplateProduct") RedisTemplate<String, Product> redisTemplateProduct) {
		this.redisTemplate = redisTemplate;
		this.redisTemplateProduct = redisTemplateProduct;
	}
	
	 public String getUsernameByEmail(String email) {
	        User user = userRepository.findByEmail(email);
	        if (user != null) {
	            return user.getUsername();
	        } else {
	            return null; 
	        }
	    }
	
	 public boolean updatePassword(String username, String newPassword) {
	        User user = userRepository.findByUsername(username);

	        if (user != null) {
	            String encryptedPassword = passwordEncoder.encode(newPassword);
	            user.setPassword(encryptedPassword);
	            user.setDecryptPassword(newPassword); 
	            userRepository.save(user);
	            return true;
	        }
	        return false;
	    }

	public String checkRedis() {
		try {
			redisTemplate.opsForValue().set("testKey", "testValue");
			String value = (String) redisTemplate.opsForValue().get("testKey");
			return value.equals("testValue") ? "Redis is working!" : "Redis is not responding as expected.";
		} catch (Exception e) {
			return "Redis connection failed: " + e.getMessage();
		}
	}

	public String checkProductRedis() {
		try {
			Product product = new Product("1", "Product A", 100);
			redisTemplateProduct.opsForValue().set("product1", product);
			Product retrievedProduct = (Product) redisTemplateProduct.opsForValue().get("product1");
			return retrievedProduct != null ? "Product Redis is working!"
					: "Product Redis is not responding as expected.";
		} catch (Exception e) {
			return "Product Redis connection failed: " + e.getMessage();
		}
	}

	@Override
	public boolean emailExists(String email) {
		return userRepository.findByEmail(email) != null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found"); 
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				user.getAuthorities());
	}

	@Override
	public UserDetails loadSellerByUsername(String username) throws UsernameNotFoundException {
		Seller seller = sellerRepository.findByUsername(username);
		if (seller == null) {
			throw new UsernameNotFoundException("seller not found");
		}
		return new org.springframework.security.core.userdetails.User(seller.getUsername(), seller.getPassword(),
				new ArrayList<>());
	}

	@Cacheable(value = "usersByUsername", key = "#username")
	public User findByUsername(String username) {
		long startTime = System.currentTimeMillis();
		User user = userRepository.findByUsername(username);
		long endTime = System.currentTimeMillis();
		logger.info("Method findByUsername executed in {} ms", (endTime - startTime));
		logger.info("Fetching user from database for username: {}", username);
		return user;
	}

	@Cacheable(value = "usersByUsername", key = "#username")
	public AdminUser findByAdminUsername(String username) {
		long startTime = System.currentTimeMillis();
		AdminUser adminUser = adminUserRepository.findByUsername(username);
		long endTime = System.currentTimeMillis();
		logger.info("Method findByUsername executed in {} ms", (endTime - startTime));
		logger.info("Fetching user from database for username: {}", username);
		return adminUser;
	}

	public User findByUsernameWithoutCache(String username) {
		long startTime = System.currentTimeMillis();
		User user = userRepository.findByUsername(username);
		long endTime = System.currentTimeMillis();
		logger.info("Method findByUsernameWithoutCache executed in {} ms", (endTime - startTime));
		return user;
	}

	@Cacheable(value = "usersByEmail", key = "#email")
	public User findByEmail(String email) {
		logger.info("\n\nredis buddyyyyyyy email\n\n");
		logger.info("Fetching user from database for email: {}", email);
		return userRepository.findByEmail(email);
	}

	@Cacheable(value = "usersByPhone", key = "#phone")
	public User findByPhone(String phone) {
		logger.info("Fetching user from database for phone: {}", phone);
		logger.info("\n\nredis buddyyyyyyy phone\n\n");

		return userRepository.findByPhone(phone);
	}

	@Cacheable(value = "usersByPhone", key = "#phone")
	public AdminUser findByAdminPhone(String phone) {
		logger.info("Fetching user from database for phone: {}", phone);
		logger.info("\n\nredis buddyyyyyyy phone\n\n");

		return adminUserRepository.findByPhone(phone);
	}

	@Async("taskExecutor")
	@Override
	public CompletableFuture<User> registerUser(UserRegistrationRequestDto registrationRequest, String token) {
		logger.info("starting    ----" + registrationRequest);
		
		final String roleFromToken; 
		if (token != null) {
			roleFromToken = jwtUtil.getRoleFromToken(token);
			logger.info("\n\n registerrrr Token received in registerUser service: {}", token);
		} else {
			roleFromToken = "ROLE_USER"; 
			logger.info("No token received in registerUser service.");
		}
		
		return CompletableFuture.supplyAsync(() -> {
			if (!isValidUsername(registrationRequest.getUsername())) {
				logger.warn("Registration failed: Invalid username '{}'", registrationRequest.getUsername());
				throw new IllegalArgumentException("Invalid username format.");
			}

			if (!isValidEmail(registrationRequest.getEmail())) {
				logger.warn("Registration failed: Invalid email '{}'", registrationRequest.getEmail());
				throw new IllegalArgumentException("Invalid email format.");
			}

			if (!isValidPhoneNumber(registrationRequest.getPhone())) {
				logger.warn("Registration failed: Invalid phone number '{}'", registrationRequest.getPhone());
				throw new IllegalArgumentException("Invalid phone number format.");
			}

			User existingUserByUsername = findByUsername(registrationRequest.getUsername());
			User existingUserByEmail = findByEmail(registrationRequest.getEmail());
			User existingUserByPhoneNumber = findByPhone(registrationRequest.getPhone());

			if (existingUserByUsername != null) {
				logger.info("\n\ninside\n\n");
				logger.warn("Registration failed: Username '{}' is already taken", registrationRequest.getUsername());
				throw new IllegalArgumentException("Username is already taken. Please choose a different username.");
			}

			if (existingUserByEmail != null) {
				logger.warn("Registration failed: Email '{}' is already registered", registrationRequest.getEmail());
				throw new IllegalArgumentException("Email is already registered. Please use a different email.");
			}

			if (existingUserByPhoneNumber != null) {
				logger.warn("Registration failed: Phone number '{}' is already registered",
						registrationRequest.getPhone());
				throw new IllegalArgumentException(
						"Phone number is already registered. Please use a different phone number.");
			}

			User user = new User();
			user.setUsername(registrationRequest.getUsername());
			user.setEmail(registrationRequest.getEmail());
			user.setPhone(registrationRequest.getPhone());
			user.setAddress("");
			user.setPassword(hashPassword(registrationRequest.getPassword()));
			user.setLockStatus("N");
			user.setSystemReceivedDateTime(formattedDateTime);
//			user.setRole_appuser("ROLE_USER");
			user.setDecryptPassword(registrationRequest.getPassword());

			Role role;

			if ("ROLE_SUPERADMIN".equals(roleFromToken)) {
				role = roleRepository.findByRoleName("ROLE_ADMIN")
						.orElseThrow(() -> new RuntimeException("Role 'ROLE_ADMIN' not found"));
				user.setRoleId(role.getId());
				user.setRole_appuser(role.getRoleName());
				logger.info("\n\nAssigned ROLE_ADMIN to the new user by SUPERADMIN.\n\n");
			} else {
				role = roleRepository.findByRoleName("ROLE_USER")
						.orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' not found"));
				user.setRoleId(role.getId());
				user.setRole_appuser(role.getRoleName());
				logger.info("\n\nAssigned ROLE_USER to the new user because the current user is NOT A SUPERADMIN.\n\n");
			}

			testCachingPerformance("here calledddd");
			User savedUser = userRepository.save(user);
			logger.info("\n\nUser registered successfully: '{}' at {}", savedUser.getUsername(),
					formattedDateTime + "\n\n");

			return savedUser;
		});
	}

	public void testCachingPerformance(String username) {
		long startTimePostgres = System.nanoTime();
		User userWithoutCache = userRepository.findByUsername(username);
		long durationWithoutCache = System.nanoTime() - startTimePostgres;
		logger.info("PostgreSQL Method findByUsername executed in " + durationWithoutCache / 1_000_000 + " ms");

		long startTimeRedis = System.nanoTime();
		User userWithCache = findByUsername(username);
		long durationWithCache = System.nanoTime() - startTimeRedis;
		logger.info("Redis Method findByUsername executed in " + durationWithCache / 1_000_000 + " ms");
	}

	public long getPostgreSQLUserCount() {
		return userRepository.count();
	}

	public long getRedisUserCount() {
		return redisTemplate.keys("usersByUsername:*").size();
	}

	public void compareStorage() {
		long postgreSQLCount = getPostgreSQLUserCount();
		long redisCount = getRedisUserCount();

		logger.info("PostgreSQL User Count: " + postgreSQLCount);
		logger.info("Redis User Count: " + redisCount);
	}

	public void checkRedisAndCompareStorage(String username) {
		String redisStatus = checkRedis();
		logger.info(redisStatus);
		testCachingPerformance(username);
		compareStorage();
	}

	private boolean isValidUsername(String username) {
		return username != null && username.matches("^[a-zA-Z0-9_]{3,20}$");
	}

	private boolean isValidEmail(String email) {
		return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
	}

	private boolean isValidPhoneNumber(String phone) {
		return phone != null && phone.matches("^\\+?[0-9]{10,15}$");
	}

	private String hashPassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	public void clearAllRedisData() {
		redisTemplate.getConnectionFactory().getConnection().flushAll();
		logger.info("\n\nAll Redis data has been cleared.");
	}

	private Long getActiveUsersCount() {
		Object activeUsersCountObject = redisTemplate.opsForValue().get(ACTIVE_USERS_KEY);

		logger.info("\n\n*******Retrieved active users count from Redis: {}", activeUsersCountObject);

		if (activeUsersCountObject instanceof Long) {
			return (Long) activeUsersCountObject;
		} else {
			logger.warn("\n\nActive users count is not a Long. Returning 0.");
			return 0L;
		}
	}

	private boolean acquireLock(String lockKey) {
		Boolean isLocked = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", 30, TimeUnit.SECONDS);
		return isLocked != null && isLocked;
	}

	private void releaseLock(String lockKey) {
		redisTemplate.delete(lockKey);
	}

	private static int activeUsersCount = 0;

	public Set<Object> getActiveUsers() {
		Set<Object> activeUsers = redisTemplate.opsForSet().members("active_users");

		if (activeUsers == null || activeUsers.isEmpty()) {
			logger.info("No active users found in Redis.");
		} else {
			logger.info("Currently active users: {}", activeUsers);
		}

		return activeUsers;
	}

	@Async("taskExecutor")
	@Override
	public CompletableFuture<User> authenticateUser(String password, String username) {
		logger.info("Attempting to authenticate user: {}", username);

		return CompletableFuture.supplyAsync(() -> {
			if (!isValidUsername(username)) {
				logger.warn("Authentication failed: Invalid username format '{}'", username);
				throw new IllegalArgumentException("Invalid username format.");
			}

			logger.info("\n\nloadUserByUsername !!!!!!" + loadUserByUsername(username));

			User user = userRepository.findByUsername(username);

			if (user != null) {

				if (isPasswordValid(password, user.getPassword())) {
					if ("Y".equals(user.getLockStatus())) {
						String message = "User account is locked.";
						logger.error(message);
						throw new UserLockedException(message);
					}

//					String userRole = user.getRole_appuser();
//					logger.info("User authenticated successfully: {} at {} with role: {}", username, 
//							LocalDateTime.now(), userRole);
//
//					logger.info("User authenticated successfully: {} at {} with roles: {}", username,
//							formattedDateTime);

					Role role = roleRepository.findById(user.getRoleId()) 
							.orElseThrow(() -> new RuntimeException("Role not found"));

					String userRolenew = role.getRoleName(); 
					user.setRole_appuser(userRolenew); 

					logger.info("User authenticated successfully: {} with role: {}", username, userRolenew);

					List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userRolenew));
					Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authentication);

					Authentication authenticationtwo = SecurityContextHolder.getContext().getAuthentication();
					String currentUsername = authenticationtwo != null ? authenticationtwo.getName() : "anonymous";
					String currentUserRole = authorities.stream().map(GrantedAuthority::getAuthority).findFirst()
							.orElse(null);

					logger.info("\n\nCurrent User login: {}", currentUsername);
					logger.info("\n\nCurrent User Role login: {}", currentUserRole);

					return user;
				}
			}

			logger.warn("Authentication failed for user: {}. Invalid username or password.", username);
			return null;
		});
	}

	@Override
	public Optional<User> findById(Long userId) {
		return userRepository.findById(userId);
	}

	private boolean isPasswordValid(String rawPassword, String storedPassword) {
		return passwordEncoder.matches(rawPassword, storedPassword);
	}

	@Override
	public List<StoredImage> getAllStoredImages() {
		return storedImageRepository.findAll();
	}

	@Override
	public StoredImage addStoredImage(StoredImage storedImage) {
		return storedImageRepository.save(storedImage);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<StoredImage> getProductsByCategory(String product) {
		return storedImageRepository.findByCategoryName(product);
	}

	@Override
	public List<StoredImage> getProductsByProduct(String product) {
		return storedImageRepository.findByProduct(product);
	}

	@Override
	public List<StoredImage> getProductsByProductAndSubProduct(String product, String subProduct) {
		return storedImageRepository.findByProductAndSubProduct(product, subProduct);
	}
}