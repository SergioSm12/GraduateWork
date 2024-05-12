import React, { useEffect } from "react";
import { useReports } from "../../../hooks/useReports";
import { Link } from "react-router-dom";
import { DataTableReports } from "../DataTableReports";
import { GenerateReportMonthlySpecific } from "./GenerateReportMonthlySpecific";
import { LinesChart } from "../../Statistics/LinesChart";

export const MonthlyReceipt = () => {
  const {
    getCurrentMonthlyReceiptReport,
    getCurrentBiweeklyReceiptReport,
    getCurrentWeeklyReceiptReport,
    currentWeeklyReceiptReport,
    currentBiweeklyReceiptReport,
    currentMonthlyReceiptReport,
  } = useReports();

  useEffect(() => {
    getCurrentMonthlyReceiptReport();
    getCurrentBiweeklyReceiptReport();
    getCurrentWeeklyReceiptReport();
  }, []);

  return (
    <>
      {/*Title */}
      <div className="mb-10">
        <h1 className="font-bold text-gray-100 text-xl">Reporte de ingresos</h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary transition-colors">
            Principal
          </Link>
          <span>-</span>
          <span>Centro de reportes recibos diurnos</span>
        </div>
      </div>
      <div className="grid grid-cols-1">
        <div className="flex justify-center bg-secondary-100 p-8 rounded-lg overflow-auto">
          <div className="w-[450px] h-[230px] md:w-full md:h-full">
            <LinesChart dataReport={currentMonthlyReceiptReport} />
          </div>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mt-8">
        <div className="bg-secondary-100 p-8 rounded-lg">
          <GenerateReportMonthlySpecific />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentMonthlyReceiptReport} />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentBiweeklyReceiptReport} />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentWeeklyReceiptReport} />
        </div>
      </div>
    </>
  );
};
