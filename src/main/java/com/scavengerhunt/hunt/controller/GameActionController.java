package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.dto.UseCardDto;
import com.scavengerhunt.hunt.service.GameMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameActionController {

    private final GameMasterService gameMasterService;

    @PostMapping("/play-card")
    public ResponseEntity<String> playCard(@RequestBody UseCardDto dto) {
        try {
            gameMasterService.useCard(dto.getAttackerTeamId(), dto.getCardId());
            return ResponseEntity.ok("Card jucat cu succes!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}