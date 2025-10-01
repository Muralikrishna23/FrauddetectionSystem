package com.mj.frauddetectionsystem.repository;


import com.mj.frauddetectionsystem.model.FraudAlert;
import com.mj.frauddetectionsystem.model.Transaction;
import com.mj.frauddetectionsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {

    List<FraudAlert> findByUserOrderByAlertTimeDesc(User user);
    Page<FraudAlert> findByUserOrderByAlertTimeDesc(User user, Pageable pageable);

    List<FraudAlert> findByStatus(FraudAlert.AlertStatus status);
    List<FraudAlert> findBySeverity(FraudAlert.Severity severity);

    // ✅ No @Query needed — use method above
    default List<FraudAlert> findOpenAlerts() {
        return findByStatus(FraudAlert.AlertStatus.OPEN);
    }

    @Query("SELECT a FROM FraudAlert a WHERE a.alertTime BETWEEN :startTime AND :endTime ORDER BY a.alertTime DESC")
    List<FraudAlert> findAlertsInTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ✅ Use parameters for both dynamic values
    @Query("SELECT COUNT(a) FROM FraudAlert a WHERE a.severity = :severity AND a.status = :status")
    Long countAlertsBySeverityAndStatus(
        @Param("severity") FraudAlert.Severity severity,
        @Param("status") FraudAlert.AlertStatus status
    );

    default Long countOpenAlertsBySeverity(FraudAlert.Severity severity) {
        return countAlertsBySeverityAndStatus(severity, FraudAlert.AlertStatus.OPEN);
    }
}
