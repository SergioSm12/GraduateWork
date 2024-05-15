package com.sergio.spring.rest.usuariosvehiculos.app.service;

import java.time.Month;
import java.util.Map;

public interface IReportsUnifiedService {

    Map<String, Double> generateMonthlyIncomeReport();

    Map<String, Double> generateMonthlyIncomeReport(int year, Month month);

    Map<String, Double> biWeeklyIncomeUnified();

    Map<String, Double> getWeeklyIncomeUnified();

}
