package com.ivan.alkemybackendchallenge.security.domain;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * Represents one type of user credential.
 *
 * Used by the security system to recognize if the user has the authorization to access certain functionalities.
 */
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_role_seq")
    @SequenceGenerator(name = "app_user_role_seq", sequenceName = "app_user_role_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    public Role() {
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role(String name) {
       this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Role setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Role role = (Role) o;
        if (role.id != null && this.id != null) {
            return Objects.equals(id, role.id);
        }
        return name != null && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}