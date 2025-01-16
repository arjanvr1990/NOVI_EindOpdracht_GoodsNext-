package com.arjanvanraamsdonk.goodsnext.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ShopInputDto {

    @NotBlank(message = "Shop name is required")
    @Size(max = 255, message = "Shop name must be at most 255 characters")
    private String shopName;

    private String logo;

    private ContactInfoDto contactInfo;

    // Default Constructor
    public ShopInputDto() {
    }

    // Getters and Setters
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

