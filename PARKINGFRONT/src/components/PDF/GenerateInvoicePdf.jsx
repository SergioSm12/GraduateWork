import jsPDF from "jspdf";
import "jspdf-autotable";
import React, { useEffect, useState } from "react";
import { FaRegFilePdf } from "react-icons/fa6";
import ustalogo from "../../assets/Recurso 13.png";
import { formatInTimeZone } from "date-fns-tz";
import { es } from "date-fns/locale";
import { useQRCode } from "../../hooks/useQRCode";

const GeneratePDF = ({ dataReceipt, qrCodeImage, receiptType }) => {
  // Formatear moneda
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
    }).format(amount);
  };

  const doc = new jsPDF();
  // Definir background
  doc.setFillColor(15, 23, 42);
  // Definir valores en 0
  doc.rect(
    0,
    0,
    doc.internal.pageSize.getWidth(),
    doc.internal.pageSize.getHeight(),
    "F"
  );

  const text =
    receiptType === "nocturno" ? "Recibo de pago nocturno" : "Recibo de pago";
  const fontSize = 16; // Tamaño de fuente
  const textWidth = doc.getTextWidth(text); // Obtener el ancho del texto
  // Centrar texto a la mitad
  const centerXtext = (doc.internal.pageSize.getWidth() - textWidth) / 2;

  // Encabezado de la factura
  doc.setTextColor(255, 255, 255);
  doc.setFontSize(fontSize);
  doc.text(text, centerXtext, 20);

  // Calcular posicion de la imagen
  const pdfWidth = doc.internal.pageSize.getWidth();
  const imgScale = pdfWidth / 1082;
  doc.addImage(ustalogo, "PNG", 10, 35, pdfWidth - 20, 316 * imgScale);

  // Crear columnas
  const columns = [
    { header: "Id", dataKey: "id" },
    { header: "Placa", dataKey: "Plate" },
    { header: "Tipo", dataKey: "type" },
    { header: "Precio", dataKey: "amount" },
    {
      header: "Fecha inicio",
      dataKey: receiptType === "nocturno" ? "initialTime" : "issueDate",
    },
    {
      header: "Fecha vencimiento",
      dataKey: receiptType === "nocturno" ? "departureTime" : "dueDate",
    },
    { header: "Usuario", dataKey: "user" },
    { header: "Estado", dataKey: "paymentStatus" },
  ];

  const dataReceiptTable = [
    [
      `${dataReceipt.id}`,
      `${
        receiptType === "visitante"
          ? dataReceipt.plate
          : dataReceipt.vehicle.plate
      }`,
      `${
        receiptType === "visitante"
          ? "vehículo visitante"
          : dataReceipt.vehicle.vehicleType.name
      }`,
      `${formatCurrency(
        receiptType === "nocturno"
          ? dataReceipt.amount
          : dataReceipt.rate.amount
      )}`,
      `${formatInTimeZone(
        receiptType === "nocturno"
          ? dataReceipt.initialTime
          : dataReceipt.issueDate,
        "America/Bogota",
        "dd 'de' MMMM 'del' yyyy 'a las' HH:mm",
        { locale: es }
      )}`,
      `${formatInTimeZone(
        receiptType === "nocturno"
          ? dataReceipt.departureTime
          : dataReceipt.dueDate,
        "America/Bogota",
        "dd 'de' MMMM 'del' yyyy 'a las' HH:mm",
        { locale: es }
      )}`,
      `${receiptType === "visitante" ? "Visitante" : dataReceipt.user.name} ${
        receiptType === "visitante" ? "" : dataReceipt.user.lastName
      }`,
      `${dataReceipt.paymentStatus ? "Pago" : "Pendiente"}`,
    ],
  ];

  doc.autoTable({
    startY: 130,
    head: [columns.map((column) => column.header)],
    body: dataReceiptTable,
    margin: { top: 10 },
    headStyles: {
      fillColor: [30, 41, 60], // Color de fondo por defecto
      textColor: [255, 255, 255], // Color del texto por defecto
      fontStyle: "bold",
    },
    bodyStyles: [
      {
        fillColor: [255, 255, 255],
        textColor: [15, 23, 42],
        fontStyle: "bold",
      }, // Estilo para la segunda fila
    ],
  });

  // Calcular la posición para la imagen QR en el centro
  const qrWidth = 125;
  const qrHeight = 125;
  const centerX = pdfWidth / 2 - qrWidth / 2;
  const centerY =
    doc.autoTable.previous.finalY +
    (doc.internal.pageSize.getHeight() - doc.autoTable.previous.finalY) / 2 -
    qrHeight / 2;

  // Agregar la imagen QR
  doc.addImage(qrCodeImage, "PNG", centerX, centerY, qrWidth, qrHeight);

  // Guardar el pdf con un nombre especifico
  doc.save(
    receiptType === "nocturno"
      ? `reciboNocturno_${dataReceipt.id}.pdf`
      : receiptType === "visitante"
      ? `reciboVisitante_${dataReceipt.id}.pdf`
      : `recibo_${dataReceipt.id}.pdf`
  );

  return null;
};

export const GenerateInvoicePdf = ({ row, receiptType }) => {
  const { getQRCodeUsers, getQRCodeNightly, getQRCodeVisitors } = useQRCode();
  const handleGeneratePDF = async () => {
    let qrCodeImage;
    if (receiptType === "nocturno") {
      qrCodeImage = await getQRCodeNightly(row.original.id);
    } else if (receiptType === "visitante") {
      qrCodeImage = await getQRCodeVisitors(row.original.id);
    } else {
      qrCodeImage = await getQRCodeUsers(row.original.id);
    }
    GeneratePDF({ dataReceipt: row.original, qrCodeImage, receiptType }); // Cuando se hace clic en el botón, establece el estado para mostrar el componente PDF
  };

  return (
    <>
      <button
        className="p-2 flex  items-center justify-center bg-secondary-100 rounded-lg  w-full hover:border hover:text-red-500 border-primary/80 transition-colors"
        onClick={handleGeneratePDF}
      >
        <FaRegFilePdf />
      </button>
    </>
  );
};
