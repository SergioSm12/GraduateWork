import React, { useEffect, useState } from "react";
import {
  RiCloseCircleLine,
  RiPoliceCarLine,
  RiTicket2Line,
} from "react-icons/ri";
import { useReceipts } from "../../hooks/useReceipts";
import { useRates } from "../../hooks/useRates";
import { useParams } from "react-router-dom";

const formatCurrency = (amount) => {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
  }).format(amount);
};

export const CreateReceipt = () => {
  const {
    handlerCloseModalFormReceipt,
    receiptSelected,
    vehicleSelected,
    vehicle,
    handlerAddReceiptByUser,
    initialReceiptForm,
  } = useReceipts();
  const { rates, getRates } = useRates();

  const { id } = useParams();

  //estado para traer los datos del vehicle
  const [vehicleForm, setVehicleForm] = useState(vehicle);
  //Estado para guardar lso datos del recibo
  const [receiptForm, setReceiptForm] = useState(initialReceiptForm);
  const [selectedRate, setSelectedRate] = useState(null);

  useEffect(() => {
    if (vehicleSelected) {
      setVehicleForm({
        ...vehicleForm,
        id: vehicleSelected.id,
        plate: vehicleSelected.plate,
        vehicleType: vehicleSelected.vehicleType,
      });
    }
  }, [vehicleSelected]);

  useEffect(() => {
    getRates();
  }, []);
  const onCloseForm = () => {
    handlerCloseModalFormReceipt();
  };

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    /*
    if (name === "vehicle") {
      const selectVehicleId = value;
      console.log(selectVehicleId);
      if (vehicleSelected.id == selectVehicleId) {
        setReceiptForm({
          ...receiptForm,
          [name]: vehicleForm,
        });
      }
      console.log(vehicleForm);
    }*/

    if (name === "rate") {
      const rateId = value;
      const rate = rates.find((r) => r.id === parseInt(rateId));
      setSelectedRate(rate);

      setReceiptForm({
        ...receiptForm,
        [name]: rate,
      });
    } else {
      setReceiptForm({
        ...receiptForm,
        [name]: value,
      });
    }
  };

  const onSubmit = (e) => {
    e.preventDefault();
    const updatedReceiptForm = {
      ...receiptForm,
      vehicle: vehicleForm,
    };
    console.log(updatedReceiptForm);
    handlerAddReceiptByUser(id, updatedReceiptForm);
  };
  return (
    <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          Generar <span className="text-primary">Recibo</span>
        </h1>
        <button
          className=" py-2 px-2 text-red-600 hover:text-black bg-secondary-900/80  hover:bg-red-600/50 rounded-lg  transition-colors"
          type="button"
          onClick={() => onCloseForm()}
        >
          <RiCloseCircleLine className="text-2xl " />
        </button>
      </div>
      <form onSubmit={onSubmit}>
        <div className="relative mb-2">
          <RiPoliceCarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary " />
          <div
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
            id="vehicle"
          >
            {`${vehicleForm.plate} - ${vehicleForm.vehicleType.name}`}
          </div>
        </div>
        <div className="relative mb-4">
          <RiTicket2Line className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <select
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
            id="rate"
            name="rate"
            value={selectedRate ? selectedRate.id : ""}
            onChange={onInputChange}
          >
            <option defaultValue="">seleccione la tarifa</option>
            {rates.map((rate) => (
              <option key={rate.id} value={rate.id}>
                {rate.time} - {formatCurrency(rate.amount)}
              </option>
            ))}
          </select>
        </div>
        <button
          type="submit"
          className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
        >
          Generar
        </button>
      </form>
    </div>
  );
};
