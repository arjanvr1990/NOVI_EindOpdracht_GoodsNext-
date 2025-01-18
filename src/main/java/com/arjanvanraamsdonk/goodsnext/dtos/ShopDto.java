package com.arjanvanraamsdonk.goodsnext.dtos;



public class ShopDto {

    private Long shopId;
    private String shopName;
    private String logo;
    private ContactInfoDto contactInfo;

    // Constructor
    public ShopDto(Long shopId, String shopName, String logo, ContactInfoDto contactInfo) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.logo = logo;
        this.contactInfo = contactInfo;
    }

    // Default Constructor
    public ShopDto() {
    }

    // Getters and Setters
    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ContactInfoDto getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoDto contactInfo) {
        this.contactInfo = contactInfo;
    }
}
