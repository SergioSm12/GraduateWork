package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserChangePasswordRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserRequest;

public interface IUserService {

    // Users
    List<UserDto> findAll();

    long getTotalCountUsers();

    Page<UserDto> findAll(Pageable pageable);

    Optional<UserDto> findById(Long id);

    List<UserDto> findActiveUsers();

    List<UserDto> findInactiveUsers();

    Optional<User> findByIdUser(Long id);

    UserDto save(User user);

    Optional<UserDto> update(UserRequest user, Long id);

    void changePassword(UserChangePasswordRequest password, Long id);

    void activateUser(Long userId);

    void deactivateUser(Long userId);

    void remove(Long id);



}
