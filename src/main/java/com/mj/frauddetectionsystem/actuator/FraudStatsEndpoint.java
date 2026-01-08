package com.mj.frauddetectionsystem.actuator;

import com.mj.frauddetectionsystem.service.FraudDetectionService;
import com.mj.frauddetectionsystem.dto.FraudStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "fraud-stats")
public class FraudStatsEndpoint {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @ReadOperation
    public FraudStatistics fraudStatistics() {
        return fraudDetectionService.getFraudStatistics();
    }
}