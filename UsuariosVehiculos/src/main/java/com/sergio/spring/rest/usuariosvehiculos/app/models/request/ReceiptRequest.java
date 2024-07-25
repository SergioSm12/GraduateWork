package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;

import jakarta.validation.constraints.NotNull;

public class ReceiptRequest {

    @NotNull
    private LocalDateTime issueDate;
    @NotNull
    private LocalDateTime dueDate;
    @NotNull
    private boolean paymentStatus;

    private Rate rate;

    public ReceiptRequest() {
    }

    public ReceiptRequest(LocalDateTime issueDate, LocalDateTime dueDate, boolean paymentStatus, Rate rate) {
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.paymentStatus = paymentStatus;
        this.rate = rate;
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

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

}
