package com.scavengerhunt.hunt.dto;
import lombok.Data;

@Data
public class LocationUpdateDto {
    private Long teamId;
    private double lat;
    private double lng;
}