package com.yan.webapp.controller;


import com.yan.webapp.dto.AccountUpdateRequest;
import com.yan.webapp.dto.ProductDTO;
import com.yan.webapp.model.Product;
import com.yan.webapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
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
     * @param product
     * @return ProductDTO
     */
    @PostMapping("v1/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody Product product){
        ProductDTO productDTO = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    @PutMapping("v1/product/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("productId") Long id,
            @Valid @RequestBody Product product
            ) {
        productService.updateProductById(id, product);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("v1/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
}
