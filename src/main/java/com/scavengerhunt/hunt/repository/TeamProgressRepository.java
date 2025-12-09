package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.TeamProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamProgressRepository extends JpaRepository<TeamProgress, Long> {
    // Verifică dacă echipa a mai făcut acest POI
    boolean existsByTeamIdAndPoiId(Long teamId, Long poiId);
}