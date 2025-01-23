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
        // Controleer of de input null is
        if (inputDto == null) {
            throw new IllegalArgumentException("Input data for creating product cannot be null");
        }

        // Controleer of shopId aanwezig is
        if (inputDto.getShopId() == null) {
            throw new IllegalArgumentException("Shop ID cannot be null");
        }

        // Zoek de shop op basis van het shopId
        Shop shop = shopRepository.findById(inputDto.getShopId())
                .orElseThrow(() -> new RecordNotFoundException("Shop not found with ID: " + inputDto.getShopId()));

        // Zet het ProductInputDto om naar een Product-entiteit
        Product product = toEntity(inputDto);

        // Koppel de gevonden Shop aan het Product
        if (shop != null) {
            product.setShop(shop);
        } else {
            throw new IllegalArgumentException("Shop object is null. Unable to create product.");
        }

        // Sla het Product op in de database
        Product savedProduct = productRepository.save(product);

        // Converteer het opgeslagen Product naar een DTO en retourneer dit
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

    // Helper Methods
    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setProductId(product.getId());
        dto.setShopId(product.getShop() != null ? product.getShop().getShopId() : null); // Voeg shopId mapping toe
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
