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
    private final CloudinaryService cloudinaryService; // <--- INJECTAM NOUL SERVICIU
    public Card createCard(String name, String description, String type, int power, MultipartFile image) {
        // 1. Upload imagine în Cloudinary
        String imageUrl = cloudinaryService.uploadImage(image); // Acum primim un URL direct

        // 2. Salvare date în baza de date
        Card card = new Card();
        card.setName(name);
        card.setDescription(description);

        try {
            card.setType(CardType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tip card invalid!");
        }

        card.setDropRateWeight(power);
        card.setImageUrl(imageUrl); // <--- Salvăm URL-ul complet, nu doar numele fișierului

        return cardRepository.save(card);
    }
}