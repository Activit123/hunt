package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.dto.PlayerPoiDto;
import com.scavengerhunt.hunt.dto.TeamLoginDto;
import com.scavengerhunt.hunt.model.Poi;
import com.scavengerhunt.hunt.model.Team;
import com.scavengerhunt.hunt.model.TeamInventory;
import com.scavengerhunt.hunt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/app") // Endpoint-uri publice pentru aplicatie
@RequiredArgsConstructor
public class TeamDataController {

    private final TeamRepository teamRepository;
    private final PoiRepository poiRepository;
    private final TeamInventoryRepository inventoryRepository;
    private final TeamProgressRepository teamProgressRepository;
    private final DiscoveredPoiRepository discoveredPoiRepository; // NOU: Injectare

    // 1. Login Echipă
    @PostMapping("/login")
    public ResponseEntity<Team> login(@RequestBody TeamLoginDto dto) {
        return teamRepository.findByLoginCode(dto.getAccessCode())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    // NOU: Endpoint pentru a returna ID-urile POI-urilor descoperite SAU completate
    @GetMapping("/discovered-poi-ids/{teamId}")
    public Set<Long> getDiscoveredPoiIds(@PathVariable Long teamId) {
        List<Long> completed = teamProgressRepository.findCompletedPoiIdsByTeamId(teamId);
        List<Long> discovered = discoveredPoiRepository.findDiscoveredPoiIdsByTeamId(teamId);

        // Combinăm cele două liste într-un set pentru a elimina duplicatele
        return Stream.concat(completed.stream(), discovered.stream()).collect(Collectors.toSet());
    }

    // 3. Vezi Inventarul (Cardurile mele)
    @GetMapping("/inventory/{teamId}")
    public List<TeamInventory> getMyInventory(@PathVariable Long teamId) {
        return inventoryRepository.findByTeamIdAndIsUsedFalse(teamId);
    }
    
    // 4. Vezi statusul curent (Score, etc - refresh)
    @GetMapping("/team/{teamId}")
    public ResponseEntity<Team> getTeamDetails(@PathVariable Long teamId) {
         return teamRepository.findById(teamId)
                 .map(ResponseEntity::ok)
                 .orElse(ResponseEntity.notFound().build());
    }
    // NOUL ENDPOINT: Clasament Live
    @GetMapping("/leaderboard")
    public List<Team> getLeaderboard() {
        // Returnează echipele sortate descrescător după scor
        return teamRepository.findAll(Sort.by(Sort.Direction.DESC, "score"));
    }
    @GetMapping("/map-points")
    public List<PlayerPoiDto> getMapPoints() {
        return poiRepository.findAll().stream().map(poi -> {
            PlayerPoiDto dto = new PlayerPoiDto();
            dto.setId(poi.getId());
            dto.setLatitude(poi.getLatitude());
            dto.setLongitude(poi.getLongitude());
            dto.setRadius(poi.getRadius());
            dto.setChallengeText(poi.getChallengeText());
            dto.setRewardPoints(poi.getRewardPoints());
            dto.setIntermediate(poi.isIntermediate());
            return dto;
        }).toList();
    }

    // NOU: Lista de provocări intermediare (separată de hartă, cum ai cerut)
    @GetMapping("/intermediate-challenges")
    public List<PlayerPoiDto> getIntermediateChallenges() {
        return poiRepository.findAll().stream()
                .filter(Poi::isIntermediate)
                .map(poi -> {
                    // Mapping similar ca mai sus
                    PlayerPoiDto dto = new PlayerPoiDto();
                    dto.setId(poi.getId());
                    dto.setChallengeText(poi.getChallengeText());
                    dto.setRewardPoints(poi.getRewardPoints());
                    dto.setIntermediate(true);
                    return dto;
                }).toList();
    }
}