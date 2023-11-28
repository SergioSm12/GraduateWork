package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class ReceiptService implements IReceiptService {
    @Autowired
    private IReceiptRepository receiptRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IVehicleRepository vehicleRepository;

    // Rate
    @Autowired
    private IRateRepository rateRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> receiptList() {
        List<Receipt> receipts = (List<Receipt>) receiptRepository.findAllByOrderByIssueDateDesc();
        return receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build())
                .collect(Collectors.toList());
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
        return unpaidReceipts.stream().map(ru -> DtoMapperReceipt.builder().setReceipt(ru).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> getPaidReceipts() {
        List<Receipt> paidReceipts = receiptRepository.findByPaymentStatusTrue();
        return paidReceipts.stream().map(rp -> DtoMapperReceipt.builder().setReceipt(rp).build())
                .collect(Collectors.toList());
    }

    // Obtener recibos por usuario
    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> getReceiptsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        List<Receipt> receipts = receiptRepository.findByUser(user);

        return receipts.stream().map(r -> DtoMapperReceipt.builder().setReceipt(r).build())
                .collect(Collectors.toList());
    }

    // Obtener recibos pendientes de pago por user
    @Override
    @Transactional(readOnly = true)
    public List<ReceiptDto> getUnpaidReceiptsByUser(User user) {
        return receiptRepository.findByUserAndPaymentStatus(user, false)
                .stream()
                .map(r -> DtoMapperReceipt.builder().setReceipt(r).build())
                .collect(Collectors.toList());
    }

    // Servicio para contar recibos no pagos
    @Override
    @Transactional(readOnly = true)
    public long getTotalUnpaidReceipts() {
        return receiptRepository.countByPaymentStatusFalse();
    }

    // Servicio para contar recibos pagos
    @Override
    @Transactional(readOnly = true)
    public long getTotalPaidReceipts() {
        return receiptRepository.countByPaymentStatusTrue();
    }

    // Servicio para devolver el total de recibos:
    @Override
    @Transactional(readOnly = true)
    public long getTotalReceipts() {
        return receiptRepository.count();
    }

    // Receipt
    // save Receipt
    @Override
    @Transactional
    public ReceiptDto saveReceipt(Receipt receipt) {
        // Obtener user y asociarlo
        Optional<User> userOptional = userRepository.findById(receipt.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(receipt.getVehicle().getId());
        if (vehicleOptional.isEmpty()) {
            throw new IllegalArgumentException("Vehicle not found");
        }
        // Validacion para que solo se permita crear cuando el vehiculo se encuentra en
        // los vehiculos del user
        User user = userOptional.get();
        Vehicle vehicle = vehicleOptional.get();
        if (!user.getVehicles().contains(vehicle)) {
            throw new IllegalArgumentException("Vehicle is not associated with the user");
        }

        // Obtiene el rate asociado
        Optional<Rate> rateOptional = rateRepository.findById(receipt.getRate().getId());
        if (rateOptional.isEmpty()) {
            throw new IllegalArgumentException("Rate not found");
        }

        receipt.setUser(userOptional.get());
        receipt.setVehicle(vehicleOptional.get());
        receipt.setRate(rateOptional.get());
        receipt.setIssueDate(LocalDate.now());
        // Calcular dueDate segunRateTime
        // FECHA ACTUAL
        LocalDate currentDateTime = LocalDate.now();
        // VARIABLE
        String rateTime = rateOptional.get().getTime();
        // CALCULAR
        LocalDate dueDate = calculateDueDate(rateTime, currentDateTime);
        receipt.setDueDate(dueDate);

        receipt.setPaymentStatus(false);

        Receipt savedReceipt = receiptRepository.save(receipt);

        return DtoMapperReceipt.builder().setReceipt(savedReceipt).build();
    }

    @Override
    @Transactional
    public Optional<ReceiptDto> updateReceipt(ReceiptRequest receiptRequest, Long receiptId) {
        Optional<Receipt> o = receiptRepository.findById(receiptId);
        Receipt receiptOptional = null;
        if (o.isPresent()) {
            Receipt receiptDb = o.orElseThrow();

            receiptDb.setIssueDate(receiptRequest.getIssueDate());
            receiptDb.setDueDate(receiptRequest.getDueDate());
            receiptDb.setPaymentStatus(receiptRequest.isPaymentStatus());
            receiptDb.setRate(receiptRequest.getRate());

            receiptOptional = receiptRepository.save(receiptDb);
        }
        return Optional.ofNullable(DtoMapperReceipt.builder().setReceipt(receiptOptional).build());
    }

    // Cambia estado de pago
    @Override
    @Transactional
    public void changePaymentStatus(Long receiptId) {
        Optional<Receipt> receiptOptional = receiptRepository.findById(receiptId);
        if (receiptOptional.isPresent()) {
            Receipt receipt = receiptOptional.get();
            receipt.setPaymentStatus(!receipt.isPaymentStatus());
            receiptRepository.save(receipt);
        }
    }

    @Override
    @Transactional
    public void remove(Long receiptId) {
        receiptRepository.deleteById(receiptId);
    }

    private LocalDate calculateDueDate(String rateTime, LocalDate currentDate) {
        // casos segun rateTime y calculo
        rateTime = rateTime.replaceAll("\\s", "");
        return switch (rateTime.toUpperCase()) {
            case "DIAMOTO", "MOTODIA", "DIACARRO", "CARRODIA", "DIABICICLETA", "BICICLETADIA" ->
                currentDate.plusDays(1);
            case "SEMANAMOTO", "MOTOSEMANA", "SEMANACARRO", "CARROSEMANA", "SEMANABICICLETA", "BICICLETASEMANA" ->
                currentDate.plusWeeks(1);
            case "QUINCENAMOTO", "MOTOQUINCENA", "QUINCENACARRO", "CARROQUINCENA", "QUINCENABICICLETA",
                    "BICICLETAQUINCENA" ->
                currentDate.plus(2, ChronoUnit.WEEKS);
            case "MESMOTO", "MOTOMES", "MESCARRO", "CARROMES", "MESBICICLETA", "BICICLETAMES" ->
                currentDate.plusMonths(1);
            default -> null;
        };
    }

}
