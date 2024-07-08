package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Role;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;

import com.sergio.spring.rest.usuariosvehiculos.app.models.interfaces.IUser;

import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IRoleRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IUserRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVehicleRepository;


@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    //vehicle
    @Autowired
    private IVehicleRepository vehicleRepository;


    //Encriptar
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users
                .stream()
                .map(u -> DtoMapperUser.builder()
                        .setUser(u)
                        .build())
                .collect(Collectors.toList());
    }

    //count users 
    @Override
    @Transactional(readOnly = true)
    public long getTotalCountUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(u -> DtoMapperUser.builder().setUser(u).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findByEmail(String email) {
        return userRepository.getUserByEmail(email).map(u ->
                DtoMapperUser
                        .builder()
                        .setUser(u)
                        .build()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(u ->
                DtoMapperUser
                        .builder()
                        .setUser(u)
                        .build());
    }

    //Listar vehiculos activos
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findActiveUsers() {
        List<User> activeUsers = userRepository.findByActiveTrue();
        return activeUsers.stream()
                .map(u -> DtoMapperUser.builder().setUser(u).build())
                .collect(Collectors.toList());
    }


    //listar vehiculos desactivados
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findInactiveUsers() {
        List<User> inactiveUsers = userRepository.findByActiveFalse();
        return inactiveUsers.stream()
                .map(u -> DtoMapperUser.builder().setUser(u).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        String passwordBCrypt = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBCrypt);
        user.setActive(true);
        user.setRoles(getRoles(user));
        String emailLower = user.getEmail().toLowerCase();
        user.setEmail(emailLower);
        //DESCRIPCION return: trae la instancia asigna el user y construye
        return DtoMapperUser.builder().setUser(userRepository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserRequest user, Long id) {
        Optional<User> o = userRepository.findById(id);
        User userOptional = null;

        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setRoles(getRoles(user));
            userDb.setName(user.getName());
            userDb.setLastName(user.getLastName());
            userDb.setEmail(user.getEmail());
            userDb.setPhoneNumber(user.getPhoneNumber());
            userDb.setActive(true);
            userOptional = userRepository.save(userDb);
        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    }

    @Override
    @Transactional
    public void changePassword(UserChangePasswordRequest password, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String passwordBCrypt = passwordEncoder.encode(password.getPassword());
            user.setPassword(passwordBCrypt);
            userRepository.save(user);
        }
    }

    //activate Vehicle
    @Override
    @Transactional
    public void activateUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(true);
            userRepository.save(user);
        }
    }

    //Deactivate Vehicle
    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }


    //Create

    //Roles
    private List<Role> getRoles(IUser user) {
        Optional<Role> ou = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        if (ou.isPresent()) {
            roles.add(ou.orElseThrow());
        }

        //Añadir admin
        if (user.isAdmin()) {
            Optional<Role> oa = roleRepository.findByName("ROLE_ADMIN");
            if (oa.isPresent()) {
                roles.add(oa.orElseThrow());
            }
        }
        //Añadir guard
        if (user.isGuard()) {
            Optional<Role> og = roleRepository.findByName("ROLE_GUARD");
            if (og.isPresent()) {
                roles.add(og.orElseThrow());
            }
        }

        return roles;
    }
}
