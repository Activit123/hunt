package com.scavengerhunt.hunt.service;

import com.scavengerhunt.hunt.model.ActiveEffect;
import com.scavengerhunt.hunt.repository.ActiveEffectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameScheduler {

    private final ActiveEffectRepository effectRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Scheduled(fixedRate = 10000) // Verifică la 10 secunde
    @Transactional
    public void checkExpiredEffects() {
        // Luăm efectele active care au depășit timpul de final
        List<ActiveEffect> expiredEffects = effectRepository.findAll().stream()
                .filter(ActiveEffect::isActive)
                .filter(e -> e.getEndTime().isBefore(LocalDateTime.now()))
                .toList();

        for (ActiveEffect effect : expiredEffects) {
            effect.setActive(false); // Doar îl marcăm inactiv
            effectRepository.save(effect);

            // Anunțăm echipa că a scăpat de efect (indiferent că a fost Legendary sau Disadvantage)
            messagingTemplate.convertAndSend("/topic/team/" + effect.getTargetTeam().getId() + "/alerts", 
                "Efectul " + effect.getCard().getName() + " a expirat!");
        }
    }
}