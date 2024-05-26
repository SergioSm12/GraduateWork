import React, { useEffect } from "react";
import { useAuth } from "../../auth/hooks/useAuth";
import { DataTableAforo } from "../../components/HomeSecurityGuard/DataTableAforo";
import { useReceipts } from "../../hooks/useReceipts";
import { useVisitorReceipt } from "../../hooks/useVisitorReceipt";
import { useNightlyReceipts } from "../../hooks/useNightlyReceipts";
import { Tab } from "@headlessui/react";
import { DataTableReceipt } from "../../components/Receipts/DataTableReceipt";
import { DataTableVisitorReceipt } from "../../components/VisitorReceipt/DataTableVisitorReceipt";
import { DataTableNightlyReceipts } from "../../components/NightlyReceipt/DataTableNightlyReceipts";
import { RiAlertLine } from "react-icons/ri";
import { useVehicle } from "../../hooks/useVehicle";
import { AllVehicles } from "../../components/Vehicles/AllVehiclesList";

export const HomeSecurityGuard = () => {
  const { login, handlerLogout } = useAuth();

  const { getReceipts, receipts } = useReceipts();
  const { getVisitorReceipts, visitorReceipts } = useVisitorReceipt();
  const { getNightlyReceipts, nightlyReceipts } = useNightlyReceipts();
  const { vehiclesAll, getVehiclesAll,totalRegisteredVehicles,getTotalRegisteredVehiclesCount } = useVehicle();

  useEffect(() => {
    getReceipts();
    getNightlyReceipts();
    getVisitorReceipts();
    getVehiclesAll();
    getTotalRegisteredVehiclesCount();
  }, []);

  return (
    <>
      <div className="mb-10 ">
        <h1 className="font-bold text-gray-100 text-xl">Modulo de seguridad</h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <span>Centro de consultas, entradas y salidas de vehiculos.</span>
        </div>
      </div>
      <div className="flex items-center justify-center  bg-gray-900 ">
        <div className="col-span-12">
          <div className="overflow-auto lg:overflow-visible">
            <DataTableAforo />
          </div>
        </div>
      </div>
      <div className="bg-secondary-100 p-8 rounded-xl">
        {receipts.length === 0 &&
        visitorReceipts.length === 0 &&
        nightlyReceipts.length === 0 ? (
          <div className=" flex justify-center mt-2">
            <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
              <RiAlertLine className="text-red-600" /> No hay recibos
              registrados
            </span>
          </div>
        ) : (
          <Tab.Group>
            <Tab.List className="flex flex-col md:flex-row md:items-center md:justify-between gap-x-2 gap-y-6 bg-secondary-900/50 py-3 px-4 rounded-lg mb-8">
              <div className="flex flex-col md:flex-row md:items-center gap-2">
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Diurnos
                </Tab>
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Visitantes
                </Tab>
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Nocturnos
                </Tab>
              </div>
            </Tab.List>
            <Tab.Panels>
              <Tab.Panel>
                <DataTableReceipt dataReceipts={receipts} />
              </Tab.Panel>
              <Tab.Panel>
                <DataTableVisitorReceipt dataReceipts={visitorReceipts} />
              </Tab.Panel>
              <Tab.Panel>
                <DataTableNightlyReceipts dataReceipts={nightlyReceipts} />
              </Tab.Panel>
            </Tab.Panels>
          </Tab.Group>
        )}
      </div>
      <div className="bg-secondary-100 rounded-lg p-8 mt-8">
        <div className="bg-secondary-900  text-center p-2 rounded-lg mb-3">
          <h3>
            Total de vehiculos registrados:{" "}
            <span className="text-primary font-bold">
              {totalRegisteredVehicles}
            </span>
          </h3>
        </div>
        <div>
          <AllVehicles dataVehicles={vehiclesAll} />
        </div>
      </div>
    </>
  );
};
