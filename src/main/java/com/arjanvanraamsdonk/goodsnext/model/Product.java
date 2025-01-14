package com.arjanvanraamsdonk.goodsnext.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    @Column(length = 1000)
    private String productDescription;

    private Double productPrice;

    private Boolean productAvailability;

    private String productImg;


    public Long getProductId() {return productId;}

    public void setProductId() {this.productId = productId;}

    public String getProductName() {return productName;}

    public void setProductName() {this.productName = productName;}

    public String getProductDescription() {return productDescription;}

    public void setProductDescription() {this.productDescription = productDescription;}

    public  Double getProductPrice() {return productPrice;}

    public void setProductPrice() {this.productPrice = productPrice;}

    public Boolean getProductAvailability() {return productAvailability;}

    public void setProductAvailability() {this.productAvailability = productAvailability;}

    public String getProductImg() {return productImg;}

    public void setProductImg() {this.productImg = productImg;}
}
