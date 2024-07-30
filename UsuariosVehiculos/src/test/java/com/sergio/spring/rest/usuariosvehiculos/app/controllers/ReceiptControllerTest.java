package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sergio.spring.rest.usuariosvehiculos.app.data.*;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IRateService;
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

import java.time.LocalDateTime;
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


        verify(receiptService, never()).getUnpaidReceiptsByUser(existingUser);
    }


    @Test
    @WithMockUser
    void getCountUnpaidReceipts() throws Exception {
        //given
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());
        long unPaidReceipts = receipts.size();
        when(receiptService.getTotalUnpaidReceipts()).thenReturn(unPaidReceipts);

        //when
        mvc.perform(get("/receipt/count-unpaid").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)));
    }

    @Test
    @WithMockUser
    void getCountPaidReceipts() throws Exception {
        //given
        List<Receipt> receipts = List.of(DataReceipt.createReceipt002().orElseThrow());
        long paidReceipts = receipts.size();
        when(receiptService.getTotalPaidReceipts()).thenReturn(paidReceipts);

        //when
        mvc.perform(get("/receipt/count-paid").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)));
    }

    @Test
    @WithMockUser
    void getTotalReceipts() throws Exception {
        //given
        List<Receipt> receipts = Arrays.asList(DataReceipt.createReceipt001().orElseThrow(), DataReceipt.createReceipt002().orElseThrow());
        long totalReceipts = receipts.size();
        when(receiptService.getTotalReceipts()).thenReturn(totalReceipts);

        //when
        mvc.perform(get("/receipt/count-total").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(2)));
    }

    @Test
    @WithMockUser
    void createReceiptByUser() throws Exception {
        LocalDateTime issueDate = LocalDateTime.of(2024, 7, 25, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 7, 25, 22, 00);
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        Receipt receipt = new Receipt(3L, user, DataVehicle.createVehicle001().orElseThrow(), DataRate.createRate001().orElseThrow(), issueDate, dueDate, false);
        ReceiptDto receiptDto = DtoMapperReceipt.builder().setReceipt(receipt).build();
        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        when(userService.findByIdUser(user.getId())).thenReturn(Optional.of(user));
        when(receiptService.saveReceipt(any(Receipt.class))).thenReturn(receiptDto);

        //when
        mvc.perform(post("/receipt/{userId}/create", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receipt))
                        .with(csrf()))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.user.name").value("Sergio"))
                .andExpect(jsonPath("$.vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$.rate.time").value("DIA MOTO"))
                .andExpect(jsonPath("$.rate.amount").value(3000));
        verify(receiptService).saveReceipt(any(Receipt.class));
    }

    @Test
    @WithMockUser
    void createReceiptValidationErrors() throws Exception {
        //given
        LocalDateTime issueDate = LocalDateTime.of(2024, 7, 25, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 7, 25, 22, 00);
        User user = DataUser.createUser001().orElseThrow();
        Receipt receipt = new Receipt(3L, user, null, DataRate.createRate001().orElseThrow(), issueDate, dueDate, false);
        //when
        mvc.perform(post("/receipt/{userId}/create", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receipt))
                        .with(csrf()))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicle").value("No hay un vehículo seleccionado"));

        verify(receiptService, never()).saveReceipt(any(Receipt.class));
    }

    @Test
    @WithMockUser
    void testCreateReceiptUserNotFound() throws Exception {
        //given
        LocalDateTime issueDate = LocalDateTime.of(2024, 7, 25, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 7, 25, 22, 00);
        User user = DataUser.createUser001().orElseThrow();
        Receipt receipt = new Receipt(3L, user, DataVehicle.createVehicle001().orElseThrow(), DataRate.createRate001().orElseThrow(), issueDate, dueDate, false);

        when(userService.findById(user.getId())).thenReturn(Optional.empty());

        //when
        mvc.perform(post("/receipt/{userId}/create", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receipt))
                        .with(csrf()))
                //then
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void testCreateReceiptUserIdMismatch() throws Exception {
        //given
        LocalDateTime issueDate = LocalDateTime.of(2024, 7, 25, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 7, 25, 22, 00);
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        Receipt receipt = new Receipt(3L, user, DataVehicle.createVehicle001().orElseThrow(), DataRate.createRate001().orElseThrow(), issueDate, dueDate, false);
        userDto.setId(user.getId() + 1);

        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        //when
        mvc.perform(post("/receipt/{userId}/create", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receipt))
                        .with(csrf()))
                //then
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Usuario no encontrado"));
        verify(receiptService, never()).saveReceipt(any(Receipt.class));
    }

    @Test
    @WithMockUser
    void updateReceipt() throws Exception {
        // given
        Receipt existingReceipt = DataReceipt.createReceipt001().orElseThrow();
        ReceiptRequest updateReceipt = new ReceiptRequest(existingReceipt.getIssueDate(), existingReceipt.getDueDate(), true, new Rate(6L, DataVehicleType.createVehicleType001().orElseThrow(), "MES MOTO", 30000));
        Receipt savedReceipt = new Receipt();
        savedReceipt.setId(existingReceipt.getId());
        savedReceipt.setUser(existingReceipt.getUser());
        savedReceipt.setVehicle(existingReceipt.getVehicle());
        savedReceipt.setRate(updateReceipt.getRate());
        savedReceipt.setIssueDate(updateReceipt.getIssueDate());
        savedReceipt.setDueDate(updateReceipt.getDueDate());
        savedReceipt.setPaymentStatus(updateReceipt.isPaymentStatus());
        ReceiptDto receiptDto = DtoMapperReceipt.builder().setReceipt(savedReceipt).build();

        System.out.println(updateReceipt.getRate().getTime());
        // when
        when(receiptService.updateReceipt(updateReceipt, existingReceipt.getId())).thenReturn(Optional.of(receiptDto));

        // then
        mvc.perform(put("/receipt/{receiptId}/update", existingReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReceipt))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(existingReceipt.getId().intValue())))
                .andExpect(jsonPath("$.rate.time", is("MES MOTO")))
                .andExpect(jsonPath("$.rate.amount", is(30000)))
                .andExpect(jsonPath("$.paymentStatus", is(true)))
                .andExpect(content().json(objectMapper.writeValueAsString(receiptDto)));

        verify(receiptService).updateReceipt(updateReceipt, existingReceipt.getId());
    }

    @Test
    void changePaymentStatus() {
    }

    @Test
    void deleteReceipt() {
    }
}