package com.ivan.alkemybackendchallenge.security.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "app_user")
public class AppUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq")
    @SequenceGenerator(name = "app_user_seq", sequenceName = "app_user_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 320)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", length = 60)
    private String firstName;

    @Column(name = "last_name", length = 60)
    private String lastName;

    @Column(name = "alias", length = 30)
    private String alias;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "app_user_roles",
            joinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    private Collection<Role> roles = new ArrayList<>();

    public AppUser() {

    }

    public AppUser(Long id, String email, String password, String firstName, String lastName, String alias, Collection<Role> roles) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.roles = roles;
    }

    public AppUser(String email, String password, String firstName, String lastName, String alias, Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.roles = roles;
    }

    public AppUser(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public AppUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public AppUser setId(Long id) {
        this.id = id;
        return this;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public AppUser setRoles(Collection<Role> roles) {
        this.roles = roles;
        return this;
    }

    public AppUser addRole(Role role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
        }
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public AppUser setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AppUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AppUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AppUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AppUser setEmail(String email) {
        this.email = email;
        return this;
    }

}