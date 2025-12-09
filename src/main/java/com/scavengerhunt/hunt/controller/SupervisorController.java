package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.dto.SupervisorPoiDto;
import com.scavengerhunt.hunt.model.*;
import com.scavengerhunt.hunt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supervisor")
@RequiredArgsConstructor
public class SupervisorController {

    private final TeamRepository teamRepository;
    private final ActiveEffectRepository activeEffectRepository;
    private final SupervisorAssignmentRepository assignmentRepository;
    private final AppUserRepository userRepository;
    private final PoiRepository poiRepository;

    // 1. Asignare echipă (Persistentă)
    // Param: userId (supraveghetorul), teamCode (echipa)
    @PostMapping("/assign-team")
    public ResponseEntity<Team> assignTeam(@RequestParam Long userId, @RequestParam String teamCode) {
        AppUser supervisor = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Team team = teamRepository.findByLoginCode(teamCode)
                .orElseThrow(() -> new RuntimeException("Invalid Team Code"));

        // Verificăm dacă are deja o asignare și o actualizăm, sau creăm una nouă
        SupervisorAssignment assignment = assignmentRepository.findByUserId(userId)
                .orElse(new SupervisorAssignment());

        assignment.setUser(supervisor);
        assignment.setTeam(team);
        assignmentRepository.save(assignment);

        return ResponseEntity.ok(team);
    }

    // 2. Obține echipa deja asignată (când redeschide aplicația)
    @GetMapping("/my-team/{userId}")
    public ResponseEntity<Team> getAssignedTeam(@PathVariable Long userId) {
        return assignmentRepository.findByUserId(userId)
                .map(SupervisorAssignment::getTeam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Vezi efecte (Rămâne la fel)
    @GetMapping("/effects/{teamId}")
    public List<ActiveEffect> getTeamEffects(@PathVariable Long teamId) {
        return activeEffectRepository.findByTargetTeamIdAndIsActiveTrue(teamId);
    }

    // 4. Șterge efect (Rămâne la fel)
    @DeleteMapping("/effects/{effectId}")
    public ResponseEntity<String> removeEffect(@PathVariable Long effectId) {
        return activeEffectRepository.findById(effectId).map(effect -> {
            effect.setActive(false);
            activeEffectRepository.save(effect);
            return ResponseEntity.ok("Efect eliminat!");
        }).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/map-full")
    public List<SupervisorPoiDto> getSupervisorMap() {
        return poiRepository.findAll().stream().map(poi -> {
            SupervisorPoiDto dto = new SupervisorPoiDto();
            dto.setId(poi.getId());
            dto.setLatitude(poi.getLatitude());
            dto.setLongitude(poi.getLongitude());
            dto.setChallengeText(poi.getChallengeText());
            dto.setClaimCode(poi.getClaimCode()); // Aici trimitem codul
            dto.setIntermediate(poi.isIntermediate());
            return dto;
        }).toList();
    }
}