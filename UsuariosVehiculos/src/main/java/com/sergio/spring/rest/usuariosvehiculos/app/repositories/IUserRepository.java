package com.sergio.spring.rest.usuariosvehiculos.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;

public interface IUserRepository extends CrudRepository<User, Long> {

    List<User> findByActiveTrue();

    List<User> findByActiveFalse();

    @Query("select u from User  u where u.email=?1")
    Optional<User> getUserByEmail(String email);


    //paginacion
    Page<User> findAll(Pageable pageable);


}
