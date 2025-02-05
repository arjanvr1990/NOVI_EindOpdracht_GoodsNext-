package com.arjanvanraamsdonk.goodsnext.services;


import com.arjanvanraamsdonk.goodsnext.dtos.ProductDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ProductInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Product;
import com.arjanvanraamsdonk.goodsnext.models.Shop;
import com.arjanvanraamsdonk.goodsnext.repositories.ProductRepository;
import com.arjanvanraamsdonk.goodsnext.repositories.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    public ProductService(ProductRepository productRepository, ShopRepository shopRepository) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return toDto(product);
        } else {
            throw new RecordNotFoundException("Product not found with ID: " + id);
        }
    }

    public ProductDto createProduct(ProductInputDto inputDto) {
        if (inputDto == null) {
            throw new IllegalArgumentException("Input data for creating product cannot be null");
        }

        if (inputDto.getShopId() == null) {
            throw new IllegalArgumentException("Shop ID cannot be null");
        }

        Shop shop = shopRepository.findById(inputDto.getShopId())
                .orElseThrow(() -> new RecordNotFoundException("Shop not found with ID: " + inputDto.getShopId()));

        Optional<Product> existingProduct = productRepository.findByShopAndProductName(shop, inputDto.getProductName());
        if (existingProduct.isPresent()) {
            throw new IllegalArgumentException("Product with name " + inputDto.getProductName() + " already exists in this shop.");
        }

        Product product = toEntity(inputDto);

        product.setShop(shop);

        Product savedProduct = productRepository.save(product);

        return toDto(savedProduct);
    }




    public ProductDto updateProduct(Long id, ProductInputDto inputDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            updateProductFields(product, inputDto);
            Product updatedProduct = productRepository.save(product);
            return toDto(updatedProduct);
        } else {
            throw new RecordNotFoundException("Product not found with ID: " + id);
        }
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Product not found with ID: " + id);
        }
    }

    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getId());
        dto.setShopId(product.getShop() != null ? product.getShop().getShopId() : null);
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductAvailability(product.getProductAvailability());
        return dto;
    }


    private Product toEntity(ProductInputDto inputDto) {
        Product product = new Product();
        product.setProductName(inputDto.getProductName());
        product.setProductDescription(inputDto.getProductDescription());
        product.setProductPrice(inputDto.getProductPrice());
        product.setProductAvailability(inputDto.getProductAvailability());

        if (inputDto.getShopId() != null) {
            Shop shop = shopRepository.findById(inputDto.getShopId())
                    .orElseThrow(() -> new RecordNotFoundException("Shop not found with ID: " + inputDto.getShopId()));
            product.setShop(shop);
        } else {
            throw new IllegalArgumentException("Shop ID cannot be null");
        }

        return product;
    }


    private void updateProductFields(Product product, ProductInputDto inputDto) {
        product.setProductName(inputDto.getProductName());
        product.setProductDescription(inputDto.getProductDescription());
        product.setProductPrice(inputDto.getProductPrice());
        product.setProductAvailability(inputDto.getProductAvailability());
    }
}
