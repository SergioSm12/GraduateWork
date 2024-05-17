import React, { useEffect } from "react";
import { useCapacity } from "../../hooks/useCapacity";
import { RiAlertLine, RiDeleteBin5Line, RiEdit2Line } from "react-icons/ri";

export const DataTableCapacity = () => {
  const {
    capacities,
    getCapacities,
    handlerCapacitySelected,
    handlerRemoveCapacity,
  } = useCapacity();

  useEffect(() => {
    getCapacities();
  }, []);
  return (
    <>
      {capacities.length > 0 ? (
        <>
          <div className="text-white text-xl md:text-2xl mb-4">
            <h1>Aforos:</h1>
          </div>
          <div className="overflow-x-auto">
            <table className="table-auto min-w-full border">
              <thead>
                <tr className="text-center">
                  <th className="border">Edificio</th>
                  <th className="border">Vehiculo</th>
                  <th className="border">Espacios disponibles</th>
                  <th className="border">Espacios Ocupados</th>
                  <th className="border"> Espacios Totales</th>
                  <th colSpan={2}>Opciones</th>
                </tr>
              </thead>
              <tbody>
                {capacities.map((capacity) => (
                  <tr
                    key={capacity.id}
                    className="bg-secondary-900 text-center"
                  >
                    <td className="border bg-secondary-900 p-2">
                      {capacity.building.name}
                    </td>
                    <td className="border bg-secondary-900 p-2">
                      {capacity.vehicleType.name}
                    </td>
                    <td className="border bg-secondary-900 p-2">
                      {capacity.parkingSpaces}
                    </td>
                    <td className="border bg-secondary-900 p-2">
                      {capacity.occupiedSpaces}
                    </td>
                    <td className="border bg-secondary-900 p-2">
                      {capacity.maxParking}
                    </td>
                    <td className="border bg-secondary-900 flex items-center justify-center gap-2 px-2 py-2">
                      <button
                        type="button"
                        className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                        onClick={() => {
                          handlerCapacitySelected({
                            id: capacity.id,
                            building: capacity.building,
                            vehicleType: capacity.vehicleType,
                            parkingSpaces: capacity.parkingSpaces,
                          });
                        }}
                      >
                        <RiEdit2Line className="text-xl" />
                      </button>{" "}
                      <button
                        className="py-2 px-4 bg-secondary-100/50 hover:bg-secondary-100 text-red-500/70 hover:text-red-500
          transition-colors rounded-lg  "
                        onClick={() => handlerRemoveCapacity(capacity.id)}
                      >
                        <RiDeleteBin5Line className="text-xl" />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      ) : (
        <div className="flex justify-center">
          <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
            <RiAlertLine className="text-red-600" /> No hay aforo registrados en
            la base de datos
          </span>
        </div>
      )}
    </>
  );
};
