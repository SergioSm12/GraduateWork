import React, { useEffect } from "react";
import { useVehicle } from "../../hooks/useVehicle";
import {
  RiDeleteBin7Line,
  RiDeleteBinLine,
  RiEdit2Line,
  RiMoreLine,
  RiSettings5Line,
  RiTicket2Line,
} from "react-icons/ri";
import "@szhsin/react-menu/dist/index.css";
import "@szhsin/react-menu/dist/transitions/slide.css";
import { Menu, MenuItem, MenuButton } from "@szhsin/react-menu";
import { Link } from "react-router-dom";
export const DataTableVehicle = ({ id }) => {
  const { vehicles, getVehicles } = useVehicle();

  useEffect(() => {
    getVehicles(id);
  }, []);
  console.log(vehicles);
  return (
    <>
      <h1 className="text-2xl text-white mb-8">Vehiculos</h1>
      <div className="bg-secondary-100 py-8 px-2 rounded-lg ">
        <div className="overflow-x-auto">
          <table className="table-auto border min-w-full ">
            <thead>
              <tr className="py-2 ">
                <th>Placa</th>
                <th>Vehiculo</th>
                <th>Opciones</th>
              </tr>
            </thead>
            <tbody>
              {vehicles.map(({ id, plate, vehicleType }) => (
                <tr key={id} className="bg-secondary-900 text-center">
                  <td className="border">{plate}</td>
                  <td className="border">{vehicleType.name}</td>
                  <td className="border flex  justify-center">
                    <Menu
                      menuButton={
                        <MenuButton
                          className="flex items-center gap-x-2 bg-primary/80 hover:bg-primary text-black
                       p-2 rounded-lg transition-colors my-2"
                        >
                          <RiSettings5Line />
                        </MenuButton>
                      }
                      align="end"
                      transition
                      menuClassName="bg-secondary-100 "
                    >
                      <MenuItem className="p-0 hover:bg-transparent">
                        <button
                          className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-2 p-2 flex-1"
                        >
                          <RiTicket2Line className="text-[#8DE800]" /> Crear
                          recibo
                        </button>
                      </MenuItem>

                      <MenuItem className="p-0 hover:bg-transparent">
                        <button
                          className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-2 p-2 flex-1"
                        >
                          <RiEdit2Line className="text-blue-500" /> Editar
                        </button>
                      </MenuItem>

                      <MenuItem className="p-0 hover:bg-transparent">
                        <button
                          className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-2 p-2 flex-1"
                        >
                          <RiDeleteBinLine className="text-red-500" /> Eliminar
                        </button>
                      </MenuItem>
                    </Menu>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </>
  );
};
