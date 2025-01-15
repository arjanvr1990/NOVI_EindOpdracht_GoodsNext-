package com.arjanvanraamsdonk.goodsnext.service;


import com.arjanvanraamsdonk.goodsnext.models.Product;
import com.arjanvanraamsdonk.goodsnext.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.dto.ProductDto;
import com.arjanvanraamsdonk.goodsnext.dto.ProductInputDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    // We importeren de repository nu in de service in plaats van in de controller.
    // dit mag met constructor injection of autowire.
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    // Vanuit de repository kunnen we een lijst van Products krijgen, maar de communicatie container tussen Service en
    // Controller is de Dto. We moeten de Products dus vertalen naar ProductDtos. Dit moet een voor een, omdat
    // de translateToDto() methode geen lijst accepteert als argument, dus gebruiken we een for-loop.
    public List<ProductDto> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductDto> productDtoList = new ArrayList<>();

        for(Product product : productList) {
            ProductDto dto = transferToDto(product);
            productDtoList.add(dto);
        }
        return productDtoList;
    }

    // Vanuit de repository kunnen we een lijst van Products met een bepaalde brand krijgen, maar de communicatie
    // container tussen Service en Controller is de Dto. We moeten de Products dus vertalen naar ProductDtos. Dit
    // moet een voor een, omdat de translateToDto() methode geen lijst accepteert als argument, dus gebruiken we een for-loop.
    public List<ProductDto> getAllProductsByBrand(String brand) {
        List<Product> productList = productRepository.findAllProductsByProductNameEqualsIgnoreCase(brand);
        List<ProductDto> productDtoList = new ArrayList<>();

        for(Product product : productList) {
            ProductDto dto = transferToDto(product);
            productDtoList.add(dto);
        }
        return productDtoList;
    }

    // Deze methode is inhoudelijk hetzelfde als het was in de vorige opdracht. Wat verandert is, is dat we nu checken
    // op optional.isPresent in plaats van optional.isEmpty en we returnen een ProductDto in plaats van een Product.
    public ProductDto getProductById(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()){
            Product product = productOptional.get();
            return transferToDto(product);
        } else {
            throw new RecordNotFoundException("geen product gevonden");
        }
    }

    // In deze methode moeten we twee keer een vertaal methode toepassen.
    // De eerste keer van dto naar product, omdat de parameter een dto is.
    // De tweede keer van product naar dto, omdat de return waarde een dto is.
    public ProductDto addProduct(ProductInputDto dto) {

        Product product = transferToProduct(dto);
        productRepository.save(product);

        return transferToDto(product);
    }

    // Deze methode is inhoudelijk niet veranderd. Het is alleen verplaatst naar de Service laag.
    public void deleteProduct(@RequestBody Long id) {

        productRepository.deleteById(id);

    }

    // Deze methode is inhoudelijk niet veranderd, alleen staat het nu in de Service laag en worden er Dto's en
    // vertaal methodes gebruikt.
    public ProductDto updateProduct(Long id, ProductInputDto newProduct) {

        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()){

            Product product1 = productOptional.get();


            product1.setProductName(newProduct.getProductName());
            product1.setProductDescription(newProduct.getProductDescription());
            product1.setProductPrice(newProduct.getProductPrice());
            product1.setProductAvailability(newProduct.getProductAvailability());
            product1.setProductImg(newProduct.getProductImg());
            Product returnProduct = productRepository.save(product1);

            return transferToDto(returnProduct);

        } else {

            throw new  RecordNotFoundException("geen product gevonden");

        }

    }

    // Dit is de vertaal methode van ProductInputDto naar Product.
    public Product transferToProduct(ProductInputDto dto){
        var product = new Product();

        product.setProductName(dto.getProductName());
        product.setProductDescription(dto.getProductDescription());
        product.setProductPrice(dto.getProductPrice());
        product.setProductAvailability(dto.getProductAvailability());
        product.setProductImg(dto.getProductImg());

        return product;
    }

    // Dit is de vertaal methode van Product naar ProductDto
    public ProductDto transferToDto(Product product){
        ProductDto dto = new ProductDto();

        dto.setId(product.getId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductAvailability(product.getProductAvailability());
        dto.setProductImg(product.getProductImg());


        return dto;
    }
};