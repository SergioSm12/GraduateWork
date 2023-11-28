import React, { useEffect } from "react";
import { CardTicket } from "../../components/Home/CardTicket";
import { RiAlertLine, RiPoliceCarLine } from "react-icons/ri";
import { useAuth } from "../../auth/hooks/useAuth";
import { useReceipts } from "../../hooks/useReceipts";
import { useUsers } from "../../hooks/useUsers";
import { DataTableReceipt } from "../../components/Receipts/DataTableReceipt";

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
  const { getTotalCountUsers, totalCountState } = useUsers();

  useEffect(() => {
    getCountUnpaid();
    getCountPaid();
    getCountTotal();
    getTotalCountUsers();
    getReceipts();
  }, []);
  return (
    <div>
      {/*Welcome */}
      <div className="flex items-center justify-between mb-10">
        <h1 className=" md:text-4xl text-white">
          Bienvenido : {login.user?.email}
        </h1>
        <div className="flex  items-center gap-2 text-2xl md:text-5xl">
          <RiPoliceCarLine />
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
        {/* */}
        <CardTicket
          ticket="pending"
          totalTickets={totalUnpaidState}
          text="Recibos Pendientes"
        />
        <CardTicket
          ticket="payments"
          totalTickets={totalPaidState}
          text="Recibos Pagos"
        />
        <CardTicket
          ticket="total"
          totalTickets={totalState}
          text="Recibos totales"
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
        {receipts.length === 0 ? (
          <div className=" flex justify-center">
            <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
              <RiAlertLine className="text-red-600" /> No hay recibos
              registrados
            </span>
          </div>
        ) : (
          <DataTableReceipt dataReceipts={receipts} />
        )}
      </div>
    </div>
  );
};
