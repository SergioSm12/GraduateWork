import React, { useEffect, useState } from "react";
import {
  RiAlertLine,
  RiCalendarCheckLine,
  RiCalendarCloseLine,
  RiCloseCircleLine,
  RiPoliceCarLine,
  RiTicket2Line,
} from "react-icons/ri";
import { useReceipts } from "../../hooks/useReceipts";
import { useRates } from "../../hooks/useRates";
import { useParams } from "react-router-dom";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { formatInTimeZone } from "date-fns-tz";
import { parse } from "date-fns";
import { es } from "date-fns/locale";

const formatCurrency = (amount) => {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
  }).format(amount);
};

export const CreateReceipt = () => {
  const {
    handlerCloseModalFormReceipt,
    receiptSelected,
    vehicleSelected,
    vehicle,
    handlerAddReceiptByUser,
    initialReceiptForm,
    errorsReceipt,
  } = useReceipts();
  const { rates, getRates } = useRates();

  //estado para traer los datos del vehicle
  const [vehicleForm, setVehicleForm] = useState(vehicle);
  const [vehicleFormEdit, setVehicleFormEdit] = useState(vehicle);
  //Estado para guardar lso datos del recibo
  const [receiptForm, setReceiptForm] = useState(initialReceiptForm);
  const [selectedRate, setSelectedRate] = useState(null);
  //DatePicker fechas
  const [issueDate, setIssueDate] = useState(new Date());
  const [dueDate, setDueDate] = useState(new Date());
  const { id } = useParams();

  //Agrega el recibo si viene seleccionado
  useEffect(() => {
    setReceiptForm({
      ...receiptSelected,
    });
    setSelectedRate(receiptSelected.rate);
    setVehicleFormEdit(receiptSelected.vehicle);
    if (receiptSelected.issueDate) {
      const formattedIssueDate = formatInTimeZone(
        receiptSelected.issueDate,
        "America/Bogota",
        "yyyy-MM-dd"
      );

      //parsear fecha formateada a timezone
      const parsedIssueDate = parse(
        formattedIssueDate,
        "yyyy-MM-dd",
        new Date()
      );
      setIssueDate(parsedIssueDate);
    }
    if (receiptSelected.dueDate) {
      const formattedDueDate = formatInTimeZone(
        receiptSelected.dueDate,
        "America/Bogota",
        "yyyy-MM-dd"
      );
      //Parsear fecha formatead timeZone
      const parsedDueDate = parse(formattedDueDate, "yyyy-MM-dd", new Date());
      setDueDate(parsedDueDate);
    }
  }, [receiptSelected]);

  //Traer rates y llenar estado vehicle
  useEffect(() => {
    getRates();
    setVehicleForm(vehicleSelected);
  }, []);

  //cerrar formulario
  const onCloseForm = () => {
    handlerCloseModalFormReceipt();
  };

  //Monitorea el cambio en los inputs y los agreaga al state receiptform
  const onInputChange = ({ target }) => {
    const { name, value } = target;

    if (name === "rate") {
      const rateId = value;
      const rate = rates.find((r) => r.id === parseInt(rateId));
      setSelectedRate(rate);

      setReceiptForm({
        ...receiptForm,
        [name]: rate,
      });
    } else {
      setReceiptForm({
        ...receiptForm,
        [name]: value,
      });
    }
  };

  //cambia la data del datePicker
  const handleIssueDateChange = (date) => {
    setIssueDate(date);
  };

  const handleDueDateChange = (date) => {
    setDueDate(date);
  };

  //cambia la data de paymentStatus
  const onPaymentStatusChange = (e) => {
    const { checked } = e.target;
    setReceiptForm((prevState) => ({
      ...prevState,
      paymentStatus: checked,
    }));
  };

  //Envia la data cuando se encia el formulario
  const onSubmit = (e) => {
    e.preventDefault();
    const updatedReceiptForm = {
      ...receiptForm,
      vehicle: vehicleForm,
      issueDate: issueDate,
      dueDate: dueDate,
    };

    handlerAddReceiptByUser(id, updatedReceiptForm);
  };

  return (
    <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
      <div className="flex items-start justify-between">
        <h1 className=" text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
          {receiptForm.id > 0 ? "Editar" : "Generar"}{" "}
          <span className="text-primary">Recibo</span>
        </h1>
        <button
          className=" py-2 px-2 text-red-600 hover:text-black bg-secondary-900/80  hover:bg-red-600/50 rounded-lg  transition-colors"
          type="button"
          onClick={() => onCloseForm()}
        >
          <RiCloseCircleLine className="text-2xl " />
        </button>
      </div>
      {receiptForm.id > 0 ? (
        <div className="mb-3">
          <div className=" flex justify-center">
            <span className="flex items-center gap-2  bg-secondary-900 py-4 px-4 rounded-lg">
              <RiAlertLine className="text-yellow-600" /> Revise las fechas
              antes de editar.
            </span>
          </div>
        </div>
      ) : (
        <></>
      )}
      <form onSubmit={onSubmit}>
        <label className="ml-2 text-white">Vehiculo :</label>
        <div className="relative my-2">
          <RiPoliceCarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary " />
          <div className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none">
            {receiptForm.id > 0
              ? `${vehicleFormEdit.plate} - ${vehicleFormEdit.vehicleType.name}`
              : `${vehicleForm.plate} - ${vehicleForm.vehicleType.name}`}
          </div>
        </div>
        <label htmlFor="rate" className="ml-2 text-white">
          Tarifa:
        </label>
        <div className="relative my-1">
          <RiTicket2Line className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
          <select
            className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
            id="rate"
            name="rate"
            value={selectedRate ? selectedRate.id : ""}
            onChange={onInputChange}
          >
            <option defaultValue="">seleccione la tarifa</option>
            {rates.map((rate) => (
              <option key={rate.id} value={rate.id}>
                {rate.time} - {formatCurrency(rate.amount)}
              </option>
            ))}
          </select>
        </div>
        <div className="relative mb-2">
          <p className="text-red-500">
            {errorsReceipt?.rate == "Debe seleccionar una tarifa"
              ? JSON.stringify(errorsReceipt?.rate)
              : ""}
          </p>
        </div>

        {/*Filtrar campos en caso de que no sea para editar */}
        {receiptForm.id > 0 ? (
          <>
            <label htmlFor="issueDate" className="ml-2 text-white">
              Fecha válida desde el:
            </label>
            <div className="my-2">
              <DatePicker
                id="issueDate"
                showIcon
                selected={issueDate}
                onChange={handleIssueDateChange}
                locale={es}
                className="py-3 pl-8 pr-4 text-center bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
                timeInputLabel="Time:"
                dateFormat={"dd 'de' MMMM yyyy"}
                icon={<RiCalendarCheckLine className="text-primary" />}
              />
            </div>
            <label htmlFor="dueDate" className="ml-2 text-white">
              Fecha inválida desde:
            </label>
            <div className="my-2">
              <DatePicker
                id="dueDate"
                showIcon
                selected={dueDate}
                onChange={handleDueDateChange}
                locale={es}
                className="py-3 pl-8 pr-4 text-center bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
                timeInputLabel="Time:"
                dateFormat={"dd 'de' MMMM yyyy"}
                icon={<RiCalendarCloseLine className="text-primary" />}
              />
            </div>
            <hr className="m-4 border border-dashed border-gray-400/50" />
            <div className="relative mb-2  ">
              <label
                htmlFor="paymentStatus"
                className="flex items-center space-x-2"
              >
                <input
                  type="checkbox"
                  id="paymentStatus"
                  name="paymentStatus"
                  checked={receiptForm.paymentStatus || ""}
                  onChange={(e) => onPaymentStatusChange(e)}
                />
                <span className="">Recibo pagado</span>
              </label>
              <hr className="m-4 border border-dashed border-gray-400/50" />
            </div>
          </>
        ) : (
          <></>
        )}

        <button
          type="submit"
          className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
        >
          {receiptForm.id > 0 ? "Editar" : "Generar"}
        </button>
      </form>
    </div>
  );
};
