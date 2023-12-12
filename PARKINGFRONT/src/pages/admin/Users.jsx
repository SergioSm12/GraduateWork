import React, { useEffect } from "react";
import { Link } from "react-router-dom";
import { CardTicket } from "../../components/Home/CardTicket";
import portadaUsers from "../../assets/portadaUsers.png";
import { DataTable } from "../../components/Users/DataTable";
import { useUsers } from "../../hooks/useUsers";
import { RiAlertLine } from "react-icons/ri";
import { Tab } from "@headlessui/react";
export const Users = () => {
  const {
    users,
    activeUsers,
    inactiveUsers,
    getUsers,
    getTotalCountUsers,
    totalCountState,
    getActiveUsers,
    getInactiveUsers,
    visibleFormCreate,
    handlerOpenFormCreate,
  } = useUsers();
  useEffect(() => {
    getUsers();
    getActiveUsers();
    getInactiveUsers();
    getTotalCountUsers();
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
            totalTickets={totalCountState}
            text="Usuarios Totales"
            icon="user"
            textEnd="user"
          />
        </div>
        <div className="flex justify-center mt-2">
          <img src={portadaUsers} className="w-72 " />
        </div>
      </div>
      {/*Card Table */}
      <div className="bg-secondary-100 rounded-xl p-8 grid grid-cols-1 items-center">
        {users.length === 0 ? (
          <div className=" flex justify-center">
            <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
              <RiAlertLine className="text-red-600" /> No hay usuarios
              registrados en la base de datos
            </span>
          </div>
        ) : (
          <Tab.Group>
            <Tab.List className="flex flex-col md:flex-row md:items-center md:justify-between gap-x-2 gap-y-6 bg-secondary-900/50  py-3 px-4 rounded-lg">
              <div className="flex flex-col md:flex-row  md:items-center gap-2">
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
               transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary "
                >
                  Activos
                </Tab>
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
                 transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary "
                >
                  Inactivos
                </Tab>
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
                 transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary "
                >
                  Todos
                </Tab>
              </div>
              <div className="flex justify-center">
                {visibleFormCreate || (
                  <button
                    className="font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                    onClick={handlerOpenFormCreate}
                  >
                    Agregar Usuario
                  </button>
                )}
              </div>
            </Tab.List>
            <Tab.Panels className="mt-8">
              <Tab.Panel>
                {activeUsers.length == 0 ? (
                  <div className=" flex justify-center">
                    <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                      <RiAlertLine className="text-red-600" /> No hay usuarios
                      activos.
                    </span>
                  </div>
                ) : (
                  <DataTable
                    dataUsers={activeUsers}
                    visibleFormCreate={visibleFormCreate}
                  />
                )}
              </Tab.Panel>
              <Tab.Panel>
                {inactiveUsers.length == 0 ? (
                  <div className=" flex justify-center">
                    <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                      <RiAlertLine className="text-red-600" /> No hay usuarios
                      inactivos.
                    </span>
                  </div>
                ) : (
                  <DataTable
                    dataUsers={inactiveUsers}
                    visibleFormCreate={visibleFormCreate}
                  />
                )}
              </Tab.Panel>
              <Tab.Panel>
                <DataTable
                  dataUsers={users}
                  visibleFormCreate={visibleFormCreate}
                />
              </Tab.Panel>
            </Tab.Panels>
          </Tab.Group>
        )}
      </div>
    </div>
  );
};
