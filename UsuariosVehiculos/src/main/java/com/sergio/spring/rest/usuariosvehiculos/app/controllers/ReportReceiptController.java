package com.sergio.spring.rest.usuariosvehiculos.app.controllers;

import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IVisitorReceiptRepository;
import com.sergio.spring.rest.usuariosvehiculos.app.service.INightlyReceiptService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IReceiptService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IReportsUnifiedService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IVisitorReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/reports")
public class ReportReceiptController {

    @Autowired
    private IReceiptService receiptService;

    @Autowired
    private INightlyReceiptService nightlyReceiptService;

    @Autowired
    private IVisitorReceiptService visitorReceiptService;

    @Autowired
    private IReportsUnifiedService reportsUnifiedService;

    //obtener ingresos mensuales receipt
    @GetMapping("/income/monthly")
    public ResponseEntity<?> geMonthlyIncome() {
        Map<String, Double> monthlyIncome = receiptService.getWeeklyIncome();
        //Obtener el mes actual
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        Month month = currentDate.getMonth();

        Locale locale = new Locale("es", "CO");

        //Obtener el nombre del mes
        String monthName = month.getDisplayName(TextStyle.FULL, locale);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncome);
        return ResponseEntity.ok(response);
    }

    //obtener ingresos mensuales de un mes determinado
    @PostMapping("/income/monthly")
    public ResponseEntity<Map<String, Object>> getMonthlyIncomeSpecific(@RequestBody(required = false) Map<String, Integer> requestParams) {
        int year;
        Month month;

        //Verificar si se proporcionaron parametros
        if (requestParams != null && requestParams.containsKey("year") && requestParams.containsKey("month")) {
            year = requestParams.get("year");
            month = Month.of(requestParams.get("month"));

        } else {
            LocalDate currentDate = LocalDate.now();
            year = currentDate.getYear();
            month = currentDate.getMonth();
        }

        Map<String, Double> monthlyIncomeReport = receiptService.getWeeklyIncome(year, month);

        Locale locale = new Locale("es", "CO");
        String monthName = month.getDisplayName(TextStyle.FULL, locale);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncomeReport);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Obtener ingresos de la semana actual receipt
    @GetMapping("/income/weekly")
    public ResponseEntity<?> getCurrentWeeklyIncome() {
        Map<String, Double> weeklyIncome = receiptService.getDailyIncomeForCurrentWeek();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //Formatear fechas semana actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfWeekFormatter = startOfWeek.format(formatter);
        String endOfWeekFormatted = endOfWeek.format(formatter);

        String weekInfo = "SEMANA DEL " + startOfWeekFormatter + " AL " + endOfWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("semana", weekInfo);
        response.putAll(weeklyIncome);


        return ResponseEntity.ok(response);
    }

    //Obtener ingresos de la quincena actual
    @GetMapping("/income/biweekly")
    public ResponseEntity<?> getCurrentBiWeeklyIncome() {
        Map<String, Double> biWeeklyIncome = receiptService.getBiWeeklyIncome();
// Obtener el primer y último día de la quincena actual
        LocalDate today = LocalDate.now();
        LocalDate middleOfMonth = today.withDayOfMonth(1).plusDays(today.lengthOfMonth() / 2);
        boolean isAfterMiddleOfMonth = today.isAfter(middleOfMonth);
        LocalDate startDate;
        LocalDate endDate;
        if (isAfterMiddleOfMonth) {
            startDate = middleOfMonth.plusDays(1);
            endDate = today.withDayOfMonth(today.lengthOfMonth());
        } else {
            startDate = today.withDayOfMonth(1);
            endDate = middleOfMonth;
        }

        // Formatear las fechas de la quincena actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfBiWeekFormatted = startDate.format(formatter);
        String endOfBiWeekFormatted = endDate.format(formatter);
        String biWeekInfo = "QUINCENA DEL  " + startOfBiWeekFormatted + " AL " + endOfBiWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("quincena", biWeekInfo);
        response.putAll(biWeeklyIncome);

        return ResponseEntity.ok(response);
    }

    //Visitor receipt

    //obtener ingresos mensuales receipt
    @GetMapping("/income/monthly/visitor")
    public ResponseEntity<?> geMonthlyIncomeVisitor() {
        Map<String, Double> monthlyIncome = visitorReceiptService.getWeeklyIncome();
        //Obtener el mes actual
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        Month month = currentDate.getMonth();

        Locale locale = new Locale("es", "CO");

        //Obtener el nombre del mes
        String monthName = month.getDisplayName(TextStyle.FULL, locale);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncome);
        return ResponseEntity.ok(response);
    }

    //obtener ingresos mensuales de un mes determinado
    @PostMapping("/income/monthly/visitor")
    public ResponseEntity<Map<String, Object>> getMonthlyIncomeVisitorSpecific(@RequestBody(required = false) Map<String, Integer> requestParams) {
        int year;
        Month month;

        //Verificar si se proporcionaron parametros
        if (requestParams != null && requestParams.containsKey("year") && requestParams.containsKey("month")) {
            year = requestParams.get("year");
            month = Month.of(requestParams.get("month"));

        } else {
            LocalDate currentDate = LocalDate.now();
            year = currentDate.getYear();
            month = currentDate.getMonth();
        }

        Map<String, Double> monthlyIncomeReport = visitorReceiptService.getWeeklyIncome(year, month);

        Locale locale = new Locale("es", "CO");
        String monthName = month.getDisplayName(TextStyle.FULL, locale);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncomeReport);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Obtener ingresos de la semana actual receipt
    @GetMapping("/income/weekly/visitor")
    public ResponseEntity<?> getCurrentWeeklyIncomeVisitor() {
        Map<String, Double> weeklyIncome = visitorReceiptService.getDailyIncomeForCurrentWeek();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //Formatear fechas semana actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfWeekFormatter = startOfWeek.format(formatter);
        String endOfWeekFormatted = endOfWeek.format(formatter);

        String weekInfo = "SEMANA DEL " + startOfWeekFormatter + " AL " + endOfWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("semana", weekInfo);
        response.putAll(weeklyIncome);


        return ResponseEntity.ok(response);
    }

    //Obtener ingresos de la quincena actual
    @GetMapping("/income/biweekly/visitor")
    public ResponseEntity<?> getCurrentBiWeeklyIncomeVisitor() {
        Map<String, Double> biWeeklyIncome = visitorReceiptService.getBiWeeklyIncome();
// Obtener el primer y último día de la quincena actual
        LocalDate today = LocalDate.now();
        LocalDate middleOfMonth = today.withDayOfMonth(1).plusDays(today.lengthOfMonth() / 2);
        boolean isAfterMiddleOfMonth = today.isAfter(middleOfMonth);
        LocalDate startDate;
        LocalDate endDate;
        if (isAfterMiddleOfMonth) {
            startDate = middleOfMonth.plusDays(1);
            endDate = today.withDayOfMonth(today.lengthOfMonth());
        } else {
            startDate = today.withDayOfMonth(1);
            endDate = middleOfMonth;
        }

        // Formatear las fechas de la quincena actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfBiWeekFormatted = startDate.format(formatter);
        String endOfBiWeekFormatted = endDate.format(formatter);
        String biWeekInfo = "QUINCENA DEL  " + startOfBiWeekFormatted + " AL " + endOfBiWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("quincena", biWeekInfo);
        response.putAll(biWeeklyIncome);

        return ResponseEntity.ok(response);
    }


    //Nightly receipt


    @GetMapping("/income/monthly/nightly")
    public ResponseEntity<?> geMonthlyIncomeNightly() {
        Map<String, Double> monthlyIncome = nightlyReceiptService.getWeeklyIncome();
        //Obtener el mes actual
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        Month month = currentDate.getMonth();

        Locale locale = new Locale("es", "CO");

        //Obtener el nombre del mes
        String monthName = month.getDisplayName(TextStyle.FULL, locale);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncome);
        return ResponseEntity.ok(response);
    }

    //obtener ingresos mensuales de un mes determinado
    @PostMapping("/income/monthly/nightly")
    public ResponseEntity<Map<String, Object>> getMonthlyIncomeSpecificNightly(@RequestBody(required = false) Map<String, Integer> requestParams) {
        int year;
        Month month;

        //Verificar si se proporcionaron parametros
        if (requestParams != null && requestParams.containsKey("year") && requestParams.containsKey("month")) {
            year = requestParams.get("year");
            month = Month.of(requestParams.get("month"));

        } else {
            LocalDate currentDate = LocalDate.now();
            year = currentDate.getYear();
            month = currentDate.getMonth();
        }

        Map<String, Double> monthlyIncomeReport = nightlyReceiptService.getWeeklyIncome(year, month);

        Locale locale = new Locale("es", "CO");
        String monthName = month.getDisplayName(TextStyle.FULL, locale);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncomeReport);


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/income/weekly/nightly")
    public ResponseEntity<?> getCurrentWeeklyIncomeNightly() {
        Map<String, Double> weeklyIncome = nightlyReceiptService.getDailyIncomeForCurrentWeek();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //Formatear fechas semana actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfWeekFormatter = startOfWeek.format(formatter);
        String endOfWeekFormatted = endOfWeek.format(formatter);

        String weekInfo = "SEMANA DEL " + startOfWeekFormatter + " AL " + endOfWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("semana", weekInfo);
        response.putAll(weeklyIncome);


        return ResponseEntity.ok(response);
    }

    //Obtener ingresos de la quincena actual
    @GetMapping("/income/biweekly/nightly")
    public ResponseEntity<?> getCurrentBiWeeklyIncomeNightly() {
        Map<String, Double> biWeeklyIncome = nightlyReceiptService.getBiWeeklyIncome();
// Obtener el primer y último día de la quincena actual
        LocalDate today = LocalDate.now();
        LocalDate middleOfMonth = today.withDayOfMonth(1).plusDays(today.lengthOfMonth() / 2);
        boolean isAfterMiddleOfMonth = today.isAfter(middleOfMonth);
        LocalDate startDate;
        LocalDate endDate;
        if (isAfterMiddleOfMonth) {
            startDate = middleOfMonth.plusDays(1);
            endDate = today.withDayOfMonth(today.lengthOfMonth());
        } else {
            startDate = today.withDayOfMonth(1);
            endDate = middleOfMonth;
        }

        // Formatear las fechas de la quincena actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfBiWeekFormatted = startDate.format(formatter);
        String endOfBiWeekFormatted = endDate.format(formatter);
        String biWeekInfo = "QUINCENA DEL  " + startOfBiWeekFormatted + " AL " + endOfBiWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("quincena", biWeekInfo);
        response.putAll(biWeeklyIncome);

        return ResponseEntity.ok(response);
    }

    //Reports unified

    @GetMapping("/income/monthly/unified")
    public ResponseEntity<?> getMonthlyIncomeUnified() {
        Map<String, Double> monthlyIncomeUnified = reportsUnifiedService.generateMonthlyIncomeReport();
        //Obtener el mes actual
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        Month month = currentDate.getMonth();

        Locale locale = new Locale("es", "CO");

        //Obtener el nombre del mes
        String monthName = month.getDisplayName(TextStyle.FULL, locale);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncomeUnified);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/income/monthly/unified")
    public ResponseEntity<?> getMonthlyIncomeSpecificUnified(@RequestBody(required = false) Map<String, Integer> requestParams) {
        int year;
        Month month;

        //Verificar si se proporcionaron parametros
        if (requestParams != null && requestParams.containsKey("year") && requestParams.containsKey("month")) {
            year = requestParams.get("year");
            month = Month.of(requestParams.get("month"));

        } else {
            LocalDate currentDate = LocalDate.now();
            year = currentDate.getYear();
            month = currentDate.getMonth();
        }

        Map<String, Double> monthlyIncomeReportUnified = reportsUnifiedService.generateMonthlyIncomeReport(year, month);

        Locale locale = new Locale("es", "CO");
        String monthName = month.getDisplayName(TextStyle.FULL, locale);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Mes", monthName.toUpperCase() + " DEL " + year);
        response.putAll(monthlyIncomeReportUnified);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/income/biweekly/unified")
    public ResponseEntity<?> getCurrentBiweeklyIncomeUnified() {
        Map<String, Double> biWeeklyIncome = reportsUnifiedService.biWeeklyIncomeUnified();
        //Obtener el primer y último día de la quincena actual
        LocalDate today = LocalDate.now();
        LocalDate middleOfMonth = today.withDayOfMonth(1).plusDays(today.lengthOfMonth() / 2);
        boolean isAfterMiddleOfMonth = today.isAfter(middleOfMonth);
        LocalDate startDate;
        LocalDate endDate;
        if (isAfterMiddleOfMonth) {
            startDate = middleOfMonth.plusDays(1);
            endDate = today.withDayOfMonth(today.lengthOfMonth());
        } else {
            startDate = today.withDayOfMonth(1);
            endDate = middleOfMonth;
        }

        // Formatear las fechas de la quincena actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfBiWeekFormatted = startDate.format(formatter);
        String endOfBiWeekFormatted = endDate.format(formatter);
        String biWeekInfo = "QUINCENA DEL  " + startOfBiWeekFormatted + " AL " + endOfBiWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("quincena", biWeekInfo);
        response.putAll(biWeeklyIncome);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/income/weekly/unified")
    public ResponseEntity<?> getCurrentWeeklyIncomeUnified() {
        Map<String, Double> weeklyIncome = reportsUnifiedService.getWeeklyIncomeUnified();
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        //Formatear fechas semana actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startOfWeekFormatter = startOfWeek.format(formatter);
        String endOfWeekFormatted = endOfWeek.format(formatter);

        String weekInfo = "SEMANA DEL " + startOfWeekFormatter + " AL " + endOfWeekFormatted;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("semana", weekInfo);
        response.putAll(weeklyIncome);

        return ResponseEntity.ok(response);
    }


}
