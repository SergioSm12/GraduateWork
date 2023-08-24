package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface IReceiptService {
    List<ReceiptDto> receiptList();

    List<ReceiptDto> getUnpaidReceipts();

    List<ReceiptDto> getPaidReceipts();
    List<ReceiptDto> getReceiptsByUserId(Long userId);
    List<ReceiptDto> getUnpaidReceiptsByUser(User user);

    Optional<ReceiptDto> findByIdReceipt(Long receiptId);

    ReceiptDto saveReceipt(Receipt receipt);

    Optional<ReceiptDto> updateReceipt(ReceiptRequest receipt, Long receiptId);

    public void remove(Long receiptId);

}
