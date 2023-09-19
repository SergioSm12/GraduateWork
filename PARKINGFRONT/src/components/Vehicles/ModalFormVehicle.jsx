import React from "react";
import { useVehicle } from "../../hooks/useVehicle";
import { CreateVehicle } from "./CreateVehicle";

export const ModalFormVehicle = () => {
  const { vehicleSelected, handlerCloseFormVehicle } = useVehicle();
  return (
    <div className="abrir-modal animacion fadeIn">
      <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center transition-opacity duration-300">
        <div className="bg-secondary-900 p-8  rounded-lg ">
          <CreateVehicle
            handlerCloseFormVehicle={handlerCloseFormVehicle}
            vehicleSelected={vehicleSelected}
          />
        </div>
      </div>
    </div>
  );
};
