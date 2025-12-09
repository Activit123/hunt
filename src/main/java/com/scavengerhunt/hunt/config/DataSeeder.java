package com.scavengerhunt.hunt.config;

import com.scavengerhunt.hunt.model.AppUser;
import com.scavengerhunt.hunt.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificăm dacă există deja adminul 'admin'
        if (userRepository.findByUsername("admin").isEmpty()) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword("admin123"); // Parolă simplă pentru dev
            admin.setRole("ADMIN");
            
            userRepository.save(admin);
            System.out.println("✅ CONT ADMIN CREAT: User: admin | Pass: admin123");
        }
    }
}