package com.arjanvanraamsdonk.goodsnext.dto;

import java.util.List;

public class UserInputDto {
    private String username;
    private String password;
    private ContactInfoDto contactInfo;
    private List<RoleDto> roles; // Add this field

    // Default Constructor
    public UserInputDto() {
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ContactInfoDto getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoDto contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<RoleDto> getRoles() {
        return roles; // Add getter
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles; // Add setter
    }
}
