import React from "react";
import { Link } from "react-router-dom";
import { RiBillLine, RiMore2Line, RiTeamLine } from "react-icons/ri";
import "@szhsin/react-menu/dist/index.css";
import "@szhsin/react-menu/dist/transitions/slide.css";
import { Menu, MenuItem, MenuButton } from "@szhsin/react-menu";

const formatNumber = (num) => {
  if (num >= 1000 && num < 1000000) {
    return (num / 1000).toFixed(1) + "k";
  } else if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + "M";
  } else {
    return num.toString();
  }
};

export const CardTicket = (props) => {
  const {
    ticket,
    totalTickets,
    text,
    icon,
    textVisitor,
    textNight,
    totalTicketsVisitor,
    totalTicketsNight,
  } = props;
  let status = "";
  let textColor = "";
  let background = "";

  switch (ticket) {
    case "pending":
      status = "bg-red-500/10 text-red-500";
      textColor = "text-xs text-center text-red-500";
      background = "bg-secondary-100";
      break;

    case "payments":
      status = "bg-green-500/10 text-green-500";
      textColor = "text-xs text-center text-green-500";
      background = "bg-secondary-100";
      break;

    case "total":
      status = "bg-blue-500/10 text-blue-500";
      textColor = "text-xs text-center text-blue-500";
      background = "bg-secondary-100";
      break;

    case "usersHome":
      status = "bg-yellow-500/10 text-yellow-500";
      textColor = "text-xs text-center text-yellow-500";
      background = "bg-secondary-100";
      break;
    case "users":
      status = "bg-yellow-500/10 text-yellow-500";
      textColor = "text-xs text-center text-yellow-500";
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
                to="/reports/receipt"
                className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-4 p-2 flex-1"
              >
                Reportes de ingresos diurnos.
              </Link>
            </MenuItem>
            <MenuItem className="p-0 hover:bg-transparent">
              <Link
                to="/reports/nightlyreceipt"
                className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-4 p-2 flex-1"
              >
                Reportes de ingresos nocturnos 
              </Link>
            </MenuItem>
            <MenuItem className="p-0 hover:bg-transparent">
              <Link
                to="/reports/visitorreceipt"
                className="rounded-lg transition-colors text-gray-300 hover:bg-secondary-900
                     flex items-center gap-x-4 p-2 flex-1"
              >
                Reportes de ingresos visitantes
              </Link>
            </MenuItem>
          </Menu>
        </div>
      </div>
      {/*Number of tickets */}
      <div className="flex  items-center justify-between">
        <div className="flex flex-col gap-3 p-2 bg-secondary-900/90 rounded-lg mr-2">
          <h1 className="text-4xl text-center text-white font-bold ">
            {formatNumber(totalTickets)}
          </h1>
          <p className={textColor}>{text}</p>
        </div>
        {icon == "user" ? (
          <></>
        ) : (
          <>
            <div className="flex flex-col gap-3 p-2 bg-secondary-900/90 rounded-lg">
              <h1 className="text-4xl text-center text-white font-bold ">
                {formatNumber(totalTicketsVisitor)}
              </h1>
              <p className={textColor}>{textVisitor}</p>
            </div>
            <div className="flex flex-col gap-3 p-2 bg-secondary-900/90 rounded-lg ml-3">
              <h1 className="text-4xl text-center text-white font-bold ">
                {formatNumber(totalTicketsNight)}
              </h1>
              <p className={textColor}>{textNight}</p>
            </div>
          </>
        )}
      </div>
      <hr className="border border-dashed border-gray-500/50 my-4"></hr>
      <div>
        {icon == "user" ? (
          <Link to="/users" className="text-primary hover:underline">
            {" "}
            Usuarios parqueadero{" "}
          </Link>
        ) : (
          <p className="flex items-center gap-2 text-primary">
            Total :{" "}
            <span className="bg-secondary-900 px-2 py-0.5 rounded-lg text-white font-bold">
              {formatNumber(
                totalTickets + totalTicketsVisitor + totalTicketsNight
              )}
            </span>
          </p>
        )}
      </div>
    </div>
  );
};
