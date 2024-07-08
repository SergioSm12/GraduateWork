package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.INightlyReceiptRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IReceiptRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IVisitorReceiptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsUnifiedService implements IReportsUnifiedService {

    @Autowired
    private IReceiptRepository receiptRepository;

    @Autowired
    private IVisitorReceiptRepository visitorReceiptRepository;

    @Autowired
    private INightlyReceiptRepository nightlyReceiptRepository;

    //Reporte de ingresos mensual

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> generateMonthlyIncomeReport(){
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        Month month = currentDate.getMonth();

        return  generateMonthlyIncomeReport(year,month);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> generateMonthlyIncomeReport(int year, Month month) {
        Map<String, Double> incomeReport = new LinkedHashMap<>();

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        double receiptIncome = calculateReceiptIncome(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        double visitorReceiptIncome = calculateVisitorReceiptIncome(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        double nightlyReceiptIncome = calculateNightlyReceiptIncome(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        //pendinte y pago
        double pendingPayment = calculateIncomePaymentStatus(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), false);
        double paidAmount = calculateIncomePaymentStatus(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), true);

        //total
        double totalIncome = receiptIncome + visitorReceiptIncome + nightlyReceiptIncome;

        //Construir reporte
        incomeReport.put("Recibos diurnos " + startDate.toString() + " - " + endDate.toString(), receiptIncome);
        incomeReport.put("Recibos nocturnos " + startDate.toString() + " - " + endDate.toString(), nightlyReceiptIncome);
        incomeReport.put("Recibos visitantes " + startDate.toString() + " - " + endDate.toString(), visitorReceiptIncome);

        incomeReport.put("Saldo pendiente de pago ", pendingPayment);
        incomeReport.put("Saldo Pago ", paidAmount);
        incomeReport.put("Total ", totalIncome);

        return incomeReport;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> biWeeklyIncomeUnified() {
        Map<String, Double> biWeeklyIncome = new LinkedHashMap<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int lastDayOfMonthValue = firstDayOfMonth.lengthOfMonth();

        LocalDate middleOfMonth = firstDayOfMonth.plusDays(lastDayOfMonthValue / 2);

        //Determinar si el dia actual esta antes o despues de la mitad del mes
        boolean isAfterMiddleOfMonth = currentDate.isAfter(middleOfMonth);

        //Definir fechas de inicio y fin
        LocalDate startDate;
        LocalDate endDate;
        if (isAfterMiddleOfMonth) {
            startDate = middleOfMonth.plusDays(1);
            endDate = firstDayOfMonth.plusMonths(1).minusDays(1);
        } else {
            startDate = firstDayOfMonth;
            endDate = middleOfMonth;
        }


        double paidIncome = calculateIncomePaymentStatus(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), true);
        double unPaidIncome = calculateIncomePaymentStatus(startDate.atStartOfDay(), endDate.atTime(23, 59, 59), false);

        double totalIncomeSum = 0.0;
        while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
            LocalDate endOfPeriod = startDate.plusDays(6);

            if (endOfPeriod.isAfter(endDate)) {
                endOfPeriod = endDate;
            }

            double receiptIncome = calculateReceiptIncome(startDate.atStartOfDay(), endOfPeriod.atTime(23, 59, 59));
            double visitorReceiptIncome = calculateVisitorReceiptIncome(startDate.atStartOfDay(), endOfPeriod.atTime(23, 59, 59));
            double nightlyReceiptIncome = calculateNightlyReceiptIncome(startDate.atStartOfDay(), endOfPeriod.atTime(23, 59, 59));

            double totalIncome = receiptIncome + visitorReceiptIncome + nightlyReceiptIncome;

            biWeeklyIncome.put(startDate.toString() + " - " + endOfPeriod.toString(), totalIncome);
            totalIncomeSum += totalIncome;
            startDate = endOfPeriod.plusDays(1);
        }
        biWeeklyIncome.put("Saldo pendiente de pago ", unPaidIncome);
        biWeeklyIncome.put("Saldo pago ", paidIncome);
        biWeeklyIncome.put("Total ", totalIncomeSum);
        return biWeeklyIncome;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Double> getWeeklyIncomeUnified() {
        Map<String, Double> weeklyIncome = new LinkedHashMap<>();
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lastDayOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LocalDate currentDateOfWeek = firstDayOfWeek;
        double incomePaid = calculateIncomePaymentStatus(firstDayOfWeek.atStartOfDay(), lastDayOfWeek.atTime(23, 59, 59), true);
        double incomeUnpaid = calculateIncomePaymentStatus(firstDayOfWeek.atStartOfDay(), lastDayOfWeek.atTime(23, 59, 59), false);

        double totalIncomeSum = 0.0;
        while (!currentDateOfWeek.isAfter(lastDayOfWeek)) {
            double incomeReceipt = calculateReceiptIncome(currentDateOfWeek.atStartOfDay(), currentDateOfWeek.atTime(23, 59, 59));
            double incomeVisitorReceipt = calculateVisitorReceiptIncome(currentDateOfWeek.atStartOfDay(), currentDateOfWeek.atTime(23, 59, 59));
            double incomeNightlyReceipt = calculateNightlyReceiptIncome(currentDateOfWeek.atStartOfDay(), currentDateOfWeek.atTime(23, 59, 59));
            double totalIncome = incomeReceipt + incomeVisitorReceipt + incomeNightlyReceipt;

            weeklyIncome.put(currentDateOfWeek.toString(),totalIncome);

            totalIncomeSum += totalIncome;

            //avanzar al siguiente dia
            currentDateOfWeek = currentDateOfWeek.plusDays(1);
        }

        weeklyIncome.put("Saldo pendiente de pago ", incomeUnpaid);
        weeklyIncome.put("Saldo pago ", incomePaid);
        weeklyIncome.put("Total ", totalIncomeSum);
        return weeklyIncome;
    }


    //Calcular ingresos del receipt para el mes
    private double calculateReceiptIncome(LocalDateTime startDate, LocalDateTime endDate) {
        List<Receipt> receipts = receiptRepository.findByIssueDateBetween(startDate, endDate);
        return receipts.stream()
                .mapToDouble(receipt -> receipt.getRate().getAmount())
                .sum();
    }

    private double calculateVisitorReceiptIncome(LocalDateTime startDate, LocalDateTime endDate) {
        List<VisitorReceipt> visitorReceipts = visitorReceiptRepository.findByIssueDateBetween(startDate, endDate);
        return visitorReceipts.stream()
                .mapToDouble(vr -> vr.getRate().getAmount())
                .sum();
    }

    private double calculateNightlyReceiptIncome(LocalDateTime startDate, LocalDateTime endDate) {
        List<NightlyReceipt> nightlyReceipts = nightlyReceiptRepository.findByInitialTimeBetween(startDate, endDate);
        return nightlyReceipts.stream()
                .mapToDouble(NightlyReceipt::getAmount)
                .sum();
    }

    private double calculateIncomePaymentStatus(LocalDateTime startDate, LocalDateTime endaDate, boolean paymentStatus) {
        List<Receipt> receipts = receiptRepository.findByIssueDateBetweenAndPaymentStatus(startDate, endaDate, paymentStatus);
        double calculatePaymentStatus = receipts.stream().mapToDouble(r -> r.getRate().getAmount()).sum();

        List<VisitorReceipt> visitorReceipts = visitorReceiptRepository.findByIssueDateBetweenAndPaymentStatus(startDate, endaDate, paymentStatus);
        calculatePaymentStatus += visitorReceipts.stream().mapToDouble(vr -> vr.getRate().getAmount()).sum();

        List<NightlyReceipt> nightlyReceipts = nightlyReceiptRepository.findByInitialTimeBetweenAndPaymentStatus(startDate, endaDate, paymentStatus);
        calculatePaymentStatus += nightlyReceipts.stream().mapToDouble(nr -> nr.getAmount()).sum();

        return calculatePaymentStatus;
    }
}
