package com.example.loginregister.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.loginregister.entity.Product;
import com.example.loginregister.entity.StoredImage;
import com.example.loginregister.repository.ProductRepository;
import com.example.loginregister.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	String formattedDateTime = now.format(formatter);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public Optional<StoredImage> getProductById(Long id) {
		return productRepository.findById(id);
	}

	@Override
	public List<Product> getAllProducts() {

		long startTime = System.currentTimeMillis();

        List<Product> allProducts = new ArrayList<>();

//		String stocksQuery = "SELECT id, product, sub_product, category_name, \"name\", img, videos, description, price, options, stocks, user_id, seller_id, is_best_seller" + 
//				" FROM stocks_available order by id ASC";
		
        String stocksQuery = "SELECT sa.id, sa.product, sa.sub_product, sa.category_name, sa.\"name\", sa.img, sa.videos, sa.description, sa.price, sa.options, sa.stocks, sa.user_id, sa.seller_id, sa.is_best_seller " +
                "FROM stocks_available sa " +
                "JOIN sellers s ON sa.seller_id = s.id " +
                "WHERE s.is_active = TRUE " + // Ensure the seller is active 
                "ORDER BY sa.id ASC";
        
//        String stocksQuery = "SELECT sa.id, sa.product, sa.sub_product, sa.category_name, sa.\"name\", sa.img, sa.videos, sa.description, sa.price, sa.options, sa.stocks, sa.user_id, sa.seller_id, sa.is_best_seller " +
//                "FROM stocks_available sa " +
//                "LEFT JOIN sellers s ON sa.seller_id = s.id " + // Use LEFT JOIN to include products without a seller (admin products)
//                "WHERE sa.seller_id IS NULL OR s.is_active = TRUE " + // Show products added by admin or by active sellers
//                "ORDER BY sa.id ASC";


		
		List<Product> stocksAvailableProducts = jdbcTemplate.query(stocksQuery, (rs, rowNum) -> new Product(
                rs.getLong("id"), 
                rs.getString("product"),
                rs.getString("sub_product"),
                rs.getString("category_name"),
                rs.getString("name"),
                rs.getString("img"),
                rs.getString("videos"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("options"), 
                rs.getLong("stocks"), 
                rs.getLong("user_id"), 
                rs.getLong("seller_id"),  
                rs.getBoolean("is_best_seller")
        ));

		allProducts.addAll(stocksAvailableProducts);

		long endTime = System.currentTimeMillis(); 
		logger.info("\n\nTime taken to fetch products from DB: {} ms", (endTime - startTime));

		return allProducts;
    }
	
    @Override
	public List<Product> getAllProductsRedis() {
		String redisKey = "allProducts";
		long startTime = System.currentTimeMillis();
		List<Product> cachedProducts = (List<Product>) redisTemplate.opsForValue().get(redisKey);
		List<Product> allProducts = new ArrayList<>();
		if (cachedProducts != null) {
			allProducts.addAll(cachedProducts);
			long endTime = System.currentTimeMillis();
			logger.info("\n\nCache hit. Time taken to fetch products from Redis: {} ms", (endTime - startTime));
			return allProducts;
		}

		String stocksQuery = "SELECT id, product, sub_product, category_name, \"name\", img, videos, description, price, options, stocks, user_id, seller_id, is_best_seller FROM stocks_available order by id ASC";
		List<Product> stocksAvailableProducts = jdbcTemplate.query(stocksQuery, (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("product"),
                rs.getString("sub_product"),
                rs.getString("category_name"),
                rs.getString("name"),
                rs.getString("img"),
                rs.getString("videos"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("options"),
                rs.getLong("stocks"), 
                rs.getLong("user_id"),
                rs.getLong("seller_id"),
                rs.getBoolean("is_best_seller")
        ));
		
		allProducts.addAll(stocksAvailableProducts);
		redisTemplate.opsForValue().set(redisKey, allProducts, 2, TimeUnit.MINUTES); 
		long endTime = System.currentTimeMillis();
		logger.info("\n\nCache miss. Time taken to fetch products from DB and store in Redis: {} ms", 
				(endTime - startTime));

		return allProducts;
	}
    
	@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
	public void refreshRedisCache() {
		String redisKey = "allProducts"; 

		redisTemplate.delete(redisKey); 

		List<Product> updatedProducts = fetchUpdatedProductsFromDB();

		redisTemplate.opsForValue().set(redisKey, updatedProducts, 2, TimeUnit.MINUTES);
		
	    LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	    String formattedDateTimein = now.format(formatter);

		logger.info("Redis cache refreshed with updated products. Time: {}", formattedDateTimein); 

	}

    private List<Product> fetchUpdatedProductsFromDB() {
		String stocksQuery = "SELECT id, product, sub_product, category_name, \"name\", img, videos,description, price, options, stocks, user_id, seller_id, is_best_seller FROM stocks_available  order by id ASC";
        return jdbcTemplate.query(stocksQuery, (rs, rowNum) -> new Product(
                rs.getLong("id"),
                rs.getString("product"),
                rs.getString("sub_product"),
                rs.getString("category_name"),
                rs.getString("name"),
                rs.getString("img"),
                rs.getString("videos"),
                rs.getString("description"),
                rs.getDouble("price"),
                rs.getString("options"),
                rs.getLong("stocks"), 
                rs.getLong("user_id"),
                rs.getLong("seller_id"),
                rs.getBoolean("is_best_seller")
        ));
    }

	private List<String> getSellerTableNames() {
		String query = "SELECT table_name FROM information_schema.tables WHERE table_name LIKE 'products_seller_%'";
		return jdbcTemplate.queryForList(query, String.class);
	}

	@Override
	public boolean addProductToSellerTable(Long sellerId, Product product) {
		String tableName = "products_seller_" + sellerId;

		String maxStockIdQuery = "SELECT MAX(id) FROM public.stocks_available";
		Long maxStockId = jdbcTemplate.queryForObject(maxStockIdQuery, Long.class);

		String maxSellerIdQuery = "SELECT MAX(id) FROM public." + tableName;
		Long maxSellerId = jdbcTemplate.queryForObject(maxSellerIdQuery, Long.class);
        
        Long nextId = Math.max(maxStockId != null ? maxStockId : 0, maxSellerId != null ? maxSellerId : 0) + 1;

		String insertProductSql = "INSERT INTO public." + tableName
				+ " (product,name,sub_product, category_name, description, price, img, videos, options, stocks, user_id, seller_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		String insertStocksAvailableSql = "INSERT INTO public.stocks_available (id,product,name,sub_product, category_name, description, price, img, videos, options, stocks, user_id, seller_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try { 
            jdbcTemplate.update(insertProductSql,
                    product.getProduct(),
                    product.getName(),
                    product.getSubProduct(),
                    product.getCategoryName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getImg(),
                    product.getVideos(),
                    product.getOptions(),
                    product.getStocks(),
                    product.getUserId(),
                    product.getSellerId());
            
            jdbcTemplate.update(insertStocksAvailableSql,
            		nextId,
                    product.getProduct(),
                    product.getName(),
                    product.getSubProduct(),
                    product.getCategoryName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getImg(),
                    product.getVideos(),
                    product.getOptions(),
                    product.getStocks(),
                    product.getUserId(),
                    product.getSellerId());
          
            if (product.isBestSeller() == null) {
                product.setIsBestSeller(false);
            }
            
            return true;
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e); 
            return false; 
        }
    }
}
