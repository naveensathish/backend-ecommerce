package com.example.loginregister.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.loginregister.security.JwtRequestFilter;
import com.example.loginregister.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and()
				.authorizeHttpRequests(authz -> authz.requestMatchers("/**").permitAll().requestMatchers("/api/auth/**")
						.permitAll().requestMatchers("/api/process_message").permitAll()
						.requestMatchers("/api/sellers/sellersget").permitAll().requestMatchers("/api/otp/send")
						.permitAll().requestMatchers("/api/auth/login")
						.permitAll().requestMatchers("/api/auth/updatePassword")
						.permitAll().requestMatchers("/api/auth/getUsernameByEmail/**")
						.permitAll().requestMatchers("/api/cart/**")
						.permitAll().requestMatchers("/api/report/list/**").permitAll()
						.requestMatchers("/api/auth/getAllUsers").permitAll()
						.requestMatchers("/api/report/download/**").permitAll().requestMatchers("/send").permitAll()
						.requestMatchers("/api/auth/**").permitAll().requestMatchers("/api/cart/update/**").permitAll()
						.requestMatchers("/api/cart/add").permitAll().requestMatchers("/api/prices/**").permitAll() 
						.requestMatchers("/api/products/**").permitAll().requestMatchers("/api/payment**").permitAll()
						.requestMatchers("/api/ratings/**").permitAll().requestMatchers("/api/sellers/**").permitAll()
						.requestMatchers("/api/products/productview").permitAll().anyRequest().authenticated() 
 
				).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build(); 
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(myUserDetailsService);
		return authenticationManagerBuilder.build();
	}
}