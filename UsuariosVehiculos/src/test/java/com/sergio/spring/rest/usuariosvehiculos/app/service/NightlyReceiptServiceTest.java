package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.INightlyReceiptRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NightlyReceiptServiceTest {

    @MockBean
    INightlyReceiptRepository nightlyReceiptRepository;

    @Autowired
    INightlyReceiptService nightlyReceiptService;

    @Test
    void nightlyReceiptList() {

    }

    @Test
    void findByIdNightlyReceipt() {
    }

    @Test
    void getTotalUnpaidNightlyReceipts() {
    }

    @Test
    void getTotalPaidNightlyReceipts() {
    }

    @Test
    void getTotalNightlyReceipts() {
    }

    @Test
    void getNightlyReceiptsByUserId() {
    }

    @Test
    void findByIdReceiptWithDetails() {
    }

    @Test
    void saveReceipt() {
    }

    @Test
    void updateNightlyReceipt() {
    }

    @Test
    void changePaymentStatus() {
    }

    @Test
    void remove() {
    }

    @Test
    void getWeeklyIncome() {
    }

    @Test
    void testGetWeeklyIncome() {
    }

    @Test
    void getBiWeeklyIncome() {
    }

    @Test
    void getDailyIncomeForCurrentWeek() {
    }
}