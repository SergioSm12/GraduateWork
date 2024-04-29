import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
import {
  changePaymentStatusNight,
  createNightlyReceiptByUser,
  deleteNightlyReciptById,
  findAllNightlyReceipts,
  findNightlyReceiptsByUser,
  totalCountReceipts,
  totalPaid,
  totalUnpaid,
  updateNightlyReceipt,
} from "../services/nightlyReceiptService";
import {
  addNightlyReceiptSlice,
  initialNightlyReceipt,
  loadingErrorsNightlyReceipt,
  loadingNightlyReceipts,
  loadingNightlyReceiptsByUser,
  loadingNightPaidCount,
  loadingNightTotalCount,
  loadingNightUnpaidCount,
  onCloseModalFormNightlyReceipt,
  onCloseQRModalNightlyReceipt,
  onCloseShowModalNightlyReceipt,
  onNightlyReceiptSelectedForm,
  onNightlyReceiptShowModalSelected,
  onOpenModalFormNightlyReceipt,
  onOpenQRModalNightlyReceipt,
  removeNightlyReceipt,
  updateNightlyReceiptSlice,
} from "../store/slices/receipt/nightlyReceiptSlice";
import { useAuth } from "../auth/hooks/useAuth";
import {
  updateReceiptSlice,
  vehicle,
} from "../store/slices/receipt/receiptSlice";

export const useNightlyReceipts = () => {
  const {
    nightlyReceipts,
    nightlyReceiptsByUser,
    totalNightUnPaidState,
    totalNightPaidState,
    totalNightState,
    nightlyReceiptSelected,
    idQRNightlyReceipt,

    visibleFormNightlyReceiptModal,
    vehicleSelected,
    errorsNightlyReceipt,
    visibleQRModalNightlyReceipt,
    visibleShowNightlyReceiptModal,
  } = useSelector((state) => state.nightlyReceipts);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { login, handlerLogout } = useAuth();

  //Alertas
  const Toast = Swal.mixin({
    toast: true,
    position: "top",
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.addEventListener("mouseenter", Swal.stopTimer);
      toast.addEventListener("mouseleave", Swal.resumeTimer);
    },
  });

  const getNightlyReceipts = async () => {
    try {
      const result = await findAllNightlyReceipts();
      dispatch(loadingNightlyReceipts(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getNightlyReceiptsByUser = async (id) => {
    try {
      const result = await findNightlyReceiptsByUser(id);
      dispatch(loadingNightlyReceiptsByUser(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  //create receipt
  const handlerAddNightlyReceiptByUser = async (
    userId,
    nightlyReceipt,
    redirectTo
  ) => {
    let response;
    try {
      if (nightlyReceipt.id === 0) {
        response = await createNightlyReceiptByUser(userId, nightlyReceipt);
        dispatch(addNightlyReceiptSlice(response.data));
        if (redirectTo) {
          navigate(redirectTo);
        }
      } else {
        //update
        response = await updateNightlyReceipt(
          nightlyReceipt.id,
          nightlyReceipt
        );
        dispatch(updateNightlyReceiptSlice(response.data));
      }
      Toast.fire({
        icon: "success",
        title: nightlyReceipt.id === 0 ? "Recibo creado" : "Recibo actualizado",
      });
      handlerCloseModalFormNighlyReceipt();
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorsNightlyReceipt(error.response.data));
      } else if (error.response?.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  //Cambiar estado de pago
  const handlerChangePaymentStatus = async (receiptNightlyId, id = null) => {
    Swal.fire({
      title: "¿ Desea cambiar el estado de pago ?",
      text: "¡ El estado de pago sera cambiado !  ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, cambiar!",
      cancelButtonText: "Cancelar",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await changePaymentStatusNight(receiptNightlyId);

          Toast.fire({
            icon: "success",
            title: "Estado de pago actualizado",
          });
          await getNightlyReceipts();
          await getCountNightPaid();
          await getCountNightUnpaid();
          //Cargamos los recibos con el nuevo estado de pago
          if (id !== null) {
            //await getReciptsByUser(id);
          }
        } catch (error) {
          if (error.response?.status == 401) {
            handlerLogout();
          } else {
            throw error;
          }
        }
      }
    });
  };

  const handlerRemoveNightlyReceipt = async (nightlyReceiptId, id = null) => {
    Swal.fire({
      title: "Esta seguro que desea eliminar?",
      text: "El recibo sera eliminado.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, eliminar!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await deleteNightlyReciptById(nightlyReceiptId);
          dispatch(removeNightlyReceipt(nightlyReceiptId));

          Toast.fire({
            icon: "success",
            title: "Recibo eliminado ",
          });

          await getCountNightPaid();
          await getCountNightUnpaid();
          await getCountNightTotal();
          /*
          if (id !== null) {
            await getReciptsByUser(id);
          }*/
        } catch (error) {
          if (error.response?.status == 401) {
            handlerLogout();
          }
        }
      }
    });
  };

  const getCountNightUnpaid = async () => {
    try {
      const total = await totalUnpaid();
      dispatch(loadingNightUnpaidCount(total.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCountNightPaid = async () => {
    try {
      const total = await totalPaid();
      dispatch(loadingNightPaidCount(total.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const getCountNightTotal = async () => {
    try {
      const total = await totalCountReceipts();
      dispatch(loadingNightTotalCount(total.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  //modal form nightly receipt
  const handlerNightlyReceiptSelectedModalForm = (nightlyReceipt) => {
    dispatch(onNightlyReceiptSelectedForm({ ...nightlyReceipt }));
  };

  const handlerOpenModalFormNightlyReceipt = (vehicle) => {
    dispatch(onOpenModalFormNightlyReceipt({ ...vehicle }));
  };

  const handlerCloseModalFormNighlyReceipt = () => {
    dispatch(onCloseModalFormNightlyReceipt());
    dispatch(loadingErrorsNightlyReceipt({}));
  };

  //show info
  const handlerNightlyReceiptSelectedModalShow = (nightlyReceipt) => {
    dispatch(onNightlyReceiptShowModalSelected({ ...nightlyReceipt }));
  };

  const handlerCloseModalShowNightlyReceipt = () => {
    dispatch(onCloseShowModalNightlyReceipt());
  };

  //QrModal
  const handlerOpenModalQRNightlyReceipt = (idNightlyReceipt) => {
    dispatch(onOpenQRModalNightlyReceipt(idNightlyReceipt));
  };
  const handlerCloseModalQRNightlyReceipt = () => {
    dispatch(onCloseQRModalNightlyReceipt());
  };

  return {
    nightlyReceipts,
    nightlyReceiptsByUser,
    getNightlyReceipts,
    getNightlyReceiptsByUser,
    getCountNightUnpaid,
    getCountNightPaid,
    getCountNightTotal,
    totalNightUnPaidState,
    totalNightPaidState,
    totalNightState,
    idQRNightlyReceipt,
    nightlyReceiptSelected,
    visibleFormNightlyReceiptModal,
    vehicleSelected,
    errorsNightlyReceipt,
    vehicle,

    handlerAddNightlyReceiptByUser,
    handlerNightlyReceiptSelectedModalForm,
    handlerOpenModalFormNightlyReceipt,
    handlerCloseModalFormNighlyReceipt,
    handlerNightlyReceiptSelectedModalShow,
    handlerCloseModalShowNightlyReceipt,
    handlerChangePaymentStatus,
    handlerOpenModalQRNightlyReceipt,
    handlerCloseModalQRNightlyReceipt,
    visibleShowNightlyReceiptModal,
    visibleQRModalNightlyReceipt,
    handlerRemoveNightlyReceipt,
    initialNightlyReceipt,
  };
};
