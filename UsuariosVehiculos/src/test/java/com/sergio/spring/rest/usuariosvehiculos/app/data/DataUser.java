package com.sergio.spring.rest.usuariosvehiculos.app.data;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Role;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;

import java.util.List;
import java.util.Optional;

public class DataUser {

    public static Optional<User> createUser001() {
        List<Role> roles = List.of(new Role(1L, "ROLE_ADMIN"), new Role(2L, "ROLE_USER"));
        return Optional.of(new User(1L, "Sergio", "Mesa", "sergio.mesa@usantoto.edu.co", "sergio", roles, "3205410147", List.of(), null, true, true, false));
    }

    public static Optional<User> createUser002() {
        List<Role> roles = List.of(new Role(3L, "ROLE_GUARD"), new Role(2L, "ROLE_USER"));
        return Optional.of(new User(2L, "David", "Buitrago", "david@correo.edu.co", "david", roles, "3105410147", List.of(), null, true, false, true));
    }

    public static Optional<User> createUser003() {
        List<Role> roles = List.of(new Role(1L, "ROLE_ADMIN"), new Role(2L, "ROLE_USER"));
        return Optional.of(new User(3L, "Pedro", "Martinez", "pedro@correo.edu.co", "pedro", roles, "3175410147", List.of(), null, false, true, false));
    }

    public static Optional<User> createUser004() {
        List<Role> roles = List.of(new Role(2L, "ROLE_GUARD"), new Role(2L, "ROLE_USER"));
        return Optional.of(new User(3L, "Pepa", "Cacerez", "pepa@correo.edu.co", "pepa", roles, "3175410147", List.of(), null, false, false, true));
    }
}
