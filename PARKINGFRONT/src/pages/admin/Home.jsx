import React, { useEffect } from "react";
import { CardTicket } from "../../components/Home/CardTicket";
import { RiAlertLine, RiMoonLine, RiPoliceCarLine, RiSunLine, RiUserShared2Line } from "react-icons/ri";
import { useAuth } from "../../auth/hooks/useAuth";
import { useReceipts } from "../../hooks/useReceipts";
import { useUsers } from "../../hooks/useUsers";
import { DataTableReceipt } from "../../components/Receipts/DataTableReceipt";
import { Tab } from "@headlessui/react";
import { useVisitorReceipt } from "../../hooks/useVisitorReceipt";
import { DataTableVisitorReceipt } from "../../components/VisitorReceipt/DataTableVisitorReceipt";
import { ModalFormVisitorReceipt } from "../../components/VisitorReceipt/ModalFormVisitorReceipt";
import { Link } from "react-router-dom";
import { useNightlyReceipts } from "../../hooks/useNightlyReceipts";
import { DataTableNightlyReceipts } from "../../components/NightlyReceipt/DataTableNightlyReceipts";

export const Home = () => {
  const { login, handlerLogout } = useAuth();
  const {
    getReceipts,
    getCountUnpaid,
    getCountPaid,
    getCountTotal,
    totalUnpaidState,
    totalPaidState,
    totalState,
    receipts,
  } = useReceipts();

  const {
    getVisitorReceipts,
    visitorReceipts,
    handlerOpenModalFormVisitorReceipt,
    visibleFormReceiptVisitorModal,

    getCountUnpaidVisitor,
    getCountPaidVisitor,
    getCountTotalVisitor,
    totalUnpaidVisitorState,
    totalPaidVistorState,
    totalVisitorReceipt,
  } = useVisitorReceipt();

  const {
    getNightlyReceipts,
    nightlyReceipts,
    getCountNightUnpaid,
    getCountNightPaid,
    getCountNightTotal,

    totalNightUnPaidState,
    totalNightPaidState,
    totalNightState,
  } = useNightlyReceipts();
  const { getTotalCountUsers, totalCountState } = useUsers();

  useEffect(() => {
    getCountUnpaid();
    getCountPaid();
    getCountTotal();
    getTotalCountUsers();
    getCountUnpaidVisitor(),
      getCountPaidVisitor(),
      getCountTotalVisitor(),
      getReceipts();
    getVisitorReceipts();
    getNightlyReceipts();
    getCountNightUnpaid();
    getCountNightPaid();
    getCountNightTotal();
  }, []);

  return (
    <div>
      {/*Modal para generar recibo */}
      {!visibleFormReceiptVisitorModal || <ModalFormVisitorReceipt />}
      {/*Welcome */}
      <div className="flex items-center justify-between mb-10">
        <h1 className=" md:text-4xl text-white">
          Bienvenido : {login.user?.email}
        </h1>
        <div className="flex  items-center gap-2 text-2xl md:text-5xl">
          <RiPoliceCarLine />
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-3 gap-8">
        {/* */}
        <CardTicket
          ticket="pending"
          totalTickets={totalUnpaidState}
          totalTicketsVisitor={totalUnpaidVisitorState}
          totalTicketsNight={totalNightUnPaidState}
          text="Recibos pendientes usuarios."
          textVisitor="Recibos pendientes visitantes."
          textNight="Recibos pendientes nocturnos."
        />
        <CardTicket
          ticket="payments"
          totalTickets={totalPaidState}
          totalTicketsVisitor={totalPaidVistorState}
          totalTicketsNight={totalNightPaidState}
          text="Recibos pagos usuarios."
          textVisitor="Recibos pagos visitantes."
          textNight="Recibos pagos nocturnos."
        />
        <CardTicket
          ticket="total"
          totalTickets={totalState}
          totalTicketsVisitor={totalVisitorReceipt}
          totalTicketsNight={totalNightState}
          text="Recibos totales usuarios."
          textVisitor="Recibos totales visitantes."
          textNight="Recibos totales nocturnos."
        />
        <CardTicket
          ticket="usersHome"
          totalTickets={totalCountState}
          text="Usuarios Totales"
          icon="user"
        />
      </div>
      <div>
        <h1 className="text-2xl text-white my-10">Administrar Facturas :</h1>
      </div>
      {/*Date Table */}

      <div className="bg-secondary-100 p-8 rounded-xl">
        {receipts.length === 0 && visitorReceipts.length === 0 ? (
          <>
            <div className="flex items-center justify-center gap-2">
              <Link
                to="/users"
                className="font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
              >
                Agregar recibo usuario
              </Link>
              <button
                className="font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                onClick={() => {
                  handlerOpenModalFormVisitorReceipt();
                }}
              >
                Agregar recibo visitante
              </button>
            </div>

            <div className=" flex justify-center mt-2">
              <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                <RiAlertLine className="text-red-600" /> No hay recibos
                registrados
              </span>
            </div>
          </>
        ) : (
          <Tab.Group>
            <Tab.List className="flex flex-col md:flex-row md:items-center md:justify-between gap-x-2 gap-y-6 bg-secondary-900/50 py-3 px-4 rounded-lg mb-8">
              <div className="flex flex-col md:flex-row md:items-center gap-2">
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Registrados
                </Tab>
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Visitantes
                </Tab>
                <Tab
                  className="py-2 px-4 rounded-lg hover:bg-secondary-900 hover:text-gray-100
              transition-colors outline-none ui-selected:bg-secondary-900 ui-selected:text-primary"
                >
                  Nocturno
                </Tab>
              </div>
              <div className="flex items-center justify-center gap-2">
                <Link
                  to="/users"
                  className="flex items-center gap-1 font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                >
                  Agregar recibo usuario <RiSunLine />
                </Link>
                <Link
                  to="/vehicleType"
                  className="font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors flex items-center gap-1"
                >
                  Agregar recibo nocturno <RiMoonLine />
                </Link>
                <button
                  className=" flex items-center gap-1 font-bold text-xs py-2 px-4 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                  onClick={() => {
                    handlerOpenModalFormVisitorReceipt();
                  }}
                >
                  Agregar recibo visitante <RiUserShared2Line />
                </button>
              </div>
            </Tab.List>
            <Tab.Panels>
              <Tab.Panel>
                {receipts.length == 0 ? (
                  <div className=" flex justify-center">
                    <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                      <RiAlertLine className="text-red-600" /> No hay recibos
                      registrados.
                    </span>
                  </div>
                ) : (
                  <DataTableReceipt dataReceipts={receipts} />
                )}
              </Tab.Panel>
              <Tab.Panel>
                {visitorReceipts.length == 0 ? (
                  <div className=" flex justify-center">
                    <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                      <RiAlertLine className="text-red-600" /> No hay recibos
                      registrados para los visitantes.
                    </span>
                  </div>
                ) : (
                  <DataTableVisitorReceipt dataReceipts={visitorReceipts} />
                )}
              </Tab.Panel>
              <Tab.Panel>
                {nightlyReceipts.length == 0 ? (
                  <div className=" flex justify-center">
                    <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
                      <RiAlertLine className="text-red-600" /> No hay recibos
                      nocturnos registrados.
                    </span>
                  </div>
                ) : (
                  <DataTableNightlyReceipts dataReceipts={nightlyReceipts} />
                )}
              </Tab.Panel>
            </Tab.Panels>
          </Tab.Group>
        )}
      </div>
    </div>
  );
};
