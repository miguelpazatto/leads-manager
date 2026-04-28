package com.miguelpazatto.leadsmanager.infra.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/salesman").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/salesman/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/salesman/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/questions").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/questions/**").hasRole("ADMIN")	
						.requestMatchers(HttpMethod.DELETE, "/questions/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/options").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/questions/**").hasRole("ADMIN")
						.anyRequest().authenticated()
				)
				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
