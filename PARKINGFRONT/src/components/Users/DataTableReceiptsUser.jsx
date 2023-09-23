import {
  flexRender,
  getCoreRowModel,
  useReactTable,
  getPaginationRowModel,
  getFilteredRowModel,
  getSortedRowModel,
} from "@tanstack/react-table";
import { rankItem } from "@tanstack/match-sorter-utils";
import classNames from "classnames";
import React, { useEffect, useState } from "react";
import {
  RiDeleteBin7Line,
  RiEdit2Line,
  RiInformationLine,
  RiLineHeight,
  RiSearch2Line,
  RiSortAsc,
  RiSortDesc,
} from "react-icons/ri";
import { NavLink } from "react-router-dom";
import { format } from "date-fns";

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

export const DataTableReceiptsUser = ({ dataReceipts }) => {
  const [data, setData] = useState(dataReceipts);
  const [globalFilter, setGlobalFilter] = useState("");
  const [sorting, setSorting] = useState([]);

  useEffect(() => {
    setData(dataReceipts);
  }, [dataReceipts]);

  console.log(data);

  const columns = [
    {
      accessorKey: "vehiclePlate",
      header: () => <span>Placa</span>,
    },
    {
      accessorKey: "paymentStatus",
      header: () => <span>Estado de pago</span>,
    },
    {
      accessorKey: "issueDate",
      header: () => <span>Fecha de emision</span>,
    },
    {
      accessorKey: "dueDate",
      header: () => <span>Fecha de vencimiento</span>,
    },
    
    {
      accessorKey: "actions",
      header: "Acciones",
      enableSorting: false,
    },
  ];

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

  return (
    <>
      {/*input*/}
      <div className="mb-5">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-y-4 ">
          <h1 className=" font-bold text-sm md:text-3xl mb-6">
            Busca el recibo por cualquier campo.
          </h1>
        </div>

        <div className="relative">
          <RiSearch2Line className="absolute top-1/2  -translate-y-1/2 left-4" />
          <DebuncedInput
            type="text"
            value={globalFilter ?? ""}
            onchange={(value) => {
              setGlobalFilter(String(value));
              //getUsers();
            }}
            className="bg-secondary-900 outline-none py-2 pr-4 pl-10 rounded-lg
              placeholder:text-gray-500 w-full"
            placeholder="Buscar..."
          />
        </div>
      </div>
      <div className="overflow-x-auto">
        <table className="table-auto min-w-full">
          <thead>
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th
                    key={header.id}
                    className={classNames({
                      "py-2 px-4 sm:px-6 md:px-8 lg:px-10 xl:px-12": true,
                      "": header.id === "vehiclePlate",
                    })}
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
                    className={classNames({
                      "py-2 px-4 sm:px-6 md:px-8 lg:px-10 xl:px-12 border ": true,
                      "w-1/4": cell.column.id === "vehiclePlate",
                    })}
                  >
                    {cell.column.id === "vehiclePlate" && (
                      <div className=" flex justify-center">
                        {row.original.vehicle.plate}
            
                      </div>
                    )}
                    {cell.column.id === "paymentStatus" && (
                      <div
                        className={classNames({
                          "p-2 text-red-500/80 bg-secondary-100 rounded-lg":
                            !row.original.paymentStatus,
                          "p-2 text-green-500/80 bg-secondary-100 rounded-lg":
                            row.original.paymentStatus,
                        })}
                      >
                        {row.original.paymentStatus ? "Pagado" : "Pendiente"}
                      </div>
                    )}

                    {cell.column.id === "issueDate" && (
                      <div>
                        {format(
                          new Date(row.original.issueDate),
                          "dd 'de' MMMM 'del' yyyy 'a las' HH:mm"
                        )}
                      </div>
                    )}

                    {cell.column.id === "dueDate" && (
                      <div>
                        {format(
                          new Date(row.original.dueDate),
                          "dd 'de' MMMM 'del' yyyy 'a las' HH:mm"
                        )}
                      </div>
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
    </>
  );
};
