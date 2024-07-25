package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataRate;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VisitorReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IRateRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVisitorReceiptRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class VisitorReceiptServiceTest {
    @MockBean
    IVisitorReceiptRepository visitorReceiptRepository;

    @MockBean
    IRateRepository rateRepository;

    @Autowired
    IVisitorReceiptService visitorReceiptService;

    @Test
    void visitorReceiptList() {
        List<VisitorReceipt> data = Arrays.asList(DataVisitorReceipt.createVisitorReceipt001().orElseThrow(), DataVisitorReceipt.createVisitorReceipt002().orElseThrow());
        when(visitorReceiptRepository.findAll()).thenReturn(data);

        List<VisitorReceiptDto> visitorReceipts = visitorReceiptService.visitorReceiptList();

        assertFalse(visitorReceipts.isEmpty());
        assertEquals(2, visitorReceipts.size());
        assertTrue(visitorReceipts.stream().anyMatch(visitorReceiptDto -> visitorReceiptDto.getId().equals(DataVisitorReceipt.createVisitorReceipt001().orElseThrow().getId())));
        verify(visitorReceiptRepository).findAll();
    }

    @Test
    void findByIdVisitorReceipt() {
        when(visitorReceiptRepository.findById(2L)).thenReturn(DataVisitorReceipt.createVisitorReceipt002());

        VisitorReceiptDto visitorReceiptDto = visitorReceiptService.findByIdVisitorReceipt(2L).orElseThrow();
        assertNotNull(visitorReceiptDto);
        assertEquals(2L, visitorReceiptDto.getId());
        assertEquals(5000, visitorReceiptDto.getRateDto().getAmount());
        assertEquals("VISITANTE CARRO", visitorReceiptDto.getRateDto().getTime());
        verify(visitorReceiptRepository).findById(any());
    }

    @Test
    void findByIdVisitorReceiptDetails() {
        when(visitorReceiptRepository.findByIdWithDetails(2L)).thenReturn(DataVisitorReceipt.createVisitorReceipt002());

        VisitorReceipt visitorReceipt = visitorReceiptService.findByIdVisitorReceiptDetails(2L).orElseThrow();
        assertNotNull(visitorReceipt);
        assertEquals(2L, visitorReceipt.getId());
        assertEquals(5000, visitorReceipt.getRate().getAmount());
        assertEquals("VISITANTE CARRO", visitorReceipt.getRate().getTime());
        verify(visitorReceiptRepository).findByIdWithDetails(any());
    }

    @Test
    void saveVisitorReceipt() {
        Rate rate = DataRate.createRate004().orElseThrow();
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 24, 11, 00);
        VisitorReceipt visitorReceipt = new VisitorReceipt(null, rate, issueDate, dueDate, false, "ZXS74U");

        when(rateRepository.findById(4L)).thenReturn(Optional.of(rate));
        when(visitorReceiptRepository.save(any())).then(invocationOnMock -> {
            VisitorReceipt vr = invocationOnMock.getArgument(0);
            vr.setId(3L);
            return vr;
        });

        VisitorReceiptDto visitorReceiptDto = visitorReceiptService.saveVisitorReceipt(visitorReceipt);

        assertNotNull(visitorReceiptDto);
        assertEquals(3L, visitorReceiptDto.getId());
        assertEquals("VISITANTE MOTO", visitorReceiptDto.getRateDto().getTime());
        assertEquals("ZXS74U", visitorReceiptDto.getPlate());
        verify(rateRepository).findById(any());
        verify(visitorReceiptRepository).save(any());
    }

    @Test
    void saveVisitorReceiptRateNotFound() {
        Rate rate = DataRate.createRate004().orElseThrow();
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 24, 11, 00);
        VisitorReceipt visitorReceipt = new VisitorReceipt(null, rate, issueDate, dueDate, false, "ZXS74U");

        when(rateRepository.findById(any())).thenReturn(Optional.empty());
        when(visitorReceiptRepository.save(any())).then(invocationOnMock -> {
            VisitorReceipt vr = invocationOnMock.getArgument(0);
            vr.setId(3L);
            return vr;
        });

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            visitorReceiptService.saveVisitorReceipt(visitorReceipt);
        });

        assertEquals("Rate not found", exception.getMessage());
    }

    @Test
    void updateVisitorReceipt() {
        VisitorReceipt existVisitorReceipt = DataVisitorReceipt.createVisitorReceipt001().orElseThrow();

        when(visitorReceiptRepository.findById(existVisitorReceipt.getId())).thenReturn(Optional.of(existVisitorReceipt));
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 25, 7, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 25, 12, 00);
        VisitorReceipt updateVisitorReceipt = new VisitorReceipt();
        updateVisitorReceipt.setId(existVisitorReceipt.getId());
        updateVisitorReceipt.setIssueDate(issueDate);
        updateVisitorReceipt.setDueDate(dueDate);
        updateVisitorReceipt.setPaymentStatus(true);
        updateVisitorReceipt.setPlate("XZA78T");
        updateVisitorReceipt.setRate(existVisitorReceipt.getRate());
        when(visitorReceiptRepository.save(any())).thenReturn(updateVisitorReceipt);

        VisitorReceiptDto visitorReceiptDto = visitorReceiptService.updateVisitorReceipt(updateVisitorReceipt, existVisitorReceipt.getId()).orElseThrow();

        assertNotNull(visitorReceiptDto);
        assertTrue(visitorReceiptDto.isPaymentStatus());
        assertEquals(issueDate, visitorReceiptDto.getIssueDate());
        assertEquals("XZA78T", visitorReceiptDto.getPlate());
        verify(visitorReceiptRepository).findById(any());
        verify(visitorReceiptRepository).save(any());
    }

    @Test
    void changePaymentStatus() {
        VisitorReceipt visitorReceipt = DataVisitorReceipt.createVisitorReceipt002().orElseThrow();
        when(visitorReceiptRepository.findById(visitorReceipt.getId())).thenReturn(Optional.of(visitorReceipt));
        when(visitorReceiptRepository.save(any())).thenReturn(visitorReceipt);

        visitorReceiptService.changePaymentStatus(visitorReceipt.getId());

        verify(visitorReceiptRepository).findById(anyLong());
        verify(visitorReceiptRepository).save(any());
    }

    @Test
    void removeVisitorReceipt() {
        Long vistorReceiptId = 1L;
        doNothing().when(visitorReceiptRepository).deleteById(vistorReceiptId);

        visitorReceiptService.removeVisitorReceipt(vistorReceiptId);
        verify(visitorReceiptRepository).deleteById(vistorReceiptId);
    }

    @Test
    void getTotalVisitorUnpaidReceipts() {
        List<VisitorReceipt> visitorReceipts = List.of(DataVisitorReceipt.createVisitorReceipt001().orElseThrow());
        Long totalVisitorReceipts = (long) visitorReceipts.size();
        when(visitorReceiptRepository.countByPaymentStatusFalse()).thenReturn(totalVisitorReceipts);

        long result = visitorReceiptService.getTotalVisitorUnpaidReceipts();

        assertEquals(totalVisitorReceipts, result);
        assertFalse(visitorReceipts.get(0).isPaymentStatus());
        verify(visitorReceiptRepository).countByPaymentStatusFalse();
    }

    @Test
    void getTotalVisitorPaidReceipts() {
        List<VisitorReceipt> visitorReceipts = List.of(DataVisitorReceipt.createVisitorReceipt002().orElseThrow());
        Long totalVisitorReceipts = (long) visitorReceipts.size();
        when(visitorReceiptRepository.countByPaymentStatusTrue()).thenReturn(totalVisitorReceipts);

        long result = visitorReceiptService.getTotalVisitorPaidReceipts();

        assertEquals(totalVisitorReceipts, result);
        assertTrue(visitorReceipts.get(0).isPaymentStatus());
        verify(visitorReceiptRepository).countByPaymentStatusTrue();
    }

    @Test
    void getTotalVisitorReceipt() {
        List<VisitorReceipt> visitorReceipts = List.of(DataVisitorReceipt.createVisitorReceipt001().orElseThrow(), DataVisitorReceipt.createVisitorReceipt002().orElseThrow());
        Long totalVisitorReceipts = (long) visitorReceipts.size();
        when(visitorReceiptRepository.count()).thenReturn(totalVisitorReceipts);

        long result = visitorReceiptService.getTotalVisitorReceipt();

        assertEquals(totalVisitorReceipts, result);
        verify(visitorReceiptRepository).count();
    }
}