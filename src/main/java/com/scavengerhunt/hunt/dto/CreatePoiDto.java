package com.scavengerhunt.hunt.dto;
import lombok.Data;
import lombok.Getter;

@Data
public class CreatePoiDto {
    private double lat;
    private double lng;
    private String challengeText;
    private int rewardPoints;
    private int radius;
    private boolean intermediate; // true = provocare intermediara, false = locatie harta

    @Getter
    public String claimCode;

}