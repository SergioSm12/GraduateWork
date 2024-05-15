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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        List<NightlyReceipt> nightlyReceipts = nightlyReceiptRepository.findByInitialTimeBetween(startDate, endDate);
        return nightlyReceipts.stream()
                .mapToDouble(receipt -> receipt.getAmount())
                .sum();
    }

    //Calcular ingreso entre dos fechas y estado de pago
    private Double calculateIncomeBetweenDatesAndPaymentStatus(LocalDateTime startDate, LocalDateTime endDate, boolean paymentStatus) {
        List<NightlyReceipt> nightlyReceipts = nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(startDate, endDate, paymentStatus);
        return nightlyReceipts.stream()
                .mapToDouble(receipt -> receipt.getAmount())
                .sum();
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
