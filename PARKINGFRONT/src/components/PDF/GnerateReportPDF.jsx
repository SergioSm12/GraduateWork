import React from "react";
import { FaRegFilePdf } from "react-icons/fa6";

export const GnerateReportPDF = () => {
  return (
    <button className="p-2  bg-secondary-900 rounded-lg   hover:border hover:text-red-500 border-primary/80 transition-colors">
      {" "}
      <FaRegFilePdf />
    </button>
  );
};
