package com.arjanvanraamsdonk.goodsnext.dtos;

import jakarta.validation.constraints.*;



public class ProductInputDto {

    private Long shopId;
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


    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }


    public Boolean getProductAvailability() {
        return productAvailability;
    }

    public void setProductAvailability(Boolean productAvailability) { this.productAvailability = productAvailability; }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
}
