import React from "react";
import { useCapacity } from "../../hooks/useCapacity";
import { RiAddFill, RiAlertLine, RiSubtractFill } from "react-icons/ri";

export const DataTableAforo = () => {
  const { capacities, handlerVehicleEntry, handlerVehicleExit } = useCapacity();
  

  return (
    <>
      {capacities.length == 0 ? (
        <>
          <div className=" flex justify-center mt-2">
            <span className="flex items-center gap-2  bg-secondary-100 py-4 px-4 rounded-lg">
              <RiAlertLine className="text-red-600" /> No hay espacios
              registrados.
            </span>
          </div>
        </>
      ) : (
        <>
          <table className="table text-gray-400 border-separate space-y-6 text-sm ">
            <thead className="bg-gray-800 text-white/80">
              <tr>
                <th className="p-3 br">Edificios</th>
                <th className="p-3 text-center">Tipo</th>
                <th className="p-3 text-center">Espacios disponibles</th>
                <th className="p-3 text-center hidden md:table-cell">
                  Espacios ocupados
                </th>
                <th className="p-3 text-center hidden md:table-cell">
                  Espacios totales
                </th>
                <th className="p-3 text-center border-r ">Acciones</th>
              </tr>
            </thead>
            <tbody>
              {capacities.map((capacity) => (
                <tr className="bg-gray-800 text-center " key={capacity.id}>
                  <td className="p-3">{capacity.building.name}</td>
                  <td className="p-3">{capacity.vehicleType.name}</td>
                  <td className="p-3 font-bold">
                    <span className="bg-secondary-900 rounded-md p-2">
                      {capacity.parkingSpaces}
                    </span>
                  </td>
                  <td className="p-3 hidden md:table-cell">
                    {capacity.occupiedSpaces}
                  </td>
                  <td className="p-3 hidden md:table-cell">
                    {capacity.maxParking}
                  </td>
                  <td className="p-3 flex items-center justify-center gap-2 border-r">
                    <button
                      className="p-2 flex items-center justify-center gap-1 rounded-lg bg-secondary-900 hover:text-red-500 hover:border  mr-2 transition-all"
                      onClick={() => {
                        handlerVehicleExit(capacity.id);
                      }}
                    >
                      <RiSubtractFill />

                      <span className="text-xs hidden md:inline">Salida</span>
                    </button>
                    <button
                      className="p-2 flex items-center justify-center gap-1 bg-secondary-900 rounded-lg hover:text-primary hover:border transition-all"
                      onClick={() => {
                        handlerVehicleEntry(capacity.id);
                      }}
                    >
                      <RiAddFill />
                      <span className="text-xs hidden md:inline">Entrada</span>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <style>{`
        .table {
          border-spacing: 0 15px;
        }

        .table tr {
          border-radius: 20px;
        }

        tr td:nth-child(n + 6),
        tr th:nth-child(n + 6) {
          border-radius: 0 0.625rem 0.625rem 0;
        }

        tr td:nth-child(1),
        tr th:nth-child(1) {
          border-radius: 0.625rem 0 0 0.625rem;
        }
      `}</style>
        </>
      )}
    </>
  );
};
