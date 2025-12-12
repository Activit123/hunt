package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.TeamProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamProgressRepository extends JpaRepository<TeamProgress, Long> {
    // Verifică dacă echipa a mai făcut acest POI
    boolean existsByTeamIdAndPoiId(Long teamId, Long poiId);

    // MODIFICAT: Asigurăm că interogarea selectează doar ID-ul POI-ului
    @Query("SELECT tp.poi.id FROM TeamProgress tp WHERE tp.team.id = :teamId")
    List<Long> findCompletedPoiIdsByTeamId(Long teamId);
}