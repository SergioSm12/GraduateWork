package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperVehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IVehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    IVehicleService vehicleService;

    @MockBean
    IUserService userService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser
    void listVehicles() throws Exception {
        //given
        List<Vehicle> vehicles = Arrays.asList(DataVehicle.createVehicle001().orElseThrow(), DataVehicle.createVehicle002().orElseThrow()
                , DataVehicle.createVehicle003().orElseThrow(), DataVehicle.createVehicle004().orElseThrow());
        List<VehicleDto> vehicleDtos = new ArrayList<>();
        for (Vehicle v : vehicles) {
            VehicleDto build = DtoMapperVehicleDto.builder().setVehicle(v).build();
            vehicleDtos.add(build);
        }
        when(vehicleService.findVehicles()).thenReturn(vehicleDtos);

        //when
        mvc.perform(get("/vehicle/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].vehicleType.name").value("MOTO"))
                .andExpect(jsonPath("$[3].id").value(4))
                .andExpect(jsonPath("$[3].plate").value("BGT420"))
                .andExpect(jsonPath("$[3].vehicleType.name").value("CARRO"))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(content().json(objectMapper.writeValueAsString(vehicleDtos)));
        verify(vehicleService).findVehicles();

    }

    @Test
    @WithMockUser
    void getTotalRegisteredReceipts() throws Exception {
        //given
        List<Vehicle> vehicles = Arrays.asList(DataVehicle.createVehicle001().orElseThrow(), DataVehicle.createVehicle002().orElseThrow()
                , DataVehicle.createVehicle003().orElseThrow(), DataVehicle.createVehicle004().orElseThrow());
        long totalVehicles = vehicles.size();
        when(vehicleService.getTotalRegisteredReceipt()).thenReturn(totalVehicles);
        //when
        mvc.perform(get("/vehicle/count-total").contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(String.valueOf(4)));
        verify(vehicleService).getTotalRegisteredReceipt();

    }

    @Test
    @WithMockUser
    void listVehiclesByUser() throws Exception {
        //given
        List<Vehicle> vehicles = Arrays.asList(
                DataVehicle.createVehicle001().orElseThrow()
                , DataVehicle.createVehicle004().orElseThrow());
        List<VehicleDto> vehicleDtos = vehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        when(userService.findById(1L)).thenReturn(Optional.of(userDto));
        when(vehicleService.findVehiclesByUserId(user.getId())).thenReturn(vehicleDtos);

        //when
        mvc.perform(get("/vehicle/{userId}/list", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].vehicleType.name").value("MOTO"))
                .andExpect(jsonPath("$[1].id").value(4))
                .andExpect(jsonPath("$[1].plate").value("BGT420"))
                .andExpect(jsonPath("$[1].vehicleType.name").value("CARRO"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(vehicleDtos)));

        verify(vehicleService).findVehiclesByUserId(1L);
    }

    @Test
    @WithMockUser
    void listVehiclesByUserNotFound() throws Exception {
        //given
        List<Vehicle> vehicles = Arrays.asList(
                DataVehicle.createVehicle001().orElseThrow()
                , DataVehicle.createVehicle004().orElseThrow());
        List<VehicleDto> vehicleDtos = vehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());
        when(userService.findById(1L)).thenReturn(Optional.empty());
        when(vehicleService.findVehiclesByUserId(anyLong())).thenReturn(vehicleDtos);
        //when
        mvc.perform(get("/vehicle/1/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                //then
                .andExpect(status().isNotFound());
        verify(userService).findById(1L);
    }

    @Test
    @WithMockUser
    void listActiveVehiclesByUser() throws Exception {
        //given
        List<Vehicle> vehicles = List.of(DataVehicle.createVehicle001().orElseThrow());
        List<VehicleDto> vehicleDtos = vehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());
        when(vehicleService.findActiveVehiclesByUserId(1L)).thenReturn(vehicleDtos);
        //when
        mvc.perform(get("/vehicle/1/active-vehicles").contentType(MediaType.APPLICATION_JSON).with(csrf()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].vehicleType.name").value("MOTO"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(content().json(objectMapper.writeValueAsString(vehicleDtos)));

        verify(vehicleService).findActiveVehiclesByUserId(1L);

    }

    @Test
    @WithMockUser
    void listInactiveVehiclesByUser() throws Exception {

        //given
        List<Vehicle> vehicles = List.of(DataVehicle.createVehicle004().orElseThrow());
        List<VehicleDto> vehicleDtos = vehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());
        when(vehicleService.findInactiveVehiclesByUserId(1L)).thenReturn(vehicleDtos);
        //when
        mvc.perform(get("/vehicle/1/inactive-vehicles").contentType(MediaType.APPLICATION_JSON).with(csrf()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(4))
                .andExpect(jsonPath("$[0].plate").value("BGT420"))
                .andExpect(jsonPath("$[0].vehicleType.name").value("CARRO"))
                .andExpect(jsonPath("$[0].active").value(false))
                .andExpect(content().json(objectMapper.writeValueAsString(vehicleDtos)));

        verify(vehicleService).findInactiveVehiclesByUserId(1L);
    }

    @Test
    @WithMockUser
    void createVehicleByUser() throws Exception {
        //given
        Vehicle vehicle = new Vehicle(null, "XIM311", DataUser.createUser001().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow(), true);
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        when(userService.findByIdUser(user.getId())).thenReturn(Optional.of(user));
        when(vehicleService.saveVehicle(any(Vehicle.class))).then(invocationOnMock -> {
            Vehicle v = invocationOnMock.getArgument(0);
            v.setId(5L);
            VehicleDto vehicleDto = DtoMapperVehicleDto.builder().setVehicle(v).build();
            return vehicleDto;
        });

        //when
        mvc.perform(post("/vehicle/{userId}/create", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicle))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.plate", is("XIM311")))
                .andExpect(jsonPath("$.user.name", is("Sergio")))
                .andExpect(jsonPath("$.vehicleType.name", is("CARRO")))
                .andExpect(jsonPath("$.active", is(true)));
        verify(vehicleService).saveVehicle(any(Vehicle.class));
    }

    @Test
    @WithMockUser
    void testCreateVehicleByUserValidationErrors() throws Exception {
        //given
        Vehicle vehicle = new Vehicle(null, null, DataUser.createUser001().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow(), true);
        User user = DataUser.createUser001().orElseThrow();
        //when
        mvc.perform(post("/vehicle/{userId}/create", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicle))
                        .with(csrf()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.plate").value("El campo placa no puede estar vacio"));

        verify(vehicleService, never()).saveVehicle(any(Vehicle.class));
    }

    @Test
    @WithMockUser
    void testCreateVehicleByUserNotFound() throws Exception {
        Vehicle vehicle = new Vehicle(null, "XIM311", DataUser.createUser001().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow(), true);
        User user = DataUser.createUser001().orElseThrow();
        when(userService.findById(user.getId())).thenReturn(Optional.empty());
        //when
        mvc.perform(post("/vehicle/{userId}/create", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicle))
                        .with(csrf()))
                //then
                .andExpect(status().isNotFound());
        verify(userService).findById(anyLong());
        verify(userService, never()).findByIdUser(anyLong());
        verify(vehicleService, never()).saveVehicle(any(Vehicle.class));
    }


    @Test
    @WithMockUser
    void updateVehicleByUser() throws Exception {
        Vehicle existingVehicle = DataVehicle.createVehicle001().orElseThrow();
        Vehicle updateVehicle = new Vehicle(null, "UPT845", existingVehicle.getUser(), DataVehicleType.createVehicleType002().orElseThrow(), true);
        Vehicle savedVehicle = new Vehicle();
        savedVehicle.setId(existingVehicle.getId());
        savedVehicle.setPlate(updateVehicle.getPlate());
        savedVehicle.setUser(existingVehicle.getUser());
        savedVehicle.setVehicleType(updateVehicle.getVehicleType());
        savedVehicle.setActive(existingVehicle.isActive());
        VehicleDto vehicleDto = DtoMapperVehicleDto.builder().setVehicle(savedVehicle).build();

        when(vehicleService.updateVehicle(eq(1L), eq(1L), any())).thenReturn(Optional.of(vehicleDto));

        mvc.perform(put("/vehicle/1/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVehicle))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.plate", is("UPT845")))
                .andExpect(jsonPath("$.user.name", is("Sergio")))
                .andExpect(jsonPath("$.vehicleType.name", is("CARRO")))
                .andExpect(jsonPath("$.active", is(true)));

        verify(vehicleService).updateVehicle(eq(1L), eq(1L), any(Vehicle.class));
    }

    @Test
    @WithMockUser
    void testUpdateVehicleByUserValidationErrors() throws Exception {
        Vehicle existingVehicle = DataVehicle.createVehicle001().orElseThrow();
        Vehicle updateVehicle = new Vehicle(null, null, existingVehicle.getUser(), DataVehicleType.createVehicleType002().orElseThrow(), true);


        mvc.perform(put("/vehicle/1/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVehicle))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.plate").value("El campo placa no puede estar vacio"));

        verify(vehicleService, never()).updateVehicle(eq(1L), eq(1L), any());

    }

    @Test
    @WithMockUser
    void testUpdateVehicleByUserNotFound() throws Exception {
        when(vehicleService.updateVehicle(anyLong(), anyLong(), any())).thenReturn(Optional.empty());
        Vehicle updateVehicle = new Vehicle(null, "UPT845", DataUser.createUser001().orElseThrow(), DataVehicleType.createVehicleType002().orElseThrow(), true);
        mvc.perform(put("/vehicle/1/update/1").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVehicle))
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(vehicleService).updateVehicle(anyLong(), anyLong(), any());
    }

    @Test
    @WithMockUser
    void deleteVehicleByUser() throws Exception {
        //given
        Vehicle existingVehicle = DataVehicle.createVehicle001().orElseThrow();
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        when(vehicleService.findVehicleByIdAndUserId(existingVehicle.getId(), user.getId())).thenReturn(Optional.of(existingVehicle));
        doNothing().when(vehicleService).removeVehicleByUser(existingVehicle.getId());

        //when
        mvc.perform(delete("/vehicle/1/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(vehicleService).removeVehicleByUser(existingVehicle.getId());
    }

    @Test
    @WithMockUser
    void testDeleteVehicleByUserUserNotFound() throws Exception {
        User user = DataUser.createUser001().orElseThrow();
        when(userService.findById(user.getId())).thenReturn(Optional.empty());
        //when
        mvc.perform(delete("/vehicle/1/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(userService).findById(user.getId());
        verify(vehicleService, never()).findVehicleByIdAndUserId(1L, user.getId());
        verify(vehicleService, never()).removeVehicleByUser(1L);
    }

    @Test
    @WithMockUser
    void testDeleteVehicleByUserVehicleNotFound() throws Exception {
        Vehicle existingVehicle = DataVehicle.createVehicle001().orElseThrow();
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        when(vehicleService.findVehicleByIdAndUserId(existingVehicle.getId(), user.getId())).thenReturn(Optional.empty());
        mvc.perform(delete("/vehicle/1/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
        verify(userService).findById(user.getId());
        verify(vehicleService).findVehicleByIdAndUserId(existingVehicle.getId(), user.getId());
        verify(vehicleService, never()).removeVehicleByUser(1L);
    }

    @Test
    @WithMockUser
    void activateVehicleByUser() throws Exception {
        Vehicle existingVehicle = DataVehicle.createVehicle001().orElseThrow();
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();

        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        when(vehicleService.findVehicleByIdAndUserId(existingVehicle.getId(), user.getId())).thenReturn(Optional.of(existingVehicle));
        doNothing().when(vehicleService).activateVehicleByUser(existingVehicle.getId());

        mvc.perform(get("/vehicle/1/activate-vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(userService).findById(user.getId());
        verify(vehicleService).findVehicleByIdAndUserId(existingVehicle.getId(), user.getId());
        verify(vehicleService).activateVehicleByUser(existingVehicle.getId());
    }

    @Test
    @WithMockUser
    void testActivateVehicleByUserNotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(Optional.empty());
        mvc.perform(get("/vehicle/1/activate-vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
        verify(userService).findById(1L);
        verify(vehicleService, never()).findVehicleByIdAndUserId(1L, 1L);
        verify(vehicleService, never()).activateVehicleByUser(1L);
    }

    @Test
    @WithMockUser
    void testActivateVehicleByUserVEhicleNotFound() throws Exception {
        Vehicle existingVehicle = DataVehicle.createVehicle001().orElseThrow();
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();

        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        when(vehicleService.findVehicleByIdAndUserId(existingVehicle.getId(), user.getId())).thenReturn(Optional.empty());

        mvc.perform(get("/vehicle/1/activate-vehicle/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(userService).findById(user.getId());
        verify(vehicleService).findVehicleByIdAndUserId(existingVehicle.getId(), user.getId());
        verify(vehicleService, never()).activateVehicleByUser(existingVehicle.getId());
    }
}