package com.yan.webapp.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.yan.webapp.controller.AccountController;
import com.yan.webapp.exception.ForbiddenException;
import com.yan.webapp.exception.ResourceNotFoundException;
import com.yan.webapp.model.Account;
import com.yan.webapp.model.Image;
import com.yan.webapp.model.Product;
import com.yan.webapp.repository.ImageRepository;
import com.yan.webapp.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final UtilityService utilityService;
    private final S3Service s3Service;

    @Value("${aws.bucket.name}")
    private String bucketName;

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    public ImageService(ImageRepository imageRepository, ProductRepository productRepository, UtilityService utilityService, S3Service s3Service) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.utilityService = utilityService;
        this.s3Service = s3Service;
    }

    public Image uploadImage(Long productId, MultipartFile multipartFile) throws IOException {
        // Get Auth user
        Account authUser = utilityService.authenticateUser();

        // Check auth
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        if (product.getAccount() != authUser) {
            throw new ForbiddenException("You are not allowed to upload images to other people's product");
        }

        // Get file from multipartFile
        UUID uuid = UUID.randomUUID();
        String originalFileName = multipartFile.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "-" + uuid.toString() + suffix;
        File file = File.createTempFile(fileName, suffix);
        multipartFile.transferTo(file);

        // Upload to S3
        String key = authUser.getEmail() + "/" + productId + "/" + fileName;
        PutObjectResult result =  s3Service.putObject(bucketName,
                key,
                file);
        logger.info("File {} uploaded to S3 successfully with key {}", fileName, key);

        // Save metadata to database
        Image image = new Image();
        image.setFileName(fileName);
        image.setS3_bucket_path(key);
        image.setProductId(productId);
        image = imageRepository.save(image);
        logger.info("Image with file name {} has been saved to database", fileName);

        return image;
    }

    public List<Image> getImagesByProductId(Long productId) {
        // Get Auth user
        Account authUser = utilityService.authenticateUser();

        // Check auth
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        if (product.getAccount() != authUser) {
            throw new ForbiddenException("You are not allowed to view images uploaded by other people");
        }

        List<Image> images = imageRepository.findByProductId(productId);
        return images;
    }

    public Image getImageById(Long productId, Long imageId) {
        // Get Auth user
        Account authUser = utilityService.authenticateUser();

        // Check auth
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        if (product.getAccount() != authUser) {
            throw new ForbiddenException("You are not allowed to view images uploaded by other people");
        }

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image with id [%s] not found".formatted(imageId)
                ));

        if (image.getProductId() != productId) {
            throw new ForbiddenException("You are not allowed to view images uploaded by other people");
        }

        return image;
    }

    public void deleteImageById(Long productId, Long imageId) {
        // Get Auth user
        Account authUser = utilityService.authenticateUser();

        // Check auth
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        if (product.getAccount() != authUser) {
            throw new ForbiddenException("You are not allowed to delete images uploaded by other people");
        }

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image with id [%s] not found".formatted(imageId)
                ));

        if (image.getProductId() != productId) {
            throw new ForbiddenException("You are not allowed to delete images uploaded by other people");
        }

        s3Service.deleteObject(bucketName, image.getS3_bucket_path());
        imageRepository.deleteById(imageId);
    }
}
