package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.SupervisorAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SupervisorAssignmentRepository extends JpaRepository<SupervisorAssignment, Long> {
    Optional<SupervisorAssignment> findByUserId(Long userId);
}