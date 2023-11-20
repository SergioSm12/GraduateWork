import React from "react";
import { RiDeleteBin5Line, RiEdit2Line } from "react-icons/ri";
import { useRates } from "../../hooks/useRates";
import { vehicleType } from "../../store/slices/rate/rateSlice";

// funcion de formato moneda
const formatCurrency = (amount) => {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
  }).format(amount);
};
export const DataTableRate = () => {
  const { rates, handlerRateSelectedForm, handlerRemoveRate } = useRates();
  return (
    <div className="overflow-x-auto">
      <table className="table-auto min-w-full  border ">
        <thead>
          <tr>
            <th>Tiempo</th>
            <th>Valor</th>
            <th>Vehiculo</th>
            <th>Opciones</th>
          </tr>
        </thead>
        <tbody className="">
          {rates.map((rate) => (
            <tr key={rate.id}>
              <td className="border bg-secondary-900 p-2">{rate.time}</td>
              <td className="border bg-secondary-900 p-2">
                {formatCurrency(rate.amount)}
              </td>
              <td className="border bg-secondary-900 p-2">
                {rate.vehicleType.name}
              </td>
              <td className="border bg-secondary-900 flex items-center justify-center gap-2 px-2 py-2">
                <button
                  type="button"
                  className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                  onClick={() => {
                    handlerRateSelectedForm({
                      id: rate.id,
                      time: rate.time,
                      amount: rate.amount,
                      vehicleType: rate.vehicleType,
                    });
                  }}
                >
                  <RiEdit2Line className="text-xl" />
                </button>{" "}
                <button
                  className="py-2 px-4 bg-secondary-100/50 hover:bg-secondary-100 text-red-500/70 hover:text-red-500
                 transition-colors rounded-lg  "
                  onClick={() => handlerRemoveRate(rate.id)}
                >
                  <RiDeleteBin5Line className="text-xl" />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
