package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "team_inventory")
@Data
public class TeamInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Card card;

    private boolean isUsed = false;
    private LocalDateTime acquiredAt = LocalDateTime.now();
}