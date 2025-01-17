package com.arjanvanraamsdonk.goodsnext.dtos;

import jakarta.validation.constraints.NotBlank;

public class ShopInputDto {

    @NotBlank(message = "Shop name is mandatory")
    private String shopName;
    private Long logo; // ID van PhotoUpload
    private ContactInfoDto contactInfo;

    // Getters en Setters
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getLogo() {
        return logo;
    }

    public void setLogo(Long logo) {
        this.logo = logo;
    }

    public ContactInfoDto getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoDto contactInfo) {
        this.contactInfo = contactInfo;
    }
}
