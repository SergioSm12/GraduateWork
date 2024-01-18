package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;

public interface IVisitorReceiptRepository  extends CrudRepository<VisitorReceipt,Long> {

    //contar recibos visitantes
    long countByPaymentStatusFalse();
    long countByPaymentStatusTrue();

    //consulta optimizada qr
    @Query("SELECT vr FROM VisitorReceipt vr LEFT JOIN FETCH vr.rate r LEFT JOIN FETCH r.vehicleType WHERE vr.id = :visitorReceiptId")
    Optional<VisitorReceipt> findByIdWithDetails(@Param("visitorReceiptId") Long visitorReceiptId);
}
