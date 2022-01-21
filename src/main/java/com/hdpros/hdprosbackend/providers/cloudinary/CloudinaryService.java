package com.hdpros.hdprosbackend.providers.cloudinary;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map<String, String> upload(MultipartFile file);

    ResponseEntity<ByteArrayResource> downloadImg(String publicId, int width, int height, boolean isAvatar);
}
