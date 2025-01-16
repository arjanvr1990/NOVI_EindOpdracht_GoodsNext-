package com.arjanvanraamsdonk.goodsnext.dto;

import java.util.List;

public class UserDto {

    private Long userId;
    private String username;
    private String password; // In productie zou je dit nooit retourneren!
    private ContactInfoDto contactInfo;
    private List<RoleDto> roles;

    // Constructors
    public UserDto() {
    }

    public UserDto(Long userId, String username, ContactInfoDto contactInfo, List<RoleDto> roles) {
        this.userId = userId;
        this.username = username;
        this.contactInfo = contactInfo;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ContactInfoDto getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoDto contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }
}
