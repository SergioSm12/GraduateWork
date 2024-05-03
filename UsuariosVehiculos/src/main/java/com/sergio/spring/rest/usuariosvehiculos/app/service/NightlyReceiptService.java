package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.NightlyReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperNightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.*;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.NightlyReceiptRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.INightlyReceiptRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IRateRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IUserRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NightlyReceiptService implements INightlyReceiptService {

    @Autowired
    private INightlyReceiptRepository nightlyReceiptRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IVehicleRepository vehicleRepository;

    @Autowired
    private IRateRepository rateRepository;


    //obtener recibos
    @Override
    @Transactional(readOnly = true)
    public List<NightlyReceiptDto> nightlyReceiptList() {
        List<NightlyReceipt> nightlyReceipts = (List<NightlyReceipt>) nightlyReceiptRepository.findAll();
        return nightlyReceipts.stream().map(nr -> DtoMapperNightlyReceipt.builder().setNightlyReceipt(nr).build()).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<NightlyReceiptDto> findByIdNightlyReceipt(Long nightlyReceiptId) {
        return nightlyReceiptRepository.findById(nightlyReceiptId).map(nr -> DtoMapperNightlyReceipt.builder().setNightlyReceipt(nr).build());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUnpaidNightlyReceipts() {
        return nightlyReceiptRepository.countByPaymentStatusFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPaidNightlyReceipts() {
        return nightlyReceiptRepository.countByPaymentStatusTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalNightlyReceipts() {
        return nightlyReceiptRepository.count();
    }


    // Obtener recibos por usuario
    @Override
    @Transactional(readOnly = true)
    public List<NightlyReceiptDto> getNightlyReceiptsByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = userOptional.get();
        List<NightlyReceipt> receipts = nightlyReceiptRepository.findByUser(user);

        return receipts.stream().map(r -> DtoMapperNightlyReceipt.builder().setNightlyReceipt(r).build())
                .collect(Collectors.toList());
    }

    //servicio optimizado qr
    @Override
    @Transactional(readOnly = true)
    public Optional<NightlyReceipt> findByIdReceiptWithDetails(Long receiptId) {
        return nightlyReceiptRepository.findByIdWithDetails(receiptId);
    }

    @Override
    @Transactional
    public NightlyReceiptDto saveReceipt(NightlyReceipt nightlyReceipt) {
        // Obtener user y asociarlo
        Optional<User> userOptional = userRepository.findById(nightlyReceipt.getUser().getId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(nightlyReceipt.getVehicle().getId());
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
        Optional<Rate> rateOptional = rateRepository.findById(nightlyReceipt.getRate().getId());
        if (rateOptional.isEmpty()) {
            throw new IllegalArgumentException("Rate not found");
        }

        nightlyReceipt.setUser(userOptional.get());
        nightlyReceipt.setVehicle(vehicleOptional.get());
        //Agregar hora de inicio por defecto.
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime initialTime = currentDateTime.withHour(22).withMinute(0).withSecond(0).withNano(0);
        nightlyReceipt.setInitialTime(initialTime);
        // Calcular departureTime por defecto
        LocalDateTime departureTime = initialTime.plusHours(9);
        nightlyReceipt.setDepartureTime(departureTime);
        nightlyReceipt.setRate(rateOptional.get());
        // obtener datos de la tarifa
        String rateTime = rateOptional.get().getTime();
        double rateAmount = rateOptional.get().getAmount();
        //Calcular monto dependiendo de la tarifa y hora
        double amount = calculateAmount(initialTime, departureTime, rateTime, rateAmount);
        nightlyReceipt.setAmount(amount);
        nightlyReceipt.setPaymentStatus(false);

        NightlyReceipt savedNightlyReceipt = nightlyReceiptRepository.save(nightlyReceipt);

        return DtoMapperNightlyReceipt.builder().setNightlyReceipt(savedNightlyReceipt).build();
    }

    @Override
    @Transactional
    public Optional<NightlyReceiptDto> updateNightlyReceipt(NightlyReceiptRequest nightlyReceiptRequest, Long nightlyReceiptId) {
        Optional<NightlyReceipt> o = nightlyReceiptRepository.findById(nightlyReceiptId);
        NightlyReceipt nightlyReceiptOptional = null;
        if (o.isPresent()) {
            NightlyReceipt nightlyReceiptdb = o.orElseThrow();

            //Establecer initialTime
            nightlyReceiptdb.setInitialTime(nightlyReceiptRequest.getInitialTime());
            nightlyReceiptdb.setDepartureTime(nightlyReceiptRequest.getDepartureTime());
            Optional<Rate> rateOptional = rateRepository.findById(nightlyReceiptRequest.getRate().getId());
            if (rateOptional.isEmpty()) {
                throw new IllegalArgumentException("Rate not found");
            }
            nightlyReceiptdb.setRate(rateOptional.get());
            //obtener datos de la tarifa.
            String rateTime = rateOptional.get().getTime();
            double rateAmount = rateOptional.get().getAmount();

            double amount = calculateAmount(nightlyReceiptRequest.getInitialTime(), nightlyReceiptRequest.getDepartureTime(), rateTime, rateAmount);
            nightlyReceiptdb.setAmount(amount);


            nightlyReceiptdb.setPaymentStatus(nightlyReceiptRequest.isPaymentStatus());


            nightlyReceiptOptional = nightlyReceiptRepository.save(nightlyReceiptdb);
        }

        return Optional.ofNullable(DtoMapperNightlyReceipt.builder().setNightlyReceipt(nightlyReceiptOptional).build());
    }

    @Override
    @Transactional
    public void changePaymentStatus(Long receiptId) {
        Optional<NightlyReceipt> nightlyReceiptOptional = nightlyReceiptRepository.findById(receiptId);
        if (nightlyReceiptOptional.isPresent()) {
            NightlyReceipt nightlyReceipt = nightlyReceiptOptional.get();
            nightlyReceipt.setPaymentStatus(!nightlyReceipt.isPaymentStatus());
            nightlyReceiptRepository.save(nightlyReceipt);
        }
    }

    @Override
    @Transactional
    public void remove(Long nightlyReceiptId) {
        nightlyReceiptRepository.deleteById(nightlyReceiptId);
    }

    public double calculateAmount(LocalDateTime initialTime, LocalDateTime departureTime, String rateTime, double rateAmount) {
        //calcular las horas
        long hours = ChronoUnit.HOURS.between(initialTime, departureTime);
        //Verificar si el monto es de hora nocturna tener en cuenta que del front debe venir igual.
        if (rateTime.equalsIgnoreCase("hora moto") || rateTime.equalsIgnoreCase("hora carro")) {
            return rateAmount * hours;
        } else {
            throw new IllegalArgumentException("Tarifa no valida para calculo de monto nocturno.");
        }

    }


}
