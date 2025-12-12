package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.DiscoveredPoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscoveredPoiRepository extends JpaRepository<DiscoveredPoi, Long> {

    // Verifică dacă POI-ul a fost deja descoperit de echipă
    boolean existsByTeamIdAndPoiId(Long teamId, Long poiId);

    // MODIFICAT: Returnează ID-urile POI-urilor descoperite de o echipă
    @Query("SELECT dp.poi.id FROM DiscoveredPoi dp WHERE dp.team.id = :teamId")
    List<Long> findDiscoveredPoiIdsByTeamId(Long teamId);
}