package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReceiptRequest {
    @NotNull
    private LocalDate dueDate;
    @NotNull
    private boolean paymentStatus;

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
}
