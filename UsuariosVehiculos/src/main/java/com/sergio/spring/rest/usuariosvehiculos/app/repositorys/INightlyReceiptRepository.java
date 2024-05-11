package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.NightlyReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface INightlyReceiptRepository extends CrudRepository<NightlyReceipt, Long> {

    // contar recibos no pagos
    long countByPaymentStatusFalse();

    // contar recibos pagos
    long countByPaymentStatusTrue();

    //Trae los recibos por usuario
    List<NightlyReceipt> findByUser(User user);


    @Query("SELECT r FROM NightlyReceipt r LEFT JOIN FETCH r.rate LEFT JOIN FETCH r.user LEFT JOIN FETCH r.vehicle LEFT JOIN FETCH r.vehicle.vehicleType WHERE r.id = :receiptId")
    Optional<NightlyReceipt> findByIdWithDetails(@Param("receiptId") Long receiptId);

    //Reportes filtrar recibos
    List<NightlyReceipt> findByInitialTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    //filtrar recibos por estado de pago
    List<NightlyReceipt> findByInitialTimeBetweenAndPaymentStatus(LocalDateTime startDate, LocalDateTime endDate, boolean paymentStatus);
}
