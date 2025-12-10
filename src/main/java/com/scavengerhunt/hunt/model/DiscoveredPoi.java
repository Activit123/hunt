package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "discovered_poi")
@Data
public class DiscoveredPoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "poi_id", nullable = false)
    private Poi poi;

    @Column(name = "discovered_at")
    private LocalDateTime discoveredAt = LocalDateTime.now();
}