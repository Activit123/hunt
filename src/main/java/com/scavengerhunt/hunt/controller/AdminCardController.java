package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.model.Card;
import com.scavengerhunt.hunt.repository.CardRepository;
import com.scavengerhunt.hunt.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/cards")
@RequiredArgsConstructor
public class AdminCardController {

    private final CardService cardService;
    private final CardRepository cardRepository;

    // 1. GET ALL CARDS (Rămâne neschimbat)
    @GetMapping
    public Iterable<Card> getAllCards() {
        return cardRepository.findAll();
    }

    // 2. ADD CARD (Rămâne neschimbat, folosește CardService care face upload-ul)
    @Operation(summary = "Adaugă un cartonaș nou", description = "Upload poză + detalii. Tipurile sunt: ADVANTAGE, DISADVANTAGE, LEGENDARY")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Card> addCard(
            @Parameter(description = "Numele cartonașului") @RequestParam("name") String name,
            @Parameter(description = "Descrierea efectului") @RequestParam("description") String description,
            @Parameter(description = "Tipul (ADVANTAGE/DISADVANTAGE/LEGENDARY)") @RequestParam("type") String type,
            @Parameter(description = "Putere sau Șansă Drop (ex: 10)") @RequestParam("power") int power,
            @Parameter(description = "Fișierul imagine", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
            @RequestParam("image") MultipartFile image
    ) {
        Card newCard = cardService.createCard(name, description, type, power, image);
        return ResponseEntity.ok(newCard);
    }
    // NOU: 3. UPDATE CARD (Fără Poză)
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(
            @PathVariable Long id,
            @RequestBody Map<String, Object> cardDetails // Folosim Map ca DTO generic
    ) {
        // Valori presupuse din Flutter: name, description, type, power (ca int)
        String name = (String) cardDetails.get("name");
        String description = (String) cardDetails.get("description");
        String type = (String) cardDetails.get("type");
        Integer power = (Integer) cardDetails.get("power");

        if (name == null || description == null || type == null || power == null) {
            return ResponseEntity.badRequest().build();
        }

        Card updatedCard = cardService.updateCard(id, name, description, type, power);
        return ResponseEntity.ok(updatedCard);
    }

    // NOU: 4. DELETE CARD
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}