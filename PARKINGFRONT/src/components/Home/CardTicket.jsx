import React from "react";
import { Link } from "react-router-dom";
import {
  RiBillLine,
  RiMore2Line,
  RiTeamLine,
  RiFileInfoLine,
  RiAddLine,
} from "react-icons/ri";
import "@szhsin/react-menu/dist/index.css";
import "@szhsin/react-menu/dist/transitions/slide.css";
import { Menu, MenuItem, MenuButton } from "@szhsin/react-menu";

export const CardTicket = (props) => {
  const { ticket, totalTickets, text, icon, textEnd } = props;
  let status = "";
  let textColor = "";
  let background = "";

  switch (ticket) {
    case "pending":
      status = "bg-red-500/10 text-red-500";
      textColor = "text-red-500";
      background = "bg-secondary-100";
      break;

    case "payments":
      status = "bg-green-500/10 text-green-500";
      textColor = "text-green-500";
      background = "bg-secondary-100";
      break;

    case "total":
      status = "bg-blue-500/10 text-blue-500";
      textColor = "text-blue-500";
      background = "bg-secondary-100";
      break;

    case "usersHome":
      status = "bg-yellow-500/10 text-yellow-500";
      textColor = "text-yellow-500";
      background = "bg-secondary-100";
      break;
    case "users":
      status = "bg-yellow-500/10 text-yellow-500";
      textColor = "text-yellow-500";
      background = "bg-secondary-900";
      break;

    default:
      break;
  }

  return (
    <div className={`${background} p-8 rounded-xl`}>
      <div className="flex items-center justify-between mb-4 ">
        <div>
          {icon == "user" ? (
            <RiTeamLine
              className={`text-4xl ${status} p-2 box-content rounded-xl`}
            />
          ) : (
            <RiBillLine
              className={`text-4xl ${status} p-2 box-content rounded-xl`}
            />
          )}
        </div>
        <div>
          <Menu
            menuButton={
              <MenuButton
                className="flex items-center gap-x-2 hover:bg-secondary-900
             p-2 rounded-lg transition-colors"
              >
                <RiMore2Line />
              </MenuButton>
            }
            align="end"
            transition
            menuClassName="bg-secondary-100 p-4 "
          >
            <MenuItem className="p-0 hover:bg-transparent">
              <Link
                to="/"
                className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-4 p-2 flex-1"
              >
                Dashboard tickets
              </Link>
            </MenuItem>
            <MenuItem className="p-0 hover:bg-transparent">
              <Link
                to="/"
                className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-4 p-2 flex-1"
              >
                Informacion
              </Link>
            </MenuItem>
          </Menu>
        </div>
      </div>
      {/*Number of tickets */}
      <div>
        <h1 className="text-4xl text-white font-bold mb-4">{totalTickets} </h1>
        <p className={textColor}>{text}</p>
      </div>
      <hr className="border border-dashed border-gray-500/50 my-4"></hr>
      <div>
        {textEnd == "user" || icon=="user" ? (
         <Link to="/users" className="text-primary" > Usuarios parqueadero  </Link>
        ) : (
          <Link
            to="/"
            className="flex items-center gap-2 text-primary hover:underline"
          ><RiAddLine/>Añadir recibo</Link>
        )}
      </div>
    </div>
  );
};
