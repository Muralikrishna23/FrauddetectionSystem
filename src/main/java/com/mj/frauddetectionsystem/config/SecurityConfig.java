
package com.mj.frauddetectionsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
            )
            .authorizeHttpRequests(authz -> authz
               
               
                // Fraud Detection endpoints
                .requestMatchers(AntPathRequestMatcher.antMatcher("/fraud-detection/analyze")).hasRole("USER")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/fraud-detection/statistics")).hasRole("ADMIN")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/fraud-detection/alerts/**")).hasRole("ANALYST")
                
                // Blockchain endpoints
                .requestMatchers(AntPathRequestMatcher.antMatcher("/blockchain/stats")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/blockchain/initialize")).hasRole("ADMIN")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/blockchain/**")).hasRole("ANALYST")
                
                // Smart Contract endpoints
                .requestMatchers(AntPathRequestMatcher.antMatcher("/smart-contracts/create")).hasRole("ADMIN")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/smart-contracts/**")).hasRole("ANALYST")
                
                // Actuator
                .requestMatchers(AntPathRequestMatcher.antMatcher("/actuator/**")).hasRole("ADMIN")
                
                // Swagger
                .requestMatchers(AntPathRequestMatcher.antMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/v3/api-docs/**")).permitAll()
                
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> {});

        
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("fraud-user")
                .password(passwordEncoder().encode("password123"))
                .roles("USER")
                .build();

        UserDetails analyst = User.builder()
                .username("fraud-analyst")
                .password(passwordEncoder().encode("analyst123"))
                .roles("USER", "ANALYST")
                .build();

        UserDetails admin = User.builder()
                .username("fraud-admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("USER", "ANALYST", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, analyst, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}