package com.scavengerhunt.hunt.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    // Schimbăm cheile să citească direct din Variabilele de Mediu (cele setate în Railway)
    // De asemenea, am adăugat o valoare default (:) ca să NU crape la pornire dacă valoarea lipsește
    @Value("${MINIO_URL:http://minio-production-15f0.up.railway.app}")
    private String url;

    @Value("${MINIO_ACCESS_KEY:admin}") // Folosim cheia MINIO_ACCESS_KEY din Railway
    private String accessKey;

    @Value("${MINIO_SECRET_KEY:password12345}") // Folosim cheia MINIO_SECRET_KEY din Railway
    private String secretKey;

    // ... restul codului ...

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                // Aici va citi valorile (care nu mai sunt goale)
                .credentials(accessKey, secretKey)
                .build();
    }
}