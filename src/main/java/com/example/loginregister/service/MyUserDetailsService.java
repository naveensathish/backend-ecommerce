package com.example.loginregister.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.loginregister.entity.Seller;
import com.example.loginregister.repository.SellerRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	private final SellerRepository sellerRepository;

	public MyUserDetailsService(SellerRepository sellerRepository) {
		this.sellerRepository = sellerRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Seller seller = sellerRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Seller not found with email: " + email));

		return User.builder().username(seller.getEmail()).password(seller.getPassword()).roles("SELLER").build();
	}
}
