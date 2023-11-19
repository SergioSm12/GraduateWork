import React, { useEffect, useState } from "react";
import {
  RiCloseCircleLine,
  RiParkingBoxLine,
  RiPoliceCarLine,
} from "react-icons/ri";
import { useVehicle } from "../../hooks/useVehicle";
import { useParams } from "react-router-dom";
export const CreateVehicle = ({ handlerCloseFormVehicle, vehicleSelected }) => {
  const {
    handlerAddVehicle,
    initialVehicleForm,
    errorsVehicle,
    getVehicleTypes,
    vehicleTypes,
    getVehicles,
    getVehiclesActive,
    getVehiclesInactive,
  } = useVehicle();
  const [vehicleForm, setVehicleForm] = useState(initialVehicleForm);
  const [selectedTypeVehicle, setSelectedTypeVehicle] = useState(null);
  const { id } = useParams();

  useEffect(() => {
    getVehicleTypes();
  }, []);

  useEffect(() => {
    if (vehicleSelected) {
      setVehicleForm({
        ...vehicleForm,
        id: vehicleSelected.id,
        plate: vehicleSelected.plate,
        vehicleType: vehicleSelected.vehicleType,
      });
      setSelectedTypeVehicle(vehicleSelected.vehicleType);
    }
  }, [vehicleSelected]);

  const onCloseForm = () => {
    handlerCloseFormVehicle();
  };

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    //validar type Vehicle y traer
    if (name === "vehicleType") {
      const selectedTypeVehicleId = value;
      const typeVehicle = vehicleTypes.find(
        (tV) => tV.id === parseInt(selectedTypeVehicleId)
      );
      setSelectedTypeVehicle(typeVehicle);

      setVehicleForm({
        ...vehicleForm,
        [name]: typeVehicle,
      });
    } else {
      setVehicleForm({
        ...vehicleForm,
        [name]: value,
      });
    }
  };

  const onSubmit = (e) => {
    e.preventDefault();

    handlerAddVehicle(id, vehicleForm).then(() => {
      getVehicles(id);
      getVehiclesActive(id);
      getVehiclesInactive(id);
    });
  };


  return (
    <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          {vehicleForm.id > 0 ? "Editar" : "Registrar"}{" "}
          <span className="text-primary">vehiculo</span>
        </h1>
        <button
          className=" py-2 px-2 text-red-600 hover:text-black bg-secondary-900/80  hover:bg-red-600/50 rounded-lg  transition-colors"
          type="button"
          onClick={() => onCloseForm()}
        >
          <RiCloseCircleLine className="text-2xl " />
        </button>
      </div>
      <form className="mb-8" onSubmit={onSubmit}>
        <div className="relative ">
          <RiParkingBoxLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Placa"
            name="plate"
            value={vehicleForm.plate}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errorsVehicle.plate}</p>
        </div>
        <div className="relative ">
          <RiPoliceCarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <select
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
            id="vehicleType"
            name="vehicleType"
            value={selectedTypeVehicle ? selectedTypeVehicle.id : ""}
            onChange={onInputChange}
          >
            <option defaultValue={-1}>Tipo de vehiculo</option>
            {vehicleTypes.map((vehicleType) => (
              <option key={vehicleType.id} value={vehicleType.id}>
                {vehicleType.name}
              </option>
            ))}
          </select>
        </div>

        <div className="relative mb-8">
          <p className="text-red-500">
            {errorsVehicle?.vehicleType ==
            "Debe seleccionar un tipo de vehiculo."
              ? JSON.stringify(errorsVehicle?.vehicleType)
              : ""}
          </p>
        </div>
        <div>
          <button
            type="submit"
            className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
          >
            {vehicleForm.id > 0 ? "Editar" : "Registrar"}
          </button>
        </div>
      </form>
    </div>
  );
};
