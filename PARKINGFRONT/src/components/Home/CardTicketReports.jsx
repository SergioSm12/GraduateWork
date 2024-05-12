import React from "react";
import { BarsChart } from "../Statistics/BarsChart";

export const CardTicketReports = ({
  totalPaidState,
  totalUnpaidState,
  totalState,
  totalUnpaidVisitorState,
  totalPaidVistorState,
  totalVisitorReceipt,
  totalNightUnPaidState,
  totalNightPaidState,
  totalNightState,
}) => {
  return (
    <div className=" flex justify-center bg-secondary-100 p-8 rounded-xl overflow-auto ">
      <div className=" w-[450px] h-[230px]  ">

        <BarsChart
          totalUnpaidState={totalUnpaidState}
          totalPaidState={totalPaidState}
          totalState={totalState}
          totalUnpaidVisitorState={totalUnpaidVisitorState}
          totalPaidVistorState={totalPaidVistorState}
          totalVisitorReceipt={totalVisitorReceipt}
          totalNightUnPaidState={totalNightUnPaidState}
          totalNightPaidState={totalNightPaidState}
          totalNightState={totalNightState}
          
        />
      </div>
    </div>
  );
};
