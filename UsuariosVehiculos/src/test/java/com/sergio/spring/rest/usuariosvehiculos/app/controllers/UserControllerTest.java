package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Role;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserChangePasswordRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    IUserService userService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser
    void index() throws Exception {
        //given
        List<User> users = Arrays.asList(DataUser.createUser001().orElseThrow(), DataUser.createUser002().orElseThrow()
                , DataUser.createUser003().orElseThrow()
                , DataUser.createUser004().orElseThrow());
        List<UserDto> userDtos = users.stream().map(user -> DtoMapperUser.builder().setUser(user).build()).collect(Collectors.toList());
        when(userService.findAll()).thenReturn(userDtos);

        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON).with(csrf()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sergio"))
                .andExpect(jsonPath("$[0].email").value("sergio.mesa@usantoto.edu.co"))
                .andExpect(jsonPath("$[3].id").value(3))
                .andExpect(jsonPath("$[3].name").value("Pepa"))
                .andExpect(jsonPath("$[3].email").value("pepa@correo.edu.co"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(content().json(objectMapper.writeValueAsString(userDtos)));

        verify(userService).findAll();
    }

    @Test
    @WithMockUser
    void getTotalUsersCount() throws Exception {
        //given
        List<User> users = Arrays.asList(DataUser.createUser001().orElseThrow(), DataUser.createUser002().orElseThrow()
                , DataUser.createUser003().orElseThrow()
                , DataUser.createUser004().orElseThrow());
        long totalUsers = users.size();
        when(userService.getTotalCountUsers()).thenReturn(totalUsers);

        //when
        mvc.perform(get("/users/count-total").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(4)));

    }

    @Test
    @WithMockUser
    void list() throws Exception {
        // Given
        List<User> users = Arrays.asList(DataUser.createUser001().orElseThrow(), DataUser.createUser002().orElseThrow(),
                DataUser.createUser003().orElseThrow(), DataUser.createUser004().orElseThrow());
        List<UserDto> userDtos = users.stream().map(user -> DtoMapperUser.builder().setUser(user).build()).collect(Collectors.toList());
        Page<UserDto> page = new PageImpl<>(userDtos, PageRequest.of(0, 10), userDtos.size());
        when(userService.findAll(PageRequest.of(0, 10))).thenReturn(page);

        // When
        mvc.perform(get("/users/page/0").contentType(MediaType.APPLICATION_JSON).with(csrf()))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Sergio"))
                .andExpect(jsonPath("$.content[0].email").value("sergio.mesa@usantoto.edu.co"))
                .andExpect(jsonPath("$.content[3].id").value(3))
                .andExpect(jsonPath("$.content[3].name").value("Pepa"))
                .andExpect(jsonPath("$.content[3].email").value("pepa@correo.edu.co"))
                .andExpect(jsonPath("$.content", hasSize(4)));

        verify(userService).findAll(PageRequest.of(0, 10));
    }

    @Test
    @WithMockUser
    void listActiveUsers() throws Exception {
        //given
        List<User> activeUsers = Arrays.asList(DataUser.createUser001().orElseThrow(), DataUser.createUser002().orElseThrow());
        List<UserDto> activeUsersDto = activeUsers.stream().map(user -> DtoMapperUser.builder().setUser(user).build()).collect(Collectors.toList());
        when(userService.findActiveUsers()).thenReturn(activeUsersDto);

        //when
        mvc.perform(get("/users/active-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Sergio"))
                .andExpect(jsonPath("$[0].email").value("sergio.mesa@usantoto.edu.co"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("David"))
                .andExpect(jsonPath("$[1].email").value("david@correo.edu.co"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(activeUsersDto)));
        verify(userService).findActiveUsers();
    }

    @Test
    @WithMockUser
    void listDeactivateUsers() throws Exception {
        //given
        List<User> activeUsers = Arrays.asList(DataUser.createUser003().orElseThrow(), DataUser.createUser004().orElseThrow());
        List<UserDto> deactiveUsersDto = activeUsers.stream().map(user -> DtoMapperUser.builder().setUser(user).build()).collect(Collectors.toList());
        when(userService.findInactiveUsers()).thenReturn(deactiveUsersDto);

        //when
        mvc.perform(get("/users/inactive-users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Pedro"))
                .andExpect(jsonPath("$[0].email").value("pedro@correo.edu.co"))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("Pepa"))
                .andExpect(jsonPath("$[1].email").value("pepa@correo.edu.co"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(deactiveUsersDto)));
        verify(userService).findInactiveUsers();
    }

    @Test
    @WithMockUser
    void show() throws Exception {
        //given
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));

        //when
        mvc.perform(get("/users/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sergio"))
                .andExpect(jsonPath("$.email").value("sergio.mesa@usantoto.edu.co"))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService).findById(1L);
    }

    @Test
    @WithMockUser
    void showUserNotFound() throws Exception {
        //given
        User user = DataUser.createUser001().orElseThrow();
        when(userService.findById(user.getId())).thenReturn(Optional.empty());

        //when
        mvc.perform(get("/users/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isNotFound());

        verify(userService).findById(1L);
    }

    @Test
    @WithMockUser
    void getUserByEmail() throws Exception {
        // Given
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        String email = "sergio.mesa@usantoto.edu.co";
        when(userService.findByEmail(email)).thenReturn(Optional.of(userDto));

        // When
        mvc.perform(get("/users/email").param("email", email).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Sergio"))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));

        verify(userService).findByEmail(email);
    }

    @Test
    @WithMockUser
    void getUserByEmailNotFound() throws Exception {
        // Given
        String email = "notfound@correo.edu.co";
        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        // When
        mvc.perform(get("/users/email").param("email", email).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                // Then
                .andExpect(status().isNotFound());

        verify(userService).findByEmail(email);
    }

    @Test
    @WithMockUser
    void create() throws Exception {
        // Given
        User user = new User(5L, "Alejandra", "Pinilla", "alejandra@gmail.com", "alejandra",
                List.of(new Role(2L, "ROLE_USER")), "320147841", null, null, true, false, false);
        List<Vehicle> vehicles = List.of(DataVehicle.createVehicle001().orElseThrow());
        user.setVehicles(vehicles);
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        when(userService.save(any(User.class))).thenReturn(userDto);

        // When
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(csrf()))
                // Then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.name", is("Alejandra")))
                .andExpect(jsonPath("$.lastName", is("Pinilla")))
                .andExpect(jsonPath("$.email", is("alejandra@gmail.com")));

        verify(userService).save(any(User.class));
    }

    @Test
    @WithMockUser
    void createValidationError() throws Exception {
        // Given
        User user = new User();
        String errorMessage = "EL campo nombre no puede estar vacío.";

        // When
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(csrf()))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(errorMessage));

        verify(userService, never()).save(any(User.class));
    }

    @Test
    @WithMockUser
    void createDataIntegrityViolation() throws Exception {
        // Given
        User user = DataUser.createUser001().orElseThrow();
        when(userService.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // When
        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(csrf()))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("Ya hay una cuenta asociada con este correo"));

        verify(userService).save(any(User.class));
    }


    @Test
    @WithMockUser
    void update() throws Exception {
        //given
        User existingUser = DataUser.createUser001().orElseThrow();
        UserRequest userRequest = new UserRequest("Juan", "Gonzales", existingUser.getEmail(), existingUser.getPhoneNumber(), true, false);
        User savedUser = new User();
        savedUser.setId(existingUser.getId());
        savedUser.setName(userRequest.getName());
        savedUser.setLastName(userRequest.getLastName());
        savedUser.setEmail(userRequest.getEmail());
        savedUser.setPassword(existingUser.getPassword());
        savedUser.setRoles(existingUser.getRoles());
        savedUser.setPhoneNumber(existingUser.getPhoneNumber());
        savedUser.setVehicles(existingUser.getVehicles());
        savedUser.setReceipts(existingUser.getReceipts());
        savedUser.setActive(existingUser.isActive());
        savedUser.setAdmin(userRequest.isAdmin());
        savedUser.setGuard(userRequest.isGuard());
        UserDto userDto = DtoMapperUser.builder().setUser(savedUser).build();
        when(userService.update(userRequest, existingUser.getId())).thenReturn(Optional.of(userDto));

        //when
        mvc.perform(put("/users/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .with(csrf()))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Juan")))
                .andExpect(jsonPath("$.lastName", is("Gonzales")))
                .andExpect(jsonPath("$.email", is(existingUser.getEmail())))
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
        verify(userService).update(userRequest, existingUser.getId());

    }

    @Test
    @WithMockUser
    void updateValidationErrors() throws Exception {
        //given
        User existingUser = DataUser.createUser001().orElseThrow();
        UserRequest userRequest = new UserRequest(null, "Gonzales", existingUser.getEmail(), existingUser.getPhoneNumber(), true, false);
        //when
        mvc.perform(put("/users/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .with(csrf()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("El campo nombre no puede estar vacio")));
        verify(userService, never()).update(any(UserRequest.class), anyLong());

    }

    @Test
    @WithMockUser
    void testUpdateUserNotFound() throws Exception {
        //given
        User existingUser = DataUser.createUser001().orElseThrow();
        UserRequest userRequest = new UserRequest("Juan", "Gonzales", existingUser.getEmail(), existingUser.getPhoneNumber(), true, false);
        when(userService.update(userRequest, existingUser.getId())).thenReturn(Optional.empty());
        //when
        mvc.perform(put("/users/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .with(csrf()))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void changePassword() throws Exception {
        // Given
        UserChangePasswordRequest changePasswordRequest = new UserChangePasswordRequest("newpassword");
        User user = DataUser.createUser001().orElseThrow();

        when(userService.findByIdUser(1L)).thenReturn(Optional.of(user));
        doNothing().when(userService).changePassword(any(UserChangePasswordRequest.class), eq(1L));

        // When
        mvc.perform(patch("/users/changePassword/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
                        .with(csrf()))
                // Then
                .andExpect(status().isOk());

        verify(userService).findByIdUser(1L);
        verify(userService).changePassword(any(UserChangePasswordRequest.class), eq(1L));
    }

    @Test
    @WithMockUser
    void changePasswordValidationErrors() throws Exception {
        // Given
        UserChangePasswordRequest changePasswordRequest = new UserChangePasswordRequest(null);
        User user = DataUser.createUser001().orElseThrow();

        when(userService.findByIdUser(1L)).thenReturn(Optional.of(user));
        doNothing().when(userService).changePassword(any(UserChangePasswordRequest.class), eq(1L));

        // When
        mvc.perform(patch("/users/changePassword/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
                        .with(csrf()))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password", is("El campo contraseña no puede estar vacío.")));

        verify(userService, never()).findByIdUser(1L);
        verify(userService, never()).changePassword(any(UserChangePasswordRequest.class), eq(1L));
    }

    @Test
    @WithMockUser
    void testChangePasswordUserNotFound() throws Exception {
        // Given
        UserChangePasswordRequest changePasswordRequest = new UserChangePasswordRequest("newpassword");
        when(userService.findByIdUser(1L)).thenReturn(Optional.empty());

        // When
        mvc.perform(patch("/users/changePassword/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest))
                        .with(csrf()))
                // Then
                .andExpect(status().isNotFound());

        verify(userService).findByIdUser(1L);
        verify(userService, never()).changePassword(any(UserChangePasswordRequest.class), eq(1L));
    }

    @Test
    @WithMockUser
    void activateUser() throws Exception {
        User existingUser = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(existingUser).build();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.of(userDto));
        doNothing().when(userService).activateUser(existingUser.getId());

        mvc.perform(put("/users/activate/{id}", existingUser.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk());
        verify(userService).findById(existingUser.getId());
        verify(userService).activateUser(existingUser.getId());
    }

    @Test
    @WithMockUser
    void activateUserNotFound() throws Exception {
        User existingUser = DataUser.createUser001().orElseThrow();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.empty());

        mvc.perform(put("/users/activate/{id}", existingUser.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isNotFound());

        verify(userService).findById(existingUser.getId());
        verify(userService, never()).activateUser(existingUser.getId());
    }

    @Test
    @WithMockUser
    void deactivateUser() throws Exception {
        User existingUser = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(existingUser).build();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.of(userDto));
        doNothing().when(userService).deactivateUser(existingUser.getId());

        mvc.perform(put("/users/deactivate/{id}", existingUser.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk());
        verify(userService).findById(existingUser.getId());
        verify(userService).deactivateUser(existingUser.getId());
    }

    @Test
    @WithMockUser
    void deactivateUserNotFound() throws Exception {
        User existingUser = DataUser.createUser001().orElseThrow();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.empty());

        mvc.perform(put("/users/deactivate/{id}", existingUser.getId()).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isNotFound());
        verify(userService).findById(existingUser.getId());
        verify(userService, never()).deactivateUser(existingUser.getId());
    }

    @Test
    @WithMockUser
    void remove() throws Exception {
        //given
        User existingUser = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(existingUser).build();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.of(userDto));
        doNothing().when(userService).remove(existingUser.getId());

        //when
        mvc.perform(delete("/users/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isNoContent());
        verify(userService).findById(existingUser.getId());
        verify(userService).remove(existingUser.getId());
    }

    @Test
    @WithMockUser
    void removeNotFound() throws Exception {
        //given
        User existingUser = DataUser.createUser001().orElseThrow();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.empty());
        //when
        mvc.perform(delete("/users/{id}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isNotFound());
        verify(userService).findById(existingUser.getId());
        verify(userService, never()).remove(existingUser.getId());
    }
}