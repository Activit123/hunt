package com.scavengerhunt.hunt.dto;
import lombok.Data;

@Data
public class TeamLoginDto {
    private String teamName; // Opțional la login, obligatoriu la înregistrare
    private String accessCode; // Codul dat de admin
}