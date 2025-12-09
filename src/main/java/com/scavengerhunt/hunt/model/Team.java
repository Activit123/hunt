package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "team")
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "login_code", unique = true)
    private String loginCode;

    private int score = 0;

    @Column(name = "current_lat")
    private double currentLat;

    @Column(name = "current_lng")
    private double currentLng;

    // AM SCOS isFrozen. Nu mai avem nevoie de el.
}