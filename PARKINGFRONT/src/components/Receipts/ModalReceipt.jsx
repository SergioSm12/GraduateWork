import React, { useEffect, useState } from "react";
import { useReceipts } from "../../hooks/useReceipts";
import {
  RiCloseCircleLine,
  RiParkingBoxLine,
  RiPoliceCarLine,
  RiTicket2Line,
} from "react-icons/ri";
import { format } from "date-fns/esm";
export const ModalReceipt = () => {
  const { receiptSelected, handlerCloseModalShowReceipt, initialReceiptForm } =
    useReceipts();
  const [receiptShow, setReceiptShow] = useState(receiptSelected);

  useEffect(() => {
    if (receiptSelected) {
      setReceiptShow({ ...receiptSelected });
    }
  
  }, [receiptSelected]);

  const onCloseModal = () => {
    handlerCloseModalShowReceipt();
  };
  // funcion de formato moneda
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
    }).format(amount);
  };
  return (
    <div className="abrir-modal animacion fadeIn">
      <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center transition-opacity duration-300">
        <div className="bg-secondary-900 p-8 rounded-lg">
          <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
            <div className="flex items-start justify-between">
              <h1 className="text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
                Detalle Recibo
              </h1>
              <button
                className=" py-2 px-2 text-red-600 hover:text-black bg-secondary-900/80  hover:bg-red-600/50 rounded-lg  transition-colors"
                type="button"
                onClick={() => onCloseModal()}
              >
                <RiCloseCircleLine className="text-2xl " />
              </button>
            </div>
            <div>
              <div>
                <h5 className="text-gray-100 text-md mb-1">Nombre: </h5>
                <p className="text-gray-500 text-sm">
                  {receiptShow.user.name} {receiptShow.user.lastName}
                </p>
                <h5 className="text-gray-100 text-md mb-1">Correo: </h5>
                <p className="text-gray-500 text-sm">
                  {receiptShow.user.email}
                </p>
              </div>
              <hr className="my-4 border-gray-500/30" />
              <div className="flex  items-center justify-between">
                <div>
                  <h5 className="text-gray-100 text-md mb-1">Vehiculo: </h5>
                  <p className="text-gray-500 text-sm">
                    {receiptShow.vehicle.vehicleType.name}
                  </p>
                </div>
                <div>
                <h5 className="text-gray-100 text-md mb-1">Placa: </h5>
                  <p className="text-gray-500 text-sm">
                    {receiptShow.vehicle.plate}
                  </p>
                </div>
              </div>
              <hr className="my-4 border-gray-500/30" />
              <div className="flex items-center justify-between mb-2">
                <h1 className="text-xl text-primary font-bold">Factura</h1>
                <span className="flex items-center gap-2">
                  <RiTicket2Line /> {receiptShow.id}
                </span>
              </div>
              <div>
                <div>
                  <h5 className="text-gray-100 text-md mb-1">
                    Estado de pago :{" "}
                  </h5>
                  {receiptShow.paymentStatus ? (
                    <p className="text-black text-center font-bold p-1 rounded-lg  bg-green-600 text-sm">
                      Pago
                    </p>
                  ) : (
                    <p className="text-black text-center font-bold p-1 rounded-lg  bg-red-600/80 text-sm">
                      Pendiente de pago
                    </p>
                  )}
                </div>
                <div className="md:flex items-center justify-between my-4">
                  <div>
                    <h5 className="text-gray-100 text-md mb-1">
                      Fecha de emisión:
                    </h5>
                    <p className="text-primary/80 bg-secondary-900 p-1 rounded-lg text-sm">
                      {format(
                        new Date(receiptShow.issueDate),
                        "dd 'de' MMMM 'del' yyyy"
                      )}
                    </p>
                  </div>
                  <div>
                    <h5 className="text-gray-100 text-md mb-1">
                      Fecha de vencimiento:
                    </h5>
                    <p className="text-primary/80 bg-secondary-900 p-1 rounded-lg text-sm">
                      {format(
                        new Date(receiptShow.dueDate),
                        "dd 'de' MMMM 'del' yyyy"
                      )}
                    </p>
                  </div>
                </div>
                <div>
                  <h5 className="text-gray-100 text-md mb-1 flex items-center justify-between">
                    Total {receiptShow.paymentStatus ? "cancelado" : "a pagar"}{" "}
                    :
                  </h5>
                  <p className="text-primary/90 font-bold text-center bg-secondary-900 p-1 rounded-lg text-sm mb-2">
                    {formatCurrency(receiptShow.rate.amount)}
                  </p>
                  <hr className="my-4 border-gray-500/30" />
                  <p className="text-center">{receiptShow.rate.time}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};