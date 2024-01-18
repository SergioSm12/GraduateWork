package com.sergio.spring.rest.usuariosvehiculos.app.util.services;

import java.awt.image.BufferedImage;

import com.google.zxing.WriterException;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;

public interface IQRCodeReceiptService {

    BufferedImage generateQRCodeReceipt(Receipt receipt) throws WriterException;

    BufferedImage generateQRCodeReceiptVisitor(VisitorReceipt receipt) throws WriterException;
}
