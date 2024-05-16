import React, { useEffect } from "react";
import { useBuildings } from "../../hooks/useBuildings";
import { RiAlertLine, RiDeleteBin5Line, RiEdit2Line } from "react-icons/ri";

export const DataTableBuilding = () => {
  const {
    buildings,
    getBuildings,
    handlerBuildingSelected,
    handlerRemoveBuilding,
  } = useBuildings();


  useEffect(() => {
    getBuildings();
  }, []);

  return (
    <>
      {buildings.length > 0 ? (
        <>
          <div className="text-white text-xl md:text-2xl mb-4 ">
            <h1>Edificios:</h1>
          </div>
          <div className="overflow-x-auto">
            <table className="table-auto min-w-full border">
              <thead>
                <tr className="text-center">
                  <th className="border">Edificio</th>
                  <th colSpan={2}>Opciones</th>
                </tr>
              </thead>
              <tbody className="">
                {buildings.map((building) => (
                  <tr
                    key={building.id}
                    className="bg-secondary-900 text-center"
                  >
                    <td className="border bg-secondary-900 p-2">
                      {building.name}
                    </td>

                    <td className="border bg-secondary-900 flex items-center justify-center gap-2 px-2 py-2">
                      <button
                        type="button"
                        className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                        onClick={() => {
                          handlerBuildingSelected({
                            id: building.id,
                            name: building.name,
                            carSpaces: building.carSpaces,
                            motorcycleSpaces: building.motorcycleSpaces,
                          });
                        }}
                      >
                        <RiEdit2Line className="text-xl" />
                      </button>{" "}
                      <button
                        className="py-2 px-4 bg-secondary-100/50 hover:bg-secondary-100 text-red-500/70 hover:text-red-500
          transition-colors rounded-lg  "
                        onClick={() => handlerRemoveBuilding(building.id)}
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
            <RiAlertLine className="text-red-600" /> No hay edificios
            registrados en la base de datos
          </span>
        </div>
      )}
    </>
  );
};
