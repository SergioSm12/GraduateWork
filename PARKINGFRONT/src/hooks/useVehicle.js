import { useDispatch, useSelector } from "react-redux";
import {
  findAllVehicleType,
  findAllVehiclesByUser,
  saveVehicle,
} from "../services/vehicleService";
import {
  addVehicle,
  loadingErrorVehicle,
  loadingVehicles,
  initialVehicleForm,
  onCloseFormVehicle,
  onOpenFormVehicle,
  onVehicleSelectedForm,
  loadingVehicleTypes,
} from "../store/slices/vehicle/vehicleSlice";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

export const useVehicle = () => {
  const {
    vehicles,
    vehicleTypes,
    vehicleSelected,
    errorsVehicle,
    visibleFormVehicle,
  } = useSelector((state) => state.vehicles);
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

  const getVehicles = async (id) => {
    try {
      const result = await findAllVehiclesByUser(id);
      dispatch(loadingVehicles(result.data));
    } catch (error) {
      throw error;
    }
  };

  const getVehicleTypes = async () => {
    try {
      const result = await findAllVehicleType();
      dispatch(loadingVehicleTypes(result.data));
    } catch (error) {
      throw error;
    }
  };

  const handlerAddVehicle = async (userId, vehicle) => {
    let response;
    try {
      if (vehicle.id === 0) {
        response = await saveVehicle(userId, vehicle);
        dispatch(addVehicle(response.data));
      } else {
        console.log("actualizar");
      }
      Toast.fire({
        icon: "success",
        title: vehicle.id === 0 ? "Vehiculo creado" : "Vehiculo Actualizado",
      });
      handlerCloseFormVehicle();
      navigate(`/users/show/${userId}`);
    } catch (error) {
      if (error.response && error.response.status == 400) {
        dispatch(loadingErrorVehicle(error.response.data));
      } else if (error.response?.status == 401) {
        //manejamos el cierre de sesion cuando tengamos autenticacion
        // handlerLogout();
      } else {
        throw error;
      }
    }
  };

  const handlerInitialErrorsVehicle = () => {
    dispatch(loadingErrorVehicle({}));
  };

  //selccionar user para update
  const handlerVehicleSelectedForm = (vehicle) => {
    dispatch(onVehicleSelectedForm({ ...vehicle }));
  };

  //mostrar formulario
  const handlerOpenFormVehicle = () => {
    dispatch(onOpenFormVehicle());
  };

  //Ocultar formulario
  const handlerCloseFormVehicle = () => {
    dispatch(onCloseFormVehicle());
    dispatch(loadingErrorVehicle({}));
  };

  return {
    vehicles,
    visibleFormVehicle,
    vehicleSelected,
    errorsVehicle,
    handlerAddVehicle,
    handlerInitialErrorsVehicle,
    getVehicles,
    getVehicleTypes,
    handlerVehicleSelectedForm,
    handlerOpenFormVehicle,
    handlerCloseFormVehicle,
    initialVehicleForm,
    vehicleTypes,
    
  };
};
