package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.NightlyReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface INightlyReceiptService {
    List<NightlyReceiptDto> nightlyReceiptList();

    NightlyReceiptDto saveReceipt(NightlyReceipt nightlyReceipt);

    void changePaymentStatus(Long receiptId);

    Optional<NightlyReceiptDto> findByIdNightlyReceipt(Long nightlyReceiptId);

    Optional<NightlyReceipt> findByIdReceiptWithDetails(Long receiptId);

    List<NightlyReceiptDto> getNightlyReceiptsByUserId(Long userId);

    long getTotalUnpaidNightlyReceipts();

    long getTotalPaidNightlyReceipts();

    long getTotalNightlyReceipts();

    void remove(Long nightlyReceiptId);
}
