package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "supervisor_assignment")
@Data
public class SupervisorAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user; // Supraveghetorul

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team; // Echipa alocată
}