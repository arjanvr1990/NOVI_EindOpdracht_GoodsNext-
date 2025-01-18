package com.arjanvanraamsdonk.goodsnext.dto;


public class ProductDto {

    private Long id;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Boolean productAvailability;
    private String productImg;

    // Constructor
    public ProductDto(
            Long id,
            String productName,
            String productDescription,
            Double productPrice,
            Boolean productAvailability,
            String productImg) {
        this.id = id;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productAvailability = productAvailability;
        this.productImg = productImg;
    }

    // Default Constructor
    public ProductDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

