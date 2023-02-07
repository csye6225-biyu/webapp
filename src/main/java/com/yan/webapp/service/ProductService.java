package com.yan.webapp.service;

import com.yan.webapp.dto.ProductDTO;
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

    public ProductService(ProductRepository productRepository, AccountRepository accountRepository) {
        this.productRepository = productRepository;
        this.accountRepository = accountRepository;
    }

    ModelMapper modelMapper = new ModelMapper();

    public Boolean createProduct(Product product) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                // access user details, such as username, authorities, etc.
                String userName = userDetails.getUsername();
                Account accountExists = accountRepository.findByEmail(userName)
                        .orElse(null);
                product.setAccount(accountExists);
                productRepository.save(product);
            }
        }
        return true;
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return modelMapper.map(product, ProductDTO.class);
    }

}
