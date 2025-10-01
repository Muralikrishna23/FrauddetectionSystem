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
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers("/fraud-detection/analyze").hasRole("USER")
                .requestMatchers("/fraud-detection/statistics").hasRole("ADMIN")
                .requestMatchers("/fraud-detection/alerts/**").hasRole("ANALYST")
                .requestMatchers("/actuator/**").hasRole("ADMIN")
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic();

        // Allow H2 Console in iframe — SECURE
        http.headers().frameOptions().sameOrigin();

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