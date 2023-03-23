package com.yan.webapp.controller;


import com.yan.webapp.dto.ProductDTO;
import com.yan.webapp.dto.ProductPatchRequest;
import com.yan.webapp.model.Product;
import com.yan.webapp.service.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    /**
     * GET /v1/product/{productId}
     *
     * @param id
     * @return ProductDTO
     */

    @GetMapping("v1/product/{productId}")
    public ProductDTO getProduct(@PathVariable("productId") Long id) {
        logger.info("Received request to get product with ID {}", id);
        ProductDTO productDTO = productService.getProductById(id);
        logger.info("Returning product: {}", productDTO);
        return productDTO;
    }


    /**
     * POST /v1/product
     *
     * @param product
     * @return ProductDTO
     */
    @PostMapping("v1/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody Product product){
        logger.info("Received request to create product: {}", product);
        ProductDTO productDTO = productService.createProduct(product);
        logger.info("Created product: {}", productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    /**
     * PUT /v1/product/{productId}
     * @param id
     * @param product
     * @return
     */
    @PutMapping("v1/product/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("productId") Long id,
            @Valid @RequestBody Product product
            ) {
        logger.info("Received request to update product with ID {}: {}", id, product);
        productService.updateProductById(id, product);
        logger.info("Product with ID {} updated successfully", id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("v1/product/{productId}")
    public ResponseEntity<Void> partialUpdateUser(
            @PathVariable("productId") Long id,
            @RequestBody @Valid ProductPatchRequest productPatchRequest
            ){
        logger.info("Received request to partially update product with ID {}: {}", id, productPatchRequest);
        productService.partialUpdateProductById(id, productPatchRequest);
        logger.info("Partially updated product with ID {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /v1/product/{productId}De
     * @param id
     * @return
     */
    @DeleteMapping("v1/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id){
        logger.info("Received request to delete product with ID {}", id);
        productService.deleteProductById(id);
        logger.info("Deleted product with ID {}", id);
        return ResponseEntity.noContent().build();
    }
}
