package com.yan.webapp.controller;


import com.yan.webapp.dto.ProductDTO;
import com.yan.webapp.dto.ProductPatchRequest;
import com.yan.webapp.model.Product;
import com.yan.webapp.service.ProductService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Validated
public class ProductController {
    private final ProductService productService;
    private final Validator validator;

    public ProductController(ProductService productService, Validator validator) {
        this.productService = productService;
        this.validator = validator;
    }

    /**
     * GET /v1/product/{productId}
     *
     * @param id
     * @return ProductDTO
     */

    @GetMapping("v1/product/{productId}")
    public ProductDTO getProduct(@PathVariable("productId") Long id) {
        return productService.getProductById(id);
    }


    /**
     * POST /v1/product
     *
     * @param product
     * @return ProductDTO
     */
    @PostMapping("v1/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody Product product){
        ProductDTO productDTO = productService.createProduct(product);
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
        productService.updateProductById(id, product);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("v1/product/{productId}")
    public ResponseEntity<Void> partialUpdateUser(
            @PathVariable("productId") Long id,
            @RequestBody @Valid ProductPatchRequest productPatchRequest
            ){
        productService.partialUpdateProductById(id, productPatchRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /v1/product/{productId}De
     * @param id
     * @return
     */
    @DeleteMapping("v1/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
