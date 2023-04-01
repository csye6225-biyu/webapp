package com.yan.webapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.util.Date;

public class ProductDTO {

        @Column(name = "id")
        private Long productId;

        private String name;

        private String description;

        private String sku;

        private String manufacturer;

        private Long quantity;

        @JsonProperty("date_added")
        private Date dateAdded;

        @JsonProperty("date_last_updated")
        private Date dataLastUpdated;

        @JsonProperty("owner_user_id")
        private Long accountId;

        public Long getProductId() {
                return productId;
        }

        public void setProductId(Long productId) {
                this.productId = productId;
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

        public Long getAccountId() {
                return accountId;
        }

        public void setAccountId(Long accountId) {
                this.accountId = accountId;
        }

        @Override
        public String toString() {
                return "ProductDTO{" +
                        "productId=" + productId +
                        ", name='" + name + '\'' +
                        ", description='" + description + '\'' +
                        ", sku='" + sku + '\'' +
                        ", manufacturer='" + manufacturer + '\'' +
                        ", quantity=" + quantity +
                        ", dateAdded=" + dateAdded +
                        ", dataLastUpdated=" + dataLastUpdated +
                        ", accountId=" + accountId +
                        '}';
        }
}
