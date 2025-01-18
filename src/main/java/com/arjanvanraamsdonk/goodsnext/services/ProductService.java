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
        return productRepository.findAll()
                .stream()
                .map(this::transferToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getAllProductsByName(String productName) {
        return productRepository.findAllProductsByProductNameEqualsIgnoreCase(productName)
                .stream()
                .map(this::transferToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No product found with id: " + id));
        return transferToDto(product);
    }

    public ProductDto addProduct(ProductInputDto productInputDto) {
        Product product = transferToEntity(productInputDto);

        Shop shop = shopRepository.findById(productInputDto.getShopId())
                .orElseThrow(() -> new RecordNotFoundException("Shop not found with id: " + productInputDto.getShopId()));
        product.setShop(shop);

        Product savedProduct = productRepository.save(product);
        return transferToDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductInputDto productInputDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No product found with id: " + id));

        product.setProductName(productInputDto.getProductName());
        product.setProductDescription(productInputDto.getProductDescription());
        product.setProductPrice(productInputDto.getProductPrice());
        product.setProductAvailability(productInputDto.getProductAvailability());
        product.setProductImg(productInputDto.getProductImg());

        Product updatedProduct = productRepository.save(product);
        return transferToDto(updatedProduct);
    }

    public ProductDto updatePartialProduct(Long id, ProductInputDto productInputDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("No product found with id: " + id));

        if (productInputDto.getProductName() != null) {
            product.setProductName(productInputDto.getProductName());
        }
        if (productInputDto.getProductDescription() != null) {
            product.setProductDescription(productInputDto.getProductDescription());
        }
        if (productInputDto.getProductPrice() != null) {
            product.setProductPrice(productInputDto.getProductPrice());
        }
        if (productInputDto.getProductAvailability() != null) {
            product.setProductAvailability(productInputDto.getProductAvailability());
        }
        if (productInputDto.getProductImg() != null) {
            product.setProductImg(productInputDto.getProductImg());
        }

        Product updatedProduct = productRepository.save(product);
        return transferToDto(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RecordNotFoundException("No product found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDto transferToDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getShop() != null ? product.getShop().getShopId() : null,
                product.getProductName(),
                product.getProductDescription(),
                product.getProductPrice(),
                product.getProductAvailability(),
                product.getProductImg()
        );
    }


    private Product transferToEntity(ProductInputDto productInputDto) {
        Product product = new Product();
        product.setProductName(productInputDto.getProductName());
        product.setProductDescription(productInputDto.getProductDescription());
        product.setProductPrice(productInputDto.getProductPrice());
        product.setProductAvailability(productInputDto.getProductAvailability());
        product.setProductImg(productInputDto.getProductImg());
        return product;
    }
}
