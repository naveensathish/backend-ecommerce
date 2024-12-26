package com.example.loginregister.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.loginregister.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

	@Autowired
	private JwtUtil jwtUtil;

	private AuthService authService;

	@Autowired
	@Lazy
	public void setAuthService(AuthService authService) {
		this.authService = authService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		logger.info("\n\nAuthorization Header: {}", authHeader);

		if (request.getServletPath().equals("/api/auth/login") || request.getServletPath().equals("/api/auth/register")
				|| request.getServletPath().equals("/api/auth/adminLogin")
				|| request.getServletPath().equals("/api/auth/updatePassword")
				|| request.getServletPath().equals("/api/auth/adminRegister")
				|| request.getServletPath().equals("/api/auth/getUsernameByEmail")
				|| request.getServletPath().equals("/api/otp/send")
				|| request.getServletPath().equals("/api/otp/verify")
				|| request.getServletPath().equals("/api/otp/send")
				|| request.getServletPath().equals("/api/sellers/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String jwt = authHeader.substring(7);
			String username = jwtUtil.extractUsername(jwt);
			logger.info("\n\nExtracted Username: {}", username);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { 
				if (jwtUtil.validateToken(jwt, username)) { 

					UserDetails userDetails;
					 if (request.getServletPath().startsWith("/api/auth/displayProducts") || request.getServletPath().startsWith("/api/report/download/**") || request.getServletPath().startsWith("/api/auth/getAllUsers")  || request.getServletPath().startsWith("/api/report/list/**") || request.getServletPath().startsWith("/api/auth/login") || request.getServletPath().startsWith("/api/products/productview") || request.getServletPath().startsWith("/api/sellers/onboard") 
							 || request.getServletPath().startsWith("/api/cart/add") || request.getServletPath().startsWith("/api/ratings/") || request.getServletPath().startsWith("/api/chat/") || request.getServletPath().startsWith("/api/cart/fetchitemsincart") || request.getServletPath().startsWith("/api/cart/") || request.getServletPath().startsWith("/api/cart/addOrderedItems") || request.getServletPath().startsWith("/api/payment") || request.getServletPath().startsWith("/api/wishlist") || request.getServletPath().startsWith("/api/wishlist/remove") || request.getServletPath().startsWith("/api/auth/displayProducts")) {   
						 userDetails = authService.loadUserByUsername(username); 
		                } 
					 else if(request.getServletPath().startsWith("/api/sellers/deactivate") || request.getServletPath().startsWith("/api/sellers/activate")) {
							 userDetails = authService.loadUserByUsername(username); 
					 }
					else if (request.getServletPath().startsWith("/api/sellers")
							&& !request.getServletPath().startsWith("/api/sellers/onboard") && !request.getServletPath().startsWith("/api/sellers/sellersget")) { 
						userDetails = authService.loadSellerByUsername(username);
					} 
					else {
						userDetails = authService.loadUserByUsername(username);
					}
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
							null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authToken);
					logger.info("\n\nUser {} authenticated successfully", username);
				} else {
					logger.warn("\n\nInvalid JWT token for user: {}", username);
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
					return;
				}
			}
		} else {
			logger.warn("\n\nNo Authorization header found or invalid format");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
			return;
		}

		filterChain.doFilter(request, response);
	}
}