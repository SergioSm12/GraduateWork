import { GenerateReportPDF } from "../PDF/GenerateReportPDF";

export const DataTableReports = ({ dataReports }) => {
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
    }).format(amount);
  };
  // Determinar qué propiedad (mes, quincena o semana) mostrar en el encabezado
  let headerValue = "";
  if (dataReports?.Mes) {
    headerValue = dataReports.Mes;
  } else if (dataReports?.quincena) {
    headerValue = dataReports.quincena;
  } else if (dataReports?.semana) {
    headerValue = dataReports.semana;
  }

  return (
    <div className="overflow-x-auto">
      <table className="table-auto min-w-full border">
        <thead>
          <tr className="text-center">
            <th className="border">{headerValue}</th>
            <th className="border p-2">
            <GenerateReportPDF dataReports={dataReports} />
            </th>
          </tr>
          <tr className="text-center">
            <th className="border">Fecha y detalles</th>
            <th className="border">Saldo</th>
          </tr>
        </thead>
        <tbody>
          {dataReports &&
            Object.entries(dataReports)
              .filter(
                ([key]) =>
                  key !== "Mes" && key !== "quincena" && key !== "semana"
              ) // Filtrar la entrada "mes", "quincena", "semana"
              .map(([key, value]) => (
                <tr
                  key={key}
                  className="border bg-secondary-900 p-2 text-center"
                >
                  <td className="border">{key}</td>
                  <td className="border">{formatCurrency(value)}</td>
                </tr>
              ))}
        </tbody>
      </table>
    </div>
  );
};
