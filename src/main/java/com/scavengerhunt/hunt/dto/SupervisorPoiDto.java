package com.scavengerhunt.hunt.dto;
import lombok.Data;

@Data
public class SupervisorPoiDto {
    private Long id;
    private double latitude;
    private double longitude;
    private String challengeText;
    private String claimCode; // Supraveghetorul trebuie să vadă codul!
    private boolean isIntermediate;
}