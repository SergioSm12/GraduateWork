package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VisitorReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperVisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IRateRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVisitorReceiptRepository;

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
    @Transactional(readOnly = true)
    public Optional<VisitorReceipt> findByIdVisitorReceiptDetails(Long visitorReceiptId) {
        return visitorReceiptRepository.findByIdWithDetails(visitorReceiptId);
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
        visitorReceipt.setIssueDate(LocalDateTime.now());

        LocalDateTime dueDate = LocalDateTime.now().withHour(22).withMinute(0).withSecond(0).withNano(0);
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
            visitorReceiptDb.setIssueDate(visitorReceipt.getIssueDate());
            visitorReceiptDb.setDueDate(visitorReceipt.getDueDate());
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
        List<VisitorReceipt> visitorReceipts = visitorReceiptRepository.findByIssueDateBetween(startDate, endDate);
        return visitorReceipts.stream()
                .mapToDouble(receipt -> receipt.getRate().getAmount())
                .sum();
    }

    //Calcular ingreso entre dos fechas y estado de pago
    private Double calculateIncomeBetweenDatesAndPaymentStatus(LocalDateTime startDate, LocalDateTime endDate, boolean paymentStatus) {
        List<VisitorReceipt> visitorReceipts = visitorReceiptRepository.findByIssueDateBetweenAndPaymentStatus(startDate, endDate, paymentStatus);
        return visitorReceipts.stream()
                .mapToDouble(receipt -> receipt.getRate().getAmount())
                .sum();
    }

    //contar recibos no pagos
    @Override
    @Transactional(readOnly = true)
    public long getTotalVisitorUnpaidReceipts() {
        return visitorReceiptRepository.countByPaymentStatusFalse();
    }

    //contar recibos pagos
    @Override
    @Transactional(readOnly = true)
    public long getTotalVisitorPaidReceipts() {
        return visitorReceiptRepository.countByPaymentStatusTrue();
    }

    //devolver el total de recibos visitante
    @Override
    @Transactional(readOnly = true)
    public long getTotalVisitorReceipt() {
        return visitorReceiptRepository.count();
    }
}
