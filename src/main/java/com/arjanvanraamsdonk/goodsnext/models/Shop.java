package com.arjanvanraamsdonk.goodsnext.models;


import jakarta.persistence.*;

@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    private String shopName;

    private String logo;

    @OneToOne
    @JoinColumn(name = "contact_info_id")
    private ContactInfo contactInfo;

    public Shop() {
    }

    public Shop(Long shopId, String shopName, String logo, ContactInfo contactInfo) {
        this.shopId = shopId;
        this.shopName = shopName;
        this.logo = logo;
        this.contactInfo = contactInfo;
    }

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

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }
}
