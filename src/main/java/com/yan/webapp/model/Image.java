package com.yan.webapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "image")
public class Image {

    @Id
    @SequenceGenerator(
            name = "image_id_sequence",
            sequenceName = "image_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_id_sequence"
    )
    private Long imageId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "file_name")
    @NotBlank
    private String fileName;

    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date dateCreated;

    @Column(name = "s3_bucket_path")
    @NotBlank
    private String s3BucketPath;


    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getS3_bucket_path() {
        return s3BucketPath;
    }

    public void setS3_bucket_path(String s3_bucket_path) {
        this.s3BucketPath = s3_bucket_path;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", productId=" + productId +
                ", fileName='" + fileName + '\'' +
                ", dateCreated=" + dateCreated +
                ", s3BucketPath='" + s3BucketPath + '\'' +
                '}';
    }
}
