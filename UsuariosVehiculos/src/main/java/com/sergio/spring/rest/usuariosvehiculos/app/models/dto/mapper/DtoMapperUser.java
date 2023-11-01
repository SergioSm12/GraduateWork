package com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleTypeDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;

import java.util.List;
import java.util.stream.Collectors;

//Clase para mapear entity con dto usando patron Builder
public class DtoMapperUser {


    private User user;

    private DtoMapperUser() {
    }

    //Manera de acceder al constructor vacio
    public static DtoMapperUser builder() {
        return new DtoMapperUser();
    }

    //Pasamos el user al get instance de esta manera traemos los datos del entity a esta clase
    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    //Teniendo los datos del user los poblamos en el dto(los metodos anteriores funcionan encadenados)
    public UserDto build() {
        if (user == null) {
            throw new RuntimeException("Debe pasar el entity user");
        }

        List<VehicleDto> vehicleDtos = this.user.getVehicles().stream()
                .map(vehicle -> {
                            VehicleTypeDto vehicleTypeDto = new VehicleTypeDto(
                                    vehicle.getVehicleType().getId(),
                                    vehicle.getVehicleType().getName()
                            );

                            return new VehicleDto(vehicle.getId(), vehicle.getPlate(), vehicleTypeDto, vehicle.isActive());
                        }
                ).collect(Collectors.toList());

        boolean isAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
        boolean isGuard = user.getRoles().stream().anyMatch(r -> "ROLE_GUARD".equals(r.getName()));
        return new UserDto(this.user.getId(), this.user.getName(), this.user.getLastName(), this.user.getEmail(), this.user.getPhoneNumber(), vehicleDtos, this.user.isActive(), isAdmin, isGuard);
    }
}
