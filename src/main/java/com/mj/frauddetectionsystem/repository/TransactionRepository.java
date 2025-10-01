package com.mj.frauddetectionsystem.repository;


import com.mj.frauddetectionsystem.model.Transaction;
import com.mj.frauddetectionsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionId(String transactionId);
    
    List<Transaction> findByUserOrderByTimestampDesc(User user);
    
    Page<Transaction> findByUserOrderByTimestampDesc(User user, Pageable pageable);
    
    List<Transaction> findByIsFraudulentTrueOrderByTimestampDesc();
    
    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.timestamp > :cutoffTime ORDER BY t.timestamp DESC")
    List<Transaction> findUserTransactionsAfter(@Param("user") User user, @Param("cutoffTime") LocalDateTime cutoffTime);
    
    @Query("SELECT AVG(t.amount) FROM Transaction t WHERE t.user = :user")
    BigDecimal getAverageTransactionAmount(@Param("user") User user);
    
    @Query("SELECT t FROM Transaction t WHERE t.amount > :amount ORDER BY t.amount DESC")
    List<Transaction> findTransactionsAboveAmount(@Param("amount") BigDecimal amount);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.timestamp >= :startTime AND t.timestamp <= :endTime")
    Long countTransactionsInTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.isFraudulent = true AND t.timestamp >= :startTime AND t.timestamp <= :endTime")
    Long countFraudulentTransactionsInTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}