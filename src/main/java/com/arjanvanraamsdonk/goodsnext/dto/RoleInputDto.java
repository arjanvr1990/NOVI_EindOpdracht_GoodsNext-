package com.arjanvanraamsdonk.goodsnext.dto;

import jakarta.validation.constraints.NotBlank;

public class RoleInputDto {

    @NotBlank(message = "Role name is required")
    private String roleName;

    // Constructor
    public RoleInputDto() {
    }

    public RoleInputDto(String roleName) {
        this.roleName = roleName;
    }

    // Getters and Setters
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
