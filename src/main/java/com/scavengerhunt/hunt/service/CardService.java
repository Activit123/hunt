package com.scavengerhunt.hunt.service;

import com.scavengerhunt.hunt.model.Card;
import com.scavengerhunt.hunt.model.CardType;
import com.scavengerhunt.hunt.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final MinioService minioService;

    public Card createCard(String name, String description, String type, int power, MultipartFile image) {
        // 1. Urcăm poza în MinIO și primim numele fișierului salvat
        String imageFileName = minioService.uploadFile(image);

        // 2. Creăm obiectul Card
        Card card = new Card();
        card.setName(name);
        card.setDescription(description);
        
        // Convertim String-ul primit (ex: "LEGENDARY") în Enum
        try {
            card.setType(CardType.valueOf(type.toUpperCase())); 
        } catch (IllegalArgumentException e) {
            // Default sau eroare dacă tipul e greșit
            throw new RuntimeException("Tip card invalid! Folosește: ADVANTAGE, DISADVANTAGE, LEGENDARY");
        }

        card.setDropRateWeight(power); // Puterea sau șansa de drop
        card.setImageUrl(imageFileName); // Salvăm doar referința la fișier

        // 3. Salvăm în baza de date
        return cardRepository.save(card);
    }
}