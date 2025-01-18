package com.arjanvanraamsdonk.goodsnext.dto;

public class RoleDto {
    private Long roleId;
    private String roleName;

    // Constructors
    public RoleDto() {
    }

    public RoleDto(Long roleId, String roleName) {
        this.roleId = roleId;
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
