package com.scavengerhunt.hunt.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;
    
    // Metoda de upload care va fi apelată din CardService
    public String uploadImage(MultipartFile multipartFile) {
        try {
            // 1. Convertim MultipartFile in File temporar
            File file = convertMultipartToFile(multipartFile);
            
            // 2. Upload in Cloudinary
            Uploader uploader = cloudinary.uploader();
            Map uploadResult = uploader.upload(file, ObjectUtils.emptyMap());
            
            // 3. Stergem fisierul temporar
            file.delete();

            // 4. Returnam URL-ul public (sau ID-ul, dar URL e mai simplu)
            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            System.err.println("Cloudinary Upload Error: " + e.getMessage());
            throw new RuntimeException("Eroare la încărcarea imaginii pe Cloudinary", e);
        }
    }

    // Utilitar pentru a converti MultipartFile în File (Cloudinary nu acceptă direct InputStream-ul)
    private File convertMultipartToFile(MultipartFile file) throws IOException {
        // Genereaza nume temporar
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        // Creează fișierul fizic temporar
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}