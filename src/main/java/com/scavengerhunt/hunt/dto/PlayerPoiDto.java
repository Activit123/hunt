package com.scavengerhunt.hunt.dto;
import lombok.Data;

@Data
public class PlayerPoiDto {
    private Long id;
    private double latitude;
    private double longitude;
    private int radius;
    private String challengeText; // Provocarea o văd doar când sunt aproape (frontend logic)
    private int rewardPoints;
    private boolean isIntermediate;
    // FARA claimCode!
}