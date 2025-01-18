package com.arjanvanraamsdonk.goodsnext.models;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    @Column(length = 1000)
    private String productDescription;

    private Double productPrice;

    private Boolean productAvailability;

    private String productImg;

    public Product(
            Long productId,
            String productName,
            String productDescription,
            Double productPrice,
            Boolean productAvailability,
            String productImg) {
        this.id = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productAvailability = productAvailability;
        this.productImg = productImg;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long productId) {
        this.id = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Boolean getProductAvailability() {
        return productAvailability;
    }

    public void setProductAvailability(Boolean productAvailability) {
        this.productAvailability = productAvailability;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

}
