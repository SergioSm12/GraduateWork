import jsPDF from "jspdf";
import "jspdf-autotable";
import React from "react";
import { FaRegFilePdf } from "react-icons/fa6";
import ustalogo from "../../assets/Recurso 13.png";

const GeneratePDF = ({ dataReports }) => {
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

  const text = `REPORTE DE INGRESOS ${
    dataReports.Mes || dataReports.quincena || dataReports.semana
  }`;
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

  const columns = ["Fecha y detalles", "Total"];

  //Iterar data
  const dataTable = Object.entries(dataReports)
    .filter(([key]) => key !== "Mes" && key !== "quincena" && key !== "semana")
    .map(([key, value]) => [key, formatCurrency(value)]);

  doc.autoTable({
    startY: 130,
    head: [columns.map((column) => column)],
    body: dataTable,
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

  //Guardar el reporte
  doc.save(
    `REPORTE ${dataReports.Mes || dataReports.quincena || dataReports.semana}`
  );

  return null;
};

export const GenerateReportPDF = ({ dataReports }) => {
  const handlerGeneratePDF = () => {
    GeneratePDF({ dataReports: dataReports });
  };
  return (
    <>
      <button
        className="p-2  text-center  bg-secondary-900 rounded-lg   hover:border hover:text-red-500 border-primary/80 transition-colors"
        onClick={handlerGeneratePDF}
      >
        <FaRegFilePdf />
      </button>
    </>
  );
};
