package com.event.image_service.controller;

import com.event.image_service.service.ImageService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ImageController.class)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @Test
    public void testUploadImage_Success() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "dummy".getBytes());

        when(imageService.uploadImage(any())).thenReturn(
                new ResponseEntity<>(Map.of("imageUrl", "url", "imageId", "id"), HttpStatus.OK));

        mockMvc.perform(multipart("/api/images/upload").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value("url"))
                .andExpect(jsonPath("$.imageId").value("id"));
    }

    @Test
    public void testUploadImage_Error() throws Exception {
    MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg",
    "image/jpeg", "dummy".getBytes());

    when(imageService.uploadImage(any())).thenReturn(
    new ResponseEntity<>("Error uploading image",
    HttpStatus.INTERNAL_SERVER_ERROR)
    );

    mockMvc.perform(multipart("/api/images/upload").file(mockFile))
    .andExpect(status().isInternalServerError())
    .andExpect(content().string("Error uploading image"));
    }

    
}
