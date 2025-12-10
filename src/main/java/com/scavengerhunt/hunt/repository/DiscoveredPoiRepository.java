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

    // Returnează ID-urile POI-urilor descoperite de o echipă (pentru încărcare rapidă)
    @Query("SELECT dp.poi.id FROM DiscoveredPoi dp WHERE dp.team.id = :teamId")
    List<Long> findDiscoveredPoiIdsByTeamId(Long teamId);
}