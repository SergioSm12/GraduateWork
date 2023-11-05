import React, { useState } from "react";
import { Link } from "react-router-dom";
//icons
import {
  RiBarChart2Line,
  RiWalletLine,
  RiGroupLine,
  RiCalendarTodoLine,
  RiLogoutCircleLine,
  RiArrowRightSLine,
  RiMenu3Line,
  RiCloseLine,
  RiRoadsterFill
} from "react-icons/ri";
import { useAuth } from "../auth/hooks/useAuth";

export const Sidebar = () => {
  const { handlerLogout } = useAuth();
  const [showSubmenu, setShowSubmenu] = useState(false);
  const [showMenu, setShowMenu]= useState(false);
  return (

    <>
    <div className={`xl:h-[100vh] overflow-y-scroll fixed xl:static  w-[80%] md:w-[40%] lg:w-[30%] xl:w-auto h-full 
     top-0 bg-secondary-100 p-4 flex flex-col justify-between z-50 ${showMenu ? "left-0": "-left-full"} transition-all `}>
      <div>
        <h1 className="text-center text-2xl font-bold text-white mb-10">
          Parqueadero<span className="text-primary text-4xl">.</span>
        </h1>
        <ul>
          <li>
            <Link
              to="/"
              className="flex items-center gap-4 py-2 px-4 rounded-lg hover:bg-secondary-900 transition-colors"
            >
              <RiBarChart2Line className="text-primary" />
              Recibos y estadísticas.
            </Link>
          </li>

          <li>
            <Link
              to="/users"
              className="flex items-center gap-4 py-2 px-4 rounded-lg hover:bg-secondary-900 transition-colors"
            >
              <RiGroupLine className="text-primary" />
              Administar usuarios.
            </Link>
          </li>

          <li>
            <Link
              to="/rate"
              className="flex items-center gap-4 py-2 px-4 rounded-lg hover:bg-secondary-900 transition-colors"
            >
              <RiCalendarTodoLine className="text-primary" />
              Adminitrar tarifas.
            </Link>
          </li>
          <li>
            <Link
            to="/vehicleType"
            className="flex items-center gap-4 py-2 px-4 rounded-lg hover:bg-secondary-900 transition-colors"
            >
              <RiRoadsterFill className="text-primary"/>
              Administrar vehículos.
            </Link>
          </li>
        </ul>
      </div>
      <nav>
        <button
          className="flex items-center gap-4 py-2 px-4 rounded-lg hover:bg-secondary-900 transition-colors"
          onClick={handlerLogout}
        >
          <RiLogoutCircleLine className="text-primary" />
          Cerrar sesión
        </button>
      </nav>
    </div>
    <button onClick={()=> setShowMenu(!showMenu)} className="xl:hidden fixed bottom-4 right-4 bg-primary text-black p-3 rounded-full z-50">
    {showMenu ? <RiCloseLine/> : <RiMenu3Line/>}  
     
    </button>
    </>

    
  );
};
