package com.arjanvanraamsdonk.goodsnext.dtos;

public class ProductDto {

    private Long productId;
    private Long shopId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Boolean productAvailability;



    public ProductDto(
            Long productId,
            Long shopId,
            String productName,
            String productDescription,
            Double productPrice,
            Boolean productAvailability) {
        this.productId = productId;
        this.shopId = shopId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productAvailability = productAvailability;
    }


    public ProductDto() {
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

}
