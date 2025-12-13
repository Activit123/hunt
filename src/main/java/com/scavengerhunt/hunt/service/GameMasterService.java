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
import java.util.Set;

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
        // NOU: Set de nume de carduri care creează un ActiveEffect
        final Set<String> persistentEffectCards = Set.of(
                "MINI BOSS", "NO CONNECTION", "ALL TOGETHER",
                "DEAFENING SILENCE", "ONE-LEGGED LEAP", "BLINDFOLDED ESCORT",
                "POETIC TRIAD" // Toate acestea sunt DISADVANTAGE sau alte efecte negative
        );

        // NOU: Set de nume de carduri Legendare care sunt acțiuni instantanee (nu efect persistent)
        final Set<String> instantLegendaryCards = Set.of("D4C", "A WAY OUT");


        if (card.getType() == CardType.ADVANTAGE) {
            // --- LOGICĂ PENTRU AVANTAJ (Acțiuni Instantanee / Puncte) ---

            if (card.getName().toUpperCase().contains("PUNCTE") ||
                    card.getName().toUpperCase().equals("TREASURE") ||
                    card.getName().toUpperCase().equals("GOLD COINS") ||
                    card.getName().toUpperCase().equals("MAGIC LAMP") ||
                    card.getName().toUpperCase().equals("HACKER MAN") || // Hacker Man e de fapt -2
                    card.getName().toUpperCase().equals("GOBLIN THIEF"))
            {
                int pointsChange = 0;
                try {
                    // Căutăm orice număr (inclusiv negativ, datorită '-') în descriere
                    String desc = card.getDescription().replaceAll("[^\\d-]", "").trim();
                    if (!desc.isEmpty()) {
                        pointsChange = Integer.parseInt(desc);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Nu s-a putut extrage punctajul din descrierea cardului: " + card.getName());
                }

                // Logica specială pentru Goblin Thief
                if (card.getName().toUpperCase().equals("GOBLIN THIEF")) {
                    // Necesită logica de leaderboard, dar pentru simplitate dăm 0 sau -1
                    // Aici, e nevoie de GameMasterService.getLeaderboard().
                    // Pentru simplitate, vom ignora logica specială de furt/pierdere la primul loc.
                    pointsChange = 0; // Se presupune că logica este implementată client-side sau e complexă

                    // Lăsăm la 0, deoarece logica de furt e complexă și necesită clasament live.
                    // ATENTIE: Implementarea reală a lui GOBLIN THIEF ar fi complexă și ar trebui să fie aici.
                }

                attacker.setScore(attacker.getScore() + pointsChange);
                teamRepository.save(attacker);

                messagingTemplate.convertAndSend("/topic/team/" + attacker.getId() + "/alerts",
                        "Ai folosit " + card.getName() + " și ai primit/pierdut " + pointsChange + " puncte!");
            } else {
                // Alte avantaje (PEEK, CARD THIEF, REVERSE, DENY, DEFLECT, SEEK KNOWLEDGE, CURRENT STATE)
                // Acestea au efect instantaneu sau sunt folosite ca acțiune de contra-atac/informație.
                messagingTemplate.convertAndSend("/topic/team/" + attacker.getId() + "/alerts",
                        "Ai folosit " + card.getName() + " (Acțiune instantanee)");
            }
        }
        else if (card.getType() == CardType.DISADVANTAGE || card.getType() == CardType.LEGENDARY) {

            // --- LOGICĂ PENTRU DEZAVANTAJ / EFECTE PERSISTENTE ---

            if (instantLegendaryCards.contains(card.getName().toUpperCase())) {
                // Acțiuni instantanee legendare, nu creează efect activ persistent
                messagingTemplate.convertAndSend("/topic/team/" + attacker.getId() + "/alerts",
                        "Ai folosit cardul Legendar " + card.getName() + " (Acțiune instantanee)");
                return;
            }

            if (card.getType() == CardType.DISADVANTAGE || persistentEffectCards.contains(card.getName().toUpperCase())) {

                // Echipă victimă aleatorie (pentru dezavantaje)
                List<Team> potentialVictims = teamRepository.findAll();
                potentialVictims.removeIf(t -> t.getId().equals(attacker.getId()));

                if (!potentialVictims.isEmpty()) {
                    Team victim = potentialVictims.get(new Random().nextInt(potentialVictims.size()));
                    createActiveEffect(attacker, victim, card);
                    messagingTemplate.convertAndSend("/topic/team/" + victim.getId() + "/alerts",
                            "Ai primit un dezavantaj: " + card.getName());
                }
            } else if (card.getType() == CardType.LEGENDARY) {
                // Afectează TOATE celelalte echipe (Legendare de tip blestem global)

                List<Team> allTeams = teamRepository.findAll();
                allTeams.removeIf(t -> t.getId().equals(attacker.getId()));

                for (Team victim : allTeams) {
                    createActiveEffect(attacker, victim, card);
                }

                messagingTemplate.convertAndSend("/topic/global/alerts",
                        "EVENT LEGENDAR! " + attacker.getName() + " a jucat " + card.getName());
            }
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