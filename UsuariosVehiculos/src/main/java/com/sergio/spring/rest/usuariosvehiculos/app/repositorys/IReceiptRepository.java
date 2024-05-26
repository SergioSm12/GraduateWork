package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;

public interface IReceiptRepository extends CrudRepository<Receipt, Long> {

    //Orden desendente 
    List<Receipt> findAllByOrderByIssueDateDesc();

    List<Receipt> findByPaymentStatusFalse();

    List<Receipt> findByPaymentStatusTrue();

    List<Receipt> findByUserOrderByIssueDateDesc(User user);

    List<Receipt> findByUserAndPaymentStatus(User user, boolean paymentStatus);

    // contar recibos no pagos
    long countByPaymentStatusFalse();

    // contar recibos pagos
    long countByPaymentStatusTrue();

    //query optimisado find by idQR
    @Query("SELECT r FROM Receipt r LEFT JOIN FETCH r.rate LEFT JOIN FETCH r.user LEFT JOIN FETCH r.vehicle LEFT JOIN FETCH r.vehicle.vehicleType WHERE r.id = :receiptId")
    Optional<Receipt> findByIdWithDetails(@Param("receiptId") Long receiptId);


    //reportes filtrar recibos
    List<Receipt> findByIssueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    //filtar recibos por estado de pago.
    List<Receipt> findByIssueDateBetweenAndPaymentStatus(LocalDateTime startDate, LocalDateTime endDate, boolean paymentStatus);


}
