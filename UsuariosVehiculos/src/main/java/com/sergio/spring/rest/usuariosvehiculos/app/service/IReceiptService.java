package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;

public interface IReceiptService {
    List<ReceiptDto> receiptList();

    List<ReceiptDto> getUnpaidReceipts();

    List<ReceiptDto> getPaidReceipts();

    List<ReceiptDto> getReceiptsByUserId(Long userId);

    List<ReceiptDto> getUnpaidReceiptsByUser(User user);

    long getTotalUnpaidReceipts();

    long getTotalPaidReceipts();

    long getTotalReceipts();

    Optional<ReceiptDto> findByIdReceipt(Long receiptId);

    //Consulta optimizada qr
    Optional<Receipt> findByIdReceiptWithDetails(Long receiptId);

    ReceiptDto saveReceipt(Receipt receipt);

    Optional<ReceiptDto> updateReceipt(ReceiptRequest receipt, Long receiptId);

    void changePaymentStatus(Long receiptId);

    void remove(Long receiptId);

    //Reportes
    //Mes
    Map<String, Double> getWeeklyIncome();

    Map<String, Double> getWeeklyIncome(int year, Month month);

    //Quincena
    Map<String, Double> getBiWeeklyIncome();

    //Semana
    Map<String, Double> getDailyIncomeForCurrentWeek();


}
