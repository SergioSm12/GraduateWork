import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import { CardTicket } from "../../components/Home/CardTicket";
import portadaUsers from "../../assets/portadaUsers.png";
import { DataTable } from "../../components/Users/DataTable";
import { useUsers } from "../../hooks/useUsers";
import { RiAlertLine } from "react-icons/ri";
export const Users = () => {
  const { users, getUsers } = useUsers();
  useEffect(() => {
    getUsers();
  }, []);

 
  return (
    <div>
      {/*Title */}
      <div className="mb-10 ">
        <h1 className="font-bold text-gray-100 text-xl">
          Usuarios Registrados
        </h1>
        <div className="flex items-center gap-2 text-sm text-gray-500">
          <Link to="/" className="hover:text-primary transition-colors">
            Principal
          </Link>
          <span>-</span>
          <span>Centro de administración de usuarios</span>
        </div>
      </div>
      {/*Portada */}
      <div
        className="bg-secondary-100 p-8 rounded-lg  grid grid-cols-1 md:grid-cols-2
      items-center mb-6"
      >
        <div className="p-8">
          <h1 className="text-3xl mb-6">Analitica</h1>
          <CardTicket
            ticket="users"
            totalTickets={users.length}
            text="Usuarios Totales"
            icon="user"
            textEnd="user"
          />
        </div>
        <div className="flex justify-center">
          <img src={portadaUsers} className="w-72 " />
        </div>
      </div>
      {/*Card Table */}
      <div className="bg-secondary-100 rounded-xl p-8 grid grid-cols-1 items-center">
        {users.length === 0 ? (
          <div className=" flex justify-center">
            <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
              <RiAlertLine className="text-red-600"/> No hay usuarios registrados en la base de datos
            </span>
          </div>
        ) : (
          <DataTable dataUsers={users} />
        )}
      </div>
    </div>
  );
};
