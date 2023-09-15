import React, { useEffect, useState } from "react";
import { RiArrowLeftLine, RiSearch2Line } from "react-icons/ri";
import { Link, useParams } from "react-router-dom";
import { useUsers } from "../../hooks/useUsers";
import { ShowUserInformation } from "./ShowUserInformation";
import { ShowUserVehicles } from "./ShowUserVehicles";

const DebuncedInput = ({ value: keyWord, onchange, ...props }) => {
  const [value, setValue] = useState(keyWord);

  useEffect(() => {
    const timeout = setTimeout(() => {
      onchange(value);
    }, 500);

    return () => clearTimeout(timeout);
  }, [value]);

  return (
    <input
      {...props}
      value={value}
      onChange={(e) => setValue(e.target.value)}
    />
  );
};
export const ShowUser = () => {
  const [globalFilter, setGlobalFilter] = useState("");

  //Traer datos del contexto
  const { initialUserForm, users = [] } = useUsers();
  const [userSelected, setUserSelected] = useState(initialUserForm);

  //Traemos el parametro de la url
  const { id } = useParams();

  useEffect(() => {
    if (id) {
      const user = users.find((u) => u.id == id) || initialUserForm;
      setUserSelected(user);
    }
  }, [id]);


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
            to="/users"
          >
            <RiArrowLeftLine />
            Regresar
          </Link>
        </div>
      </div>
      {/*Cuerpo */}
      <div className="bg-secondary-100 p-8 rounded-lg grid grid-cols-1 xl:grid-cols-4 gap-8">
        {/*Seccion 1*/}
        <div className="md:col-span-3">
          {/*input*/}
          <div className="mb-5">
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-y-4 ">
              <h1 className=" font-bold text-sm md:text-3xl mb-6">
                Busca el recibo por cualquier campo.
              </h1>

              <button className="font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors mb-2 ">
                Agregar Recibo
              </button>
            </div>

            <div className="relative">
              <RiSearch2Line className="absolute top-1/2  -translate-y-1/2 left-4" />
              <DebuncedInput
                type="text"
                value={globalFilter ?? ""}
                onchange={(value) => {
                  setGlobalFilter(String(value));
                  //getUsers();
                }}
                className="bg-secondary-900 outline-none py-2 pr-4 pl-10 rounded-lg
              placeholder:text-gray-500 w-full"
                placeholder="Buscar..."
              />
            </div>
          </div>
        </div>
        {/*Seccion 2*/}
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-1 gap-8 ">
          <ShowUserInformation userSelected={userSelected}/>
          <ShowUserVehicles id={id} userSelected={userSelected}/>
        </div>
      </div>
    </div>
  );
};
