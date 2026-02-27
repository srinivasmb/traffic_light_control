package com.trafficlight.api.repository;

import com.trafficlight.api.entity.StateChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateChangeHistoryRepository extends JpaRepository<StateChangeHistory, Long> {
}
