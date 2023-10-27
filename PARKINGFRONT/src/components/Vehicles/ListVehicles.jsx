import React, { useEffect } from "react";
import { useVehicle } from "../../hooks/useVehicle";

import { Tab } from "@headlessui/react";
import { useParams } from "react-router-dom";
import { useReceipts } from "../../hooks/useReceipts";
import { ModalFormReceipt } from "../Receipts/ModalFormReceipt";
import { DataTableVehicles } from "./DataTableVehicles";
import { RiAlertLine } from "react-icons/ri";
export const ListVehicles = () => {
  const {
    vehicles,
    vehiclesActive,
    vehiclesInactive,
    getVehicles,
    getVehiclesActive,
    handlerVehicleSelectedForm,
    handlerRemoveVehicle,
    handlerActivateVehicle,
    getVehiclesInactive,
  } = useVehicle();
  const { visibleFormReceiptModal, handlerOpenModalFormReceipt } =
    useReceipts();
  const { id: userId } = useParams();
  useEffect(() => {
    getVehiclesActive(userId);
    getVehiclesInactive(userId);
    getVehicles(userId);
  }, []);
  return (
    <>
      {!visibleFormReceiptModal || <ModalFormReceipt />}
      <h1 className="text-2xl text-white mb-8">Vehiculos</h1>
      <Tab.Group>
        <div className="bg-secondary-100  py-4 px-2 rounded-lg rounded-br-lg mb-4">
          <Tab.List className="flex flex-col md:flex-row md:items-center md:justify-center gap-x-2 gap-y-6 bg-secondary-900/50  py-3 px-4 rounded-lg">
            <div className="flex flex-col md:flex-row  md:items-center gap-2">
              <Tab
                className="py-0.5 px-1 rounded-lg hover:bg-secondary-900 hover:text-gray-100
               transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary "
              >
                Activos
              </Tab>
              <Tab
                className="py-0.5 px-1 rounded-lg hover:bg-secondary-900 hover:text-gray-100
               transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary "
              >
                Inactivos
              </Tab>
              <Tab
                className="py-0.5 px-1 rounded-lg hover:bg-secondary-900 hover:text-gray-100
               transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary "
              >
                Todos
              </Tab>
            </div>
          </Tab.List>
        </div>
        <Tab.Panels className="">
          <Tab.Panel>
            {/*Componente Table  vehiculos activos */}
            {vehiclesActive.length > 0 ? (
              <DataTableVehicles
                vehicles={vehiclesActive}
                handlerVehicleSelectedForm={handlerVehicleSelectedForm}
                handlerRemoveVehicle={handlerRemoveVehicle}
                handlerActivateVehicle={handlerActivateVehicle}
                handlerOpenModalFormReceipt={handlerOpenModalFormReceipt}
                userId={userId}
              />
            ) : (
              <div className=" flex justify-center">
                <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                  <RiAlertLine className="text-red-600 md:text-4xl" />
                  <span className="md:text-xs">
                    No hay vehiculos activos registrados.
                  </span>
                </span>
              </div>
            )}
          </Tab.Panel>
          <Tab.Panel>
            {/*Componente Table  vehiculos Inactivos */}
            {vehiclesInactive.length > 0 ? (
              <DataTableVehicles
                vehicles={vehiclesInactive}
                handlerVehicleSelectedForm={handlerVehicleSelectedForm}
                handlerRemoveVehicle={handlerRemoveVehicle}
                handlerActivateVehicle={handlerActivateVehicle}
                handlerOpenModalFormReceipt={handlerOpenModalFormReceipt}
                userId={userId}
              />
            ) : (
              <div className=" flex justify-center">
                <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                  <RiAlertLine className="text-red-600 md:text-4xl" />
                  <span className="md:text-xs">
                    No hay vehiculos activos registrados.
                  </span>
                </span>
              </div>
            )}
          </Tab.Panel>
          <Tab.Panel>
            {/*Componente Table vehiculos todos*/}
            <DataTableVehicles
              vehicles={vehicles}
              handlerVehicleSelectedForm={handlerVehicleSelectedForm}
              handlerRemoveVehicle={handlerRemoveVehicle}
              handlerActivateVehicle={handlerActivateVehicle}
              handlerOpenModalFormReceipt={handlerOpenModalFormReceipt}
              userId={userId}
            />
          </Tab.Panel>
        </Tab.Panels>
      </Tab.Group>
    </>
  );
};
