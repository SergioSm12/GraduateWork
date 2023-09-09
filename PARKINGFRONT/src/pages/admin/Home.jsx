import React from "react";
import { CardTicket } from "../../components/Home/CardTicket";
import { RiArrowLeftSLine, RiArrowRightSLine, RiPoliceCarLine } from "react-icons/ri";
import { useAuth } from "../../auth/hooks/useAuth";

export const Home = () => {
  const { login, handlerLogout } = useAuth();
  return (
    <div>
      {/*Welcome */}
      <div className="flex items-center justify-between mb-10">
        <h1 className=" md:text-4xl text-white">Bienvenido : {login.user?.email}</h1>
        <div className="flex  items-center gap-2 text-2xl md:text-5xl">
          <RiPoliceCarLine/>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
        {/* */}
        <CardTicket
          ticket="pending"
          totalTickets="20"
          text="Recibos Pendientes"
        />
        <CardTicket ticket="payments" totalTickets="100" text="Recibos Pagos" />
        <CardTicket ticket="total" totalTickets="120" text="Recibos Pagos" />
        <CardTicket
          ticket="users"
          totalTickets="500"
          text="Usuarios Totales"
          icon="user"
        />
      </div>
    </div>
  );
};
