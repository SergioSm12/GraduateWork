package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.util.List;
import java.util.Optional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VisitorReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;

public interface IVisitorReceiptService {
    List<VisitorReceiptDto> visitorReceiptList();

    Optional<VisitorReceiptDto> findByIdVisitorReceipt(Long visitorReceiptId);

    VisitorReceiptDto saveVisitorReceipt(VisitorReceipt visitorReceipt);

    Optional<VisitorReceiptDto> updateVisitorReceipt(VisitorReceipt visitorReceipt, Long visitorReceiptId);

    void changePaymentStatus(Long visitorReceiptId);

    void removeVisitorReceipt(Long visitorReceiptId);
}