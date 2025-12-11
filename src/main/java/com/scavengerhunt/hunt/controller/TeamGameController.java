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

    // MODIFICAT: Endpoint pentru a marca un POI ca descoperit (folosim /poi/discover)
    @PostMapping("/poi/discover")
    public ResponseEntity<String> discoverPoi(
            @RequestParam Long teamId,
            @RequestParam Long poiId
    ) {
        try {
            gameMasterService.discoverPoi(teamId, poiId);
            return ResponseEntity.ok("POI marcat ca descoperit!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}