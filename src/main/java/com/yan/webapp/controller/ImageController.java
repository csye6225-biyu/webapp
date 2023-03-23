package com.yan.webapp.controller;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.yan.webapp.model.Image;
import com.yan.webapp.service.ImageService;
import com.yan.webapp.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/product")
public class ImageController {

    private final S3Service s3Service;
    private final ImageService imageService;

    public ImageController(S3Service s3Service, ImageService imageService) {
        this.s3Service = s3Service;
        this.imageService = imageService;
    }

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @PostMapping(value = "/{productId}/image")
    public ResponseEntity<Image>  uploadImage(
            @PathVariable("productId") Long id,
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException {
        logger.info("Received request to upload image for product ID {}", id);
        Image image = imageService.uploadImage(id, multipartFile);
        logger.info("Image uploaded successfully for product ID {}: {}", id, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
    }

    @GetMapping(value = "/{productId}/image")
    public ResponseEntity<List<Image>> getImages(
            @PathVariable("productId") Long productId
    ) {
        logger.info("Received request to get images for product ID {}", productId);
        List<Image> images = imageService.getImagesByProductId(productId);
        logger.info("Returning {} images for product ID {}", images.size(), productId);
        return ResponseEntity.status(HttpStatus.OK).body(images);
    }

    @GetMapping(value = "/{productId}/image/{imageId}")
    public ResponseEntity<Image> getImage(
            @PathVariable("productId") Long productId,
            @PathVariable("imageId") Long imageId
    ) {
        logger.info("Received request to get image with ID {} for product ID {}", imageId, productId);
        Image image = imageService.getImageById(productId, imageId);
        logger.info("Returning image with ID {} for product ID {}: {}", imageId, productId, image);
        return ResponseEntity.status(HttpStatus.OK).body(image);
    }

    @DeleteMapping(value = "/{productId}/image/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable("productId") Long productId,
            @PathVariable("imageId") Long imageId
    ) {
        logger.info("Received request to delete image with ID {} for product ID {}", imageId, productId);
        imageService.deleteImageById(productId, imageId);
        logger.info("Image with ID {} for product ID {} deleted successfully", imageId, productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
