package com.scavengerhunt.hunt.repository;

import com.scavengerhunt.hunt.model.ActiveEffect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActiveEffectRepository extends JpaRepository<ActiveEffect, Long> {

    /**
     * Returnează lista de efecte (dezavantaje/legendare) care sunt încă active pe o echipă.
     * Folosit de Supraveghetor ca să vadă ce pedepse are echipa.
     */
    List<ActiveEffect> findByTargetTeamIdAndIsActiveTrue(Long targetTeamId);
}