package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import java.time.LocalDate;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;

import jakarta.validation.constraints.NotNull;

public class ReceiptRequest {

    @NotNull
    private LocalDate issueDate;
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private boolean paymentStatus;

    private Rate rate;

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

}
