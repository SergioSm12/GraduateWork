package com.sergio.spring.rest.usuariosvehiculos.app.util.services;

import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;


@Service
public class QRCodeReceiptService implements IQRCodeReceiptService {


    @Override
    public BufferedImage generateQRCodeReceipt(Receipt receipt) throws WriterException {


        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        //format date
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy 'a las' HH:mm");
        String formattedText = String.format(
                "Número de Factura: %s\n" +
                        "Estado de Pago: %s\n" +
                        "Placa: %s\n" +
                        "vehiculo: %s\n" +
                        "Fecha de Emisión: %s\n" +
                        "Fecha de Vencimiento: %s\n" +
                        "valor: %s\n" +
                        "Usuario: %s",
                receipt.getId(),
                receipt.isPaymentStatus() ? "Pagada" : "Pendiente",
                receipt.getVehicle().getPlate(),
                receipt.getVehicle().getVehicleType().getName(),
                receipt.getIssueDate().format(dateTimeFormatter),
                receipt.getDueDate().format(dateTimeFormatter),
                receipt.getRate().getAmount(),
                receipt.getUser().getName()

        );
        BitMatrix bitMatrix = qrCodeWriter.encode(formattedText,
                BarcodeFormat.QR_CODE, 350, 350);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFFFFFFFF, // yellow foreground
                0xFF0F172A // blue dark  background
        );

        return MatrixToImageWriter.toBufferedImage(bitMatrix, config);
    }

    @Override
    public BufferedImage generateQRCodeReceiptVisitor(VisitorReceipt receipt) throws WriterException {


        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        //format date
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy 'a las' HH:mm");
        String formattedText = String.format(
                "Número de Factura: %s\n" +
                        "Estado de Pago: %s\n" +
                        "Placa: %s\n" +
                        "vehiculo: %s\n" +
                        "Fecha de Emisión: %s\n" +
                        "Fecha de Vencimiento: %s\n" +
                        "valor: %s\n" +
                        "Usuario: %s",
                receipt.getId(),
                receipt.isPaymentStatus() ? "Pagada" : "Pendiente",
                receipt.getPlate(),
                receipt.getRate().getVehicleType().getName(),
                receipt.getIssueDate().format(dateTimeFormatter),
                receipt.getDueDate().format(dateTimeFormatter),
                receipt.getRate().getAmount(),
                "Visitante"

        );
        BitMatrix bitMatrix = qrCodeWriter.encode(formattedText,
                BarcodeFormat.QR_CODE, 350, 350);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFFFFFFFF, // yellow foreground
                0xFF0F172A // blue dark  background
        );

        return MatrixToImageWriter.toBufferedImage(bitMatrix, config);
    }
}
