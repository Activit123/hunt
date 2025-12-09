package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "active_effect")
@Data
public class ActiveEffect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "target_team_id")
    private Team targetTeam;

    @ManyToOne
    @JoinColumn(name = "attacker_team_id")
    private Team attackerTeam;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "is_active")
    private boolean isActive = true;
}