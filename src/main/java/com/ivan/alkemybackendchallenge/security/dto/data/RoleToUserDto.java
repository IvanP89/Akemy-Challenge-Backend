package com.ivan.alkemybackendchallenge.security.dto.data;

/**
 * Encapsulates the information needed to add a role to a user.
 */
public class RoleToUserDto {

    private String username;
    private String roleName;

    public RoleToUserDto() {

    }

    public RoleToUserDto(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public RoleToUserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getRoleName() {
        return roleName;
    }

    public RoleToUserDto setRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }
    
}
