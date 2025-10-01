package com.mj.frauddetectionsystem.repository;

import com.mj.frauddetectionsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserId(String userId);
    
    Optional<User> findByEmail(String email);
    
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :since")
    List<User> findUsersCreatedSince(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user = :user")
    Long countTransactionsByUser(@Param("user") User user);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user = :user AND t.isFraudulent = true")
    Long countFraudulentTransactionsByUser(@Param("user") User user);
}