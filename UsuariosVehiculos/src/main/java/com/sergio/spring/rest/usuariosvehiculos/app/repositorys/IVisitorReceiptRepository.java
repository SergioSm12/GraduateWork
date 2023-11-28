package com.sergio.spring.rest.usuariosvehiculos.app.repositorys;

import org.springframework.data.repository.CrudRepository;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;

public interface IVisitorReceiptRepository  extends CrudRepository<VisitorReceipt,Long> {
}
