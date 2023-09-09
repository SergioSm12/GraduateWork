package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IReceiptRepository extends CrudRepository<Receipt, Long> {

    List<Receipt> findByPaymentStatusFalse();
    List<Receipt> findByPaymentStatusTrue();
    List<Receipt> findByUser(User user);
    List<Receipt> findByUserAndPaymentStatus(User user, boolean paymentStatus);

}
