package com.scavengerhunt.hunt.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "app_user")
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password; // In productie ar trebui hash-uita (BCrypt)
    
    private String role; // ADMIN, SUPERVISOR
}