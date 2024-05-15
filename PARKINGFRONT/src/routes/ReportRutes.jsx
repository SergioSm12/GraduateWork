import React from "react";
import { Route, Routes } from "react-router-dom";
import { MonthlyReceipt } from "../components/Reports/ReceiptReports/MonthlyReceipt";
import { NightlyReceipReports } from "../components/Reports/NightlyReceiptReports/NightlyReceipReports";
import { VisitorReceiptReport } from "../components/Reports/VisitorReceiptReport/VisitorReceiptReport";
import { UnifiedReport } from "../components/Reports/UnifiedReports/UnifiedReport";

export const ReportRutes = () => {
  return (
    <Routes>
      <Route path="/receipt" element={<MonthlyReceipt />} />
      <Route path="/nightlyreceipt" element={<NightlyReceipReports />} />
      <Route path="/visitorreceipt" element={<VisitorReceiptReport />} />
      <Route path="/unified" element={<UnifiedReport />} />
    </Routes>
  );
};
