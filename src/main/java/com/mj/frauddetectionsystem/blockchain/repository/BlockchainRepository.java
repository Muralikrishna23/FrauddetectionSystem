package com.mj.frauddetectionsystem.blockchain.repository;

import com.mj.frauddetectionsystem.blockchain.model.FraudBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface BlockchainRepository extends JpaRepository<FraudBlock, Long> {
    Optional<FraudBlock> findTopByOrderByBlockIndexDesc();
    List<FraudBlock> findByTransactionId(String transactionId);
    List<FraudBlock> findByFraudDecision(String fraudDecision);
    @Query("SELECT fb FROM FraudBlock fb WHERE fb.riskScore >= :minScore ORDER BY fb.timestamp DESC")
    List<FraudBlock> findHighRiskBlocks(BigDecimal minScore);
    @Query("SELECT COUNT(fb) FROM FraudBlock fb WHERE fb.isValid = false")
    Long countInvalidBlocks();
    List<FraudBlock> findTop100ByOrderByBlockIndexDesc();
    @Query("SELECT COUNT(fb) FROM FraudBlock fb WHERE fb.fraudDecision = 'BLOCKED'")
    Long countBlockedTransactions();
}