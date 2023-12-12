import {
  createVisitorReceipt,
  findAllVisitorReceipts,
} from "../services/visitorReceiptService";
import { useDispatch, useSelector } from "react-redux";
import {
  addVisitorReceipt,
  initialVisitorReceipt,
  loadingErrorVisitorReceipt,
  loadingVisitorReceipt,
} from "../store/slices/receipt/visitorReceiptSlice";
import Swal from "sweetalert2";
import { useAuth } from "../auth/hooks/useAuth";

export const useVisitorReceipt = () => {
  const { visitorReceipts } = useSelector((state) => state.visitorReceipts);
  const dispatch = useDispatch();
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

  const getVisitorReceipts = async () => {
    try {
      const result = await findAllVisitorReceipts();
      dispatch(loadingVisitorReceipt(result.data));
    } catch (error) {
      if (error.response.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerAddReceiptVisitor = async (visitorReceipt) => {
    let response;
    try {
      if (visitorReceipt.id === 0) {
        response = await createVisitorReceipt(visitorReceipt);
        dispatch(addVisitorReceipt(response.data));
      } else {
      }
      Toast.fire({
        icon: "success",
        title: visitorReceipt.id === 0 ? "Recibo creado" : "Recibo actualizado",
      });
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorVisitorReceipt(error.response.data));
      } else if (error.response?.status == 401) {
        //manejamos el cierre de sesion cuando tengamos autenticacion
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  return {
    getVisitorReceipts,
    visitorReceipts,
    initialVisitorReceipt,
    handlerAddReceiptVisitor,
  };
};
