package com.yan.webapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "product")
public class Product {

    @Id
    @SequenceGenerator(
            name = "product_id_sequence",
            sequenceName = "product_id_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_id_sequence"
    )
    private Long productId;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "sku", unique = true)
    @NotBlank
    private String sku;

    @Column(name = "manufacturer")
    @NotBlank
    private String manufacturer;

    @Min(value = 0, message = "Attribute must be a positive integer")
    @Max(value = 100, message = "Quantity can not excel 100")
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "data_added")
    private Date dateAdded;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date dataLastUpdated;

    @ManyToOne
    @JoinColumn(name = "owner_user_id", nullable = false)
    private Account account;

    public Long getId() {
        return productId;
    }

    public void setId(Long id) {
        this.productId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDataLastUpdated() {
        return dataLastUpdated;
    }

    public void setDataLastUpdated(Date dataLastUpdated) {
        this.dataLastUpdated = dataLastUpdated;
    }

    public Account getAccount() {
        return account;
    }

    public Long getOwnerUserId() {
        return account.getId();
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
