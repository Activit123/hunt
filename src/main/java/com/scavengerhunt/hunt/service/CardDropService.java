package com.scavengerhunt.hunt.service;

import com.scavengerhunt.hunt.model.Card;
import com.scavengerhunt.hunt.model.CardType;
import com.scavengerhunt.hunt.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardDropService {

    private final CardRepository cardRepository;
    private final Random random = new Random();

    /**
     * Algoritmul de extragere ponderată (Weighted Random Drop).
     * 1. Extragem tipul (Legendar, Avantaj, Dezavantaj)
     * 2. Extragem un card specific din acel tip.
     */
    public Card drawRandomCard() {
        int chance = random.nextInt(100) + 1; // 1 - 100

        CardType selectedType;

        // Logica de procente:
        // 1% sansa -> Legendar (daca chance == 100)
        // 49% sansa -> Dezavantaj (daca chance intre 51 si 99)
        // 50% sansa -> Avantaj (daca chance intre 1 si 50)
        
        if (chance == 100) {
            selectedType = CardType.LEGENDARY;
        } else if (chance > 50) {
            selectedType = CardType.DISADVANTAGE;
        } else {
            selectedType = CardType.ADVANTAGE;
        }

        // Luăm toate cardurile de tipul ales
        List<Card> possibleCards = cardRepository.findByType(selectedType);

        if (possibleCards.isEmpty()) {
            // Fallback: Dacă nu am definit inca legendare, dam un avantaj
            possibleCards = cardRepository.findByType(CardType.ADVANTAGE);
        }
        
        if (possibleCards.isEmpty()) {
             throw new RuntimeException("Nu exista cartonașe definite in baza de date!");
        }

        // Alegem un card random din lista filtrată
        return possibleCards.get(random.nextInt(possibleCards.size()));
    }
}