package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;

public interface IVisitorReceiptRepository extends CrudRepository<VisitorReceipt, Long> {

    //contar recibos visitantes
    long countByPaymentStatusFalse();

    long countByPaymentStatusTrue();

    //consulta optimizada qr
    @Query("SELECT vr FROM VisitorReceipt vr LEFT JOIN FETCH vr.rate r LEFT JOIN FETCH r.vehicleType WHERE vr.id = :visitorReceiptId")
    Optional<VisitorReceipt> findByIdWithDetails(@Param("visitorReceiptId") Long visitorReceiptId);

    //reportes filtrar recibos
    List<VisitorReceipt> findByIssueDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    //filtar recibos por estado de pago.
    List<VisitorReceipt> findByIssueDateBetweenAndPaymentStatus(LocalDateTime startDate, LocalDateTime endDate, boolean paymentStatus);
}
