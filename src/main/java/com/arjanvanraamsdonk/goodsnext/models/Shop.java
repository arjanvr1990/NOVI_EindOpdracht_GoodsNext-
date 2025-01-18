package com.arjanvanraamsdonk.goodsnext.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id") // Zorgt ervoor dat de databasekolom correct wordt gebruikt
    private Long shopId;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @ManyToOne
    @JoinColumn(name = "photo_upload_id", referencedColumnName = "upload_id")
    private PhotoUpload logo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_info_id", referencedColumnName = "id")
    private ContactInfo contactInfo;

    @ManyToMany(mappedBy = "shops")
    private Set<User> users = new HashSet<>();

    // Een bidirectionele relatie naar Product toevoegen
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    // Getters en Setters
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

    public PhotoUpload getLogo() {
        return logo;
    }

    public void setLogo(PhotoUpload logo) {
        this.logo = logo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
