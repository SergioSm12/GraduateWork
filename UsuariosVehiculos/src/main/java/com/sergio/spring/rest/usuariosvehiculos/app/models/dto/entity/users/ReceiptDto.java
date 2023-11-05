package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ReceiptDto {
    private Long id;
    private UserDto user;
    private VehicleDto vehicle;
    @JsonProperty("rate")
    private RateDto rateDto;
    private LocalDateTime issueDate;
    private LocalDateTime dueDate;
    private boolean paymentStatus;

    public ReceiptDto() {
    }

    public ReceiptDto(Long id, UserDto user, VehicleDto vehicle, RateDto rateDto, LocalDateTime issueDate, LocalDateTime dueDate, boolean paymentStatus) {
        this.id = id;
        this.user = user;
        this.vehicle = vehicle ;
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public VehicleDto getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleDto vehicle) {
        this.vehicle = vehicle;
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
