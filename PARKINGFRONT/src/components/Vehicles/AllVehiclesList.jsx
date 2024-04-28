import React, { useEffect, useState } from "react";
import {
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from "@tanstack/react-table";
import {
  RiDeleteBin7Line,
  RiLineHeight,
  RiPagesFill,
  RiPagesLine,
  RiSearch2Line,
  RiSortAsc,
  RiSortDesc,
} from "react-icons/ri";
import { Paginator } from "../Paginator";
import classNames from "classnames";
import { rankItem } from "@tanstack/match-sorter-utils";
import { useReceipts } from "../../hooks/useReceipts";
import { ModalFormReceipt } from "../Receipts/ModalFormReceipt";
import { useNightlyReceipts } from "../../hooks/useNightlyReceipts";

//Componente con TanStackReacttable
//Tabla vehiculos dentro de seccion administrar vehiculos.

const fuzzyFilter = (row, columnId, value, addMeta) => {
  const itemRank = rankItem(row.original.plate, value);

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

export const AllVehicles = ({ dataVehicles }) => {
  const [data, setData] = useState(dataVehicles);
  const [globalFilter, setGlobalFilter] = useState("");
  const [sorting, setSorting] = useState([]);
  const { handlerOpenModalFormReceipt, visibleFormReceiptModal } =
    useReceipts();
  const { handlerOpenModalFormNightlyReceipt, visibleFormNightlyReceiptModal } =
    useNightlyReceipts();

  useEffect(() => {
    setData(dataVehicles);
  }, [dataVehicles]);

  const columns = [
    {
      accessorKey: "plate",
      header: () => <span>Placa</span>,
    },
    {
      accessorKey: "vehicleType",
      header: () => <span>Tipo de vehículo</span>,
    },
    {
      accessorKey: "user",
      header: () => <span>Usuario</span>,
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
        pageSize: 10,
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
      {/*Create Nightly Receipt */}
      {!visibleFormNightlyReceiptModal || (
        <ModalFormReceipt receiptType={"nocturno"} />
      )}
      {/*Create receipt modal */}
      {!visibleFormReceiptModal || <ModalFormReceipt />}

      {/*Input*/}
      <div className="mb-5">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-y-4 ">
          <h1 className=" font-bold text-sm md:text-3xl mb-6">
            Busca el vehiculo por placa.
          </h1>
        </div>

        <div className="relative">
          <RiSearch2Line className="absolute top-1/2  -translate-y-1/2 left-4" />
          <DebuncedInput
            type="text"
            value={globalFilter ?? ""}
            onchange={(value) => {
              setGlobalFilter(String(value));
            }}
            className="bg-secondary-900 outline-none py-2 pr-4 pl-10 rounded-lg
                placeholder:text-gray-500 w-full"
            placeholder="Buscar vehiculo..."
          />
        </div>
      </div>
      {/*Table */}
      <div className="overflow-x-auto">
        <table className="table-auto min-w-full border-collapse">
          <thead>
            {table.getHeaderGroups().map((headerGroup) => (
              <tr key={headerGroup.id}>
                {headerGroup.headers.map((header) => (
                  <th key={header.id}>
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
                      "px-2 border text-lg": true,
                      "w-0": cell.column.id === "plate",
                    })}
                  >
                    {cell.column.id === "plate" && (
                      <div className=" flex justify-center">
                        {row.original.plate}
                      </div>
                    )}
                    {cell.column.id === "vehicleType" && (
                      <div className="flex justify-center">
                        {row.original.vehicleType.name}
                      </div>
                    )}

                    {cell.column.id === "user" && (
                      <div className="flex justify-center">
                        {row.original.user.name} {row.original.user.lastName}
                      </div>
                    )}

                    {cell.column.id === "actions" && (
                      <div className="flex items-center justify-center gap-2">
                        <button
                          type="button"
                          className="py-2 px-2 bg-primary/80 text-black hover:bg-secondary-100 rounded-lg transition-colors"
                          onClick={() => {
                            handlerOpenModalFormReceipt({
                              id: row.original.id,
                              plate: row.original.plate,
                              vehicleType: row.original.vehicleType,
                              active: row.original.active,
                              user: row.original.user,
                            });
                          }}
                        >
                          <RiPagesLine className="text-lg" />
                        </button>

                        <button
                          type="button"
                          className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                          onClick={() => {
                            // Pasa los datos del usuario al hacer clic en el botón de edición
                            handlerOpenModalFormNightlyReceipt({
                              id: row.original.id,
                              plate: row.original.plate,
                              vehicleType: row.original.vehicleType,
                              active: row.original.active,
                              user: row.original.user,
                            });
                          }}
                        >
                          <RiPagesFill className="text-lg" />
                        </button>
                        <button
                          type="button"
                          className="py-2 px-2 bg-secondary-100/50 hover:bg-secondary-100 text-red-500/70 hover:text-red-500
                   transition-colors rounded-lg  flex items-center "
                          onClick={() => {
                            handlerRemoveReceipt(row.original.id);
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
        {/*paginator*/}
        <Paginator getStateTable={getStateTable} table={table} />
      </div>
    </>
  );
};
