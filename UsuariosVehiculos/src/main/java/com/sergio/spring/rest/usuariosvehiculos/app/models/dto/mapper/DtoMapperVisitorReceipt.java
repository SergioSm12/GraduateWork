package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper;


import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.RateDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleTypeDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VisitorReceiptDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Rate;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VehicleType;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;

public class DtoMapperVisitorReceipt {
    private VisitorReceipt visitorReceipt;

    private DtoMapperVisitorReceipt() {

    }

    public static DtoMapperVisitorReceipt builder() {
        return new DtoMapperVisitorReceipt();
    }

    public DtoMapperVisitorReceipt setVisitorReceipt(VisitorReceipt visitorReceipt) {
        this.visitorReceipt = visitorReceipt;
        return this;
    }

    public VisitorReceiptDto build() {
        if (visitorReceipt == null) {
            throw new RuntimeException("Debe pasar  entity visitorReceipt");
        }

        // construir rate
        Rate rate = this.visitorReceipt.getRate();
        RateDto rateDto = new RateDto();
        rateDto.setId(rate.getId());
        rateDto.setTime(rate.getTime());
        rateDto.setAmount(rate.getAmount());
        // convertir vehicle type
        VehicleType vehicleType = rate.getVehicleType();
        VehicleTypeDto vehicleTypeDto = new VehicleTypeDto();
        vehicleTypeDto.setId(vehicleType.getId());
        vehicleTypeDto.setName(vehicleType.getName());
        rateDto.setVehicleType(vehicleTypeDto);

        return new VisitorReceiptDto(this.visitorReceipt.getId(), this.visitorReceipt.getPlate(), rateDto,
                this.visitorReceipt.getIssueDate(), this.visitorReceipt.getDueDate(),
                this.visitorReceipt.isPaymentStatus());
    }
}
