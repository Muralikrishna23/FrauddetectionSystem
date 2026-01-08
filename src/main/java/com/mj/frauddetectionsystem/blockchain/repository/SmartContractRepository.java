package com.mj.frauddetectionsystem.blockchain.repository;

import com.mj.frauddetectionsystem.blockchain.model.SmartContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Repository
public interface SmartContractRepository extends JpaRepository<SmartContract, Long> {
    Optional<SmartContract> findByContractId(String contractId);
    List<SmartContract> findByIsActiveTrue();
    List<SmartContract> findByActionType(String actionType);
    @Query("SELECT sc FROM SmartContract sc WHERE sc.isActive = true AND sc.riskThreshold <= :riskScore")
    List<SmartContract> findApplicableContracts(BigDecimal riskScore);
}