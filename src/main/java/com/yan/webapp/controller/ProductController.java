package com.yan.webapp.controller;


import com.timgroup.statsd.StatsDClient;
import com.yan.webapp.dto.ProductDTO;
import com.yan.webapp.dto.ProductPatchRequest;
import com.yan.webapp.model.Product;
import com.yan.webapp.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class ProductController {
    private final ProductService productService;
    private final StatsDClient statsDClient;

    public ProductController(ProductService productService, StatsDClient statsDClient) {
        this.productService = productService;
        this.statsDClient = statsDClient;
    }

    Logger logger = LoggerFactory.getLogger(AccountController.class);

    /**
     * GET /v1/product/{productId}
     *
     * @param id
     * @return ProductDTO
     */

    @GetMapping("v2/product/{productId}")
    public ProductDTO getProduct(@PathVariable("productId") Long id) {
        logger.info("Received request to get product with ID {}", id);
        statsDClient.incrementCounter("endpoint.product.http.get");
        long startTime = System.currentTimeMillis();

        ProductDTO productDTO = productService.getProductById(id);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.product.http.get.time", elapsedTime);
        logger.info("Returning product: {}", productDTO);

        return productDTO;
    }


    /**
     * POST /v1/product
     *
     * @param product
     * @return ProductDTO
     */
    @PostMapping("v2/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody Product product){
        logger.info("Received request to create product: {}", product);
        statsDClient.incrementCounter("endpoint.product.http.post");
        long startTime = System.currentTimeMillis();

        ProductDTO productDTO = productService.createProduct(product);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.product.http.post.time", elapsedTime);
        logger.info("Created product: {}", productDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    /**
     * PUT /v1/product/{productId}
     * @param id
     * @param product
     * @return
     */
    @PutMapping("v2/product/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("productId") Long id,
            @Valid @RequestBody Product product
            ) {
        logger.info("Received request to update product with ID {}: {}", id, product);
        statsDClient.incrementCounter("endpoint.product.http.put");
        long startTime = System.currentTimeMillis();

        productService.updateProductById(id, product);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.product.http.put.time", elapsedTime);
        logger.info("Product with ID {} updated successfully", id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("v2/product/{productId}")
    public ResponseEntity<Void> partialUpdateUser(
            @PathVariable("productId") Long id,
            @RequestBody @Valid ProductPatchRequest productPatchRequest
            ){
        logger.info("Received request to partially update product with ID {}: {}", id, productPatchRequest);
        statsDClient.incrementCounter("endpoint.product.http.patch");
        long startTime = System.currentTimeMillis();

        productService.partialUpdateProductById(id, productPatchRequest);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.product.http.patch.time", elapsedTime);
        logger.info("Partially updated product with ID {}", id);

        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /v1/product/{productId}De
     * @param id
     * @return
     */
    @DeleteMapping("v2/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long id){
        logger.info("Received request to delete product with ID {}", id);
        statsDClient.incrementCounter("endpoint.product.http.delete");
        long startTime = System.currentTimeMillis();

        productService.deleteProductById(id);

        long elapsedTime = System.currentTimeMillis() - startTime;
        statsDClient.recordExecutionTime("endpoint.product.http.delete.time", elapsedTime);
        logger.info("Deleted product with ID {}", id);

        return ResponseEntity.noContent().build();
    }
}
