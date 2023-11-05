import React, { useEffect, useState } from "react";
import {
  RiMoneyDollarCircleLine,
  RiPoliceCarLine,
  RiTimerLine,
} from "react-icons/ri";
import { useRates } from "../../hooks/useRates";
import { useVehicle } from "../../hooks/useVehicle";
export const CreateRate = ({ rateSelected }) => {
  const { initialRateForm, handlerAddRate, errorsRate } = useRates();
  const { vehicleTypes, getVehicleTypes } = useVehicle();
  const [rateForm, setRateForm] = useState(initialRateForm);
  const [selectedTypeVehicle, setSelectedTypeVehicle] = useState(null);

  useEffect(() => {
    getVehicleTypes();
  }, []);

  useEffect(() => {
    if (rateSelected) {
      setRateForm({
        ...rateForm,
        id: rateSelected.id,
        time: rateSelected.time,
        amount: rateSelected.amount,
        vehicleType: rateSelected.vehicleType,
      });
      setSelectedTypeVehicle(rateSelected.vehicleType);
    }
  }, [rateSelected]);

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    //validar rate y traer
    if (name == "vehicleType") {
      const selectedTypeVehicle = value;
      const vehicleType = vehicleTypes.find(
        (vt) => vt.id === parseInt(selectedTypeVehicle)
      );
      setSelectedTypeVehicle(vehicleType);

      setRateForm({
        ...rateForm,
        [name]: vehicleType,
      });
    } else {
      setRateForm({
        ...rateForm,
        [name]: value,
      });
    }
  };

  const handlerCancelEdit = () => {
    setRateForm(initialRateForm);
    setSelectedTypeVehicle(null);
  };

  const onSubmit = (e) => {
    e.preventDefault();
    handlerAddRate(rateForm);
    setRateForm(initialRateForm);
    setSelectedTypeVehicle(null);
  };

  return (
    <div className="bg-secondary-900 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          {rateForm.id > 0 ? "Editar" : "Crear"}{" "}
          <span className="text-primary">tarifa</span>
        </h1>
      </div>
      <form className="mb-8" onSubmit={onSubmit}>
        <div className="relative ">
          <RiMoneyDollarCircleLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="number"
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Valor"
            name="amount"
            value={rateForm.amount}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errorsRate?.amount}</p>
        </div>
        <div className="relative ">
          <RiTimerLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-100 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Tiempo (Dia, Mes, Quincena, etc)"
            name="time"
            value={rateForm.time}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errorsRate?.time}</p>
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

        <div className="relative mb-8">
          <p className="text-red-500">
            {errorsRate?.vehicleType == "Debe seleccionar un tipo de vehiculo."
              ? JSON.stringify(errorsRate?.vehicleType)
              : ""}
          </p>
        </div>
        <div>
          <div className="flex items-center justify-center gap-2">
            <button
              type="submit"
              className="bg-primary/80 hover:bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
            >
              {rateForm.id > 0 ? "Editar" : "Crear"}
            </button>

            {rateForm.id > 0 ? (
              <button
                type="button"
                className=" py-3 px-4 text-red-600 hover:text-black bg-secondary-100/80  hover:bg-red-600 rounded-lg  transition-colors"
                onClick={() => handlerCancelEdit()}
              >
                Cancelar
              </button>
            ):("")}
          </div>
        </div>
      </form>
    </div>
  );
};
