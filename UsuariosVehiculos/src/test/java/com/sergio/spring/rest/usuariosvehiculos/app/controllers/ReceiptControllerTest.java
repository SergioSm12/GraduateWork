package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IReceiptService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@WebMvcTest(ReceiptController.class)
class ReceiptControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    IReceiptService receiptService;

    @MockBean
    IUserService userService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @WithMockUser
    void listReceipts() throws Exception {
        //given
        List<Receipt> receipts = Arrays.asList(DataReceipt.createReceipt001().orElseThrow(), DataReceipt.createReceipt002().orElseThrow());
        List<ReceiptDto> receiptDtos = receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build()).collect(Collectors.toList());
        when(receiptService.receiptList()).thenReturn(receiptDtos);

        //when
        mvc.perform(get("/receipt").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].user.name").value("Sergio"))
                .andExpect(jsonPath("$[0].vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].rate.time").value("DIA MOTO"))
                .andExpect(jsonPath("$[0].rate.amount").value(3000))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].user.name").value("David"))
                .andExpect(jsonPath("$[1].vehicle.plate").value("QAS874"))
                .andExpect(jsonPath("$[1].rate.time").value("DIA CARRO"))
                .andExpect(jsonPath("$[1].rate.amount").value(5000))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(receiptDtos)));

        verify(receiptService).receiptList();

    }

    @Test
    @WithMockUser
    void listUnpaidReceipt() throws Exception {
        //given
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());
        List<ReceiptDto> receiptDtos = receipts.stream().map(ru -> DtoMapperReceipt.builder().setReceipt(ru).build()).collect(Collectors.toList());
        when(receiptService.getUnpaidReceipts()).thenReturn(receiptDtos);

        //when
        mvc.perform(get("/receipt/unpaid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].user.name").value("Sergio"))
                .andExpect(jsonPath("$[0].vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].rate.time").value("DIA MOTO"))
                .andExpect(jsonPath("$[0].rate.amount").value(3000))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(receiptDtos)));

        verify(receiptService).getUnpaidReceipts();
    }

    @Test
    @WithMockUser
    void lisPaidReceipt() throws Exception {
        //given
        List<Receipt> receipts = List.of(DataReceipt.createReceipt002().orElseThrow());
        List<ReceiptDto> receiptDtos = receipts.stream().map(ru -> DtoMapperReceipt.builder().setReceipt(ru).build()).collect(Collectors.toList());
        when(receiptService.getPaidReceipts()).thenReturn(receiptDtos);

        //when
        mvc.perform(get("/receipt/paid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].user.name").value("David"))
                .andExpect(jsonPath("$[0].vehicle.plate").value("QAS874"))
                .andExpect(jsonPath("$[0].rate.time").value("DIA CARRO"))
                .andExpect(jsonPath("$[0].rate.amount").value(5000))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(receiptDtos)));

        verify(receiptService).getPaidReceipts();
    }

    @Test
    @WithMockUser
    void getReceiptsByUserId() throws Exception {
        //given
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());
        List<ReceiptDto> receiptDtos = receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build()).collect(Collectors.toList());
        User existingUser = DataUser.createUser001().orElseThrow();
        when(receiptService.getReceiptsByUserId(existingUser.getId())).thenReturn(receiptDtos);

        //when
        mvc.perform(get("/receipt/user/{userId}", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].user.name").value("Sergio"))
                .andExpect(jsonPath("$[0].vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].rate.time").value("DIA MOTO"))
                .andExpect(jsonPath("$[0].rate.amount").value(3000))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(receiptDtos)));

        verify(receiptService).getReceiptsByUserId(anyLong());
    }

    @Test
    @WithMockUser
    void getUnpaidReceiptsForUser() throws Exception {
        //given
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());
        List<ReceiptDto> receiptDtos = receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build()).collect(Collectors.toList());
        User existingUser = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(existingUser).build();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.of(userDto));
        when(userService.findByIdUser(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(receiptService.getUnpaidReceiptsByUser(existingUser)).thenReturn(receiptDtos);


        //when
        mvc.perform(get("/receipt/user/{userId}/unpaid", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].user.name").value("Sergio"))
                .andExpect(jsonPath("$[0].vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].rate.time").value("DIA MOTO"))
                .andExpect(jsonPath("$[0].rate.amount").value(3000))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(content().json(objectMapper.writeValueAsString(receiptDtos)));

        verify(receiptService).getUnpaidReceiptsByUser(existingUser);
    }

    @Test
    @WithMockUser
    void testGetUnpaidReceiptsForUserNotFound() throws Exception {
        //given
        User existingUser = DataUser.createUser001().orElseThrow();
        when(userService.findById(existingUser.getId())).thenReturn(Optional.empty());

        //when
        mvc.perform(get("/receipt/user/{userId}/unpaid", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound());

        verify(receiptService, never()).getUnpaidReceiptsByUser(existingUser);
    }

    @Test
    @WithMockUser
    void getUnpaidReceiptsForUserMismatch() throws Exception {
        //given
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());
        List<ReceiptDto> receiptDtos = receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build()).collect(Collectors.toList());
        User existingUser = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(existingUser).build();
        userDto.setId(existingUser.getId() + 1);
        when(userService.findById(existingUser.getId())).thenReturn(Optional.of(userDto));
        when(userService.findByIdUser(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(receiptService.getUnpaidReceiptsByUser(existingUser)).thenReturn(receiptDtos);


        //when
        mvc.perform(get("/receipt/user/{userId}/unpaid", existingUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("No tienes permiso para acceder a los permisos de este usuario"));


        verify(receiptService,never()).getUnpaidReceiptsByUser(existingUser);
    }


    @Test
    void getCountUnpaidReceipts() {
    }

    @Test
    void getCountPaidReceipts() {
    }

    @Test
    void getTotalReceipts() {
    }

    @Test
    void createReceiptByUser() {
    }

    @Test
    void updateReceipt() {
    }

    @Test
    void changePaymentStatus() {
    }

    @Test
    void deleteReceipt() {
    }
}