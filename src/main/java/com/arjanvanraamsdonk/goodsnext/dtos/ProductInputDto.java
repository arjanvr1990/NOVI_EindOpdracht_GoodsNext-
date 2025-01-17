package com.arjanvanraamsdonk.goodsnext.dtos;

import jakarta.validation.constraints.*;



public class ProductInputDto {

//    @NotNull(message = "Product name is required")
//    private String productName;
//
//    @Size(max = 1000, message = "Description must be between 0-1000 characters")
//    private String productDescription;
//
//    @Positive(message = "Price must be higher than zero")
//    private Double productPrice;
//
//    @NotNull(message = "Availability must be specified")
//    private Boolean productAvailability;
//
//    private String productImg;

    @NotNull(message = "Product name is required")
    @Size(max = 255, message = "Product name must be at most 255 characters")
    @NotBlank(message = "Product name must not be blank")
    private String productName;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String productDescription;

    @Positive(message = "Price must be higher than zero")
    private Double productPrice;

    @NotNull(message = "Availability must be specified")
    private Boolean productAvailability;

    private String productImg;

    // Getters
    public String getProductName() {
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public Boolean getProductAvailability() {
        return productAvailability;
    }

    public String getProductImg() {
        return productImg;
    }

    // Setters
    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductAvailability(Boolean productAvailability) {
        this.productAvailability = productAvailability;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }
}
