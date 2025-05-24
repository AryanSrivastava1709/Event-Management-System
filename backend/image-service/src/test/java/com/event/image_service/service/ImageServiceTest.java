package com.event.image_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.http.HttpStatus;

public class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    public void testUploadImage_Success() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image content".getBytes());

        when(uploader.upload(any(), any(Map.class))).thenReturn(Map.of(
            "secure_url", "https://cloudinary.com/test.jpg",
            "public_id", "event_images/test"
        ));

        ResponseEntity<?> response = imageService.uploadImage(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, String> body = (Map<String, String>) response.getBody();
        assertEquals("https://cloudinary.com/test.jpg", body.get("imageUrl"));
        assertEquals("event_images/test", body.get("imageId"));
    }

    @Test
    public void testUploadImage_Exception() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "image content".getBytes());

        when(uploader.upload(any(), any(Map.class))).thenThrow(new IOException("Upload failed"));

        ResponseEntity<?> response = imageService.uploadImage(mockFile);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(((String) response.getBody()).contains("Error uploading image"));
    }
}
