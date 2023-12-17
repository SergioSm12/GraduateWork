import React, { useEffect, useState } from "react";
import { useVisitorReceipt } from "../../hooks/useVisitorReceipt";
import { useRates } from "../../hooks/useRates";
import {
  RiCalendarCheckLine,
  RiCalendarCloseLine,
  RiCarLine,
  RiCloseCircleLine,
  RiTicket2Line,
} from "react-icons/ri";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { formatInTimeZone } from "date-fns-tz";
import { parse } from "date-fns";
import { es } from "date-fns/locale";

export const ModalFormVisitorReceipt = () => {
  const {
    initialVisitorReceipt,
    handlerCloseModalFormVisitorReceipt,
    visitorReceiptSelected,
    handlerAddReceiptVisitor,
    errorsVisitorReceipt,
  } = useVisitorReceipt();
  const { rates, getRates } = useRates();

  //estado para administrar datos del recibo
  const [visitorReceiptForm, setVisitorReceiptForm] = useState(
    initialVisitorReceipt
  );
  const [selectedRate, setSelectedRate] = useState(null);
  //DatePicker fechas
  const [issueDate, setIssueDate] = useState(new Date());
  const [dueDate, setDueDate] = useState(new Date());

  //Agreagar el recibo si viene seleccionado.
  useEffect(() => {
    setVisitorReceiptForm({
      ...visitorReceiptSelected,
    });
    setSelectedRate(visitorReceiptSelected.rate);
    if (visitorReceiptSelected.issueDate) {
      const formattedIssueDate = formatInTimeZone(
        visitorReceiptSelected.issueDate,
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

    if (visitorReceiptSelected.dueDate) {
      const formattedDueDate = formatInTimeZone(
        visitorReceiptSelected.dueDate,
        "America/Bogota",
        "yyyy-MM-dd"
      );
      //Parsear fecha formatead timeZone
      const parsedDueDate = parse(formattedDueDate, "yyyy-MM-dd", new Date());
      setDueDate(parsedDueDate);
    }
  }, [visitorReceiptSelected]);

  //Traer Rates
  useEffect(() => {
    getRates();
    if (visitorReceiptSelected.id > 0) {
      setSelectedRate(visitorReceiptSelected.rate);
    }
  }, []);

  //cerrar el formulario
  const onCloseForm = () => {
    handlerCloseModalFormVisitorReceipt();
  };

  //monitorea el cambio de los inputs y los agrega al state visitorReceiptForm
  const onInputChange = ({ target }) => {
    const { name, value } = target;

    if (name === "rate") {
      const rateId = value;
      const rate = rates.find((r) => r.id === parseInt(rateId));
      setSelectedRate(rate);

      setVisitorReceiptForm({
        ...visitorReceiptForm,
        [name]: rate,
      });
    } else {
      setVisitorReceiptForm({
        ...visitorReceiptForm,
        [name]: value,
      });
    }
  };

  //cambiar data en los DatePicker
  const handleIssueDateChange = (date) => {
    setIssueDate(date);
  };

  const handleDueDateChange = (date) => {
    setDueDate(date);
  };

  

  //filtrar tarifas
  const filteredRates = rates.filter((rate) => rate.time.includes("VISITANTE"));
  //formato de moneda
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("es-CO", {
      style: "currency",
      currency: "COP",
    }).format(amount);
  };

  //cambia la data del check payment status
  const onPaymentStatusChange = (e) => {
    const { checked } = e.target;
    setVisitorReceiptForm((prevState) => ({
      ...prevState,
      paymentStatus: checked,
    }));
  };

  //Envia la data cuando se encia el formulario
  const onSubmit = (e) => {
    e.preventDefault();
    const updatedVisitorReceiptForm = {
      ...visitorReceiptForm,
      issueDate: issueDate,
      dueDate: dueDate,
    };

    handlerAddReceiptVisitor(updatedVisitorReceiptForm, "/");
  };

  return (
    <div className="abrir-modal animacion fadeIn">
      <div className="fixed inset-0 bg-black bg-opacity-30 backdrop-blur-sm flex justify-center items-center transition-opacity duration-300">
        <div className="bg-secondary-900 p-8  rounded-lg ">
          <div className="bg-secondary-100 p-8 rounded-xl shadow-2xl w-auto lg:w-[450px]">
            <div className="flex items-start justify-between">
              <h1 className="text-2xl uppercase font-bold tracking-[5px] text-white mb-8">
                {visitorReceiptForm.id > 0 ? "Editar" : "Crear"}
                <span className="text-primary"> Recibo</span>
              </h1>
              <button
                className=" py-2 px-2 text-red-600 hover:text-black bg-secondary-900/80  hover:bg-red-600/50 rounded-lg  transition-colors"
                type="button"
                onClick={() => onCloseForm()}
              >
                <RiCloseCircleLine className="text-2xl " />
              </button>
            </div>
            <form onSubmit={onSubmit}>
              <div className="flex flex-col gap-1">
                <label htmlFor="plate" className="ml-2 text-lg text-white">
                  Placa :
                </label>
                <div className="relative">
                  <RiCarLine className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
                  <input
                    type="text"
                    className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary"
                    placeholder="Placa"
                    id="plate"
                    name="plate"
                    value={visitorReceiptForm.plate || ""}
                    onChange={onInputChange}
                  />
                </div>
              </div>
              <div className="relative mb-2">
                <p className="text-red-500">{errorsVisitorReceipt.plate}</p>
              </div>
              <label htmlFor="rate" className="ml-2 text-lg text-white">
                Tarifa :
              </label>
              <div className="relative my-2">
                <RiTicket2Line className="absolute top-1/2 -translate-y-1/2 left-2 text-primary" />
                <select
                  className="py-3 pl-8 pr-4 bg-secondary-900 w-full outline-none rounded-lg focus:border focus:border-primary appearance-none"
                  id="rate"
                  name="rate"
                  value={selectedRate ? selectedRate.id : ""}
                  onChange={onInputChange}
                >
                  <option defaultValue="">seleccione la tarifa</option>
                  {filteredRates.map((rate) => (
                    <option key={rate.id} value={rate.id}>
                      {rate.time} - {formatCurrency(rate.amount)}
                    </option>
                  ))}
                </select>
              </div>
              <div className="relative mb-2">
                <p className="text-red-500">
                  {errorsVisitorReceipt?.rate == "Debe seleccionar una tarifa"
                    ? JSON.stringify(errorsVisitorReceipt?.rate)
                    : ""}
                </p>
              </div>
              {visitorReceiptForm.id > 0 && (
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
                        checked={visitorReceiptForm.paymentStatus || ""}
                        onChange={(e) => onPaymentStatusChange(e)}
                      />
                      <span className="">Recibo pagado</span>
                    </label>
                    <hr className="m-4 border border-dashed border-gray-400/50" />
                  </div>
                </>
              )}
              <button
                type="submit"
                className="bg-primary text-black uppercase font-bold text-sm w-full py-3 px-4 rounded-lg "
              >
                {visitorReceiptForm.id > 0 ? "Editar" : "Generar"}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};
