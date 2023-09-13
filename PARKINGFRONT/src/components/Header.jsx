import React, { useEffect } from "react";
import logoUsta from "../assets/pngwing.com.png";
import {
  RiRoadsterLine,
  RiLockPasswordLine,
  RiMotorbikeLine,
  RiArrowDownSLine,
  RiLogoutCircleLine,
  RiLockUnlockLine,
} from "react-icons/ri";
import { Menu, MenuItem, MenuButton } from "@szhsin/react-menu";
import "@szhsin/react-menu/dist/index.css";
import "@szhsin/react-menu/dist/transitions/slide.css";
import { Link } from "react-router-dom";
import { useAuth } from "../auth/hooks/useAuth";

export const Header = () => {
  const { login, handlerLogout } = useAuth();
  return (
    <header className="h-[7vh] md:h-[10vh] border-b border-secondary-100 p-8 flex items-center justify-end">
      <nav className="flex items-center gap-2">
        <Menu
          menuButton={
            <MenuButton className="relative hover:bg-secondary-100 p-2 rounded-lg transition-colors">
              <RiRoadsterLine />
              <span className="absolute -top-0.5 -right-0 bg-primary py-0.5 px-[5px]  box-content text-black rounded-full text-[8px] font-bold">
                2
              </span>
            </MenuButton>
          }
          align="end"
          transition
          menuClassName="bg-secondary-100 p-4"
        >
          <h1 className="text-gray-300 text-center font-medium">Aforo</h1>
          <hr className="my-6 border-gray-500"></hr>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to="/"
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockUnlockLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <span>Plazas disponibles (2)</span>
            </Link>
          </MenuItem>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to="/"
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockPasswordLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <span>Plazas ocupadas (60)</span>
            </Link>
          </MenuItem>
        </Menu>

        <Menu
          menuButton={
            <MenuButton className="relative hover:bg-secondary-100 p-2 rounded-lg transition-colors">
              <RiMotorbikeLine />
              <span className="absolute -top-0.5 -right-0 bg-primary py-0.5 px-[5px]  box-content text-black rounded-full text-[8px] font-bold">
                3
              </span>
            </MenuButton>
          }
          align="end"
          transition
          menuClassName="bg-secondary-100 p-4"
        >
          <h1 className="text-gray-300 text-center font-medium">Aforo</h1>
          <hr className="my-6 border-gray-500"></hr>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to="/"
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockUnlockLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <span>Plazas disponibles (3)</span>
            </Link>
          </MenuItem>
          <MenuItem className="p-0 hover:bg-transparent">
            <Link
              to="/"
              className="text-gray-300 text-sm flex flex-1 items-center justify-content gap-2 py-2 px-4
            hover:bg-secondary-900 transition-colors rounded-lg"
            >
              <RiLockPasswordLine className="p-2 text-primary bg-secondary-900 box-content rounded-full" />
              <span>Plazas ocupadas (20)</span>
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
                  {login.isAdmin ? "Administrador" : ""}
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
