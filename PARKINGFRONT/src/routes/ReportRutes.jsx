import React from "react";
import { Route, Routes } from "react-router-dom";
import { MonthlyReceipt } from "../components/Reports/ReceiptReports/MonthlyReceipt";

export const ReportRutes = () => {
  return (
    <Routes>
      <Route path="/monthly/receipt" element={<MonthlyReceipt />} />
    </Routes>
  );
};
