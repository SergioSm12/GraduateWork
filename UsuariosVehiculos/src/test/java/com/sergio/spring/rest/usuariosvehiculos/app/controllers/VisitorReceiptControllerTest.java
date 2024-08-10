package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataRate;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VisitorReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperVisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IVisitorReceiptService;
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


@WebMvcTest(VisitorReceiptController.class)
class VisitorReceiptControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    IVisitorReceiptService visitorReceiptService;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @WithMockUser
    void listVisitorReceipts() throws Exception {
        //given
        List<VisitorReceipt> visitorReceipts = Arrays.asList(DataVisitorReceipt.createVisitorReceipt001().orElseThrow(), DataVisitorReceipt.createVisitorReceipt002().orElseThrow());
        List<VisitorReceiptDto> visitorReceiptDtos = new ArrayList<>();
        for (VisitorReceipt vr : visitorReceipts) {
            VisitorReceiptDto build = DtoMapperVisitorReceipt.builder().setVisitorReceipt(vr).build();
            visitorReceiptDtos.add(build);
        }
        when(visitorReceiptService.visitorReceiptList()).thenReturn(visitorReceiptDtos);

        mvc.perform(get("/visitor-receipt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].plate").value("SZP85E"))
                .andExpect(jsonPath("$[0].rate.time").value("VISITANTE MOTO"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].plate").value("QSA741"))
                .andExpect(jsonPath("$[1].rate.time").value("VISITANTE CARRO"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(objectMapper.writeValueAsString(visitorReceiptDtos)));
        verify(visitorReceiptService).visitorReceiptList();
    }

    @Test
    @WithMockUser
    void createVisitorReceipt() throws Exception {
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 24, 11, 00);
        VisitorReceipt newVisitorReceipt = new VisitorReceipt(null, DataRate.createRate004().orElseThrow(), issueDate, dueDate, false, "CXS78P");
        when(visitorReceiptService.saveVisitorReceipt(newVisitorReceipt)).then(invocationOnMock -> {
            VisitorReceipt vr = invocationOnMock.getArgument(0);
            vr.setId(3L);
            VisitorReceiptDto visitorReceiptDto;
            visitorReceiptDto = DtoMapperVisitorReceipt.builder().setVisitorReceipt(vr).build();
            return visitorReceiptDto;
        });

        mvc.perform(post("/visitor-receipt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newVisitorReceipt))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.plate").value("CXS78P"))
                .andExpect(jsonPath("$.rate.time").value("VISITANTE MOTO"));

        verify(visitorReceiptService).saveVisitorReceipt(any(VisitorReceipt.class));
    }

    @Test
    @WithMockUser
    void testCreateVisitorReceiptValidationErrors() throws Exception {
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 24, 11, 00);
        VisitorReceipt invalidVisitorReceipt = new VisitorReceipt(3L, DataRate.createRate004().orElseThrow(), issueDate, dueDate, false, null);

        mvc.perform(post("/visitor-receipt")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVisitorReceipt))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.plate").value("El campo placa no puede estar vacío"));
        verify(visitorReceiptService, never()).saveVisitorReceipt(any(VisitorReceipt.class));
    }

    @Test
    @WithMockUser
    void updateVisitorReceipt() throws Exception {
        VisitorReceipt existingVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();
        VisitorReceipt updateVisitorReceipt = new VisitorReceipt(null, DataRate.createRate005().orElseThrow(), existingVisitorReceipt.getIssueDate(), existingVisitorReceipt.getDueDate(), true, existingVisitorReceipt.getPlate());
        VisitorReceipt savedVisitorReceipt = new VisitorReceipt();
        savedVisitorReceipt.setId(existingVisitorReceipt.getId());
        savedVisitorReceipt.setRate(updateVisitorReceipt.getRate());
        savedVisitorReceipt.setIssueDate(updateVisitorReceipt.getIssueDate());
        savedVisitorReceipt.setDueDate(updateVisitorReceipt.getDueDate());
        savedVisitorReceipt.setPlate(updateVisitorReceipt.getPlate());
        savedVisitorReceipt.setPaymentStatus(updateVisitorReceipt.isPaymentStatus());
        VisitorReceiptDto visitorReceiptDto = DtoMapperVisitorReceipt.builder().setVisitorReceipt(savedVisitorReceipt).build();
        when(visitorReceiptService.updateVisitorReceipt(updateVisitorReceipt, existingVisitorReceipt.getId())).thenReturn(Optional.of(visitorReceiptDto));


        mvc.perform(put("/visitor-receipt/{id}", existingVisitorReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVisitorReceipt))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.plate").value("SZP85E"))
                .andExpect(jsonPath("$.paymentStatus").value(true))
                .andExpect(jsonPath("$.rate.time").value("VISITANTE CARRO"));

        verify(visitorReceiptService).updateVisitorReceipt(any(VisitorReceipt.class), anyLong());
    }

    @Test
    @WithMockUser
    void testUpdateVisitorReceiptValidationErrors() throws Exception {
        VisitorReceipt existingVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();
        VisitorReceipt updateVisitorReceipt = new VisitorReceipt(null, DataRate.createRate005().orElseThrow(), existingVisitorReceipt.getIssueDate(), existingVisitorReceipt.getDueDate(), true, null);
        mvc.perform(put("/visitor-receipt/{id}", existingVisitorReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVisitorReceipt))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.plate").value("El campo placa no puede estar vacío"));
        verify(visitorReceiptService, never()).updateVisitorReceipt(any(VisitorReceipt.class), anyLong());
    }

    @Test
    @WithMockUser
    void testUpdateVisitorReceiptNotFound() throws Exception {
        VisitorReceipt existingVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();
        VisitorReceipt updateVisitorReceipt = new VisitorReceipt(null, DataRate.createRate005().orElseThrow(), existingVisitorReceipt.getIssueDate(), existingVisitorReceipt.getDueDate(), true, existingVisitorReceipt.getPlate());
        when(visitorReceiptService.updateVisitorReceipt(updateVisitorReceipt, existingVisitorReceipt.getId())).thenReturn(Optional.empty());
        mvc.perform(put("/visitor-receipt/{id}", existingVisitorReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVisitorReceipt))
                        .with(csrf()))
                .andExpect(status().isNotFound());
        verify(visitorReceiptService).updateVisitorReceipt(any(VisitorReceipt.class), anyLong());
    }

    @Test
    @WithMockUser
    void changePaymentStatus() throws Exception {
        VisitorReceipt existingVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();
        VisitorReceiptDto visitorReceiptDto = DtoMapperVisitorReceipt.builder().setVisitorReceipt(existingVisitorReceipt).build();
        when(visitorReceiptService.findByIdVisitorReceipt(existingVisitorReceipt.getId())).thenReturn(Optional.of(visitorReceiptDto));
        doNothing().when(visitorReceiptService).changePaymentStatus(existingVisitorReceipt.getId());

        mvc.perform(put("/visitor-receipt/change-payment/{visitorReceiptId}", existingVisitorReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(visitorReceiptService).findByIdVisitorReceipt(existingVisitorReceipt.getId());
        verify(visitorReceiptService).changePaymentStatus(existingVisitorReceipt.getId());
    }

    @Test
    @WithMockUser
    void changePaymentStatusNotFound() throws Exception {
        VisitorReceipt existingVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();
        when(visitorReceiptService.findByIdVisitorReceipt(existingVisitorReceipt.getId())).thenReturn(Optional.empty());
        mvc.perform(put("/visitor-receipt/change-payment/{visitorReceiptId}", existingVisitorReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        verify(visitorReceiptService).findByIdVisitorReceipt(existingVisitorReceipt.getId());
        verify(visitorReceiptService, never()).changePaymentStatus(existingVisitorReceipt.getId());
    }

    @Test
    @WithMockUser
    void deleteReceipt() throws Exception {
        VisitorReceipt existingVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();
        VisitorReceiptDto visitorReceiptDto = DtoMapperVisitorReceipt.builder().setVisitorReceipt(existingVisitorReceipt).build();
        when(visitorReceiptService.findByIdVisitorReceipt(existingVisitorReceipt.getId())).thenReturn(Optional.of(visitorReceiptDto));
        doNothing().when(visitorReceiptService).removeVisitorReceipt(existingVisitorReceipt.getId());

        mvc.perform(delete("/visitor-receipt/{visitorReceiptId}", existingVisitorReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(visitorReceiptService).findByIdVisitorReceipt(existingVisitorReceipt.getId());
        verify(visitorReceiptService).removeVisitorReceipt(existingVisitorReceipt.getId());
    }

    @Test
    @WithMockUser
    void testDeleteVisitorReceiptNotFound() throws Exception {
        VisitorReceipt existingVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();
        when(visitorReceiptService.findByIdVisitorReceipt(existingVisitorReceipt.getId())).thenReturn(Optional.empty());
        mvc.perform(delete("/visitor-receipt/{visitorReceiptId}", existingVisitorReceipt.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isNotFound());
        verify(visitorReceiptService).findByIdVisitorReceipt(existingVisitorReceipt.getId());
        verify(visitorReceiptService, never()).removeVisitorReceipt(existingVisitorReceipt.getId());
    }

    @Test
    @WithMockUser
    void getCountUnpaidReceipts() throws Exception {
        List<VisitorReceipt> data = List.of(DataVisitorReceipt.createVisitorReceipt001().orElseThrow());
        long totalUnpaid = data.size();
        when(visitorReceiptService.getTotalVisitorUnpaidReceipts()).thenReturn(totalUnpaid);
        mvc.perform(get("/visitor-receipt/count-unpaid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)));
        verify(visitorReceiptService).getTotalVisitorUnpaidReceipts();
    }

    @Test
    @WithMockUser
    void getCountPaidReceipts() throws Exception {
        List<VisitorReceipt> data = List.of(DataVisitorReceipt.createVisitorReceipt002().orElseThrow());
        long totalPaid = data.size();
        when(visitorReceiptService.getTotalVisitorPaidReceipts()).thenReturn(totalPaid);
        mvc.perform(get("/visitor-receipt/count-paid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(1)));
        verify(visitorReceiptService).getTotalVisitorPaidReceipts();
    }

    @Test
    @WithMockUser
    void getTotalReceipts() throws Exception {
        List<VisitorReceipt> data = List.of(DataVisitorReceipt.createVisitorReceipt002().orElseThrow(), DataVisitorReceipt.createVisitorReceipt001().orElseThrow());
        long totalPaid = data.size();
        when(visitorReceiptService.getTotalVisitorReceipt()).thenReturn(totalPaid);
        mvc.perform(get("/visitor-receipt/count-total")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(2)));
        verify(visitorReceiptService).getTotalVisitorReceipt();
    }
}