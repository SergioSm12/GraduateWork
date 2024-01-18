package com.sergio.spring.rest.usuariosvehiculos.app.util.controllers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.WriterException;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Receipt;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.VisitorReceipt;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IReceiptService;
import com.sergio.spring.rest.usuariosvehiculos.app.service.IVisitorReceiptService;
import com.sergio.spring.rest.usuariosvehiculos.app.util.services.IQRCodeReceiptService;

@RestController
@RequestMapping("/qrcode")
public class QRCodeController {
    @Autowired
    private IQRCodeReceiptService qrCodeReceiptService;

    @Autowired
    private IReceiptService receiptService;

    @Autowired
    IVisitorReceiptService visitorReceiptService;


    @GetMapping("/{id}")
    public ResponseEntity<byte[]> generateQRCodeReceipt(@PathVariable Long id) throws WriterException, IOException {


        Optional<Receipt> receiptO = receiptService.findByIdReceiptWithDetails(id);
        if (receiptO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Receipt receipt = receiptO.get();

        BufferedImage image = qrCodeReceiptService.generateQRCodeReceipt(receipt);

        //convert image to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        //headers response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageData.length);
        //si se desea que apenas se realice la petision se descarge.
        //headers.setContentDispositionFormData("attachment", "qrcode.png");

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);


    }
    @GetMapping("/visitor/{id}")
    public ResponseEntity<byte[]> generateQRCodeReceiptVisitor(@PathVariable Long id) throws WriterException, IOException {


        Optional<VisitorReceipt> receiptO = visitorReceiptService.findByIdVisitorReceiptDetails(id);
        if (receiptO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        VisitorReceipt receipt = receiptO.get();

        BufferedImage image = qrCodeReceiptService.generateQRCodeReceiptVisitor(receipt);

        //convert image to bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageData = baos.toByteArray();

        //headers response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageData.length);
        //si se desea que apenas se realice la petision se descarge.
        //headers.setContentDispositionFormData("attachment", "qrcode.png");

        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);


    }
}
