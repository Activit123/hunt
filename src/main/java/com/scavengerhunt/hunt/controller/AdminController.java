package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.dto.CreatePoiDto;
import com.scavengerhunt.hunt.model.Poi;
import com.scavengerhunt.hunt.model.Team;
import com.scavengerhunt.hunt.repository.PoiRepository;
import com.scavengerhunt.hunt.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TeamRepository teamRepository;
    private final PoiRepository poiRepository;

    // 1. Creare Echipă și Generare Cod
    @PostMapping("/teams")
    public ResponseEntity<Team> createTeam(@RequestParam String name) {
        Team team = new Team();
        team.setName(name);
        // Generăm un cod scurt de 6 caractere (ex: A1B2C3)
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        team.setLoginCode(code);
        
        return ResponseEntity.ok(teamRepository.save(team));
    }

    // 2. Adăugare Punct pe Hartă (POI)
    @PostMapping("/poi")
    public ResponseEntity<Poi> createPoi(@RequestBody CreatePoiDto dto) {
        Poi poi = new Poi();
        poi.setLatitude(dto.getLat());
        poi.setLongitude(dto.getLng());
        poi.setChallengeText(dto.getChallengeText());
        poi.setRewardPoints(dto.getRewardPoints());
        poi.setRadius(dto.getRadius());
        poi.setIntermediate(dto.isIntermediate());
        
        // Generăm automat codul de revendicare pentru acest punct (ex: POI-X9Y)
        String claimCode = "CODE-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        poi.setClaimCode(claimCode);

        return ResponseEntity.ok(poiRepository.save(poi));
    }
    // NOU: 1.1. UPDATE ECHIPĂ
    @PutMapping("/teams/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
        return teamRepository.findById(id).map(team -> {
            team.setName(teamDetails.getName());
            team.setScore(teamDetails.getScore());
            team.setLoginCode(teamDetails.getLoginCode());
            return ResponseEntity.ok(teamRepository.save(team));
        }).orElse(ResponseEntity.notFound().build());
    }

    // NOU: 1.2. DELETE ECHIPĂ
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // 3. Vezi toate echipele (pentru Admin Panel Harta)
    @GetMapping("/teams")
    public Iterable<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // NOU: 2.1. UPDATE POI
    @PutMapping("/poi/{id}")
    public ResponseEntity<Poi> updatePoi(@PathVariable Long id, @RequestBody CreatePoiDto dto) {
        return poiRepository.findById(id).map(poi -> {
            poi.setLatitude(dto.getLat());
            poi.setLongitude(dto.getLng());
            poi.setChallengeText(dto.getChallengeText());
            poi.setRewardPoints(dto.getRewardPoints());
            poi.setRadius(dto.getRadius());
            poi.setIntermediate(dto.isIntermediate());

            // Permitem actualizarea codului DOAR dacă este trimis în DTO (Flutter trimite claimCode în DTO)
            if (dto.getClaimCode() != null) {
                poi.setClaimCode(dto.getClaimCode());
            }

            return ResponseEntity.ok(poiRepository.save(poi));
        }).orElse(ResponseEntity.notFound().build());
    }

    // NOU: 2.2. DELETE POI
    @DeleteMapping("/poi/{id}")
    public ResponseEntity<Void> deletePoi(@PathVariable Long id) {
        poiRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}