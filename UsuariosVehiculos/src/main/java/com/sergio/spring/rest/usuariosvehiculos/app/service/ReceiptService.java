package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.ReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.ReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IRateRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IReceiptRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IUserRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReceiptService implements IReceiptService {
    @Autowired
    private IReceiptRepository receiptRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IVehicleRepository vehicleRepository;

    //Rate
    @Autowired
    private IRateRepository rateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> receiptList() {
        List<Receipt> receipts = (List<Receipt>) receiptRepository.findAll();
        return receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReceiptDto> findByIdReceipt(Long receiptId) {
        return receiptRepository.findById(receiptId).map(r -> DtoMapperReceipt.builder().setReceipt(r).build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> getUnpaidReceipts() {
        List<Receipt> unpaidReceipts = receiptRepository.findByPaymentStatusFalse();
        return unpaidReceipts.stream().map(ru -> DtoMapperReceipt.builder().setReceipt(ru).build()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> getPaidReceipts() {
        List<Receipt> paidReceipts = receiptRepository.findByPaymentStatusTrue();
        return paidReceipts.stream().map(rp -> DtoMapperReceipt.builder().setReceipt(rp).build()).collect(Collectors.toList());
    }

    //Obtener recibos por usuario
    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> getReceiptsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        List<Receipt> receipts = receiptRepository.findByUser(user);

        return receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build()).collect(Collectors.toList());
    }

    //Obtener recibos pendientes de pago por user
    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> getUnpaidReceiptsByUser(User user) {
        return receiptRepository.findByUserAndPaymentStatus(user, false)
                .stream()
                .map(r -> DtoMapperReceipt.builder().setReceipt(r).build())
                .collect(Collectors.toList());
    }

    //Receipt
    //save Receipt
    @Override
    @Transactional
    public ReceiptDto saveReceipt(Receipt receipt) {
        //Obtener user y asociarlo
        Optional<User> userOptional = userRepository.findById(receipt.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(receipt.getVehicle().getId());
        if (vehicleOptional.isEmpty()) {
            throw new IllegalArgumentException("Vehicle not found");
        }
        //Validacion para que solo se permita crear cuando el vehiculo se encuentra en los vehiculos del user
        User user = userOptional.get();
        Vehicle vehicle = vehicleOptional.get();
        if (!user.getVehicles().contains(vehicle)) {
            throw new IllegalArgumentException("Vehicle is not associated with the user");
        }


        Optional<Rate> rateOptional = rateRepository.findById(receipt.getRate().getId());
        if (rateOptional.isEmpty()) {
            throw new IllegalArgumentException("Rate not found");
        }

        receipt.setUser(userOptional.get());
        receipt.setVehicle(vehicleOptional.get());
        receipt.setRate(rateOptional.get());
        receipt.setIssueDate(LocalDate.now());
        receipt.setDueDate(null);
        receipt.setPaymentStatus(false);

        Receipt savedReceipt = receiptRepository.save(receipt);

        return DtoMapperReceipt.builder().setReceipt(savedReceipt).build();
    }

    @Override
    @Transactional
    public Optional<ReceiptDto> updateReceipt(ReceiptRequest receipt, Long receiptId) {
        Optional<Receipt> o = receiptRepository.findById(receiptId);
        Receipt receiptOptional = null;
        if (o.isPresent()) {
            Receipt receiptDb = o.orElseThrow();
            receiptDb.setDueDate(receipt.getDueDate());
            receiptDb.setPaymentStatus(receipt.isPaymentStatus());
            receiptOptional = receiptRepository.save(receiptDb);
        }
        return Optional.ofNullable(DtoMapperReceipt.builder().setReceipt(receiptOptional).build());
    }


    @Override
    @Transactional
    public void remove(Long receiptId) {
        receiptRepository.deleteById(receiptId);
    }


}
