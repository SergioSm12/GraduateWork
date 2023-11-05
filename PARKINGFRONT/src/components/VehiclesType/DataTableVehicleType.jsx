import React from "react";
import { useVehicleType } from "../../hooks/useVehicleType";
import { RiDeleteBin5Line, RiEdit2Line } from "react-icons/ri";

export const DataTableVehicleType = () => {
  const {
    vehicleTypes,
    handlerVehicleTypeSelectedForm,
    handlerRemoveVehicleType,
  } = useVehicleType();
  return (
    <div className="overflow-x-auto">
      <table className="table-auto min-w-full  border ">
        <thead>
          <tr>
            <th>Id</th>
            <th>Tipo</th>
          </tr>
        </thead>
        <tbody className="">
          {vehicleTypes.map((vt) => (
            <tr key={vt.id}>
              <td className="border bg-secondary-900 p-2 text-center">
                {vt.id}
              </td>
              <td className="border bg-secondary-900 p-2 text-center">
                {vt.name}
              </td>
              <td className="border bg-secondary-900 flex items-center justify-center gap-2 px-2 py-2">
                <button
                  type="button"
                  className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                  onClick={() => {
                    handlerVehicleTypeSelectedForm({
                      id: vt.id,
                      name: vt.name,
                    });
                  }}
                >
                  <RiEdit2Line className="text-xl" />
                </button>{" "}
                <button
                  type="button"
                  className="py-2 px-4 bg-secondary-100/50 hover:bg-secondary-100 text-red-500/70 hover:text-red-500
                 transition-colors rounded-lg  "
                  onClick={() => handlerRemoveVehicleType(vt.id)}
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
