package com.aat.application.controller;

import com.aat.application.data.entity.ADImage;
import com.aat.application.data.service.ADImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ImageController {

    @Autowired
    private ADImageService adImageService;

    @PostMapping("/images")
    public ResponseEntity<SaveImageResponse> saveImage(@RequestBody ImageRequest imageRequest) {
        try {
            ADImage adImage = new ADImage();
            adImage.setName(imageRequest.getName());
            adImage.setDescription(imageRequest.getDescription());
            adImage.setBinaryData(imageRequest.getImageData());
            ADImage savedImageId = (ADImage) adImageService.addNewEntity(adImage);
            SaveImageResponse response = new SaveImageResponse(Math.toIntExact(savedImageId.getAdImageId()), "Image saved successfully.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SaveImageResponse(-1, "Failed to save image: " + e.getMessage()));
        }
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<ADImage> getImageById(@PathVariable int id) {
        ADImage image = (ADImage) adImageService.findEntityByID(id);
        return Optional.ofNullable(image)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build()) ;
    }
}
