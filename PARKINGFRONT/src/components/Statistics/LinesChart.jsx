import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

const beneficios = [
  200000, 3000000, 1000000, 150000, 80000, 2000000, 2500000, 6000000, 3000000,
  5000000, 1800000, 7890000,
];

const meses = [
  "Enero",
  "Febrero",
  "Marzo",
  "Abril",
  "Mayo",
  "Junio",
  "Julio",
  "Agosto",
  "Septiembre",
  "Octubre",
  "Noviembre",
  "Diciembre",
];

export const LinesChart = ({ dataReport }) => {
  if (!dataReport) {
    return <div>No hay datos disponibles para mostrar.</div>;
  }
  const fechas = Object.keys(dataReport).filter(
    (key) =>
      !key.toLowerCase().includes("saldo") &&
      !key.toLowerCase().includes("total") &&
      !key.toLowerCase().includes("mes") 

  );

  const ingresos = fechas.map((fecha) => dataReport[fecha]);

  const midata = {
    labels: fechas,
    datasets: [
      // Cada una de las líneas del gráfico
      {
        label: `INGRESOS ${dataReport.Mes}`,
        data: ingresos,
        tension: 0.5,
        fill: true,
        borderColor: "rgb(20, 219, 255)",
        backgroundColor: "rgba(20, 219, 255, 0.5)",
        pointRadius: 5,
        pointBorderColor: "rgba(20, 219, 255)",
        pointBackgroundColor: "rgba(20, 219, 255)",
      },
    ],
  };

  const misoptions = {
    scales: {
      y: {
        min: 0,
        grid: {
          color: "rgba(255, 255, 255, 0.2)",
        },
      },
      x: {
        ticks: { color: "rgb(20, 219, 255)" },
        grid: {
          color: "rgba(255, 255, 255, 0.2)",
        },
      },
    },
  };
  return (
    <Line
      data={midata}
      options={misoptions}
      className="bg-secondary-900/70 rounded-lg"
    />
  );
};
