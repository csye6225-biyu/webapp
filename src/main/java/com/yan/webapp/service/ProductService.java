package com.yan.webapp.service;

import com.yan.webapp.dto.ProductDTO;
import com.yan.webapp.dto.ProductPatchRequest;
import com.yan.webapp.exception.BadRequestException;
import com.yan.webapp.exception.ForbiddenException;
import com.yan.webapp.exception.ResourceNotFoundException;
import com.yan.webapp.model.Account;
import com.yan.webapp.model.Product;
import com.yan.webapp.repository.AccountRepository;
import com.yan.webapp.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final UtilityService utilityService;

    public ProductService(ProductRepository productRepository,
                          AccountRepository accountRepository,
                          UtilityService utilityService) {
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
        this.utilityService = utilityService;
    }

    /**
     * Mapper to map model to modelDTO.
     */
    ModelMapper modelMapper = new ModelMapper();

    /**
     * Create a product. Authentication needed.
     * @param product
     * @return ProductDTO
     */
    public ProductDTO createProduct(Product product) {

        //Get owner user from auth
        Account authUser = utilityService.authenticateUser();

        product.setAccount(authUser);
        productRepository.save(product);

        return modelMapper.map(product, ProductDTO.class);
    }

    /**
     * Get a product info by id
     * @param id
     * @return ProductDTO
     */

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(id)
                ));
        return modelMapper.map(product, ProductDTO.class);
    }

    /**
     * Update a product
     * @param productId
     * @param productUpdateRequest
     */
    public void updateProductById(Long productId, Product productUpdateRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        //Get owner user from auth
        Account authUser = utilityService.authenticateUser();

        if (authUser.getId() != product.getOwnerUserId()) {
            throw new ForbiddenException("You are not allowed to update other people's product");
        }


        product.setName(productUpdateRequest.getName());
        product.setDescription(productUpdateRequest.getDescription());
        product.setSku(productUpdateRequest.getSku());
        product.setManufacturer(productUpdateRequest.getManufacturer());
        product.setQuantity(productUpdateRequest.getQuantity());

        productRepository.save(product);
    }

    public void deleteProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        //Get owner user from auth
        Account authUser = utilityService.authenticateUser();

        if (authUser.getId() != product.getOwnerUserId()) {
            throw new ForbiddenException("You are not allowed to update other people's product");
        }

        productRepository.deleteById(productId);
    }

    public void partialUpdateProductById(Long productId, ProductPatchRequest productPatchRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product with id [%s] not found".formatted(productId)
                ));

        //Get owner user from auth
        Account authUser = utilityService.authenticateUser();

        if (authUser.getId() != product.getOwnerUserId()) {
            throw new ForbiddenException("You are not allowed to update other people's product");
        }

        if (productPatchRequest.getName() != null) {
            if (productPatchRequest.getName().length() == 0) {
                throw new BadRequestException("Field name can not be empty");
            }
            product.setName(productPatchRequest.getName());
        }

        if (productPatchRequest.getDescription() != null) {
            if (productPatchRequest.getDescription().length() == 0) {
                throw new BadRequestException("Field description can not be empty");
            }
            product.setDescription(productPatchRequest.getDescription());
        }

        if (productPatchRequest.getSku() != null) {
            if (productPatchRequest.getSku().length() == 0) {
                throw new BadRequestException("Field sku can not be empty");
            }
            product.setSku(productPatchRequest.getSku());
        }

        if (productPatchRequest.getManufacturer() != null) {
            if (productPatchRequest.getManufacturer().length() == 0) {
                throw new BadRequestException("Field manufacturer can not be empty");
            }
            product.setManufacturer(productPatchRequest.getManufacturer());
        }

        if (productPatchRequest.getQuantity() != null) {
            product.setQuantity(productPatchRequest.getQuantity());
        }

        productRepository.save(product);
    }
}
