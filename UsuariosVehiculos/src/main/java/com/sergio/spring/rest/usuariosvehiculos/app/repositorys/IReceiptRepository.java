package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;

public interface IReceiptRepository extends CrudRepository<Receipt, Long> {

    //Orden desendente 
   List<Receipt> findAllByOrderByIssueDateDesc();
   
    List<Receipt> findByPaymentStatusFalse();

    List<Receipt> findByPaymentStatusTrue();

    List<Receipt> findByUser(User user);

    List<Receipt> findByUserAndPaymentStatus(User user, boolean paymentStatus);

    // contar recibos no pagos
    long countByPaymentStatusFalse();

    // contar recibos pagos
    long countByPaymentStatusTrue();

}
