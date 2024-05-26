import React, { useEffect } from "react";
import logoUsta from "../assets/pngwing.com.png";
import {
  RiRoadsterLine,
  RiLockPasswordLine,
  RiMotorbikeLine,
  RiArrowDownSLine,
  RiLogoutCircleLine,
  RiLockUnlockLine,
  RiParkingBoxLine,
} from "react-icons/ri";
import { Menu, MenuItem, MenuButton } from "@szhsin/react-menu";
import "@szhsin/react-menu/dist/index.css";
import "@szhsin/react-menu/dist/transitions/slide.css";
import { Link } from "react-router-dom";
import { useAuth } from "../auth/hooks/useAuth";
import { useCapacity } from "../hooks/useCapacity";

export const Header = () => {
  const { login, handlerLogout } = useAuth();
  const { getCapacities, capacities } = useCapacity();

  useEffect(() => {
    getCapacities();

    const interval = setInterval(() => {
      getCapacities();
    }, 10000);

    //Limpiar el intervalo
    return () => clearInterval(interval);
  }, []);

  const carCapacities = capacities.filter(
    (capacity) => capacity.vehicleType.name === "CARRO"
  );
  const motoCapacities = capacities.filter(
    (capacity) => capacity.vehicleType.name === "MOTO"
  );

  const totalParkingSpacesCar = carCapacities.reduce(
    (total, capacity) => total + capacity.parkingSpaces,
    0
  );

  const totalParkingSpacesMotorbike = motoCapacities.reduce(
    (total, capacity) => total + capacity.parkingSpaces,
    0
  );

  return (
    <header className="h-[7vh] md:h-[10vh] border-b border-secondary-100 p-8 flex items-center justify-end">
      <nav className="flex items-center gap-2 md:w-full">
        <span className=" mr-auto md:flex hidden ">
          Parqueadero universidad Santo Tomas
        </span>

        <Menu
          menuButton={
            <MenuButton className="relative hover:bg-secondary-100 p-2 rounded-lg transition-colors">
              <RiRoadsterLine />
              <span className="absolute -top-0.5 -right-0 bg-primary py-0.5 px-[5px]  box-content text-black rounded-full text-[8px] font-bold">
                {totalParkingSpacesCar}
              </span>
            </MenuButton>
          }
          align="end"
          transition
          menuClassName="bg-secondary-100 p-4"
        >
          <h1 className="text-gray-300 text-center font-medium">
            Aforo carros
          </h1>
          <hr className="my-6 border-gray-500"></hr>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to= {login.isAdmin ? "/aforo":"/" }
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockUnlockLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <div className="flex flex-col gap-1">
                <span className="text-lg">Plazas disponibles </span>
                {carCapacities.map((capacity) => (
                  <span className="text-xs" key={capacity.id}>
                    {capacity.building.name}{" "}
                    <span className="text-primary">
                      ({capacity.parkingSpaces})
                    </span>
                  </span>
                ))}
              </div>
            </Link>
          </MenuItem>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to={login.isAdmin ? "/aforo":"/" }
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockPasswordLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <div className="flex flex-col gap-1">
                <span className="text-lg">Plazas ocupadas </span>
                {carCapacities.map((capacity) => (
                  <span className="text-xs" key={capacity.id}>
                    {capacity.building.name}{" "}
                    <span className="text-primary">
                      ({capacity.occupiedSpaces})
                    </span>
                  </span>
                ))}
              </div>
            </Link>
          </MenuItem>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to={login.isAdmin ? "/aforo":"/" }
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiParkingBoxLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <div className="flex flex-col gap-1">
                <span className="text-lg">Plazas totales </span>
                {carCapacities.map((capacity) => (
                  <span className="text-xs" key={capacity.id}>
                    {capacity.building.name}{" "}
                    <span className="text-primary">
                      ({capacity.maxParking})
                    </span>
                  </span>
                ))}
              </div>
            </Link>
          </MenuItem>
        </Menu>

        <Menu
          menuButton={
            <MenuButton className="relative hover:bg-secondary-100 p-2 rounded-lg transition-colors">
              <RiMotorbikeLine />
              <span className="absolute -top-0.5 -right-0 bg-primary py-0.5 px-[5px]  box-content text-black rounded-full text-[8px] font-bold">
                {totalParkingSpacesMotorbike}
              </span>
            </MenuButton>
          }
          align="end"
          transition
          menuClassName="bg-secondary-100 p-4"
        >
          <h1 className="text-gray-300 text-center font-medium">
            Aforo motocicleta
          </h1>
          <hr className="my-6 border-gray-500"></hr>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to={login.isAdmin ? "/aforo":"/" }
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockUnlockLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <div className="flex flex-col gap-1">
                <span className="text-lg">Plazas disponibles </span>
                {motoCapacities.map((capacity) => (
                  <span className="text-xs" key={capacity.id}>
                    {capacity.building.name}{" "}
                    <span className="text-primary">
                      ({capacity.parkingSpaces})
                    </span>
                  </span>
                ))}
              </div>
            </Link>
          </MenuItem>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to={login.isAdmin ? "/aforo":"/" }
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockPasswordLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <div className="flex flex-col gap-1">
                <span className="text-lg">Plazas ocupadas </span>
                {motoCapacities.map((capacity) => (
                  <span className="text-xs" key={capacity.id}>
                    {capacity.building.name}{" "}
                    <span className="text-primary">
                      ({capacity.occupiedSpaces})
                    </span>
                  </span>
                ))}
              </div>
            </Link>
          </MenuItem>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to={login.isAdmin ? "/aforo":"/" }
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiParkingBoxLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <div className="flex flex-col gap-1">
                <span className="text-lg">Plazas totales </span>
                {motoCapacities.map((capacity) => (
                  <span className="text-xs" key={capacity.id}>
                    {capacity.building.name}{" "}
                    <span className="text-primary">
                      ({capacity.maxParking})
                    </span>
                  </span>
                ))}
              </div>
            </Link>
          </MenuItem>
        </Menu>

        <Menu
          menuButton={
            <MenuButton className="flex items-center gap-x-2 hover:bg-secondary-100 p-2 rounded-lg transition-colors">
              <img
                src={logoUsta}
                className="w-6 h-6 object-cover rounded-full"
              />
              <span>{login.user?.email}</span>
              <RiArrowDownSLine />
            </MenuButton>
          }
          align="end"
          transition
          menuClassName="bg-secondary-100 p-4"
        >
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to="/"
              className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
           flex items-center gap-x-4 py-2 px-6 flex-1"
            >
              <img
                src={logoUsta}
                className="w-8 h-8 object-cover rounded-full"
              />
              <div className="flex flex-col text-sm">
                <span className="text-sm">
                  {login.isAdmin
                    ? "Administrador"
                    : login.isGuard
                    ? "Guardia"
                    : "Usuario"}
                </span>
                <span className="text-xs text-gray-500">
                  {login.user?.email}
                </span>
              </div>
            </Link>
          </MenuItem>
          <hr className="my-4 border-gray-500"></hr>
          <MenuItem className="p-0 hover:bg-transparent">
            <button
              onClick={handlerLogout}
              className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900 flex items-center gap-x-4 py-2 px-6 flex-1"
            >
              <RiLogoutCircleLine className="text-primary" /> Cerrar sesión
            </button>
          </MenuItem>
        </Menu>
      </nav>
    </header>
  );
};
