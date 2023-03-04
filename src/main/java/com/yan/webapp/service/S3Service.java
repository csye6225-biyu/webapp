package com.yan.webapp.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class S3Service {

    private final AmazonS3 amazonS3Client;

    public S3Service(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    //Object level operations
    public PutObjectResult putObject(String bucketName, String key, File file) {
        return amazonS3Client.putObject(bucketName, key, file);
    }

    //get an object
    public S3Object getObject(String bucketName, String objectKey) {
        return amazonS3Client.getObject(bucketName, objectKey);
    }

    //listing objects
    public ObjectListing listObjects(String bucketName) {
        return amazonS3Client.listObjects(bucketName);
    }

    //deleting an object
    public void deleteObject(String bucketName, String objectKey) {
        amazonS3Client.deleteObject(bucketName, objectKey);
    }
}
