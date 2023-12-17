import React, { useEffect, useState } from "react";

import { RiCarLine, RiRoadsterLine, RiTicket2Line } from "react-icons/ri";
import logoUsta from "../../assets/pngwing.com.png";
import { Link } from "react-router-dom";
import { useRates } from "../../hooks/useRates";
import { useVisitorReceipt } from "../../hooks/useVisitorReceipt";
export const RegisterVisitor = () => {
  const { rates, getRates } = useRates();
  const {
    initialVisitorReceipt,
    handlerAddReceiptVisitor,
    errorsVisitorReceipt,
  } = useVisitorReceipt();

  //Estado para manejar formulario
  const [visitorReceiptForm, setVisitorReceiptForm] = useState(
    initialVisitorReceipt
  );
  const [selectedRate, setSelectedRate] = useState(null);

  useEffect(() => {
    getRates();
  }, []);

  const filteredRates = rates.filter((rate) => rate.time.includes("VISITANTE"));

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
    }).format(amount);
  };

  const onInputChange = ({ target }) => {
    const { name, value } = target;

    if (name === "rate") {
      const rateId = value;
      const rate = rates.find((r) => r.id === parseInt(rateId));
      setSelectedRate(rate);

      setVisitorReceiptForm({
        ...visitorReceiptForm,
        [name]: rate,
      });
    } else {
      setVisitorReceiptForm({
        ...visitorReceiptForm,
        [name]: value,
      });
    }
  };

  const onSubmit = (e) => {
    e.preventDefault();

    handlerAddReceiptVisitor(visitorReceiptForm,"/auth");
    setVisitorReceiptForm(initialVisitorReceipt);
    setSelectedRate(null);
  };

  return (
    <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w:auto lg:w-[450px]">
      <h1 className="text-3xl uppercase font-bold tracking-[5px] text-white mb-8">
        Registrar <span className="text-primary">Vehículo.</span>
      </h1>
      <form className="mb-8" onSubmit={onSubmit}>
        <div className="flex items-center justify-center py-3 px-4 gap-4 bg-secondary-900 w-full rounded-full mb-8 text-gray-100">
          <img src={logoUsta} className="w-8 h-8" />
          Universidad Santo Tomás <RiRoadsterLine />
        </div>
        <div className="relative">
          <RiCarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <input
            type="text"
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
            placeholder="Placa"
            name="plate"
            value={visitorReceiptForm.plate}
            onChange={onInputChange}
          />
        </div>
        <div className="relative mb-4">
          <p className="text-red-500">{errorsVisitorReceipt.plate}</p>
        </div>
        <div className="relative my-4">
          <RiTicket2Line className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <select
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
            id="rate"
            name="rate"
            value={selectedRate ? selectedRate.id : ""}
            onChange={onInputChange}
          >
            <option defaultValue="">seleccione la tarifa</option>
            {filteredRates.map((rate) => (
              <option key={rate.id} value={rate.id}>
                {rate.time} - {formatCurrency(rate.amount)}
              </option>
            ))}
          </select>
        </div>
        <div className="relative mb-2">
          <p className="text-red-500">
            {errorsVisitorReceipt?.rate == "Debe seleccionar una tarifa"
              ? JSON.stringify(errorsVisitorReceipt?.rate)
              : ""}
          </p>
        </div>

        <div>
          <button
            type="submit"
            className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg"
          >
            Generar recibo
          </button>
        </div>
      </form>
      <span className="flex items-center justify-center gap-2 mb-2">
        ¿Ya tienes cuenta ?
        <Link
          to="/auth"
          className="text-primary hover:text-gray-100 transition-colors"
        >
          Ingresa
        </Link>
      </span>
      <span className="flex items-center gap-2">
        ¿Eres Tomasiono y no tienes cuenta ?
        <Link
          to="/auth/register"
          className="text-primary hover:text-gray-100 transition-colors"
        >
          Registrate
        </Link>
      </span>
    </div>
  );
};
