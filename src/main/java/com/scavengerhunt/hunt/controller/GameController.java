package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.dto.LocationUpdateDto;
import com.scavengerhunt.hunt.service.GameMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller // Atentie: @Controller, nu @RestController pentru WebSockets
@RequiredArgsConstructor
public class GameController {

    private final GameMasterService gameMasterService;

    // Flutter trimite la: /app/move
    @MessageMapping("/move")
    public void handleLocationUpdate(@Payload LocationUpdateDto dto) {
        gameMasterService.updateTeamLocation(dto.getTeamId(), dto.getLat(), dto.getLng());
    }
}