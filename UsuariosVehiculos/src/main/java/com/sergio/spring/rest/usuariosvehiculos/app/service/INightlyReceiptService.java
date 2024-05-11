package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.NightlyReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.NightlyReceiptRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface INightlyReceiptService {
    List<NightlyReceiptDto> nightlyReceiptList();

    NightlyReceiptDto saveReceipt(NightlyReceipt nightlyReceipt);

    Optional<NightlyReceiptDto> updateNightlyReceipt(NightlyReceiptRequest nightlyReceiptRequest, Long nightlyReceiptId);

    void changePaymentStatus(Long receiptId);

    Optional<NightlyReceiptDto> findByIdNightlyReceipt(Long nightlyReceiptId);

    Optional<NightlyReceipt> findByIdReceiptWithDetails(Long receiptId);

    List<NightlyReceiptDto> getNightlyReceiptsByUserId(Long userId);

    long getTotalUnpaidNightlyReceipts();

    long getTotalPaidNightlyReceipts();

    long getTotalNightlyReceipts();

    void remove(Long nightlyReceiptId);

    //Reportes
    Map<String, Double> getWeeklyIncome();

    Map<String, Double> getWeeklyIncome(int year, Month month);

    Map<String, Double> getBiWeeklyIncome();

    Map<String, Double> getDailyIncomeForCurrentWeek();
}
