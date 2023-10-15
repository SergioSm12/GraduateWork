import React from "react";
import classNames from "classnames";
import {
  RiArrowDropLeftLine,
  RiArrowDropRightLine,
  RiArrowLeftDoubleLine,
  RiArrowRightDoubleLine,
} from "react-icons/ri";
export const Paginator = ({getStateTable, table}) => {
  return (
    <>
      {/*Paginador*/}
      <div className="mt-4 flex flex-col  items-center justify-between space-y-4 text-center">
        <div className="md:flex grid grid-cols-5 items-center gap-2">
          <button
            className="p-2 hover:bg-secondary-900 rounded-lg  hover:text-primary
          disabled:hover:bg-secondary-100 disabled:text-gray-300 transition-colors "
            onClick={() => table.setPageIndex(0)}
            disabled={!table.getCanPreviousPage()}
          >
            <RiArrowLeftDoubleLine className="text-2xl" />
          </button>
          <button
            className="p-2 hover:bg-secondary-900 rounded-lg hover:text-primary
          disabled:hover:bg-secondary-100 disabled:text-gray-300 transition-colors"
            onClick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}
          >
            <RiArrowDropLeftLine className="text-2xl" />
          </button>

          {table.getPageOptions().map((value, key) => (
            <button
              key={key}
              className={classNames({
                "text-gray-100 bg-secondary-100 py-0.5 px-2 font-bold rounded-lg hover:bg-secondary-900 hover:text-primary disabled:hover:bg-secondary-100 disabled:text-gray-300": true,
                "bg-secondary-900 text-primary":
                  value === table.getState().pagination.pageIndex,
              })}
              onClick={() => table.setPageIndex(value)}
            >
              {value + 1}
            </button>
          ))}

          <button
            className="p-2 hover:bg-secondary-900 rounded-lg hover:text-primary
              disabled:hover:bg-secondary-100 disabled:text-gray-300 transition-colors"
            onClick={() => table.nextPage()}
            disabled={!table.getCanNextPage()}
          >
            <RiArrowDropRightLine className="text-2xl" />
          </button>
          <button
            className="p-2 hover:bg-secondary-900 rounded-lg hover:text-primary
              disabled:hover:bg-secondary-100 disabled:text-gray-300 transition-colors"
            onClick={() => table.setPageIndex(table.getPageCount() - 1)}
            disabled={!table.getCanNextPage()}
          >
            <RiArrowRightDoubleLine className="text-2xl" />
          </button>
        </div>
        <div className="text-gray-600 font-semibold">
          Mostrando recibos del {getStateTable().firstIndex} a{" "}
          {getStateTable().lastIndex} de un total de {getStateTable().totalRows}{" "}
          recibos{" "}
        </div>
        {/*Selector */}
        <select
          className="bg-secondary-900 text-primary font-bold  rounded-lg outline-secondary-100 py-2"
          onChange={(e) => table.setPageSize(Number(e.target.value))}
        >
          <option value="5">5 reg.</option>
          <option value="10">10 reg.</option>
          <option value="20">20 reg.</option>
          <option value="25">25 reg.</option>
          <option value="30">30 reg.</option>
        </select>
      </div>
    </>
  );
};
