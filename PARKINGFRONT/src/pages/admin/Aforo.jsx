import React from "react";
import { Link } from "react-router-dom";
import { CreateBuilding } from "../../components/Aforo/CreateBuilding";
import { DataTableBuilding } from "../../components/Aforo/DataTableBuilding";
import { useBuildings } from "../../hooks/useBuildings";
import { CreateCapacity } from "../../components/Aforo/CreateCapacity";
import { DataTableCapacity } from "../../components/Aforo/DataTableCapacity";
import { useCapacity } from "../../hooks/useCapacity";

export const Aforo = () => {
  const { buildingSelected } = useBuildings();
  const { capacitySelected } = useCapacity();
  return (
    <>
      <div className="mb-10">
        <h1 className="font-bold text-gray-100 text-xl">Control de aforo</h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Link>Principal</Link>
          <span>-</span>
          <span>Centro de administración de tarifas</span>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div className="bg-secondary-100 p-8 rounded-lg">
          <div className="flex justify-center">
            <CreateBuilding buildingSelected={buildingSelected} />
          </div>
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg">
          <div >
            <DataTableBuilding />
          </div>
        </div>
        <div className="bg-secondary-100 p-8 rounded-lg ">
          <div className=" flex justify-center">
            <CreateCapacity capacitySelected={capacitySelected} />
          </div>
        </div>

        <div className="bg-secondary-100 p-8 rounded-lg">
          <DataTableCapacity />
        </div>
      </div>
    </>
  );
};
