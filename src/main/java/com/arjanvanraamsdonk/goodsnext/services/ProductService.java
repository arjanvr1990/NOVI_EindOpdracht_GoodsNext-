package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.ProductDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ProductInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Product;
import com.arjanvanraamsdonk.goodsnext.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(transferToDto(product));
        }
        return productDtos;
    }

    public List<ProductDto> getAllProductsByName(String productName) {
        List<Product> products = productRepository.findAllProductsByProductNameEqualsIgnoreCase(productName);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(transferToDto(product));
        }
        return productDtos;
    }

    public ProductDto getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return transferToDto(productOptional.get());
        } else {
            throw new RecordNotFoundException("No product found with id: " + id);
        }
    }

    public ProductDto addProduct(ProductInputDto productInputDto) {
        Product product = transferToEntity(productInputDto);
        Product savedProduct = productRepository.save(product);
        return transferToDto(savedProduct);
    }

    public ProductDto updateProduct(Long id, ProductInputDto productInputDto) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product productToUpdate = productOptional.get();
            productToUpdate.setProductName(productInputDto.getProductName());
            productToUpdate.setProductDescription(productInputDto.getProductDescription());
            productToUpdate.setProductPrice(productInputDto.getProductPrice());
            productToUpdate.setProductAvailability(productInputDto.getProductAvailability());
            productToUpdate.setProductImg(productInputDto.getProductImg());
            Product updatedProduct = productRepository.save(productToUpdate);
            return transferToDto(updatedProduct);
        } else {
            throw new RecordNotFoundException("No product found with id: " + id);
        }
    }

    public ProductDto updatePartialProduct(Long id, ProductInputDto productInputDto) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product productToUpdate = productOptional.get();
            if (productInputDto.getProductName() != null) {
                productToUpdate.setProductName(productInputDto.getProductName());
            }
            if (productInputDto.getProductDescription() != null) {
                productToUpdate.setProductDescription(productInputDto.getProductDescription());
            }
            if (productInputDto.getProductPrice() != null) {
                productToUpdate.setProductPrice(productInputDto.getProductPrice());
            }
            if (productInputDto.getProductAvailability() != null) {
                productToUpdate.setProductAvailability(productInputDto.getProductAvailability());
            }
            if (productInputDto.getProductImg() != null) {
                productToUpdate.setProductImg(productInputDto.getProductImg());
            }
            Product updatedProduct = productRepository.save(productToUpdate);
            return transferToDto(updatedProduct);
        } else {
            throw new RecordNotFoundException("No product found with id: " + id);
        }
    }

    private ProductDto transferToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getId());
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductPrice(product.getProductPrice());
        productDto.setProductAvailability(product.getProductAvailability());
        productDto.setProductImg(product.getProductImg());
        return productDto;
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
