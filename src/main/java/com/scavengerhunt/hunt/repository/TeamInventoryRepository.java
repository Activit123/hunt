package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.TeamInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamInventoryRepository extends JpaRepository<TeamInventory, Long> {
    
    // Lista de carduri active pentru afișare în app
    List<TeamInventory> findByTeamIdAndIsUsedFalse(Long teamId);

    // Găsește un card specific pe care echipa vrea să-l joace (să fie al lor și nefolosit)
    Optional<TeamInventory> findByTeamIdAndCardIdAndIsUsedFalse(Long teamId, Long cardId);
}