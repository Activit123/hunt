package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.Poi; // Asigură-te că ai clasa POI (o facem imediat dacă nu e)
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PoiRepository extends JpaRepository<Poi, Long> {
    Optional<Poi> findByClaimCode(String claimCode);
}