package com.yan.webapp.controller;


import com.yan.webapp.dto.ProductDTO;
import com.yan.webapp.model.Product;
import com.yan.webapp.service.ProductService;
import jakarta.validation.Valid;
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
     * Get Product Information
     *
     * @param id
     * @return Product
     */

    @GetMapping("v1/product/{productId}")
    public ProductDTO getProduct(@PathVariable("productId") Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("v1/product")
    public void createProduct(@Valid @RequestBody Product product){
        System.out.println("request body: " + product);
        productService.createProduct(product);
    }

}
