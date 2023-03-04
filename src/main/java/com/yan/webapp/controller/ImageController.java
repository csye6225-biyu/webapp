package com.yan.webapp.controller;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.yan.webapp.model.Image;
import com.yan.webapp.service.ImageService;
import com.yan.webapp.service.S3Service;
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

    @PostMapping(value = "/{productId}/image")
    public ResponseEntity<Image>  uploadImage(
            @PathVariable("productId") Long id,
            @RequestParam("file") MultipartFile multipartFile
    ) throws IOException {
        Image image = imageService.uploadImage(id, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
    }

    @GetMapping(value = "/{productId}/image")
    public ResponseEntity<List<Image>> getImages(
            @PathVariable("productId") Long productId
    ) {
        List<Image> images = imageService.getImagesByProductId(productId);
        return ResponseEntity.status(HttpStatus.OK).body(images);
    }

    @GetMapping(value = "/{productId}/image/{imageId}")
    public ResponseEntity<Image> getImage(
            @PathVariable("productId") Long productId,
            @PathVariable("imageId") Long imageId
    ) {
        Image image = imageService.getImageById(productId, imageId);
        return ResponseEntity.status(HttpStatus.OK).body(image);
    }

    @DeleteMapping(value = "/{productId}/image/{imageId}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable("productId") Long productId,
            @PathVariable("imageId") Long imageId
    ) {
        imageService.deleteImageById(productId, imageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
