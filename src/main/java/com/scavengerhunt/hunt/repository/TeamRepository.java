package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByLoginCode(String loginCode);
    boolean existsByLoginCode(String loginCode);
}