import React from "react";
import { RiArrowLeftLine } from "react-icons/ri";
import { Link } from "react-router-dom";

export const ShowUser = () => {
  return (
    <div>
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-y-4 mb-10">
        <div>
          <h1 className="font-bold text-gray-100 text-xl">Detalle usuario</h1>
          <div className="flex items-center gap-2 text-sm text-gray-500 ">
            <Link to="/" className="hover:text-primary transition-colors">
              Principal
            </Link>
            <span>-</span>
            <span>Centro de administración de usuarios</span>
          </div>
        </div>
        <div className="flex items-center">
          <Link
            className="bg-primary/90 text-black hover:bg-primary flex items-center gap-2 py-2 px-4 rounded-lg
           transition-colors"
          >
         <RiArrowLeftLine/>
            Regresar
          </Link>
        </div>
      </div>
    </div>
  );
};
