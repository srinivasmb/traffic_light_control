package com.example.demo.repository;

import com.example.demo.entity.StateChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateChangeHistoryRepository extends JpaRepository<StateChangeHistory, Long> {
}
