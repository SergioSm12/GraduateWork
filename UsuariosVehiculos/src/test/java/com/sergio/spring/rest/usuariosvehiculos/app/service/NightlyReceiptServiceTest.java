package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.data.DataNightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataRate;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataUser;
import com.sergio.spring.rest.usuariosvehiculos.app.data.DataVehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.NightlyReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.NightlyReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.INightlyReceiptRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IRateRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IUserRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVehicleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class NightlyReceiptServiceTest {

    @MockBean
    INightlyReceiptRepository nightlyReceiptRepository;

    @MockBean
    IUserRepository userRepository;

    @MockBean
    IVehicleRepository vehicleRepository;

    @MockBean
    IRateRepository rateRepository;

    @Autowired
    INightlyReceiptService nightlyReceiptService;

    @Test
    void testNightlyReceiptList() {
        List<NightlyReceipt> data = Arrays.asList(DataNightlyReceipt.createNightlyReceipt001().orElseThrow(), DataNightlyReceipt.createNightlyReceipt002().orElseThrow());
        when(nightlyReceiptRepository.findAll()).thenReturn(data);

        //when
        List<NightlyReceiptDto> nightlyReceipts = nightlyReceiptService.nightlyReceiptList();

        //then
        assertFalse(nightlyReceipts.isEmpty());
        assertEquals(2, nightlyReceipts.size());
        assertTrue(nightlyReceipts.stream().anyMatch(nightlyReceiptDto -> nightlyReceiptDto.getId().equals(DataNightlyReceipt.createNightlyReceipt001().orElseThrow().getId())));
        verify(nightlyReceiptRepository).findAll();
    }

    @Test
    void testFindByIdNightlyReceipt() {
        when(nightlyReceiptRepository.findById(1L)).thenReturn(DataNightlyReceipt.createNightlyReceipt001());

        NightlyReceiptDto nightlyReceiptDto = nightlyReceiptService.findByIdNightlyReceipt(1L).orElseThrow();

        assertEquals(1L, nightlyReceiptDto.getId());
        assertEquals("Sergio", nightlyReceiptDto.getUser().getName());
        assertEquals(18000, nightlyReceiptDto.getAmount());

        verify(nightlyReceiptRepository).findById(1L);
    }

    @Test
    void testGetTotalUnpaidNightlyReceipts() {
        List<NightlyReceipt> nightlyReceipts = List.of(DataNightlyReceipt.createNightlyReceipt001().orElseThrow());
        Long totalNightlyReceipt = (long) nightlyReceipts.size();
        when(nightlyReceiptRepository.countByPaymentStatusFalse()).thenReturn(totalNightlyReceipt);

        long result = nightlyReceiptService.getTotalUnpaidNightlyReceipts();

        assertEquals(totalNightlyReceipt, result);

        verify(nightlyReceiptRepository).countByPaymentStatusFalse();
    }

    @Test
    void testGetTotalPaidNightlyReceipts() {
        List<NightlyReceipt> nightlyReceipts = List.of(DataNightlyReceipt.createNightlyReceipt002().orElseThrow());
        Long totalNightlyReceipt = (long) nightlyReceipts.size();
        when(nightlyReceiptRepository.countByPaymentStatusTrue()).thenReturn(totalNightlyReceipt);

        long result = nightlyReceiptService.getTotalPaidNightlyReceipts();

        assertEquals(totalNightlyReceipt, result);

        verify(nightlyReceiptRepository).countByPaymentStatusTrue();
    }

    @Test
    void testGetTotalNightlyReceipts() {
        List<NightlyReceipt> nightlyReceipts = List.of(DataNightlyReceipt.createNightlyReceipt001().orElseThrow(), DataNightlyReceipt.createNightlyReceipt002().orElseThrow());
        Long totalNightlyReceipt = (long) nightlyReceipts.size();
        when(nightlyReceiptRepository.count()).thenReturn(totalNightlyReceipt);

        long result = nightlyReceiptService.getTotalNightlyReceipts();

        assertEquals(totalNightlyReceipt, result);

        verify(nightlyReceiptRepository).count();
    }

    @Test
    void testGetNightlyReceiptsByUserId() {
        User existUser = DataUser.createUser001().orElseThrow();
        List<NightlyReceipt> nightlyReceipts = Arrays.asList(DataNightlyReceipt.createNightlyReceipt001().orElseThrow(), DataNightlyReceipt.createNightlyReceipt002().orElseThrow());

        when(userRepository.findById(existUser.getId())).thenReturn(Optional.of(existUser));
        when(nightlyReceiptRepository.findByUserOrderByInitialTimeDesc(existUser)).thenReturn(nightlyReceipts);

        List<NightlyReceiptDto> result = nightlyReceiptService.getNightlyReceiptsByUserId(existUser.getId());

        verify(nightlyReceiptRepository).findByUserOrderByInitialTimeDesc(any());
        verify(userRepository).findById(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(18000, result.get(0).getAmount());

    }

    @Test
    void testFindByIdReceiptWithDetails() {
        when(nightlyReceiptRepository.findByIdWithDetails(2L)).thenReturn(DataNightlyReceipt.createNightlyReceipt002());

        NightlyReceipt nightlyReceipt = nightlyReceiptService.findByIdReceiptWithDetails(2L).orElseThrow();

        assertEquals(2L, nightlyReceipt.getId());
        assertEquals("Sergio", nightlyReceipt.getUser().getName());
        assertEquals(54000, nightlyReceipt.getAmount());

        verify(nightlyReceiptRepository).findByIdWithDetails(2L);
    }

    @Test
    void testSaveReceipt() {

        //given
        User user = DataUser.createUser001().orElseThrow();
        Vehicle vehicle = DataVehicle.createVehicle001().orElseThrow();
        Rate rate = DataRate.createRate003().orElseThrow();
        user.setVehicles(List.of(vehicle));

        LocalDateTime initialTime = LocalDateTime.of(2024, 7, 23, 22, 00);
        LocalDateTime departureTime = LocalDateTime.of(2024, 7, 23, 7, 00);
        NightlyReceipt newNightlyReceipt = new NightlyReceipt(null, user, vehicle, rate, initialTime, departureTime, false, 0.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(rateRepository.findById(3L)).thenReturn(Optional.of(rate));
        when(nightlyReceiptRepository.save(any())).then(invocationOnMock -> {
            NightlyReceipt nr = invocationOnMock.getArgument(0);
            nr.setId(3L);
            return nr;
        });

        //when
        NightlyReceiptDto nightlyReceiptDto = nightlyReceiptService.saveReceipt(newNightlyReceipt);

        //then
        assertNotNull(nightlyReceiptDto);
        assertEquals(3L, nightlyReceiptDto.getId());
        assertEquals("Sergio", nightlyReceiptDto.getUser().getName());
        assertEquals(27000, nightlyReceiptDto.getAmount());

        verify(userRepository).findById(1L);
        verify(vehicleRepository).findById(1L);
        verify(rateRepository).findById(3L);
        verify(nightlyReceiptRepository).save(any());
    }

    @Test
    void testSaveReceiptUserNotFound() {
        Vehicle vehicle = DataVehicle.createVehicle001().orElseThrow();
        Rate rate = DataRate.createRate001().orElseThrow();

        NightlyReceipt newNightlyReceipt = new NightlyReceipt(null, new User(), vehicle, rate, null, null, false, 54000);

        when(userRepository.findById(null)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            nightlyReceiptService.saveReceipt(newNightlyReceipt);
        });

        assertEquals("User is null or has no ID", exception.getMessage());
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    void testSaveReceiptVehicleNotFound() {
        User user = DataUser.createUser001().orElseThrow();
        Rate rate = DataRate.createRate001().orElseThrow();

        NightlyReceipt newNightlyReceipt = new NightlyReceipt(null, user, new Vehicle(), rate, null, null, false, 54000);

        when(vehicleRepository.findById(null)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            nightlyReceiptService.saveReceipt(newNightlyReceipt);
        });

        assertEquals("Vehicle is null or has no ID", exception.getMessage());
        verify(vehicleRepository, never()).findById(anyLong());
    }

    @Test
    void testSaveReceiptRateNotFound() {
        User user = DataUser.createUser001().orElseThrow();
        Vehicle vehicle = DataVehicle.createVehicle001().orElseThrow();

        NightlyReceipt newNightlyReceipt = new NightlyReceipt(null, user, vehicle, new Rate(), null, null, false, 54000);

        when(rateRepository.findById(null)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            nightlyReceiptService.saveReceipt(newNightlyReceipt);
        });

        assertEquals("Rate is null or has no ID", exception.getMessage());
        verify(rateRepository, never()).findById(anyLong());
    }

    @Test
    void testUpdateNightlyReceipt() {
        //given

        NightlyReceipt exitNightlyReceipt = DataNightlyReceipt.createNightlyReceipt001().orElseThrow();
        Rate existRate = DataRate.createRate003().orElseThrow();
        exitNightlyReceipt.setRate(existRate);
        LocalDateTime newInitialTime = LocalDateTime.of(2024, 7, 23, 22, 00);
        LocalDateTime newDepartureTime = LocalDateTime.of(2024, 7, 23, 23, 00);
        exitNightlyReceipt.setInitialTime(newInitialTime);
        exitNightlyReceipt.setDepartureTime(newDepartureTime);
        exitNightlyReceipt.setAmount(3000);
        NightlyReceiptRequest updateNightlyReceipt = new NightlyReceiptRequest(newInitialTime, newDepartureTime, true, existRate, exitNightlyReceipt.getAmount());
        NightlyReceipt updateWithRequest = new NightlyReceipt();
        updateWithRequest.setId(exitNightlyReceipt.getId());
        updateWithRequest.setUser(exitNightlyReceipt.getUser());
        updateWithRequest.setVehicle(exitNightlyReceipt.getVehicle());
        updateWithRequest.setRate(existRate);
        updateWithRequest.setInitialTime(updateNightlyReceipt.getInitialTime());
        updateWithRequest.setDepartureTime(updateNightlyReceipt.getDepartureTime());
        updateWithRequest.setPaymentStatus(updateNightlyReceipt.isPaymentStatus());
        updateWithRequest.setAmount(updateNightlyReceipt.getAmount());

        when(nightlyReceiptRepository.findById(1L)).thenReturn(Optional.of(exitNightlyReceipt));
        when(userRepository.findById(1L)).thenReturn(Optional.of(exitNightlyReceipt.getUser()));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(exitNightlyReceipt.getVehicle()));
        when(rateRepository.findById(3L)).thenReturn(Optional.of(exitNightlyReceipt.getRate()));
        when(nightlyReceiptRepository.save(any(NightlyReceipt.class))).thenReturn(updateWithRequest);

        //when
        Optional<NightlyReceiptDto> result = nightlyReceiptService.updateNightlyReceipt(updateNightlyReceipt, exitNightlyReceipt.getId());

        //then
        assertTrue(result.isPresent());
        assertEquals("Sergio", result.orElseThrow().getUser().getName());
        verify(rateRepository).findById(3L);
    }

    @Test
    void testChangePaymentStatus() {
        //given
        NightlyReceipt existNightlyReceipt = DataNightlyReceipt.createNightlyReceipt001().orElseThrow();
        when(nightlyReceiptRepository.findById(existNightlyReceipt.getId())).thenReturn(Optional.of(existNightlyReceipt));

        //when
        nightlyReceiptService.changePaymentStatus(existNightlyReceipt.getId());

        verify(nightlyReceiptRepository).findById(existNightlyReceipt.getId());
        assertTrue(existNightlyReceipt.isPaymentStatus());
        verify(nightlyReceiptRepository).save(any(NightlyReceipt.class));
    }

    @Test
    void testChangePaymentStatusWhenReceiptDoesNotExist() {
        //given
        Long nonExistReceipt = 999L;
        when(nightlyReceiptRepository.findById(nonExistReceipt)).thenReturn(Optional.empty());

        //When
        nightlyReceiptService.changePaymentStatus(nonExistReceipt);

        //then
        verify(nightlyReceiptRepository).findById(nonExistReceipt);
        verify(nightlyReceiptRepository, never()).save(any(NightlyReceipt.class));

    }

    @Test
    void testRemove() {
        NightlyReceipt existNightlyReceipt = DataNightlyReceipt.createNightlyReceipt001().orElseThrow();

        doNothing().when(nightlyReceiptRepository).deleteById(existNightlyReceipt.getId());

        nightlyReceiptService.remove(existNightlyReceipt.getId());
        verify(nightlyReceiptRepository).deleteById(existNightlyReceipt.getId());
    }

    @Test
    void testGetWeeklyIncome() {
        // given
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        Month month = currentDate.getMonth();

        Map<String, Double> expectedWeeklyIncome = new LinkedHashMap<>();
        expectedWeeklyIncome.put("2024-07-01 - 2024-07-06", 0.0);
        expectedWeeklyIncome.put("2024-07-07 - 2024-07-13", 0.0);
        expectedWeeklyIncome.put("2024-07-14 - 2024-07-20", 0.0);
        expectedWeeklyIncome.put("2024-07-21 - 2024-07-27", 0.0);
        expectedWeeklyIncome.put("2024-07-28 - 2024-07-31", 0.0);
        expectedWeeklyIncome.put(" Saldo pendiente de pago ", 0.0);
        expectedWeeklyIncome.put(" Saldo Pago ", 0.0);
        expectedWeeklyIncome.put(" Total ", 0.0);

        when(nightlyReceiptRepository.findByInitialTimeBetween(any(), any()))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(true)))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(false)))
                .thenReturn(List.of(new NightlyReceipt()));

        // when
        Map<String, Double> actualWeeklyIncome = nightlyReceiptService.getWeeklyIncome();

        // then
        assertEquals(expectedWeeklyIncome, actualWeeklyIncome);
    }

    @Test
    void testGetWeeklyIncomeWithYearAndMonth() {
        // given
        int year = 2024;
        Month month = Month.JULY;

        Map<String, Double> expectedWeeklyIncome = new LinkedHashMap<>();
        expectedWeeklyIncome.put("2024-07-01 - 2024-07-06", 0.0);
        expectedWeeklyIncome.put("2024-07-07 - 2024-07-13", 0.0);
        expectedWeeklyIncome.put("2024-07-14 - 2024-07-20", 0.0);
        expectedWeeklyIncome.put("2024-07-21 - 2024-07-27", 0.0);
        expectedWeeklyIncome.put("2024-07-28 - 2024-07-31", 0.0);
        expectedWeeklyIncome.put(" Saldo pendiente de pago ", 0.0);
        expectedWeeklyIncome.put(" Saldo Pago ", 0.0);
        expectedWeeklyIncome.put(" Total ", 0.0);

        when(nightlyReceiptRepository.findByInitialTimeBetween(any(), any()))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(true)))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(false)))
                .thenReturn(List.of(new NightlyReceipt()));

        // when
        Map<String, Double> actualWeeklyIncome = nightlyReceiptService.getWeeklyIncome(year, month);

        // then
        assertEquals(expectedWeeklyIncome, actualWeeklyIncome);
    }


    @Test
    void testGetBiWeeklyIncome() {
        // given
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int lastDayOfMonthValue = firstDayOfMonth.lengthOfMonth();
        LocalDate middleOfMonth = firstDayOfMonth.plusDays(lastDayOfMonthValue / 2);
        boolean isAfterMiddleOfMonth = currentDate.isAfter(middleOfMonth);
        LocalDate startDate = isAfterMiddleOfMonth ? middleOfMonth.plusDays(1) : firstDayOfMonth;
        LocalDate endDate = isAfterMiddleOfMonth ? firstDayOfMonth.plusMonths(1).minusDays(1) : middleOfMonth;

        Map<String, Double> expectedBiWeeklyIncome = new LinkedHashMap<>();
        expectedBiWeeklyIncome.put("2024-07-17 - 2024-07-23", 0.0);
        expectedBiWeeklyIncome.put("2024-07-24 - 2024-07-30", 0.0);
        expectedBiWeeklyIncome.put("2024-07-31 - 2024-07-31", 0.0);
        expectedBiWeeklyIncome.put("Saldo pendiente de pago ", 0.0);
        expectedBiWeeklyIncome.put("Saldo pago ", 0.0);
        expectedBiWeeklyIncome.put("Total ", 0.0);

        when(nightlyReceiptRepository.findByInitialTimeBetween(any(), any()))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(true)))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(false)))
                .thenReturn(Arrays.asList(new NightlyReceipt()));

        // when
        Map<String, Double> actualBiWeeklyIncome = nightlyReceiptService.getBiWeeklyIncome();

        // then
        assertEquals(expectedBiWeeklyIncome, actualBiWeeklyIncome);
    }

    @Test
    void testGetDailyIncomeForCurrentWeek() {
        // given
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Map<String, Double> expectedDailyIncome = new LinkedHashMap<>();
        expectedDailyIncome.put("2024-07-22", 0.0);
        expectedDailyIncome.put("2024-07-23", 0.0);
        expectedDailyIncome.put("2024-07-24", 0.0);
        expectedDailyIncome.put("2024-07-25", 0.0);
        expectedDailyIncome.put("2024-07-26", 0.0);
        expectedDailyIncome.put("2024-07-27", 0.0);
        expectedDailyIncome.put("2024-07-28", 0.0);
        expectedDailyIncome.put("Saldo pendiente de pago ", 0.0);
        expectedDailyIncome.put("Saldo Pago ", 0.0);
        expectedDailyIncome.put("Total ", 0.0);

        when(nightlyReceiptRepository.findByInitialTimeBetween(any(), any()))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(true)))
                .thenReturn(Arrays.asList(new NightlyReceipt(), new NightlyReceipt()));
        when(nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(any(), any(), eq(false)))
                .thenReturn(Arrays.asList(new NightlyReceipt()));

        // when
        Map<String, Double> actualDailyIncome = nightlyReceiptService.getDailyIncomeForCurrentWeek();

        // then
        assertEquals(expectedDailyIncome, actualDailyIncome);
    }

}