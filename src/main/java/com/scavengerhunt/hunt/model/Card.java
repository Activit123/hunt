package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "card")
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    
    @Column(name = "image_url")
    private String imageUrl; // Numele fișierului din MinIO

    @Enumerated(EnumType.STRING)
    private CardType type; 

    @Column(name = "drop_rate_weight")
    private int dropRateWeight;
}