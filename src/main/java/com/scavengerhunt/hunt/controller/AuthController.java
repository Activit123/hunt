package com.scavengerhunt.hunt.controller;

import com.scavengerhunt.hunt.dto.UserLoginDto;
import com.scavengerhunt.hunt.model.AppUser;
import com.scavengerhunt.hunt.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AppUserRepository userRepository;

    // Login simplu (fără token complex pentru simplitate, returnează userul)
    @PostMapping("/login")
    public ResponseEntity<AppUser> login(@RequestBody UserLoginDto dto) {
        return userRepository.findByUsername(dto.getUsername())
                .filter(user -> user.getPassword().equals(dto.getPassword()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    // Endpoint ajutător să creezi primul admin (altfel nu te poți loga)
    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser user) {
        return ResponseEntity.ok(userRepository.save(user));
    }
}