package com.scavengerhunt.hunt.service;

import com.scavengerhunt.hunt.model.*;
import com.scavengerhunt.hunt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameMasterService {

    private final PoiRepository poiRepository;
    private final TeamRepository teamRepository;
    private final TeamInventoryRepository inventoryRepository;
    private final TeamProgressRepository progressRepository;
    private final CardRepository cardRepository;
    private final ActiveEffectRepository effectRepository;
    private final CardDropService cardDropService;
    private final SimpMessagingTemplate messagingTemplate;
    private final DiscoveredPoiRepository discoveredPoiRepository;

    // --- LOGICA DE REVENDICARE (CLAIM) ---
    @Transactional
    public Card claimReward(Long teamId, String code) {
        // 1. Validări
        Poi poi = poiRepository.findByClaimCode(code)
                .orElseThrow(() -> new RuntimeException("Cod invalid!"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Echipa nu există!"));

        // Verificăm dacă a fost deja completat
        if (progressRepository.existsByTeamIdAndPoiId(teamId, poi.getId())) {
            throw new RuntimeException("Ați completat deja această provocare!");
        }

        if (poi.isIntermediate) {
            throw new RuntimeException("Provocările intermediare se completează doar cu cod!");
        }

        // 2. Acordare puncte
        team.setScore(team.getScore() + poi.getRewardPoints());
        teamRepository.save(team);

        // 3. Salvare progres (marchează ca revendicat/completat)
        TeamProgress progress = new TeamProgress();
        progress.setTeam(team);
        progress.setPoi(poi);
        progressRepository.save(progress);

        // 4. NOU: Ștergem intrarea din DiscoveredPoi (dacă există) pentru a curăța
        discoveredPoiRepository.findAll().stream()
                .filter(dp -> dp.getTeam().getId().equals(teamId) && dp.getPoi().getId().equals(poi.getId()))
                .findFirst()
                .ifPresent(discoveredPoiRepository::delete);

        // 5. Generare și salvare Cartonaș
        Card wonCard = cardDropService.drawRandomCard();

        TeamInventory inventory = new TeamInventory();
        inventory.setTeam(team);
        inventory.setCard(wonCard);
        inventoryRepository.save(inventory);

        return wonCard;
    }

    // NOU: LOGICA DE DESCPOERIRE POI (FĂRĂ RECOMPENSĂ)
    @Transactional
    public void discoverPoi(Long teamId, Long poiId) {
        // 1. Validări
        Poi poi = poiRepository.findById(poiId)
                .orElseThrow(() -> new RuntimeException("POI-ul nu există!"));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Echipa nu există!"));

        // 2. Verificăm dacă POI-ul a fost deja completat (revendicat)
        if (progressRepository.existsByTeamIdAndPoiId(teamId, poiId)) {
            // Dacă a fost deja revendicat, nu facem nimic
            return;
        }

        // 3. Verificăm dacă POI-ul a fost deja descoperit
        if (discoveredPoiRepository.existsByTeamIdAndPoiId(teamId, poiId)) {
            // Dacă a fost deja descoperit, nu facem nimic (este idempotent)
            return;
        }

        // 4. Salvarea Descoperirii
        DiscoveredPoi discoveredPoi = new DiscoveredPoi();
        discoveredPoi.setTeam(team);
        discoveredPoi.setPoi(poi);
        discoveredPoiRepository.save(discoveredPoi);
    }

    // --- LOGICA DE JOC CARTONAȘ (PLAY CARD) ---
    @Transactional
    public void useCard(Long attackerId, Long cardId) {
        Team attacker = teamRepository.findById(attackerId)
                .orElseThrow(() -> new RuntimeException("Echipa atacatoare nu există!"));

        // Verificăm dacă chiar au cardul în mână
        TeamInventory inventoryItem = inventoryRepository.findByTeamIdAndCardIdAndIsUsedFalse(attackerId, cardId)
                .orElseThrow(() -> new RuntimeException("Nu deții acest card sau a fost deja folosit!"));

        Card card = inventoryItem.getCard();

        // Marchem cardul ca folosit
        inventoryItem.setUsed(true);
        inventoryRepository.save(inventoryItem);

        // Aplicăm efectul în funcție de tip
        applyCardEffect(attacker, card);
    }

    private void applyCardEffect(Team attacker, Card card) {
        if (card.getType() == CardType.ADVANTAGE) {
            // Aici rămâne la alegerea ta dacă vrei puncte hardcodate sau nu.
            // Dacă vrei generic total, punctele pot fi un atribut al cardului (ex: card.getValue())
            attacker.setScore(attacker.getScore() + 50);
            teamRepository.save(attacker);
        }
        else if (card.getType() == CardType.DISADVANTAGE) {
            // Alege o victimă random
            List<Team> potentialVictims = teamRepository.findAll();
            potentialVictims.removeIf(t -> t.getId().equals(attacker.getId()));

            if (!potentialVictims.isEmpty()) {
                Team victim = potentialVictims.get(new Random().nextInt(potentialVictims.size()));

                // Doar creăm efectul. Nu facem nimic altceva.
                createActiveEffect(attacker, victim, card);

                messagingTemplate.convertAndSend("/topic/team/" + victim.getId() + "/alerts",
                        "Ai primit un dezavantaj: " + card.getName());
            }
        }
        else if (card.getType() == CardType.LEGENDARY) {
            // Afectează TOATE celelalte echipe
            List<Team> allTeams = teamRepository.findAll();
            allTeams.removeIf(t -> t.getId().equals(attacker.getId()));

            for (Team victim : allTeams) {
                // Doar creăm efectul pentru fiecare.
                // Backend-ul nu știe că e "freeze", știe doar că e un efect activ.
                createActiveEffect(attacker, victim, card);
            }

            messagingTemplate.convertAndSend("/topic/global/alerts",
                    "EVENT LEGENDAR! " + attacker.getName() + " a jucat " + card.getName());
        }
    }


    private void createActiveEffect(Team attacker, Team victim, Card card) {
        ActiveEffect effect = new ActiveEffect();
        effect.setAttackerTeam(attacker);
        effect.setTargetTeam(victim);
        effect.setCard(card);
        effect.setStartTime(LocalDateTime.now());
        // Durata poate fi hardcodată (5 min) sau luată din proprietățile cardului dacă adaugi un câmp 'duration'
        effect.setEndTime(LocalDateTime.now().plusMinutes(5));
        effectRepository.save(effect);
    }
    public void updateTeamLocation(Long teamId, double lat, double lng) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        team.setCurrentLat(lat);
        team.setCurrentLng(lng);
        teamRepository.save(team);

        // AICI ESTE CHEIA: Trimitem locația actualizată către Admin Panel
        // Adminul (Frontend Web) va fi abonat la "/topic/admin/map"
        messagingTemplate.convertAndSend("/topic/admin/map", team);
    }
}