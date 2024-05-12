import React, { useEffect } from "react";
import { useReports } from "../../../hooks/useReports";
import { Link } from "react-router-dom";
import { DataTableReports } from "../DataTableReports";
import { GenerateReportMonthlySpecific } from "../ReceiptReports/GenerateReportMonthlySpecific";
import { LinesChart } from "../../Statistics/LinesChart";

export const NightlyReceipReports = () => {
  const {
    currentMonthlyNightlyReceiptReport,
    currentWeeklyNightlyReceiptReport,
    currentBiweeklyNightlyReceiptReport,
    getCurrentMonthlyNightlyReceiptReport,

    getCurrentBiweeklyNightlyReceiptReport,
    getCurrentWeeklyNightlyReceiptReport,
  } = useReports();

  useEffect(() => {
    getCurrentMonthlyNightlyReceiptReport();
    getCurrentBiweeklyNightlyReceiptReport();
    getCurrentWeeklyNightlyReceiptReport();
  }, []);
  return (
    <>
      <div className="mb-10">
        <h1 className="font-bold text-gray-100 text-xl">Reporte de ingresos</h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary transition-colors">
            Principal
          </Link>
          <span>-</span>
          <span>Centro de reportes recibos nocturnos</span>
        </div>
      </div>
      <div className="grid grid-cols-1">
        <div className=" flex justify-center bg-secondary-100 p-8 rounded-lg overflow-auto ">
         <div className="w-[450px] h-[230px] md:w-full md:h-full">
         <LinesChart dataReport={currentMonthlyNightlyReceiptReport} />
         </div>
       
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mt-8">
        <div className="bg-secondary-100 p-8 rounded-lg">
          <GenerateReportMonthlySpecific reportType={"nightly"} />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentMonthlyNightlyReceiptReport} />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentBiweeklyNightlyReceiptReport} />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentWeeklyNightlyReceiptReport} />
        </div>
      </div>
    </>
  );
};
