package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Role;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserChangePasswordRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @MockBean
    IUserRepository userRepository;

    @Autowired
    IUserService userService;

    @Test
    void testFindAll() {
        List<User> data = Arrays.asList(DataUser.createUser001().orElseThrow(), DataUser.createUser002().orElseThrow());
        when(userRepository.findAll()).thenReturn(data);

        //when
        List<UserDto> users = userService.findAll();

        //then
        assertFalse(users.isEmpty());
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(userDto -> userDto.getId().equals(DataUser.createUser001().orElseThrow().getId())));
        verify(userRepository).findAll();
    }

    @Test
    void testGetTotalCountUsers() {
        List<User> users = List.of(DataUser.createUser001().orElseThrow(), DataUser.createUser002().orElseThrow());
        Long totalUsers = (long) users.size();
        when(userRepository.count()).thenReturn(totalUsers);

        long result = userService.getTotalCountUsers();

        assertEquals(totalUsers, result);

        verify(userRepository).count();
    }

    @Test
    void testFindByEmail() {
        String email = DataUser.createUser002().orElseThrow().getEmail();
        when(userRepository.getUserByEmail(email)).thenReturn(DataUser.createUser002());

        UserDto userDto = userService.findByEmail(email).orElseThrow();

        assertEquals(2L, userDto.getId());
        assertEquals("David", userDto.getName());
        assertEquals("Buitrago", userDto.getLastName());
        assertEquals("david@correo.edu.co", userDto.getEmail());

        verify(userRepository).getUserByEmail(email);
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(DataUser.createUser001());
        when(userRepository.findById(2L)).thenReturn(DataUser.createUser002());

        UserDto userDto1 = userService.findById(1L).orElseThrow();
        UserDto userDto2 = userService.findById(2L).orElseThrow();

        assertEquals(1L, userDto1.getId());
        assertEquals("Sergio", userDto1.getName());
        assertEquals("sergio.mesa@usantoto.edu.co", userDto1.getEmail());
        assertTrue(userDto1.isAdmin());

        assertEquals(2L, userDto2.getId());
        assertEquals("David", userDto2.getName());
        assertEquals("david@correo.edu.co", userDto2.getEmail());
        assertTrue(userDto2.isGuard());
        verify(userRepository, times(2)).findById(anyLong());
    }

    @Test
    void testFindActiveUsers() {
        List<User> activeUsers = Arrays.asList(DataUser.createUser001().orElseThrow(), DataUser.createUser002().orElseThrow());
        when(userRepository.findByActiveTrue()).thenReturn(activeUsers);

        List<UserDto> result = userService.findActiveUsers();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());

        UserDto user1 = DtoMapperUser.builder().setUser(DataUser.createUser001().orElseThrow()).build();
        UserDto user2 = DtoMapperUser.builder().setUser(DataUser.createUser002().orElseThrow()).build();

        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));

        verify(userRepository).findByActiveTrue();
    }

    @Test
    void testFindInactiveUsers() {
        List<User> inactiveUser = Arrays.asList(DataUser.createUser003().orElseThrow(), DataUser.createUser004().orElseThrow());
        when(userRepository.findByActiveFalse()).thenReturn(inactiveUser);

        List<UserDto> result = userService.findInactiveUsers();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());

        UserDto user1 = DtoMapperUser.builder().setUser(DataUser.createUser003().orElseThrow()).build();
        UserDto user2 = DtoMapperUser.builder().setUser(DataUser.createUser004().orElseThrow()).build();

        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
        verify(userRepository).findByActiveFalse();
    }


    @Test
    void testSave() {
        List<Role> roles = List.of(new Role(1L, "ROLE_ADMIN"), new Role(2L, "ROLE_USER"));
        User newUser = new User(null, "Prueba", "Save", "prueba@correo.edu.co", "prueba", roles, "3147845141", List.of(), null, true, true, false);
        when(userRepository.save(any())).then(invocationOnMock -> {
            User u = invocationOnMock.getArgument(0);
            u.setId(5L);
            return u;
        });

        //when
        UserDto user = userService.save(newUser);

        //then
        assertEquals(5L, user.getId());
        assertEquals("Prueba", user.getName());
        assertEquals("Save", user.getLastName());
        assertEquals("prueba@correo.edu.co", user.getEmail());
        verify(userRepository).save(any());

    }

    @Test
    void testUpdate() {
        User existUser = DataUser.createUser001().orElseThrow();
        UserRequest updateUserRequest = new UserRequest("actualizando", existUser.getLastName(), existUser.getEmail(), existUser.getPhoneNumber(), existUser.isAdmin(), existUser.isGuard());
        User updatedUser = new User();
        updatedUser.setId(existUser.getId());
        updatedUser.setName(updateUserRequest.getName());
        updatedUser.setLastName(updateUserRequest.getLastName());
        updatedUser.setEmail(updateUserRequest.getEmail());
        updatedUser.setPhoneNumber(updateUserRequest.getPhoneNumber());
        updatedUser.setActive(true);
        updatedUser.setRoles(existUser.getRoles());

        when(userRepository.findById(existUser.getId())).thenReturn(Optional.of(existUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);


        Optional<UserDto> result = userService.update(updateUserRequest, existUser.getId());

        assertTrue(result.isPresent());
        assertEquals("actualizando", result.get().getName());
        assertEquals(existUser.getLastName(), result.get().getLastName());

        verify(userRepository).save(any(User.class));
    }


    @Test
    void testChangePassword() {
    }

    @Test
    void activateUser() {
    }

    @Test
    void deactivateUser() {
    }

    @Test
    void remove() {
    }
}