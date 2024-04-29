import { useEffect } from "react";
import { RiAlertLine, RiArrowLeftLine } from "react-icons/ri";
import { Link, useParams } from "react-router-dom";
import { useUsers } from "../../hooks/useUsers";
import { ShowUserInformation } from "./ShowUserInformation";
import { ShowUserVehicles } from "./ShowUserVehicles";
import { DataTableReceiptsUser } from "./DataTableReceiptsUser";
import { useReceipts } from "../../hooks/useReceipts";
import { Tab } from "@headlessui/react";
import { useNightlyReceipts } from "../../hooks/useNightlyReceipts";

export const ShowUser = () => {
  //Traer datos del contexto
  const { userByid, getUserById } = useUsers();
  const { receiptsByUser, getReciptsByUser } = useReceipts();
  const { nightlyReceiptsByUser, getNightlyReceiptsByUser } =
    useNightlyReceipts();

  //Traemos el parametro de la url
  const { id } = useParams();

  useEffect(() => {
    getReciptsByUser(id);
    getNightlyReceiptsByUser(id);
    getUserById(id);
  }, [, id]);

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
          <Tab.Group>
            <Tab.List className="flex flex-col md:flex-row md:items-center md:justify-between gap-x-2 gap-y-6 bg-secondary-900/50 py-3 px-4 rounded-lg mb-8">
              <div className="flex flex-col md:flex-row md:items-center gap-2">
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Recibos diurnos
                </Tab>
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Recibos nocturnos
                </Tab>
              </div>
            </Tab.List>
            <Tab.Panels>
              <Tab.Panel>
                {receiptsByUser.length === 0 ? (
                  <div className=" flex justify-center">
                    <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                      <RiAlertLine className="text-red-600" /> No hay recibos
                      registrados para este usuario
                    </span>
                  </div>
                ) : (
                  <DataTableReceiptsUser dataReceipts={receiptsByUser} />
                )}
              </Tab.Panel>
              <Tab.Panel>
                {nightlyReceiptsByUser.length === 0 ? (
                  <div className=" flex justify-center">
                    <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                      <RiAlertLine className="text-red-600" /> No hay recibos
                      nocturnos registrados para este usuario
                    </span>
                  </div>
                ) : (
                  <DataTableReceiptsUser
                    receiptType={"nocturno"}
                    dataReceipts={nightlyReceiptsByUser}
                  />
                )}
              </Tab.Panel>
            </Tab.Panels>
          </Tab.Group>
        </div>
        {/*Seccion 2*/}
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-1 gap-8 ">
          <ShowUserInformation userByid={userByid} />
          <ShowUserVehicles userByid={userByid} />
        </div>
      </div>
    </div>
  );
};
