package com.sergio.spring.rest.usuariosvehiculos.app.models.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ReceiptRequest {
    @NotNull
    private LocalDateTime dueDate;
    @NotNull
    private boolean paymentStatus;

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
