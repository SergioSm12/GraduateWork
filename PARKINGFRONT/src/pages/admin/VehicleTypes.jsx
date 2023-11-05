import React, { useEffect } from "react";
import { RiAlertLine } from "react-icons/ri";
import { Link } from "react-router-dom";
import { useVehicleType } from "../../hooks/useVehicleType";
import { DataTableVehicleType } from "../../components/VehiclesType/DataTableVehicleType";
import { CreateVehicleType } from "../../components/VehiclesType/CreateVehicleType";

export const VehicleTypes = () => {
  const { vehicleTypes, getVehicleTypes,vehicleTypeSelected  } = useVehicleType();
  useEffect(() => {
    getVehicleTypes();
  }, []);
  return (
    <>
      {/*Title */}
      <div className="mb-10 ">
        <h1 className="font-bold text-gray-100 text-xl">Tipos de vehiculo.</h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary transition-colors">
            Principal
          </Link>
          <span>-</span>
          <span>Centro de administración de tipo de vehículos.</span>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/*Card create vehicle Type */}
        <div className="bg-secondary-100 p-8 rounded-lg">
          <div>
            <CreateVehicleType vehicleTypeSelected={vehicleTypeSelected} />
          </div>
        </div>
        {/*Card Listar rate*/}
        <div className="bg-secondary-100 p-8 rounded-lg">
          {vehicleTypes.length > 0 ? (
            <div className="text-white text-xl md:text-2xl mb-4">
              <h1>Tipos de vehículos:</h1>
            </div>
          ) : (
            ""
          )}
          <div>
            {/*Tabla de tarifas*/}
            {vehicleTypes.length > 0 ? (
              <DataTableVehicleType />
            ) : (
              <div className=" flex justify-center">
                <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                  <RiAlertLine className="text-red-600" /> No hay tipos de
                  vehículos registrados en la base de datos.
                </span>
              </div>
            )}
          </div>
        </div>
      </div>
    </>
  );
};
