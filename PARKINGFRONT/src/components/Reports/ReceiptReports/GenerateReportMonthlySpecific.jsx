import React from "react";
import { useState } from "react";
import { RiCalendarLine } from "react-icons/ri";
import { useReports } from "../../../hooks/useReports";
export const GenerateReportMonthlySpecific = () => {
  const { getCurrentMonthlyReceiptReportSpecific } = useReports();

  const currentYear = new Date().getFullYear();
  const currentMonth = new Date().getMonth() + 1;

  const initialReportForm = {
    year: currentYear,
    month: currentMonth,
  };

  const [reportForm, setReportForm] = useState(initialReportForm);

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    setReportForm({
      ...reportForm,
      [name]: value,
    });
  };

  const onSubmit = (e) => {
    e.preventDefault();
    getCurrentMonthlyReceiptReportSpecific(reportForm);
  };

  return (
    <div className="bg-secondary-900 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className="text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          Escoja un mes y un año para generar el{" "}
          <span className="text-primary">reporte.</span>
        </h1>
      </div>
      <form className="mb-8" onSubmit={onSubmit}>
        <div className="relative mb-2">
          <RiCalendarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="number"
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Año"
            name="year"
            value={reportForm.year}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-2">
          <RiCalendarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <select
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border-primary appearance-none"
            id="month"
            name="month"
            value={reportForm.month}
            onChange={onInputChange}
          >
            <option value="1">Enero</option>
            <option value="2">Febrero</option>
            <option value="3">Marzo</option>
            <option value="4">Abril</option>
            <option value="5">Mayo</option>
            <option value="6">Junio</option>
            <option value="7">Julio</option>
            <option value="8">Agosto</option>
            <option value="9">Septiembre</option>
            <option value="10">Octubre</option>
            <option value="11">Noviembre</option>
            <option value="12">Diciembre</option>
          </select>
        </div>
        <button
          type="submit"
          className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg"
        >
          Generar reporte mensual
        </button>
      </form>
    </div>
  );
};
