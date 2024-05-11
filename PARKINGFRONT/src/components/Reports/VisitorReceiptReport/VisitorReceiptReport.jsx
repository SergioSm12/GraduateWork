import React, { useEffect } from "react";
import { useReports } from "../../../hooks/useReports";
import { Link } from "react-router-dom";
import { GenerateReportMonthlySpecific } from "../ReceiptReports/GenerateReportMonthlySpecific";
import { DataTableReports } from "../DataTableReports";

export const VisitorReceiptReport = () => {
  const {
    currentMonthlyVisitorReceiptReport,
    currentWeeklyVisitorReceiptReport,
    currentBiweeklyVisitorReceiptReport,
    getCurrentMonthlyVisitorReceiptReport,
    getCurrentBiweeklyVisitorReceiptReport,
    getCurrentWeeklyVisitorReceiptReport,
  } = useReports();

  useEffect(()=>{
    getCurrentMonthlyVisitorReceiptReport();
    getCurrentBiweeklyVisitorReceiptReport();
    getCurrentWeeklyVisitorReceiptReport();
  },[])

  return (
    <>
    {/*Title */}
    <div className="mb-10">
        <h1 className="font-bold text-gray-100 text-xl">Reporte de ingresos visitantes</h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary transition-colors">
            Principal
          </Link>
          <span>-</span>
          <span>Centro de reportes recibos visitantes</span>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-secondary-100 p-8 rounded-lg">
          <GenerateReportMonthlySpecific reportType={"visitor"}/>
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentMonthlyVisitorReceiptReport} />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentBiweeklyVisitorReceiptReport} />
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableReports dataReports={currentWeeklyVisitorReceiptReport} />
        </div>
      </div>
    </>
  )
};
