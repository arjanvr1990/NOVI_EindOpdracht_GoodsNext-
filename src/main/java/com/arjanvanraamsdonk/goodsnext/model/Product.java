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

    private double productPrice;

    private boolean productAvailability;

}
