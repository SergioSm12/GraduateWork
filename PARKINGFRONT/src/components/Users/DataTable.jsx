import React, { useEffect, useState } from "react";
import { useUsers } from "../../hooks/useUsers";
import {
  flexRender,
  getCoreRowModel,
  useReactTable,
  getPaginationRowModel,
  getFilteredRowModel,
  getSortedRowModel,
} from "@tanstack/react-table";
import {
  RiDeleteBin7Line,
  RiEdit2Line,
  RiInformationLine,
  RiLineHeight,
  RiLoopLeftFill,
  RiSearch2Line,
  RiShutDownLine,
  RiSortAsc,
  RiSortDesc,
} from "react-icons/ri";
import { rankItem } from "@tanstack/match-sorter-utils";
import classNames from "classnames";
import { ModalForm } from "./ModalForm";
import { NavLink } from "react-router-dom";
import { Paginator } from "../Paginator";
const fuzzyFilter = (row, columnId, value, addMeta) => {
  const itemRank = rankItem(row.getValue(columnId), value);

  addMeta({ itemRank });

  return itemRank.passed;
};

const DebuncedInput = ({ value: keyWord, onchange, ...props }) => {
  const [value, setValue] = useState(keyWord);

  useEffect(() => {
    const timeout = setTimeout(() => {
      onchange(value);
    }, 500);

    return () => clearTimeout(timeout);
  }, [value]);

  return (
    <input
      {...props}
      value={value}
      onChange={(e) => setValue(e.target.value)}
    />
  );
};

export const DataTable = ({ dataUsers, visibleFormCreate }) => {
  const {
    isLoadingUsers,
    getUsers,
    handlerUserSelectedForm,
    handlerDeactivateUser,
    handlerActivateUser,
    handlerRemoveUser,
  } = useUsers();
  const [data, setData] = useState(dataUsers);
  const [globalFilter, setGlobalFilter] = useState("");
  const [sorting, setSorting] = useState([]);

  useEffect(() => {
    setData(dataUsers);
  }, [dataUsers]);

  if (isLoadingUsers) {
    return (
      <div className="flex flex-col gap-2 items-center bg-secondary-900 py-4 rounded-lg">
        <div className="animate-spin rounded-full border-t-4 border-secondary border-opacity-50 h-12 w-12"></div>
        <span className="text-primary text-2xl flex items-center gap-2">
          {" "}
          <RiLoopLeftFill /> Cargando usuarios ...
        </span>
      </div>
    );
  }

  const columns = [
    {
      accessorKey: "name",
      header: () => <span>Nombre(s)</span>,
    },
    {
      accessorKey: "lastName",
      header: () => <span>Apellido(s)</span>,
    },
    {
      accessorKey: "email",
      header: () => <span>Correo</span>,
    },
    {
      accessorKey: "active",
      header: () => <span>Estado</span>,
      cell:(row)=>{
       if(row.getValue()== true){
        return(
          <span className="py-2 px-2 rounded-lg bg-secondary-100 text-gray-400">Activo</span>
        )
       }else{
        return(
          <span className="py-2 px-2 rounded-lg text-red-500/80 bg-secondary-100 ">
Inactivo
          </span>
        )
       }
      }
    },
    {
      accessorKey: "actions",
      header: <span>Acciones</span>,
      enableSorting: false,
    },
  ];

  const getStateTable = () => {
    const totalRows = table.getFilteredRowModel().rows.length;
    const pageSize = table.getState().pagination.pageSize;
    const pageIndex = table.getState().pagination.pageIndex;
    const rowsPerPage = table.getRowModel().rows.length;

    const firstIndex = pageIndex * pageSize + 1;
    const lastIndex = pageIndex * pageSize + rowsPerPage;
    return {
      totalRows,
      firstIndex,
      lastIndex,
    };
  };

  const table = useReactTable({
    data,
    columns,
    state: {
      globalFilter,
      sorting,
    },
    initialState: {
      pagination: {
        pageSize: 5,
      },
    },
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    globalFilterFn: fuzzyFilter,
    getSortedRowModel: getSortedRowModel(),
    onSortingChange: setSorting,
  });
  return (
    <>
      <div className="">
        {/*input*/}
        <div className="mb-5">
          <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-y-4 ">
            <h1 className=" font-bold text-sm md:text-3xl mb-6">
              Busca el usuario por cualquier campo.
            </h1>
          </div>

          {visibleFormCreate || (
            <div className="relative">
              <RiSearch2Line className="absolute top-1/2  -translate-y-1/2 left-4" />
              <DebuncedInput
                type="text"
                value={globalFilter ?? ""}
                onchange={(value) => {
                  setGlobalFilter(String(value));
                  getUsers();
                }}
                className="bg-secondary-900 outline-none py-2 pr-4 pl-10 rounded-lg
              placeholder:text-gray-500 w-full"
                placeholder="Buscar..."
              />
            </div>
          )}
        </div>
        {/*Modal para crear*/}
        {!visibleFormCreate || <ModalForm />}
        {/*Tabla */}
        <div className="overflow-x-auto">
          <table className="table-auto min-w-full">
            <thead>
              {table.getHeaderGroups().map((headerGroup) => (
                <tr key={headerGroup.id}>
                  {headerGroup.headers.map((header) => (
                    <th
                      key={header.id}
                      className="py-2 px-4 sm:px-6 md:px-8 lg:px-10 xl:px-12"
                    >
                      {header.isPlaceholder ? null : (
                        <div
                          className={classNames({
                            " flex items-center gap-2 justify-between select-none cursor-pointer hover:text-primary hover:bg-secondary-900 py-2 px-2 rounded-lg":
                              header.column.getCanSort(),
                          })}
                          onClick={header.column.getToggleSortingHandler()}
                        >
                          {flexRender(
                            header.column.columnDef.header,
                            header.getContext()
                          )}
                          {{
                            asc: <RiSortAsc />,
                            desc: <RiSortDesc />,
                          }[header.column.getIsSorted()] ??
                            (header.column.getCanSort() ? (
                              <RiLineHeight />
                            ) : null)}
                        </div>
                      )}
                    </th>
                  ))}
                </tr>
              ))}
            </thead>
            <tbody>
              {table.getRowModel().rows.map((row) => (
                <tr key={row.id} className="bg-secondary-900 ">
                  {row.getVisibleCells().map((cell) => (
                    <td
                      key={cell.id}
                      className="py-2 px-4 sm:px-6 md:px-8 lg:px-10 xl:px-12 border "
                    >
                      {flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )}

                      {cell.column.id === "actions" && (
                        <div className="flex items-center gap-2">
                          <NavLink
                            className="py-2 px-2 bg-primary/80 text-black hover:bg-secondary-100 rounded-lg transition-colors"
                            to={"show/" + row.original.id}
                          >
                            <RiInformationLine className="text-lg" />
                          </NavLink>
                          <button
                            type="button"
                            className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                            onClick={() => {
                              // Pasa los datos del usuario al hacer clic en el botón de edición
                              handlerUserSelectedForm(row.original);
                            }}
                          >
                            <RiEdit2Line className="text-lg" />
                          </button>

                          {row.original.active == false ? (
                            <button
                              type="button"
                              className="py-2 px-2 bg-green-500/80 text-black  rounded-lg  hover:bg-green-500"
                              onClick={() => {
                                handlerActivateUser(row.original.id);
                              }}
                            >
                              <RiShutDownLine className="text-lg" />
                            </button>
                          ) : (
                            <button
                              type="button"
                              className="py-2 px-2 bg-red-500/80 text-black  rounded-lg  hover:bg-red-500"
                              onClick={() => {
                                handlerDeactivateUser(row.original.id);
                              }}
                            >
                              <RiShutDownLine className="text-lg" />
                            </button>
                          )}

                          <button
                            type="button"
                            className="py-2 px-2 bg-secondary-100/50 hover:bg-secondary-100 text-red-500/70 hover:text-red-500
                 transition-colors rounded-lg  flex items-center "
                            onClick={() => {
                              handlerRemoveUser(row.original.id);
                            }}
                          >
                            <RiDeleteBin7Line className="text-lg" />
                          </button>
                        </div>
                      )}
                    </td>
                  ))}
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {/*Paginador*/}
        <Paginator getStateTable={getStateTable} table={table} />
      </div>
    </>
  );
};
