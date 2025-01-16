package com.arjanvanraamsdonk.goodsnext.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;


    // Default Constructor
    public Role() {
    }

    // Constructor



    // Getters, Setters, Constructors

    public Role(String roleName) {
        this.roleName = roleName;
    }

    // Getters and Setters
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
