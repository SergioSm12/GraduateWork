import { useDispatch, useSelector } from "react-redux";
import {
  findAllRate,
  removeRate,
  saveRate,
  updateRate,
} from "../services/rateService";
import {
  addRate,
  loadingErrorRates,
  loadingRates,
  initialRateForm,
  updateRateSlice,
  onRateSelectedForm,
  removeRateSlice,
} from "../store/slices/rate/rateSlice";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/hooks/useAuth";

export const useRates = () => {
  const { rates, rateSelected, errorsRate } = useSelector(
    (state) => state.rates
  );
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { handlerLogout } = useAuth();
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
      if (error.response?.status == 401) {
        handlerLogout();
      }
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
        title: rate.id === 0 ? "Tarifa creada" : "Tarifa Actualizada",
      });
      navigate("/rate");
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorRates(error.response.data));
      } else if (error.response?.status == 401) {
        handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerRemoveRate = (id) => {
    Swal.fire({
      title: "Esta seguro que desea eliminar?",
      text: "Cuidado la tarifa sera eliminada ",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#1E293C",
      cancelButtonColor: "#d33",
      confirmButtonText: "Si, eliminar!",
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          //Eliminar desde el backend
          await removeRate(id);

          dispatch(removeRateSlice(id));

          Swal.fire(
            "Tarifa Eliminada!",
            "La tarifa ha sido eliminada con exito",
            "success"
          );
        } catch (error) {
          if (error.response?.status == 401) {
            handlerLogout();
          }
        }
      }
    });
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
    handlerRemoveRate,
    rateSelected,
    errorsRate,
  };
};
