import React, { useEffect } from "react";
import { Link } from "react-router-dom";

import { CreateRate } from "../../components/Rate/CreateRate";
import { DataTableRate } from "../../components/Rate/DataTableRate";
import { useRates } from "../../hooks/useRates";
import { RiAlertLine } from "react-icons/ri";
export const Rates = () => {
  const { rates, getRates, rateSelected } = useRates();
  useEffect(() => {
    getRates();
  }, []);
  return (
    <div>
      {/*Title */}
      <div className="mb-10 ">
        <h1 className="font-bold text-gray-100 text-xl">
          Tarifas registradas.
        </h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary transition-colors">
            Principal
          </Link>
          <span>-</span>
          <span>Centro de administración de tarifas</span>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        {/*Card create rate */}
        <div className="bg-secondary-100 p-8 rounded-lg">
          <div>
            {/*Formulario crearRate */}
            <CreateRate rateSelected={rateSelected} />
          </div>
        </div>
        {/*Card Listar rate*/}
        <div className="bg-secondary-100 p-8 rounded-lg">
          {rates.length > 0 ? (
            <div className="text-white text-xl md:text-2xl mb-4">
              <h1>Tarifas:</h1>
            </div>
          ):("")}
          <div>
            {/*Tabla de tarifas*/}
            {rates.length > 0 ? (
              <DataTableRate />
            ) : (
              <div className=" flex justify-center">
                <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                  <RiAlertLine className="text-red-600" /> No hay tarifas
                  registradas en la base de datos
                </span>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
