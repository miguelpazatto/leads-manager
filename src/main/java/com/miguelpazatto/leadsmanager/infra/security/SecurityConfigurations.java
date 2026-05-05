package com.miguelpazatto.leadsmanager.infra.security;


import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
	
	@Autowired
	SecurityFilter securityFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()))
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/salesman").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/salesman/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/salesman").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/salesman/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/salesman/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/questions").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/questions/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/questions").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/questions/**").hasRole("ADMIN")	
						.requestMatchers(HttpMethod.DELETE, "/questions/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/options").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/options/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/options").hasRole("ADMIN")
						.requestMatchers(HttpMethod.PUT, "/options/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/options").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/leads").permitAll()
						.requestMatchers(HttpMethod.GET, "/leads/public/**").permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
