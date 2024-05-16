import React, { useEffect } from "react";
import { useBuildings } from "../../hooks/useBuildings";
import { useVehicle } from "../../hooks/useVehicle";
import { useState } from "react";
import { useCapacity } from "../../hooks/useCapacity";
import {
  RiBuildingLine,
  RiParkingBoxLine,
  RiPoliceCarLine,
} from "react-icons/ri";

export const CreateCapacity = ({ capacitySelected }) => {
  const { initialCapacityForm, handlerAddCapacity, errorsCapacity } =
    useCapacity();
  const { buildings } = useBuildings();
  const { vehicleTypes, getVehicleTypes } = useVehicle();
  const [capacityForm, setCapacityForm] = useState(initialCapacityForm);
  const [selectedTypeVehicle, setSelectedTypeVehicle] = useState(null);
  const [selectedBuilding, setSelectedBuilding] = useState(null);

  useEffect(() => {
    getVehicleTypes();
  }, []);

  useEffect(() => {
    if (capacitySelected) {
      setCapacityForm({
        ...capacityForm,
        id: capacitySelected.id,
        building: capacitySelected.building,
        vehicleType: capacitySelected.vehicleType,
        parkingSpaces: capacitySelected.parkingSpaces,
      });
      setSelectedBuilding(capacitySelected.building);
      setSelectedTypeVehicle(capacitySelected.vehicleType);
    }
  }, [capacitySelected]);

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    if (name == "building") {
      const selectedBuilding = value;
      const building = buildings.find(
        (b) => b.id === parseInt(selectedBuilding)
      );
      setSelectedBuilding(building);

      setCapacityForm({
        ...capacityForm,
        [name]: building,
      });
    } else if (name == "vehicleType") {
      const selectedVehicleType = value;
      const vehicleType = vehicleTypes.find(
        (vt) => vt.id === parseInt(selectedVehicleType)
      );
      setSelectedTypeVehicle(vehicleType);

      setCapacityForm({
        ...capacityForm,
        [name]: vehicleType,
      });
    } else {
      setCapacityForm({
        ...capacityForm,
        [name]: value,
      });
    }
  };

  const handlerCancelEdit = () => {
    setCapacityForm(initialCapacityForm);
    setSelectedTypeVehicle(null);
    setSelectedBuilding(null);
  };

  const onSubmit = (e) => {
    e.preventDefault();
    handlerAddCapacity(capacityForm);
    setSelectedBuilding(null);
    setSelectedTypeVehicle(null);
  };

  return (
    <div className="bg-secondary-900 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          {capacityForm.id > 0 ? "Editar" : "Crear"}{" "}
          <span className="text-primary">aforo</span>
        </h1>
      </div>
      <form className="mb-8" onSubmit={onSubmit}>
        <div className="relative ">
          <RiBuildingLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <select
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
            id="building"
            name="building"
            value={selectedBuilding ? selectedBuilding.id : ""}
            onChange={onInputChange}
          >
            <option defaultValue="">Edificio</option>
            {buildings.map((building) => (
              <option key={building.id} value={building.id}>
                {building.name}
              </option>
            ))}
          </select>
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">
            {errorsCapacity?.building == "Debe seleccionar un edificio"
              ? JSON.stringify(errorsCapacity?.vehicleType)
              : ""}
          </p>
        </div>
        <div className="relative ">
          <RiPoliceCarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <select
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
            id="vehicleType"
            name="vehicleType"
            value={selectedTypeVehicle ? selectedTypeVehicle.id : ""}
            onChange={onInputChange}
          >
            <option defaultValue="">Tipo de vehiculo</option>
            {vehicleTypes.map((vehicleType) => (
              <option key={vehicleType.id} value={vehicleType.id}>
                {vehicleType.name}
              </option>
            ))}
          </select>
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">
            {errorsCapacity?.vehicleType ==
            "Debe seleccionar un tipo de vehiculo."
              ? JSON.stringify(errorsCapacity?.vehicleType)
              : ""}
          </p>
        </div>
        <div className="relative ">
          <RiParkingBoxLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="number"
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Espacios disponibles"
            name="parkingSpaces"
            value={capacityForm.parkingSpaces}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errorsCapacity?.parkingSpaces}</p>
        </div>
        <div>
          <div className="flex items-center justify-center gap-2">
            <button
              type="submit"
              className="bg-primary/80 hover:bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
            >
              {capacityForm.id > 0 ? "Editar" : "Crear"}
            </button>

            {capacityForm.id > 0 ? (
              <button
                type="button"
                className=" py-3 px-4 text-red-600 hover:text-black bg-secondary-100/80  hover:bg-red-600 rounded-lg  transition-colors"
                onClick={() => handlerCancelEdit()}
              >
                Cancelar
              </button>
            ) : (
              ""
            )}
          </div>
        </div>
      </form>
    </div>
  );
};
