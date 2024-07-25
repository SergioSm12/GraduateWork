package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataRate;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.*;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IRateRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IReceiptRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IUserRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReceiptServiceTest {

    @MockBean
    IReceiptRepository receiptRepository;

    @MockBean
    IUserRepository userRepository;

    @MockBean
    IVehicleRepository vehicleRepository;

    @MockBean
    IRateRepository rateRepository;

    @Autowired
    IReceiptService receiptService;

    @Test
    void testReceiptList() {
        List<Receipt> data = Arrays.asList(DataReceipt.createReceipt001().orElseThrow(), DataReceipt.createReceipt002().orElseThrow());
        when(receiptRepository.findAllByOrderByIssueDateDesc()).thenReturn(data);

        List<ReceiptDto> receipts = receiptService.receiptList();

        assertFalse(receipts.isEmpty());
        assertEquals(2, receipts.size());
        assertTrue(receipts.stream().anyMatch(receiptDto -> receiptDto.getId().equals(DataReceipt.createReceipt001().orElseThrow().getId())));
        verify(receiptRepository).findAllByOrderByIssueDateDesc();
    }

    @Test
    void testFindByIdReceipt() {
        when(receiptRepository.findById(2L)).thenReturn(DataReceipt.createReceipt002());

        ReceiptDto receipt = receiptService.findByIdReceipt(2L).orElseThrow();

        assertNotNull(receipt);
        assertEquals(2L, receipt.getId());
        assertEquals("David", receipt.getUser().getName());
        assertEquals("DIA CARRO", receipt.getRateDto().getTime());

        verify(receiptRepository).findById(2L);

    }

    @Test
    void testFindByIdReceiptWithDetails() {
        when(receiptRepository.findByIdWithDetails(2L)).thenReturn(DataReceipt.createReceipt002());

        Receipt receipt = receiptService.findByIdReceiptWithDetails(2L).orElseThrow();

        assertNotNull(receipt);
        assertEquals(2L, receipt.getId());
        assertEquals("David", receipt.getUser().getName());
        assertEquals("DIA CARRO", receipt.getRate().getTime());

        verify(receiptRepository).findByIdWithDetails(2L);

    }

    @Test
    void testGetUnpaidReceipts() {
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());
        when(receiptRepository.findByPaymentStatusFalse()).thenReturn(receipts);

        List<ReceiptDto> receiptDtos = receiptService.getUnpaidReceipts();

        assertFalse(receiptDtos.isEmpty());
        assertEquals(1, receiptDtos.size());
        assertFalse(receiptDtos.get(0).isPaymentStatus());
        assertTrue(receipts.stream().anyMatch(receipt -> receipt.getId().equals(DataReceipt.createReceipt001().orElseThrow().getId())));
        verify(receiptRepository).findByPaymentStatusFalse();

    }

    @Test
    void testGetPaidReceipts() {
        List<Receipt> receipts = List.of(DataReceipt.createReceipt002().orElseThrow());
        when(receiptRepository.findByPaymentStatusTrue()).thenReturn(receipts);

        List<ReceiptDto> receiptDtos = receiptService.getPaidReceipts();

        assertFalse(receiptDtos.isEmpty());
        assertEquals(1, receiptDtos.size());
        assertTrue(receiptDtos.get(0).isPaymentStatus());
        assertTrue(receipts.stream().anyMatch(receipt -> receipt.getId().equals(DataReceipt.createReceipt002().orElseThrow().getId())));
        verify(receiptRepository).findByPaymentStatusTrue();
    }

    @Test
    void getReceiptsByUserId() {
        User existUser = DataUser.createUser001().orElseThrow();
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());

        when(userRepository.findById(existUser.getId())).thenReturn(Optional.of(existUser));
        when(receiptRepository.findByUserOrderByIssueDateDesc(existUser)).thenReturn(receipts);

        List<ReceiptDto> result = receiptService.getReceiptsByUserId(existUser.getId());

        verify(receiptRepository).findByUserOrderByIssueDateDesc(any());
        verify(userRepository).findById(existUser.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3000, result.get(0).getRateDto().getAmount());
    }

    @Test
    void getUnpaidReceiptsByUser() {
        User existUser = DataUser.createUser001().orElseThrow();
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());

        when(receiptRepository.findByUserAndPaymentStatus(existUser, false)).thenReturn(receipts);

        List<ReceiptDto> result = receiptService.getUnpaidReceiptsByUser(existUser);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("DIA MOTO", result.get(0).getRateDto().getTime());
        verify(receiptRepository).findByUserAndPaymentStatus(existUser, false);
    }

    @Test
    void getTotalUnpaidReceipts() {
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow());
        Long totalReceipts = (long) receipts.size();
        when(receiptRepository.countByPaymentStatusFalse()).thenReturn(totalReceipts);

        long result = receiptService.getTotalUnpaidReceipts();

        assertEquals(totalReceipts, result);
        verify(receiptRepository).countByPaymentStatusFalse();
    }

    @Test
    void getTotalPaidReceipts() {
        List<Receipt> receipts = List.of(DataReceipt.createReceipt002().orElseThrow());
        Long totalReceipts = (long) receipts.size();
        when(receiptRepository.countByPaymentStatusTrue()).thenReturn(totalReceipts);

        long result = receiptService.getTotalPaidReceipts();

        assertEquals(totalReceipts, result);
        verify(receiptRepository).countByPaymentStatusTrue();
    }

    @Test
    void getTotalReceipts() {
        List<Receipt> receipts = List.of(DataReceipt.createReceipt001().orElseThrow(), DataReceipt.createReceipt002().orElseThrow());
        Long totalReceipts = (long) receipts.size();
        when(receiptRepository.count()).thenReturn(totalReceipts);

        long result = receiptService.getTotalReceipts();

        assertEquals(totalReceipts, result);
        verify(receiptRepository).count();
    }

    @Test
    void saveReceipt() {
        User user = DataUser.createUser002().orElseThrow();
        Vehicle vehicle = DataVehicle.createVehicle002().orElseThrow();
        Rate rate = DataRate.createRate002().orElseThrow();
        user.setVehicles(List.of(vehicle));
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 23, 21, 00);
        Receipt receipt = new Receipt(null, user, vehicle, rate, issueDate, dueDate, false);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(vehicleRepository.findById(2L)).thenReturn(Optional.of(vehicle));
        when(rateRepository.findById(2L)).thenReturn(Optional.of(rate));
        when(receiptRepository.save(any())).then(invocationOnMock -> {
            Receipt r = invocationOnMock.getArgument(0);
            r.setId(3L);
            return r;
        });

        ReceiptDto receiptDto = receiptService.saveReceipt(receipt);


        assertNotNull(receiptDto);
        assertEquals(3L, receiptDto.getId());
        assertEquals("David", receiptDto.getUser().getName());
        assertEquals(5000, receiptDto.getRateDto().getAmount());
        verify(userRepository).findById(user.getId());
        verify(vehicleRepository).findById(vehicle.getId());
        verify(rateRepository).findById(rate.getId());
        verify(receiptRepository).save(any());

    }

    @Test
    void testSaveReceiptUserNotFound() {
        User user = DataUser.createUser002().orElseThrow();
        Vehicle vehicle = DataVehicle.createVehicle002().orElseThrow();
        Rate rate = DataRate.createRate002().orElseThrow();
        user.setVehicles(List.of(vehicle));
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 23, 21, 00);
        Receipt receipt = new Receipt(null, user, vehicle, rate, issueDate, dueDate, false);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(vehicleRepository.findById(2L)).thenReturn(Optional.of(vehicle));
        when(rateRepository.findById(2L)).thenReturn(Optional.of(rate));
        when(receiptRepository.save(any())).then(invocationOnMock -> {
            Receipt r = invocationOnMock.getArgument(0);
            r.setId(3L);
            return r;
        });

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiptService.saveReceipt(receipt);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testSaveReceiptVehicleNotFound() {
        User user = DataUser.createUser002().orElseThrow();
        Vehicle vehicle = DataVehicle.createVehicle002().orElseThrow();
        Rate rate = DataRate.createRate002().orElseThrow();
        user.setVehicles(List.of(vehicle));
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 23, 21, 00);
        Receipt receipt = new Receipt(null, user, vehicle, rate, issueDate, dueDate, false);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(rateRepository.findById(2L)).thenReturn(Optional.of(rate));
        when(receiptRepository.save(any())).then(invocationOnMock -> {
            Receipt r = invocationOnMock.getArgument(0);
            r.setId(3L);
            return r;
        });

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiptService.saveReceipt(receipt);
        });

        assertEquals("Vehicle not found", exception.getMessage());
    }

    @Test
    void testSaveReceiptVehicleNotAssociatedWithUser() {
        User user = DataUser.createUser002().orElseThrow();
        Vehicle vehicle1 = DataVehicle.createVehicle002().orElseThrow();
        Vehicle vehicle = DataVehicle.createVehicle001().orElseThrow();
        Rate rate = DataRate.createRate002().orElseThrow();
        user.setVehicles(List.of(vehicle1));
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 23, 21, 00);
        Receipt receipt = new Receipt(null, user, vehicle1, rate, issueDate, dueDate, false);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(vehicleRepository.findById(2L)).thenReturn(Optional.of(vehicle));
        when(rateRepository.findById(2L)).thenReturn(Optional.of(rate));
        when(receiptRepository.save(any())).then(invocationOnMock -> {
            Receipt r = invocationOnMock.getArgument(0);
            r.setId(3L);
            return r;
        });

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiptService.saveReceipt(receipt);
        });

        assertEquals("Vehicle is not associated with the user", exception.getMessage());
    }

    @Test
    void testSaveReceiptRateNotFound() {
        User user = DataUser.createUser002().orElseThrow();
        Vehicle vehicle = DataVehicle.createVehicle002().orElseThrow();
        Rate rate = DataRate.createRate002().orElseThrow();
        user.setVehicles(List.of(vehicle));
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 8, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 23, 21, 00);
        Receipt receipt = new Receipt(null, user, vehicle, rate, issueDate, dueDate, false);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(vehicleRepository.findById(2L)).thenReturn(Optional.of(vehicle));
        when(rateRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(receiptRepository.save(any())).then(invocationOnMock -> {
            Receipt r = invocationOnMock.getArgument(0);
            r.setId(3L);
            return r;
        });

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            receiptService.saveReceipt(receipt);
        });

        assertEquals("Rate not found", exception.getMessage());
    }

    @Test
    void updateReceipt() {
        Receipt existReceipt = DataReceipt.createReceipt001().orElseThrow();

        when(receiptRepository.findById(existReceipt.getId())).thenReturn(Optional.of(existReceipt));
        LocalDateTime issueDate = LocalDateTime.of(2024, 07, 24, 7, 00);
        LocalDateTime dueDate = LocalDateTime.of(2024, 07, 24, 22, 00);
        ReceiptRequest receiptRequest = new ReceiptRequest(issueDate, dueDate, true, existReceipt.getRate());
        existReceipt.setIssueDate(receiptRequest.getIssueDate());
        existReceipt.setDueDate(receiptRequest.getDueDate());
        existReceipt.setPaymentStatus(receiptRequest.isPaymentStatus());
        existReceipt.setRate(receiptRequest.getRate());
        when(receiptRepository.save(existReceipt)).thenReturn(existReceipt);

        ReceiptDto receiptDto = receiptService.updateReceipt(receiptRequest, existReceipt.getId()).orElseThrow();

        assertNotNull(receiptDto);
        assertTrue(receiptDto.isPaymentStatus());
        assertEquals(issueDate, receiptDto.getIssueDate());
        assertEquals(dueDate, receiptDto.getDueDate());
        verify(receiptRepository).findById(anyLong());
        verify(receiptRepository).save(any());
    }

    @Test
    void changePaymentStatus() {
        Receipt receipt = DataReceipt.createReceipt002().orElseThrow();
        when(receiptRepository.findById(receipt.getId())).thenReturn(Optional.of(receipt));
        when(receiptRepository.save(receipt)).thenReturn(receipt);

        receiptService.changePaymentStatus(receipt.getId());

        verify(receiptRepository).findById(anyLong());
        verify(receiptRepository).save(any());
    }

    @Test
    void remove() {
        Long receiptId = 1L;
        doNothing().when(receiptRepository).deleteById(receiptId);

        receiptService.remove(receiptId);

        verify(receiptRepository, times(1)).deleteById(receiptId);
    }

    @Test
    void testRemove_ReceiptDoesNotExist() {
        Long receiptId = 1L;
        doThrow(new EmptyResultDataAccessException(1)).when(receiptRepository).deleteById(receiptId);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            receiptService.remove(receiptId);
        });

        verify(receiptRepository, times(1)).deleteById(receiptId);
    }

    @Test
    void testGetWeeklyIncome() {
        int year = 2023;
        Month month = Month.JULY;

        List<Receipt> paidReceipts = Arrays.asList(
                new Receipt(1L, new User(), new Vehicle(), new Rate(), LocalDateTime.now(), LocalDateTime.now(), true),
                new Receipt(2L, new User(), new Vehicle(), new Rate(), LocalDateTime.now(), LocalDateTime.now(), true)
        );

        List<Receipt> unpaidReceipts = Arrays.asList(
                new Receipt(3L, new User(), new Vehicle(), new Rate(), LocalDateTime.now(), LocalDateTime.now(), false)
        );

        when(receiptRepository.findByIssueDateBetweenAndPaymentStatus(any(LocalDateTime.class), any(LocalDateTime.class), eq(true)))
                .thenReturn(paidReceipts);

        when(receiptRepository.findByIssueDateBetweenAndPaymentStatus(any(LocalDateTime.class), any(LocalDateTime.class), eq(false)))
                .thenReturn(unpaidReceipts);

        Map<String, Double> result = receiptService.getWeeklyIncome(year, month);

        // Verificar que el resultado contenga los valores esperados.
        assertEquals(0.0, result.get(" Saldo Pago "));
        assertEquals(0.0, result.get(" Saldo pendiente de pago "));
        assertEquals(0.0, result.get(" Total "));
    }

}