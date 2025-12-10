package com.scavengerhunt.hunt.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "poi")
@Data
public class Poi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    private int radius;
    private String challengeText;
    private String claimCode; // Codul secret
    private int rewardPoints;
    public boolean isIntermediate;
}