package com.ivan.alkemybackendchallenge.security.dto.data;

import java.util.ArrayList;
import java.util.Collection;

public class AppUserDto {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String alias;
    private Collection<RoleDto> roles = new ArrayList<>();

    public AppUserDto() {

    }

    public AppUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AppUserDto(Long id, String email, String password, String firstName, String lastName, String alias,
                      Collection<RoleDto> roles) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public AppUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AppUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AppUserDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AppUserDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public AppUserDto setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public Collection<RoleDto> getRoles() {
        return roles;
    }

    public AppUserDto setRoles(Collection<RoleDto> roles) {
        this.roles = roles;
        return this;
    }
    
}
