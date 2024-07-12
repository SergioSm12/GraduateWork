package com.sergio.spring.rest.usuariosvehiculos.app.models.entities;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.sergio.spring.rest.usuariosvehiculos.app.models.interfaces.IUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User implements IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "EL campo nombre no puede estar vacío.")
    private String name;

    @NotBlank(message = "EL campo apellido no puede estar vacío.")
    private String lastName;
    @Column(unique = true)
    @NotBlank(message = "EL campo correo no puede estar vacío.")
    @Email
    private String email;

    @NotBlank(message = "EL campo contraseña no puede estar vacío.")
    private String password;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})})
    private List<Role> roles;
    @NotBlank(message = "EL campo numero de celular no puede estar vacío.")
    private String phoneNumber;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Receipt> receipts;

    @Column(name = "active")
    private Boolean active;
    @Transient
    private boolean admin;
    @Transient
    private boolean guard;

    public User() {
    }

    public User(Long id, String name, String lastName, String email, String password, List<Role> roles, String phoneNumber, List<Vehicle> vehicles, List<Receipt> receipts, Boolean active, boolean admin, boolean guard) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.phoneNumber = phoneNumber;
        this.vehicles = vehicles;
        this.receipts = receipts;
        this.active = active;
        this.admin = admin;
        this.guard = guard;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<Receipt> receipts) {
        this.receipts = receipts;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public boolean isGuard() {
        return guard;
    }

    public void setGuard(boolean guard) {
        this.guard = guard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return admin == user.admin && guard == user.guard && Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(vehicles, user.vehicles) && Objects.equals(receipts, user.receipts) && Objects.equals(active, user.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, email, password, roles, phoneNumber, vehicles, receipts, active, admin, guard);
    }
}
