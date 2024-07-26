package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataNightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataRate;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.NightlyReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperNightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.service.INightlyReceiptService;
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
import java.util.ArrayList;
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

@WebMvcTest(NightlyReceiptController.class)
class NightlyReceiptControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private INightlyReceiptService nightlyReceiptService;

    @MockBean
    IUserService userService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); //configuracion de LocalDate
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);//Desabilitar formatos de fecha para comparar
    }

    @Test
    @WithMockUser
    void listNightlyReceipts() throws Exception {
        //given
        List<NightlyReceipt> nightlyReceipts = Arrays.asList(DataNightlyReceipt.createNightlyReceipt001().orElseThrow(), DataNightlyReceipt.createNightlyReceipt002().orElseThrow());
        List<NightlyReceiptDto> nightlyReceiptDtos = nightlyReceipts.stream().map(nr -> DtoMapperNightlyReceipt.builder().setNightlyReceipt(nr).build()).collect(Collectors.toList());
        when(nightlyReceiptService.nightlyReceiptList()).thenReturn(nightlyReceiptDtos);

        //when
        mvc.perform(get("/nightly-receipt").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].user.name").value("Sergio"))
                .andExpect(jsonPath("$[0].vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].rate.time").value("DIA MOTO"))
                .andExpect(jsonPath("$[0].amount").value(18000))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].user.name").value("Sergio"))
                .andExpect(jsonPath("$[1].vehicle.plate").value("BGT420"))
                .andExpect(jsonPath("$[1].rate.time").value("DIA CARRO"))
                .andExpect(jsonPath("$[1].amount").value(54000))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(nightlyReceiptDtos)));

        verify(nightlyReceiptService).nightlyReceiptList();

    }

    @Test
    @WithMockUser
    void getCountUnpaidNightlyReceipts() throws Exception {
        //given
        List<NightlyReceipt> nightlyReceipts = List.of(DataNightlyReceipt.createNightlyReceipt001().orElseThrow());
        long unPaidNightlyReceipt = nightlyReceipts.size();
        when(nightlyReceiptService.getTotalUnpaidNightlyReceipts()).thenReturn(unPaidNightlyReceipt);

        //when
        mvc.perform(get("/nightly-receipt/count-unpaid").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)));
    }

    @Test
    @WithMockUser
    void getCountPaidNightlyReceipts() throws Exception {
        //given
        List<NightlyReceipt> nightlyReceipts = List.of(DataNightlyReceipt.createNightlyReceipt002().orElseThrow());
        long paidNightlyReceipt = nightlyReceipts.size();
        when(nightlyReceiptService.getTotalPaidNightlyReceipts()).thenReturn(paidNightlyReceipt);

        //when
        mvc.perform(get("/nightly-receipt/count-paid").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)));
    }

    @Test
    @WithMockUser
    void getTotalNightlyReceipts() throws Exception {
        //given
        List<NightlyReceipt> nightlyReceipts = List.of(DataNightlyReceipt.createNightlyReceipt001().orElseThrow(), DataNightlyReceipt.createNightlyReceipt002().orElseThrow());
        long totalNightlyReceipts = nightlyReceipts.size();
        when(nightlyReceiptService.getTotalNightlyReceipts()).thenReturn(totalNightlyReceipts);

        //when
        mvc.perform(get("/nightly-receipt/count-total").contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(2)));
    }

    @Test
    @WithMockUser
    void getNightlyReceiptsByUserId() throws Exception {
        //given
        List<NightlyReceipt> nightlyReceipts = Arrays.asList(DataNightlyReceipt.createNightlyReceipt001().orElseThrow(), DataNightlyReceipt.createNightlyReceipt002().orElseThrow());
        List<NightlyReceiptDto> nightlyReceiptDtos = new ArrayList<>();
        for (NightlyReceipt nr : nightlyReceipts) {
            NightlyReceiptDto build = DtoMapperNightlyReceipt.builder().setNightlyReceipt(nr).build();
            nightlyReceiptDtos.add(build);
        }
        User existUser = DataUser.createUser001().orElseThrow();
        when(nightlyReceiptService.getNightlyReceiptsByUserId(existUser.getId())).thenReturn(nightlyReceiptDtos);

        //when
        mvc.perform(get("/nightly-receipt/{userId}", existUser.getId()).contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].user.name").value("Sergio"))
                .andExpect(jsonPath("$[0].vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].rate.time").value("DIA MOTO"))
                .andExpect(jsonPath("$[0].amount").value(18000))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].user.name").value("Sergio"))
                .andExpect(jsonPath("$[1].vehicle.plate").value("BGT420"))
                .andExpect(jsonPath("$[1].rate.time").value("DIA CARRO"))
                .andExpect(jsonPath("$[1].amount").value(54000))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(nightlyReceiptDtos)));

        verify(nightlyReceiptService).getNightlyReceiptsByUserId(anyLong());
    }

    @Test
    @WithMockUser
    void createNightlyReceipt() throws Exception {

        //given
        LocalDateTime initialTime = LocalDateTime.of(2024, 7, 25, 22, 00);
        LocalDateTime departureTime = LocalDateTime.of(2024, 7, 25, 6, 00);
        User user = DataUser.createUser001().orElseThrow();
        UserDto userDto = DtoMapperUser.builder().setUser(user).build();
        NightlyReceipt nightlyReceipt = new NightlyReceipt(3L, DataUser.createUser001().orElseThrow(), DataVehicle.createVehicle001().orElseThrow(), DataRate.createRate003().orElseThrow(), initialTime, departureTime, false, 27000);
        NightlyReceiptDto nightlyReceiptDto = DtoMapperNightlyReceipt.builder().setNightlyReceipt(nightlyReceipt).build();
        when(userService.findById(user.getId())).thenReturn(Optional.of(userDto));
        when(userService.findByIdUser(user.getId())).thenReturn(Optional.of(user));
        when(nightlyReceiptService.saveReceipt(any(NightlyReceipt.class))).thenReturn(nightlyReceiptDto);

        //when
        mvc.perform(post("/nightly-receipt/{userId}/create",user.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nightlyReceipt))
                        .with(csrf()))
                //then
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.user.name").value("Sergio"))
                .andExpect(jsonPath("$.vehicle.plate").value("SZP85E"))
                .andExpect(jsonPath("$.rate.time").value("HORA MOTO"))
                .andExpect(jsonPath("$.amount").value(27000));

        verify(nightlyReceiptService).saveReceipt(any(NightlyReceipt.class));
    }

    @Test
    void testCreateNightlyReceiptValidationErrors() throws Exception{


    }

    @Test
    void updateReceipt() {

    }

    @Test
    void changePaymentStatus() {
    }

    @Test
    void deleteNightlyReceipt() {
    }
}