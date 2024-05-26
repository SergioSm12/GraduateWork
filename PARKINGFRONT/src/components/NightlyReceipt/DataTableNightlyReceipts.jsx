import { rankItem } from "@tanstack/match-sorter-utils";
import React, { useEffect, useState } from "react";
import { useNightlyReceipts } from "../../hooks/useNightlyReceipts";
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
  RiQrCodeLine,
  RiSearch2Line,
  RiSortAsc,
  RiSortDesc,
} from "react-icons/ri";
import { formatInTimeZone } from "date-fns-tz";
import { es } from "date-fns/locale";
import { Paginator } from "../Paginator";
import classNames from "classnames";
import { QRCode } from "../QR/QRCode";
import { ModalReceipt } from "../Receipts/ModalReceipt";
import { ModalFormReceipt } from "../Receipts/ModalFormReceipt";
import { GenerateInvoicePdf } from "../PDF/GenerateInvoicePdf";
import { useAuth } from "../../auth/hooks/useAuth";

const fuzzyFilter = (row, columnId, value, addMeta) => {
  const itemRank = rankItem(row.original.vehicle.plate, value);

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

export const DataTableNightlyReceipts = ({ dataReceipts }) => {
  const [data, setData] = useState(dataReceipts);
  const [globalFilter, setGlobalFilter] = useState("");
  const [sorting, setSorting] = useState([]);
  const {
    handlerChangePaymentStatus,
    visibleFormNightlyReceiptModal,
    visibleQRModalNightlyReceipt,
    handlerOpenModalQRNightlyReceipt,
    handlerNightlyReceiptSelectedModalShow,
    handlerNightlyReceiptSelectedModalForm,
    visibleShowNightlyReceiptModal,
    handlerRemoveNightlyReceipt,
  } = useNightlyReceipts();

  const { login } = useAuth();

  useEffect(() => {
    setData(dataReceipts);
  }, [dataReceipts]);

  const columns = [
    {
      accessorKey: "vehiclePlate",
      header: () => <span>Placa</span>,
      enableSorting: false,
    },
    {
      accessorKey: "paymentStatus",
      header: () => <span>Estado de pago</span>,
    },
    {
      accessorKey: "amount",
      header: () => <span>Valor</span>,
      enableSorting: false,
    },
    {
      accessorKey: "initialTime",
      header: () => <span>Fecha de emision</span>,
    },
    {
      accessorKey: "departureTime",
      header: () => <span>Fecha de salida</span>,
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

  const handlePaymentStatusChange = async (receiptId) => {
    await handlerChangePaymentStatus(receiptId);
  };

  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
    }).format(amount);
  };
  return (
    <>
      {/*input*/}
      <div className="mb-5">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-y-4 ">
          <h1 className=" font-bold text-sm md:text-3xl mb-6">
            Busca el recibo por placa.
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
            placeholder="Buscar..."
          />
        </div>
      </div>
      {/*Modal create */}
      {!visibleFormNightlyReceiptModal || (
        <ModalFormReceipt receiptType={"nocturno"} />
      )}
      {/*Modal info */}
      {!visibleShowNightlyReceiptModal || <ModalReceipt />}
      {/*Modal QR */}
      {!visibleQRModalNightlyReceipt || <QRCode />}
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
                      "w-0": cell.column.id === "vehiclePlate",
                    })}
                  >
                    {cell.column.id === "vehiclePlate" && (
                      <div className=" flex justify-between gap-2">
                        {row.original.vehicle.plate}
                        <GenerateInvoicePdf
                          row={row}
                          receiptType={"nocturno"}
                        />
                      </div>
                    )}
                    {cell.column.id === "paymentStatus" && (
                      <div className="relative">
                        <button
                          type="button"
                          className={classNames({
                            "p-1 my-3 rounded-lg text-center w-full transition-colors": true,
                            "text-red-500/80 bg-secondary-100":
                              !row.original.paymentStatus,
                            "text-green-500/80 bg-secondary-100":
                              row.original.paymentStatus,
                            "hover:border border-primary/80": login.isAdmin,
                            "cursor-not-allowed": !login.isAdmin,
                          })}
                          onClick={
                            login.isAdmin
                              ? () => handlePaymentStatusChange(row.original.id)
                              : null
                          }
                          disabled={!login.isAdmin}
                        >
                          {row.original.paymentStatus ? "Pagado" : "Pendiente"}
                        </button>
                      </div>
                    )}

                    {cell.column.id === "amount" && (
                      <div className="justify-center text-center">
                        {formatCurrency(row.original.amount)}
                      </div>
                    )}

                    {cell.column.id === "initialTime" && (
                      <div className="text-center">
                        {row.original.initialTime
                          ? formatInTimeZone(
                              row.original.initialTime,
                              "America/Bogota",
                              "dd 'de' MMMM 'del' yyyy 'a las' HH:mm",
                              { locale: es }
                            )
                          : "Sin fecha de emisión"}
                      </div>
                    )}

                    {cell.column.id === "departureTime" && (
                      <div
                        className={classNames({
                          "text-red-500/50 text-center":
                            new Date(row.original.departureTime) <= new Date(),
                          "text-center":
                            new Date(row.original.departureTime) > new Date(),
                        })}
                      >
                        {row.original.departureTime
                          ? formatInTimeZone(
                              row.original.departureTime,
                              "America/Bogota",
                              "dd 'de' MMMM 'del' yyyy 'a las' HH:mm",
                              { locale: es }
                            )
                          : "Sin fecha de vencimiento"}
                      </div>
                    )}

                    {cell.column.id === "actions" && (
                      <div className="flex items-center justify-center gap-2">
                        <button
                          type="button"
                          className="py-2 px-2 bg-primary/80 text-black hover:bg-secondary-100 rounded-lg transition-colors"
                          onClick={() =>
                            handlerOpenModalQRNightlyReceipt(row.original.id)
                          }
                        >
                          <RiQrCodeLine className="text-lg" />
                        </button>

                        <button
                          type="button"
                          className="py-2 px-2 bg-primary/80 text-black hover:bg-secondary-100 rounded-lg transition-colors"
                          onClick={() => {
                            handlerNightlyReceiptSelectedModalShow(
                              row.original
                            );
                          }}
                        >
                          <RiInformationLine className="text-lg" />
                        </button>

                        {login.isGuard ? (
                          <>
                            <button
                              type="button"
                              className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                              onClick={() => {
                                // Pasa los datos del usuario al hacer clic en el botón de edición
                                handlerNightlyReceiptSelectedModalForm(
                                  row.original
                                );
                              }}
                            >
                              <RiEdit2Line className="text-lg" />
                            </button>
                          </>
                        ) : (
                          <>
                            {login.isAdmin ? (
                              <>
                                <button
                                  type="button"
                                  className="py-2 px-2 bg-primary/80 text-black hover:bg-primary rounded-lg transition-colors"
                                  onClick={() => {
                                    // Pasa los datos del usuario al hacer clic en el botón de edición
                                    handlerNightlyReceiptSelectedModalForm(
                                      row.original
                                    );
                                  }}
                                >
                                  <RiEdit2Line className="text-lg" />
                                </button>
                                <button
                                  type="button"
                                  className="py-2 px-2 bg-secondary-100/50 hover:bg-secondary-100 text-red-500/70 hover:text-red-500
                   transition-colors rounded-lg  flex items-center "
                                  onClick={() => {
                                    handlerRemoveNightlyReceipt(
                                      row.original.id
                                    );
                                  }}
                                >
                                  <RiDeleteBin7Line className="text-lg" />
                                </button>
                              </>
                            ) : (
                              <></>
                            )}
                          </>
                        )}
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
