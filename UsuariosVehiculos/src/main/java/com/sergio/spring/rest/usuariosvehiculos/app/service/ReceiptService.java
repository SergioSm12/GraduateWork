package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.time.*;

import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    //servicio optimisado qr
    @Override
    @Transactional(readOnly = true)
    public Optional<Receipt> findByIdReceiptWithDetails(Long receiptId) {
        return receiptRepository.findByIdWithDetails(receiptId);
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
        List<Receipt> receipts = receiptRepository.findByUserOrderByIssueDateDesc(user);

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
        receipt.setIssueDate(LocalDateTime.now());
        // Calcular dueDate segunRateTime
        // FECHA ACTUAL
        LocalDateTime currentDateTime = LocalDateTime.now();
        // VARIABLE
        String rateTime = rateOptional.get().getTime();
        // CALCULAR
        LocalDateTime dueDate = calculateDueDate(rateTime, currentDateTime);
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

            //Establecer IssueDate
            LocalDate issueLocalDate = receiptRequest.getIssueDate().toLocalDate();
            LocalTime startTime = LocalTime.of(7, 0);
            LocalDateTime issueDateTime = LocalDateTime.of(issueLocalDate, startTime);
            receiptDb.setIssueDate(issueDateTime);


            LocalDate localDateDue = receiptRequest.getDueDate().toLocalDate();
            LocalTime endTime = LocalTime.of(22, 0);
            LocalDateTime dueDateTime = LocalDateTime.of(localDateDue, endTime);
            receiptDb.setDueDate(dueDateTime);


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


    //Reportes
    //Reporte mensual

    @Override
    @Transactional(readOnly = false)
    public Map<String, Double> getWeeklyIncome() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        Month month = currentDate.getMonth();

        //Llamar el metodo principal
        return getWeeklyIncome(year, month);

    }

    @Override
    @Transactional(readOnly = false)
    public Map<String, Double> getWeeklyIncome(int year, Month month) {
        //mapa ordenado
        Map<String, Double> weeklyIncome = new LinkedHashMap<>();

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        LocalDate lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);


        //recorrer las semanas
        LocalDate startDate = firstDayOfMonth;
        LocalDate endDate;

        Double totalIncomePaidSum = calculateIncomeBetweenDatesAndPaymentStatus(startDate.atStartOfDay(), lastDayOfMonth.atTime(23, 59, 59), true);
        Double totalIncomeUnpaidSum = calculateIncomeBetweenDatesAndPaymentStatus(startDate.atStartOfDay(), lastDayOfMonth.atTime(23, 59, 59), false);
        double totalIncomeSum = 0.0;
        while (startDate.isBefore(lastDayOfMonth) || startDate.equals(lastDayOfMonth)) {
            //calcular el ultimo dia de la semana en este caso sabado
            endDate = startDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
            if (endDate.isAfter(lastDayOfMonth)) {
                endDate = lastDayOfMonth;// ajustar ultimo dia en caso de que no sea sabado
            }
            Double totalIncome = calculateIncomeBetweenDates(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
            weeklyIncome.put(startDate.toString() + " - " + endDate.toString(), totalIncome);


            totalIncomeSum += totalIncome;
            startDate = endDate.plusDays(1);
        }


        weeklyIncome.put(" Saldo pendiente de pago ", totalIncomeUnpaidSum);
        weeklyIncome.put(" Saldo Pago ", totalIncomePaidSum);
        weeklyIncome.put(" Total ", totalIncomeSum);
        return weeklyIncome;

    }

    //Reporte quincenal actual
    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> getBiWeeklyIncome() {
        Map<String, Double> biWeeklyIncome = new LinkedHashMap<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int lastDayOfMonthValue = firstDayOfMonth.lengthOfMonth();


        //calcular la mitad del mes
        LocalDate middleOfMonth = firstDayOfMonth.plusDays(lastDayOfMonthValue / 2);

        //Determinar si el dia actual esta antes o despues de la mitad del mes
        boolean isAfterMiddleOfMonth = currentDate.isAfter(middleOfMonth);

        //Definir fechas de inicio de y fin para el reporte quincenal
        LocalDate startDate;
        LocalDate endDate;
        if (isAfterMiddleOfMonth) {
            startDate = middleOfMonth.plusDays(1);
            endDate = firstDayOfMonth.plusMonths(1).minusDays(1);
        } else {
            startDate = firstDayOfMonth;
            endDate = middleOfMonth;
        }


        double totalIncomeSumPaid = calculateIncomeBetweenDatesAndPaymentStatus(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), true);
        double totalIncomeSumUnpaid = calculateIncomeBetweenDatesAndPaymentStatus(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), false);

        double totalIncomeSum = 0.0;
        while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
            LocalDate endOfPeriod = startDate.plusDays(6);

            //Ajustar el final del periodo si excede el dinal del mes
            if (endOfPeriod.isAfter(endDate)) {
                endOfPeriod = endDate;
            }

            Double totalIncome = calculateIncomeBetweenDates(startDate.atStartOfDay(), endOfPeriod.atTime(23, 59, 59));

            biWeeklyIncome.put(startDate.toString() + " - " + endOfPeriod.toString(), totalIncome);
            totalIncomeSum += totalIncome;
            startDate = endOfPeriod.plusDays(1);
        }

        biWeeklyIncome.put("Saldo pendiente de pago ", totalIncomeSumUnpaid);
        biWeeklyIncome.put("Saldo pago ", totalIncomeSumPaid);
        biWeeklyIncome.put("Total ", totalIncomeSum);
        return biWeeklyIncome;
    }

    //Reporte Semana actual
    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> getDailyIncomeForCurrentWeek() {
        Map<String, Double> dailyIncome = new LinkedHashMap<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));


        LocalDate currentDateOfWeek = firstDayOfWeek;
        double totalIncomePaidSum = calculateIncomeBetweenDatesAndPaymentStatus(firstDayOfWeek.atStartOfDay(), lastDayOfWeek.atTime(23, 59, 59), true);
        double totalIncomeUnpaidSum = calculateIncomeBetweenDatesAndPaymentStatus(firstDayOfWeek.atStartOfDay(), lastDayOfWeek.atTime(23, 59, 59), false);

        double totalIncomeSum = 0.0;
        while (!currentDateOfWeek.isAfter(lastDayOfWeek)) {
            Double totalIncome = calculateIncomeBetweenDates(currentDateOfWeek.atStartOfDay(), currentDateOfWeek.atTime(23, 59, 59));
            dailyIncome.put(currentDateOfWeek.toString(), totalIncome);

            totalIncomeSum += totalIncome;
            //avanzar al siguiente dia
            currentDateOfWeek = currentDateOfWeek.plusDays(1);
        }
        dailyIncome.put("Saldo pendiente de pago ", totalIncomeUnpaidSum);
        dailyIncome.put("Saldo Pago ", totalIncomePaidSum);
        dailyIncome.put("Total ", totalIncomeSum);
        return dailyIncome;
    }

    //calcular ingreso entre dos fechas
    private Double calculateIncomeBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Receipt> receipts = receiptRepository.findByIssueDateBetween(startDate, endDate);
        return receipts.stream()
                .mapToDouble(receipt -> receipt.getRate().getAmount())
                .sum();
    }

    //Calcular ingreso entre dos fechas y estado de pago
    private Double calculateIncomeBetweenDatesAndPaymentStatus(LocalDateTime startDate, LocalDateTime endDate, boolean paymentStatus) {
        List<Receipt> receipts = receiptRepository.findByIssueDateBetweenAndPaymentStatus(startDate, endDate, paymentStatus);
        return receipts.stream()
                .mapToDouble(receipt -> receipt.getRate().getAmount())
                .sum();
    }

    //calcular fecha de vencimiento
    private LocalDateTime calculateDueDate(String rateTime, LocalDateTime currentDateTime) {
        // casos segun rateTime y calculo
        rateTime = rateTime.replaceAll("\\s", "");
        LocalTime limitTime = LocalTime.of(22, 0); //definir hora limite
        return switch (rateTime.toUpperCase()) {
            case "DIAMOTO", "MOTODIA", "DIACARRO", "CARRODIA", "DIABICICLETA", "BICICLETADIA" ->
                    currentDateTime.isBefore(currentDateTime.toLocalDate().atTime(limitTime)) ?
                            currentDateTime.withHour(limitTime.getHour()).withMinute(limitTime.getMinute())
                            : currentDateTime.plusDays(1).withHour(limitTime.getHour()).withMinute(limitTime.getMinute());
            case "MESMOTO", "MOTOMES", "MESCARRO", "CARROMES", "MESBICICLETA", "BICICLETAMES" ->
                    currentDateTime.plusMonths(1).withHour(limitTime.getHour()).withMinute(limitTime.getMinute());
            default -> null;
        };
    }

}
