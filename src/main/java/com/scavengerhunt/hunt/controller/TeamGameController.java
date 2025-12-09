package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.model.Card;
import com.scavengerhunt.hunt.service.GameMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class    TeamGameController {

    private final GameMasterService gameMasterService;

    // Endpoint: POST /api/team/claim?teamId=1&code=COD_SECRET
    @PostMapping("/claim")
    public ResponseEntity<Card> claimReward(
            @RequestParam Long teamId, 
            @RequestParam String code
    ) {
        try {
            Card card = gameMasterService.claimReward(teamId, code);
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Sau un mesaj de eroare custom
        }
    }
}