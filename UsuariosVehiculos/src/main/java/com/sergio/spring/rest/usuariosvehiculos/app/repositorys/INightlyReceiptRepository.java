package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface INightlyReceiptRepository  extends CrudRepository<NightlyReceipt,Long> {

    List<NightlyReceipt>findByUser(User user);

    @Query("SELECT r FROM NightlyReceipt r LEFT JOIN FETCH r.rate LEFT JOIN FETCH r.user LEFT JOIN FETCH r.vehicle LEFT JOIN FETCH r.vehicle.vehicleType WHERE r.id = :receiptId")
    Optional<NightlyReceipt> findByIdWithDetails(@Param("receiptId") Long receiptId);
}
