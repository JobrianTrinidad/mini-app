package com.jo.application.controller;

import com.jo.application.data.entity.ADImage;
import com.jo.application.data.service.ADImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ADImageService adImageService;

    public ImageController() {
        logger.info("ImageController initialized.");
    }
    @PostMapping("/images")
    public ResponseEntity<ImageResponse> saveImage(@RequestBody ImageRequest imageRequest) {
        try {
            ADImage adImage = new ADImage();
            adImage.setName(imageRequest.getName());
            adImage.setDescription(imageRequest.getDescription());
            adImage.setBinaryData(imageRequest.getImageData());
            ADImage savedImage = (ADImage) adImageService.addNewEntity(adImage);
            ImageResponse response = new ImageResponse(savedImage.getAdImageId(), "Image saved successfully.", 200);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageResponse(-1, "Failed to save image: " + e.getMessage(), 500));
        }
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<ADImage> getImageById(@PathVariable int id) {
        ADImage image = (ADImage) adImageService.findEntityByID(id);
        return Optional.ofNullable(image)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/images/{id}")
    public ResponseEntity<ImageResponse> updateImage(@PathVariable int id, @RequestBody ImageRequest imageRequest) {
        try {
            // Set the ID of the updatedImage to match the path variable ID
            ADImage updatedImage = (ADImage) adImageService.findEntityByID(id);
            updatedImage.setName(imageRequest.getName());
            updatedImage.setDescription(imageRequest.getDescription());
            updatedImage.setBinaryData(imageRequest.getImageData());
            // Perform the update operation
            ADImage updatedImg = adImageService.updateEntity(updatedImage);

            ImageResponse response = new ImageResponse(updatedImg.getAdImageId(), "Image Update successfully.", 200);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageResponse(-1, "Failed to Update image: " + e.getMessage(), 500));
        }
    }

    @DeleteMapping("/images/{id}")
    public ResponseEntity<ImageResponse> deleteImage(@PathVariable int id) {
        try {
            ADImage deletedImg = adImageService.deleteImage(id);
            ImageResponse response = new ImageResponse(-1, "Image deleted successfully.", 200);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageResponse(-1, "Failed to Update image: " + e.getMessage(), 500));
        }
    }
}
