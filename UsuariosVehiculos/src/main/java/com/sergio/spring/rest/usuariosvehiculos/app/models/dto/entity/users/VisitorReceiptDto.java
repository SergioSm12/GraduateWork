package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VisitorReceiptDto {
    private Long id;
    private String plate;
    @JsonProperty("rate")
    private RateDto rateDto;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private boolean paymentStatus;

    public VisitorReceiptDto() {
    }

    public VisitorReceiptDto(Long id, String plate, RateDto rateDto, LocalDateTime issueDate, LocalDateTime dueDate,
            boolean paymentStatus) {
        this.id = id;
        this.plate = plate;
        this.rateDto = rateDto;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.paymentStatus = paymentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public RateDto getRateDto() {
        return rateDto;
    }

    public void setRateDto(RateDto rateDto) {
        this.rateDto = rateDto;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
