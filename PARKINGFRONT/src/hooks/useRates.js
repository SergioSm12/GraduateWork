import { useDispatch, useSelector } from "react-redux";
import { findAllRate, saveRate, updateRate } from "../services/rateService";
import {
  addRate,
  loadingErrorRates,
  loadingRates,
  initialRateForm,
  updateRateSlice,
  onRateSelectedForm,
} from "../store/slices/rate/rateSlice";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

export const useRates = () => {
  const { rates, rateSelected, errorsRate } = useSelector(
    (state) => state.rates
  );
  const dispatch = useDispatch();
  const navigate = useNavigate();
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

  const getRates = async () => {
    try {
      const result = await findAllRate();
      dispatch(loadingRates(result.data));
    } catch (error) {
      throw error;
    }
  };

  const handlerAddRate = async (rate) => {
    let response;
    try {
      if (rate.id === 0) {
        response = await saveRate(rate);
        dispatch(addRate(response.data));
      } else {
        
        response = await updateRate(rate);
        dispatch(updateRateSlice(response.data));
      }
      Toast.fire({
        icon: "success",
        title: rate.id === 0 ? "Vehiculo creado" : "Vehiculo Actualizado",
      });
      navigate("/rate");
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorRates(error.response.data));
      } else if (error.response?.status == 401) {
        //manejamos el cierre de sesion cuando tengamos autenticacion
        // handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerInitialErrosRates = () => {
    dispatch(loadingErrorRates({}));
  };

  const handlerRateSelectedForm = (rate) => {
    dispatch(onRateSelectedForm({ ...rate }));
  };

  return {
    rates,
    getRates,
    initialRateForm,
    handlerAddRate,
    handlerRateSelectedForm,
    rateSelected,
    errorsRate,
  };
};
