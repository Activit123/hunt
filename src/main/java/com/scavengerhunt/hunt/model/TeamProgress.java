package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "team_progress")
@Data
public class TeamProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "poi_id")
    private Poi poi;

    @Column(name = "completed_at")
    private LocalDateTime completedAt = LocalDateTime.now();
}