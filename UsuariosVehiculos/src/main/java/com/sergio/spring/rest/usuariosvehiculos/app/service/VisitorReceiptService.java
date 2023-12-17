package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VisitorReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperVisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IRateRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IVisitorReceiptRepository;

@Service
public class VisitorReceiptService implements IVisitorReceiptService {
    @Autowired
    private IVisitorReceiptRepository visitorReceiptRepository;

    @Autowired
    private IRateRepository rateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VisitorReceiptDto> visitorReceiptList() {
        List<VisitorReceipt> visitorReceipts = (List<VisitorReceipt>) visitorReceiptRepository.findAll();
        return visitorReceipts.stream().map(vr -> DtoMapperVisitorReceipt.builder().setVisitorReceipt(vr).build())
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VisitorReceiptDto> findByIdVisitorReceipt(Long visitorReceiptId) {
        return visitorReceiptRepository.findById(visitorReceiptId)
                .map(vr -> DtoMapperVisitorReceipt.builder().setVisitorReceipt(vr).build());
    }

    @Override
    @Transactional
    public VisitorReceiptDto saveVisitorReceipt(VisitorReceipt visitorReceipt) {

        Optional<Rate> rateOptional = rateRepository.findById(visitorReceipt.getRate().getId());
        if (rateOptional.isEmpty()) {
            throw new IllegalArgumentException("Rate not found");
        }

        String plateUpper = visitorReceipt.getPlate().toUpperCase();
        visitorReceipt.setPlate(plateUpper);

        visitorReceipt.setRate(rateOptional.get());

        visitorReceipt.setIssueDate(LocalDate.now());

        LocalDate dueDate = LocalDate.now().plusDays(1);
        visitorReceipt.setDueDate(dueDate);

        visitorReceipt.setPaymentStatus(false);

        VisitorReceipt savedVisitorReceipt = visitorReceiptRepository.save(visitorReceipt);
        return DtoMapperVisitorReceipt.builder().setVisitorReceipt(savedVisitorReceipt).build();

    }

    @Override
    @Transactional
    public Optional<VisitorReceiptDto> updateVisitorReceipt(VisitorReceipt visitorReceipt, Long visitorReceiptId) {
        Optional<VisitorReceipt> vr = visitorReceiptRepository.findById(visitorReceiptId);
        VisitorReceipt visitorReceiptOptional = null;
        if (vr.isPresent()) {
            VisitorReceipt visitorReceiptDb = vr.orElseThrow();
            String plateUpper = visitorReceipt.getPlate().toUpperCase();
            visitorReceiptDb.setPlate(plateUpper);

            if (visitorReceipt.getIssueDate() == null) {
                LocalDate updateIssueDate = LocalDate.now();
                visitorReceiptDb.setIssueDate(updateIssueDate);
            } else {
                visitorReceiptDb.setIssueDate(visitorReceipt.getIssueDate());
            }

            if (visitorReceipt.getDueDate() == null) {
                LocalDate updatdeDueDate = LocalDate.now().plusDays(1);
                visitorReceiptDb.setDueDate(updatdeDueDate);
            } else {
                visitorReceiptDb.setDueDate(visitorReceipt.getDueDate());
            }
            visitorReceiptDb.setPaymentStatus(visitorReceipt.isPaymentStatus());
            visitorReceiptDb.setRate(visitorReceipt.getRate());
            visitorReceiptOptional = visitorReceiptRepository.save(visitorReceiptDb);
        }
        return Optional.ofNullable(DtoMapperVisitorReceipt.builder()
                .setVisitorReceipt(visitorReceiptOptional).build());

    }

    @Override
    @Transactional
    public void changePaymentStatus(Long visitorReceiptId) {
        Optional<VisitorReceipt> visitorReceiptOptional = visitorReceiptRepository.findById(visitorReceiptId);
        if (visitorReceiptOptional.isPresent()) {
            VisitorReceipt visitorReceipt = visitorReceiptOptional.get();
            visitorReceipt.setPaymentStatus(!visitorReceipt.isPaymentStatus());
            visitorReceiptRepository.save(visitorReceipt);

        }

    }

    @Override
    @Transactional
    public void removeVisitorReceipt(Long visitorReceiptId) {
        visitorReceiptRepository.deleteById(visitorReceiptId);
    }
}
