package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Vehicle;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IVehicleRepository extends CrudRepository<Vehicle, Long> {

    List<Vehicle> findByUserIdAndActiveFalse(Long userId);
    @Query("select  v from Vehicle v where v.id= :vehicleId and v.user.id = :userId")
    Optional<Vehicle> findByIdAndUserId(Long vehicleId, Long userId);

    //Metodo para eliminar
    @Modifying
    @Query("DELETE FROM Vehicle v WHERE v.id = :vehicleId")
    void deleteByVehicleId(@Param("vehicleId") Long vehicleId);

    //Metodo para comparar placa
    boolean existsByUserIdAndPlate(Long userId, String plate);
}
