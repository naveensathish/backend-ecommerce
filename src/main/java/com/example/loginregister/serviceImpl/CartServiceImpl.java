package com.example.loginregister.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.loginregister.entity.CartItem;
import com.example.loginregister.exception.DuplicateCartItemException;
import com.example.loginregister.exception.GlobalExceptionHandler;
import com.example.loginregister.repository.CartItemRepository;
import com.example.loginregister.service.CartService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CartServiceImpl implements CartService { 
	private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
	
	@Autowired
	private CartItemRepository cartItemRepository; 
	
	 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedDate = LocalDateTime.now().format(formatter);
	    
	@Override
	public List<CartItem> getCartItemsByUserId(String userId) {
		return cartItemRepository.findByUserId(userId);
	}
	
	 @Override
	    public void addItemToCartInBag(String userId, String productId) {
	        logger.info("\nAttempting to add item to cart: User ID = {}, Product ID = {}", userId, productId);
	        
	        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserIdAndProductId(userId, productId);

	        CartItem cartItem;
	        if (existingItemOpt.isPresent()) {
	            cartItem = existingItemOpt.get();
	            cartItem.setQuantity(cartItem.getQuantity() + 1);
	            cartItem.setUpdatedAt(formattedDate);
	            logger.info("\n\nquantity increased\n\n: {}", cartItem);
	        } else {
	            cartItem = new CartItem();
	            cartItem.setUserId(userId);
	            cartItem.setProductId(productId);
	            cartItem.setQuantity(1);
	            cartItem.setCreatedAt(formattedDate);
	            cartItem.setUpdatedAt(formattedDate);
	            logger.info("\n\nNew item added to cart: {}", cartItem);
	        } 

	        cartItemRepository.save(cartItem); 
	        logger.info("\n\nCart item saved successfully.");
	    }

	public void removeItemFromCart(String userId, String productId) {
	    Optional<CartItem> existingItemOpt = cartItemRepository.findByUserIdAndProductId(userId, productId);
	    
	    if (existingItemOpt.isPresent()) {
	        CartItem item = existingItemOpt.get();
	        
	        if (item.getQuantity() > 0) {
	            item.setQuantity(item.getQuantity() - 1);
	            
	            if (item.getQuantity() == 0) {
	                cartItemRepository.delete(item);
	            } else {
	                cartItemRepository.save(item);
	            }
	        }
	    }
	}

    public void removeAllItemsFromCart(String userId, String productId) {
        List<CartItem> items = cartItemRepository.findAllByUserIdAndProductId(userId, productId);
        if (!items.isEmpty()) {
            cartItemRepository.deleteAll(items); 
        }
    }
	
	@Override
	public CartItem addCartItem(CartItem cartItem) {
		logger.info("Entering addCartItem method");
		logger.debug("Cart item to add: {}", cartItem);
		
		 Optional<CartItem> existingItem = cartItemRepository.findByUserIdAndProductId(cartItem.getUserId(), cartItem.getProductId());
		    
		    if (existingItem.isPresent()) {
		        logger.warn("\n\nDuplicate entry: user_id = {} and product_id = {} already exists in the cart", cartItem.getUserId(), cartItem.getProductId());
		        throw new DuplicateCartItemException("\n\nThis item is already in the cart");
		    }
		    
		    cartItem.setCreatedAt(formattedDate);
		    cartItem.setUpdatedAt(formattedDate);
		
		CartItem savedCartItem = cartItemRepository.save(cartItem);

		logger.info("\n\nSuccessfully added cart item\n\n: {}", savedCartItem);
		return savedCartItem;
	}
	
    public CartItem addOrUpdateCartItem(String userId, String productId, int quantity) {
        Optional<CartItem> existingCartItemOpt = cartItemRepository.findByUserIdAndProductId(userId, productId);
        CartItem cartItem;

        if (existingCartItemOpt.isPresent()) {
            cartItem = existingCartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setProductName("Product Name Placeholder"); 
            cartItem.setPrice(31555.99);
        }

        return cartItemRepository.save(cartItem);
    }

    public CartItem updateCartItem(String userId, String productId, int newQuantity) {
        Optional<CartItem> cartItemOpt = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(newQuantity); 
            return cartItemRepository.save(cartItem);
        } else {
            throw new RuntimeException("Cart item not found with userId: " + userId + " and productId: " + productId);
        }
    }

    public void removeCartItem(String userId, String productId) {
        cartItemRepository.deleteByUserIdAndProductId(userId, productId);
    }

	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}
	
	 @Override
	    public void removeCartItem(Long id) {
		 logger.info("\n\nAttempting to delete cart item with id: {}", id); 
	        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
	        if (!cartItemOptional.isPresent()) {
	        	logger.warn("\n\nCart item with id: {} not found, cannot delete.", id); 
	            throw new EntityNotFoundException("\n\nCart item not found with id: " + id); 
	        }

	        cartItemRepository.deleteById(id);
	        logger.info("\n\nCart item with id: {} deleted successfully.", id);  
	    }
}
