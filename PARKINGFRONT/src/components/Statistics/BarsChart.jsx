import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

import React from "react";
import { color } from "chart.js/helpers";

export const BarsChart = ({
  totalUnpaidState,
  totalPaidState,
  totalState,
  totalUnpaidVisitorState,
  totalPaidVistorState,
  totalVisitorReceipt,
  totalNightUnPaidState,
  totalNightPaidState,
  totalNightState,
}) => {
  const recibos = [
    "Recibos diurnos",
    "Recibos visitantes",
    "Recibos nocturnos",
 
  ];
  const recibosPagos = [
    totalPaidState,
    totalPaidVistorState,
    totalNightPaidState,
  ];
  const recibosPendientes = [
    totalUnpaidState,
    totalUnpaidVisitorState,
    totalNightUnPaidState,
  ];



  const recibosTotales = [totalState, totalVisitorReceipt, totalNightState];

  const options = {
    responsive: true,
    animation: true,
    plugins: {
      legend: {
        display: true,
      },
    },
    scales: {
      y: {
        min: 0,
        max: 160,
        grid:{
            color: "rgba(255, 255, 255, 0.2)"
        }
      },
      x: {
        ticks: { color: "rgb(20, 219, 255)" },
        grid:{
            color: "rgba(255, 255, 255, 0.2)"
        }
      },
    },
  };

  const data = {
    labels: recibos,
    datasets: [
      {
        label: "Recibos totales",
        data: recibosTotales,
        backgroundColor: "rgba(20, 219, 255, 0.5)",
      },
      {
        label: "Recibos pagos",
        data: recibosPagos,
        backgroundColor: "rgba(9, 254, 2, 0.5)",
      },
      {
        label: "Recibos pendientes",
        data: recibosPendientes,
        backgroundColor: "rgba(254, 1, 0, 0.5)",
      },
      
    ],
  };

  return (
    <Bar
      data={data}
      options={options}
      className="bg-secondary-900 rounded-lg"
    />
  );
};
