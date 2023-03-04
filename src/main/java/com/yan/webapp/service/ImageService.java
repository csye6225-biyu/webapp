package com.yan.webapp.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.yan.webapp.exception.ForbiddenException;
import com.yan.webapp.exception.ResourceNotFoundException;
import com.yan.webapp.model.Account;
import com.yan.webapp.model.Image;
import com.yan.webapp.model.Product;
import com.yan.webapp.repository.ImageRepository;
import com.yan.webapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final UtilityService utilityService;
    private final S3Service s3Service;

    @Value("${aws.bucket.name}")
    private String bucketName;


    public ImageService(ImageRepository imageRepository, ProductRepository productRepository, UtilityService utilityService, S3Service s3Service) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
        this.utilityService = utilityService;
        this.s3Service = s3Service;
    }

    public Image uploadImage(Long productId, MultipartFile multipartFile) throws IOException {
        System.out.println("Upload Image service");
        // Get Auth user
        Account authUser = utilityService.authenticateUser();
        System.out.println("Auth User:" + authUser);

        // Check auth
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        System.out.println("product.getAccount(): " + product.getAccount());

        if (product.getAccount() != authUser) {
            throw new ForbiddenException("You are not allowed to upload images to other people's product");
        }

        // Get file from multipartFile
        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        File file = File.createTempFile(fileName, prefix);
        multipartFile.transferTo(file);
        System.out.println("Transfer file success");

        // Upload to S3
        System.out.println("Ready to upload photo");
        String key = authUser.getEmail() + "/" + productId + "/" + fileName;
        System.out.println("Bucket name: " + bucketName);
        System.out.println("Key: " + key);
        PutObjectResult result =  s3Service.putObject(bucketName,
                key,
                file);
        System.out.println("Upload success");

        // Save metadata to database
        Image image = new Image();
        image.setFileName(fileName);
        image.setS3_bucket_path(key);
        image.setProductId(productId);
        image = imageRepository.save(image);
        System.out.println("Data saved to database");

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

        return imageRepository.findByProductId(productId);
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
