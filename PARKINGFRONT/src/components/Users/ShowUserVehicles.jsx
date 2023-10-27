import React, { useEffect, useState } from "react";
import { useUsers } from "../../hooks/useUsers";
import { RiAlertLine, RiRoadsterLine } from "react-icons/ri";
import { useVehicle } from "../../hooks/useVehicle";
import { ModalFormVehicle } from "../Vehicles/ModalFormVehicle";
import { ListVehicles } from "../Vehicles/ListVehicles";


export const ShowUserVehicles = ({ userByid }) => {
 
  const { initialUserForm } = useUsers();
  const { visibleFormVehicle, handlerOpenFormVehicle } = useVehicle();
  const [userShow, setUserShow] = useState(initialUserForm);

  useEffect(() => {
    setUserShow({
      ...userByid,
      password: "",
    });
  }, [userByid]);

  return (
    <>
      {!visibleFormVehicle || <ModalFormVehicle />}
      <div className="flex flex-col bg-secondary-900 p-4 rounded-lg">
        <div className="flex justify-end mb-3">
          {visibleFormVehicle || (
            <button
              className="flex items-center gap-2 font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors "
              onClick={handlerOpenFormVehicle}
            >
              Agregar Vehiculo <RiRoadsterLine />
            </button>
          )}
        </div>
        {userShow.vehicles.length > 0 ? (
          <ListVehicles />
        ) : (
          <div className=" flex justify-center">
            <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
              <RiAlertLine className="text-red-600 md:text-4xl" />
              <span className="md:text-xs">
                No hay vehiculos registrados para {userShow.name}
              </span>
            </span>
          </div>
        )}
      </div>
    </>
  );
};
