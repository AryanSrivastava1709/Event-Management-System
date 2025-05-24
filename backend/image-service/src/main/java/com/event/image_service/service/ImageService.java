package com.event.image_service.service;


import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class ImageService {
    
    @Autowired
    private Cloudinary cloudinary;

    public ResponseEntity<Object> uploadImage(MultipartFile file) throws IOException{

        try {
            Map<?,?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto","public_id", file.getOriginalFilename(),"folder", "event_images"));

            String imageUrl = (String) result.get("secure_url");
            String imageId = (String) result.get("public_id");

            Map<String, String> response = Map.of(
                "imageUrl", imageUrl,
                "imageId", imageId
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading image: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
