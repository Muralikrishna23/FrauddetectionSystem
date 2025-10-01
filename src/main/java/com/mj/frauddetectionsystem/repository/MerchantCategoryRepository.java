package com.mj.frauddetectionsystem.repository;


import com.mj.frauddetectionsystem.model.MerchantCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantCategoryRepository extends JpaRepository<MerchantCategory, Long> {
    
    Optional<MerchantCategory> findByCategoryCode(String categoryCode);
    
    List<MerchantCategory> findByRiskLevel(MerchantCategory.RiskLevel riskLevel);
    
    @Query("SELECT mc FROM MerchantCategory mc ORDER BY mc.categoryName")
    List<MerchantCategory> findAllOrderByName();
}
